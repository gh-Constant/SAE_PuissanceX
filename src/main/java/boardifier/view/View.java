package boardifier.view;

import boardifier.model.GameElement;
import boardifier.model.GameException;
import boardifier.model.Model;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;


public class View {

    /**
     * The model
     */
    protected Model model;
    /**
     * The root pane
     */
    protected RootPane rootPane;
    /**
     * The current game stage view, for which looks are put within the root pane
     *
     */
    protected GameStageView gameStageView;
    /**
     * The vertical box that contains a menu bar (if needed) and the root pane.
     */
    protected VBox vbox;
    /**
     * The menu bar of the game.
     * It MUST BE created in the createMenuBar() method, which by default does nothing.
     * Thus, dev. must create a subclass of View to override createMenuBar().
     */
    protected MenuBar menuBar;

    /**
     * The primary stage.
     *
     * It is used to resize the stage to the current scene size.
     */
    protected Stage stage;
    /**
     * The current scene assigned to the primary stage.
     */
    protected Scene scene;

    public View(Model model, Stage stage, RootPane rootPane) {
        this.model = model;
        this.stage = stage;
        this.rootPane = rootPane;
        // create the vbox that will be the root node of the scene
        vbox = new VBox();

        // create the menu bar if needed
        createMenuBar();
        if (menuBar != null) {
            vbox.getChildren().add(menuBar);
        }
        vbox.getChildren().add(rootPane);

        // create the scene with the default content
        scene = new Scene(vbox);
        // WARNING: must set the scene and resize the stage BEFORE defining the clipping.
        // Otherwise, dimensions won't be correct.
        stage.setScene(scene);
        stage.sizeToScene();
        stage.setResizable(false);
    }

    public Pane getRootPane() {
        return rootPane;
    }
    /**
     * Create the menu bar.
     * By default, a view has no menu bar so this method just set menuBar to null.
     * It should be overridden in subclasses.
     */
    protected void createMenuBar() {
        menuBar = null;
    }


    public GameStageView getGameStageView() {
        return gameStageView;
    }

    public void resetView() {
        rootPane.resetToDefault();
        if (scene != null) {
            // detach the current vbox as a root node of the current scene
            // so that it can be reused for the new scene.
            scene.setRoot(new Group());
        }
        // create the scene
        scene = new Scene(vbox);
        // WARNING: must set the scene and resize the stage BEFORE defining the clipping.
        // Otherwise, dimensions won't be correct.
        stage.setScene(scene);
        stage.sizeToScene();
        stage.setResizable(false);
        // set the clipping area with the boundaries of root pane.
        Rectangle r = new Rectangle(rootPane.getWidth(), rootPane.getHeight());
        rootPane.setClip(r);
    }

    /**
     * Setup the whole view, given a pane view and a game stage view
     *
     * This method is called to setup the main window content. It is called when the application is launched, and
     * every time there is a need to change the content, for example when the game starts or when there is a change of stage.
     * It mainly consists in searching the PaneView object with the given name, and if found, to call its init() method to
     * add all nodes associated to the current game stage to the pane view group. Finally, this group
     * is added to the root pane.
     *
     * It must be notice that calling this method twice with the same pane view name will effectively
     * reset the root pane content, as if the game stage was started again.
     *
     * @param gameStageView An instance of GameStageView that contains the elements to be added to the pane view.
     * @throws GameException thrown if there is no pane view with the given name
     */
    public void setView(GameStageView gameStageView) {

        rootPane.init(gameStageView);
        //NB: gameStageView may be null if there is no game stage view to draw (cf. SimpleTextView)
        this.gameStageView = gameStageView;
        // detach the current vbox as a root node of the current scene
        // so that it can be reused for the new scene.
        scene.setRoot(new Group());
                /* create the new scene with vbox as a root node, and if specified the
                   dimensions.
                 */
        if ((this.gameStageView != null) && (this.gameStageView.getWidth() != -1) && (this.gameStageView.getHeight() != -1)) {
            double h = 0;
            if (menuBar != null) h = menuBar.getHeight();
            scene = new Scene(vbox, this.gameStageView.getWidth(), h+ this.gameStageView.getHeight());
            // set the clipping area of the root pane with the given size of the stage
            // So, if there are shape that overlap with these boundaries, they won't show totally.
            Rectangle r = new Rectangle(this.gameStageView.getWidth(), this.gameStageView.getHeight());
            rootPane.setClip(r);
            stage.setScene(scene);
            stage.sizeToScene();
        }
        else {
            scene = new Scene(vbox);
            // WARNING: must set the scene and resize the stage BEFORE defining the clipping.
            // Otherwise, dimensions won't be correct.
            stage.setScene(scene);
            stage.sizeToScene();
            stage.setResizable(false);
            // set the clipping area with the boundaries of root pane.
            Rectangle r = new Rectangle(rootPane.getWidth(), rootPane.getHeight());
            rootPane.setClip(r);
        }
    }


    /**
     *
     * @return the primary javafx stage
     */
    public Stage getStage() {
        return stage;
    }


    /* ***************************************
       TRAMPOLINE METHODS
    **************************************** */

    public ElementLook getElementLook(GameElement element) {
        return rootPane.getElementLook(element);
    }
    public ContainerLook getElementContainerLook(GameElement element) {
        return (ContainerLook) rootPane.getElementLook(element.getContainer());
    }
}
