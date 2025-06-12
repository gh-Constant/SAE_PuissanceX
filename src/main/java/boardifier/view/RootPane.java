package boardifier.view;

import boardifier.control.Logger;
import boardifier.model.Coord2D;
import boardifier.model.GameElement;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.Collections;

public class RootPane extends Pane {

    protected GameStageView gameStageView;
    protected Group group; // the group that contains all game elements of the current stage
    private Point2D posInScene; // for convenience. Setup at each call of init, even it normally does not change

    public RootPane() {
        this.gameStageView = null;
        group = new Group();
        //setBackground(Background.EMPTY);
        resetToDefault();
    }

    public final void resetToDefault() {
        createDefaultGroup();
        // add the group to the pane
        getChildren().clear();
        getChildren().add(group);
    }

    /**
     * create the element of the default group
     * This method can be overriden to define a different visual aspect.
     */
    protected void createDefaultGroup() {
        Rectangle frame = new Rectangle(100, 100, Color.LIGHTGREY);
        // remove existing children
        group.getChildren().clear();
        // adding default ones
        group.getChildren().addAll(frame);
    }
    /**
     * Initialize the content of the group.
     * It takes the elements of the model, which are initialized when starting a game stage.
     * It sorts them so that the element with the highest depth are put in first in the group.
     * So they will be hidden by elements with a lower depth.
     */
    public final void init(GameStageView gameStageView) {
        posInScene = group.localToScene(0,0); // get the position of the RootPane group within the scene
        if (gameStageView != null) {
            this.gameStageView = gameStageView;
            // first sort element by their depth
            Collections.sort(gameStageView.getLooks(), (a, b) -> a.getDepth() - b.getDepth());
            // remove existing children
            group.getChildren().clear();
            // add game element looks
            for (ElementLook look : gameStageView.getLooks()) {
                // setup rootpane reference for every look
                look.setRootPane(this);
                // just add looks with no parents
                if (!look.hasParent()) {
                    Group group = look.getGroup();
                    this.group.getChildren().add(group);
                }
            }
            // add the group to the pane
            getChildren().clear();
            getChildren().add(group);
        }
    }

    public void attachLookToRootPane(ElementLook look) {
        // get the current coordinates
        group.getChildren().add(look.getGroup());
    }

    public Coord2D getRootPaneLocationFromLookLocation(ElementLook look) {
        double localX = look.getElement().getX();
        double localY = look.getElement().getY();

        if (look.hasParent()) {
            Point2D pScene = look.getParent().getGroup().localToScene(localX, localY);
            return new Coord2D(pScene.getX()-posInScene.getX(), pScene.getY() - posInScene.getY());
        }
        // else x,y are already within RootPane space
        return new Coord2D(localX,localY);
    }
    /**
     * If the look has a parent, the parameters are used to compute the location of the
     * look within
     * @param look
     * @param rootPaneX
     * @param rootPaneY
     * @return
     */
    public Coord2D getLookLocationFromRootPaneLocation(ElementLook look, double rootPaneX, double rootPaneY) {
        if (look.hasParent()) {
            Point2D pScene = localToScene(rootPaneX, rootPaneY);
        }
        // else x,y are already within RootPane space
        return new Coord2D(rootPaneX,rootPaneY);
    }
    /* ***************************************
       TRAMPOLINE METHODS
    **************************************** */

    public ElementLook getElementLook(GameElement element) {
        if (gameStageView == null) return null;
        return gameStageView.getElementLook(element);
    }
}
