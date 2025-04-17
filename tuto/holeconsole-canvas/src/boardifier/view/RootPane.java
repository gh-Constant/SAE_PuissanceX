package boardifier.view;

import boardifier.control.Logger;
import boardifier.model.Coord2D;
import boardifier.model.GameElement;

import java.util.Collections;
import java.util.List;

public class RootPane {

    protected GameStageView gameStageView;
    private String[][] viewPort;
    private int width;
    private int height;

    public RootPane(int width, int height) {
        this.width = width;
        this.height = height;
        viewPort = new String[height][width];
        clearViewPort();
        gameStageView = null;
    }

    public RootPane() {
        this(1,1);
    }

    public void clearViewPort() {
        for(int i=0;i<height;i++) {
            for(int j=0;j<width;j++) {
                viewPort[i][j] = " ";
            }
        }
    }

    public final void init(GameStageView gameStageView) {
        if (gameStageView != null) {
            this.gameStageView = gameStageView;
            Collections.sort(gameStageView.getLooks(), (a, b) -> a.getDepth() - b.getDepth());
            for (ElementLook look : gameStageView.getLooks()) {
                // setup rootpane reference for every look
                look.setRootPane(this);
            }
        }
    }

    public void udpate() {
        /*
        // first update the game stage view if it exists
        if (gameStageView != null) {
            gameStageView.update();
        }

         */
        // then create the viewport

        // first, determine the size of the view
        int w = 0;
        int h = 0;

        List<ElementLook> looks = gameStageView.getLooks();
        for (ElementLook look : looks) {
            GameElement element = look.getElement();
            // just take elements in the stage that are visible and with a look that is not within a container look
            if (element.isInStage() && element.isVisible() && !look.hasParent()) {
                if ((look.getWidth() + element.getX()) > w) {
                    w = (int) (look.getWidth() + element.getX());
                }
                if ((look.getHeight() + element.getY()) > h) {
                    h = (int) (look.getHeight() + element.getY());
                }
            }
        }
        if ((w != width) || (h != height)) {
            width = w;
            height = h;
            viewPort = new String[height][width];
        }
        clearViewPort();

        // now render looks
        for  (ElementLook look : looks) {
            GameElement element = look.getElement();
            // just take elements in the stage that are: visible and in the scene
            if (element.isInStage() && element.isVisible() && !look.hasParent()) {
                // first render the look to its internal shape array
                look.render();
                // now copy the internal array in the viewport
                for (int i = 0; i < look.getHeight(); i++) {
                    for (int j = 0; j < look.getWidth(); j++) {
                        if ((element.getY() + i >= 0) && (element.getX() + j >= 0)) {
                            String s = look.getShapePoint(j, i);
                            if ((s != null) && (!" ".equals(s))) {
                                viewPort[(int) (element.getY() + i)][(int) (element.getX() + j)] = s;
                            }
                        }
                    }
                }
            }
        }
    }

    public void print() {
        for(int i=0;i<height;i++) {
            for(int j=0;j<width;j++) {
                System.out.print(viewPort[i][j]);
            }
            System.out.println();
        }
    }

    public Coord2D getRootPaneLocationFromLookLocation(ElementLook look) {
        double x = look.getElement().getX();
        double y = look.getElement().getY();

        // if the look has no parent container, it is already in the rootpane space
        if (!look.hasParent()) {
            return new Coord2D(x,y);
        }
        // else, get the upper most container in the hierarchy
        ContainerLook up = (ContainerLook) look.getParent();
        // at this point localX, localY are the cooridnates in the direct parent of look
        while (up.hasParent() ) {
            // update localX, localY, using the position of the parent container
            x += up.getElement().getX();
            y += up.getElement().getY();
            up = (ContainerLook) up.getParent();
        }
        // add the position of the upper most container in the root pane
        x += up.getElement().getX();
        y += up.getElement().getY();
        return new Coord2D(x, y);
    }

}
