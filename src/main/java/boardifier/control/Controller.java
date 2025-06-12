package boardifier.control;

import boardifier.model.*;
import boardifier.view.*;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.StageStyle;

import java.util.*;

public abstract class Controller {
    protected Model model;
    protected View view;
    protected ControllerAnimation controlAnimation;
    protected ControllerKey controlKey;
    protected ControllerMouse controlMouse;
    protected ControllerAction controlAction;
    protected String firstStageName;
    protected Map<GameElement, ElementLook> mapElementLook;
    private boolean inUpdate;
    private long frameNumber;
    private boolean stopProcessEvents; // declared here because must be modified from with lambda

    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;
        controlAnimation = new ControllerAnimation(model,view, this);
        firstStageName = "";
        inUpdate = false;
        frameNumber = 0;
    }

    public void setControlKey(ControllerKey controlKey) {
        this.controlKey = controlKey;
    }

    public void setControlMouse(ControllerMouse controlMouse) {
        this.controlMouse = controlMouse;
    }

    public void setControlAction(ControllerAction controlAction) {
        this.controlAction = controlAction;
    }


    public void setFirstStageName(String firstStageName) {
        this.firstStageName = firstStageName;
    }

    public void startGame() throws GameException {
        if (firstStageName.isEmpty()) throw new GameException("The name of the first stage have not been set. Abort");
        Logger.trace("START THE GAME");
        startStage(firstStageName);
    }

    public void stopGame() {
        controlAnimation.stopAnimation();
        model.reset();
    }

    /**
     * Start a stage of the game.
     * This method MUST NOT BE called directly, except in the endStage() overrideen method.
     *
     * @param stageName The name of the stage, as registered in the StageFactory.
     * @throws GameException
     */
    protected void startStage(String stageName) throws GameException {
        if (model.isStageStarted()) stopGame();
        Logger.trace("START STAGE "+stageName);
        // create the model of the stage by using the StageFactory
        GameStageModel gameStageModel = StageFactory.createStageModel(stageName, model);
        // create the elements of the stage by getting the default factory of this stage and giving it to createElements()
        gameStageModel.createElements(gameStageModel.getDefaultElementFactory());
        // create the view of the stage by using the StageFactory
        GameStageView gameStageView = StageFactory.createStageView(stageName, gameStageModel);
        // create the looks of the stage (NB: no factory this time !)
        gameStageView.createLooks();
        // create a map of GameElement <-> ElementLook, that helps the controller in its update() method
        mapElementLook = new HashMap<>();
        for(GameElement element : gameStageModel.getElements()) {
            ElementLook look = gameStageView.getElementLook(element);
            mapElementLook.put(element, look);
        }
        /* At this point, there may be elements that have been put in some containers.
           Thus events have been generated, and they must be processed before starting the game
           in order to have a correct location of all looks on the screen and to set the view with
           the correct size
         */
        processEvents();
        // start the game, from the model point of view.
        model.startGame(gameStageModel);
        // set the view so that the current pane view can integrate all the looks of the current game stage view.
        view.setView(gameStageView);
        /* CAUTION: since starting the game implies to
           remove the intro pane from root, then root has no more
           children. It seems that this removal causes a focus lost
           which must be set once again in order to catch keyboard events.
        */
        view.getRootPane().setFocusTraversable(true);
        view.getRootPane().requestFocus();



        controlAnimation.startAnimation();
    }

    private void processEvents() {
        // first, process container related events
        processContainerEvents();
        updateElements(); // in case of some element override update()
        processLookEvents();
    }

    private void processContainerEvents() {

        // for each element : process all event and then update
        mapElementLook.forEach((k,v) -> {
            // proceed events that are only manageable by the controller
            EventQueue queue = k.getEventQueue();
            for (int i=0;i<queue.getSize();i++) {
                Event e = queue.getEvent(i);
                if (e.isOutContainerEvent()) {
                    queue.removeEvent(i--);
                    ContainerElement container = (ContainerElement) e.getParameter(0);
                    int row = (Integer) e.getParameter(1);
                    int col = (Integer) e.getParameter(2);
                    removeElementLookFromContainerLookCell(container, v, row, col);
                    k.signalContainerOpEnd();
                } else if (e.isInContainerEvent()) {
                    queue.removeEvent(i--);
                    ContainerElement container = (ContainerElement) e.getParameter(0);
                    int row = (Integer) e.getParameter(1);
                    int col = (Integer) e.getParameter(2);
                    putElementLookToContainerLookCell(container, v, row, col);
                    k.signalContainerOpEnd();
                } else if (e.isMoveInContainerEvent()) {
                    queue.removeEvent(i--);
                    int rowSrc = (Integer) e.getParameter(0);
                    int colSrc = (Integer) e.getParameter(1);
                    int rowDest = (Integer) e.getParameter(2);
                    int colDest = (Integer) e.getParameter(3);
                    moveElementLookToContainerLookCell(k, v, rowSrc, colSrc, rowDest, colDest);
                    k.signalContainerOpEnd();
                }
            }
        });

    }

    private void updateElements() {
        // for each element : process all event and then update
        mapElementLook.forEach((k,v) -> {
            // update the model of the element, in case of there is really something to do (see comment before GameElement.update())
            k.update();
        });
    }

    private void processLookEvents() {

        // for each element : process all event and then update
        mapElementLook.forEach((k,v) -> {
            // proceed events that are only manageable by the controller
            EventQueue queue = k.getEventQueue();

            for (int i=0;i<queue.getSize();i++) {
                Event e = queue.getEvent(i);
                if (e.isLocationEvent()) {
                    queue.removeEvent(i--);
                    v.onLocationChange();
                }
                else if (e.isVisibilityEvent()) {
                    queue.removeEvent(i--);
                    v.onVisibilityChange();
                }
                else if (e.isSelectionEvent()) {
                    queue.removeEvent(i--);
                    v.onSelectionChange();
                }
                else if (e.isFaceEvent()) {
                    queue.removeEvent(i--);
                    v.onFaceChange();
                }
            }
        });
    }

    /**
     * Execute actions needed at the end of each stage.
     * This method does nothing by default. It can be overridden to "compute" the name of the next game stage
     * and to start it. It may also be used update the model, for example by computing reward points, or somthg else.
     */
    public void stopStage() {
        model.stopStage();
        model.reset();
    }

    /**
     * Execute actions when the current player just ended its turn
     * By default, this method does nothing because what to do is totally dependent of the game and its state.
     * For some, a new player can play, and for toehrs, the current player may play another turn.
     */
    public void endOfTurn() {}

    /**
     * Execute actions at the end of the game.
     * This method defines a default behaviour, which is to display a dialog box with the name of the
     * winner and that proposes to start a new game or to quit.
     */
    public void endGame() {
        //Logger.traceln("END THE GAME");

        String message = "";
        if (model.getIdWinner() != -1) {
            message = model.getPlayers().get(model.getIdWinner()).getName() + " wins";
        }
        else {
            message = "Draw game";
        }
        // disable all events
        model.setCaptureEvents(false);
        // create a dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        // remove the frame around the dialog
        alert.initStyle(StageStyle.UNDECORATED);
        // make it a children of the main game window => it appears centered
        alert.initOwner(view.getStage());
        // set the message displayed
        alert.setHeaderText(message);
        // define new ButtonType to fit with our needs => one type is for Quit, one for New Game
        ButtonType quit = new ButtonType("Quit");
        ButtonType newGame = new ButtonType("New Game");
        // remove default ButtonTypes
        alert.getButtonTypes().clear();
        // add the new ones
        alert.getButtonTypes().addAll(quit, newGame);
        // show the dialog and wait for the result
        Optional<ButtonType> option = alert.showAndWait();
        // check if result is quit
        if (option.get() == quit) {
            System.exit(0);
        }
        // check if result is new game
        else if (option.get() == newGame) {
            try {
                startGame();
            } catch (GameException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
        // abnormal case :-)
        else {
            System.err.println("Abnormal case: dialog closed with not choice");
            System.exit(1);
        }
    }

    /**
     * Update model and view.
     * This method MUST NOT BE called directly, and is only called by the ControllerAnimation
     * at each frame. It is used to update the model and then the view.
     * It must be noticed that the process of updating follows a fixed scheme :
     *   - update all game element of the current game stage,
     *   - update the grid cell of element that are in a grid and that have moved in space, and thus may have changed of cell,
     *   - update the looks of all elements, calling dedicated methods according the type indicators of change (location, look, selection, ...),
     *   - reset the change indicators in elements,
     *   - check if the sage is finished,
     *   - check if the game is finished.
     */
    public void update() {
        if (inUpdate) {
            System.err.println("Abnormal case: concurrent updates");
        }
        inUpdate = true;
        frameNumber += 1;

        // update container related event
        processContainerEvents();
        // update elements in case there are animated or if there state changes at each frame.
        // It is done before updating looks because it may impact the looks.
        updateElements();
        // update looks, i.e. yields a global view update.
        processLookEvents();

        if (model.isEndStage()) {
            controlAnimation.stopAnimation();
            Platform.runLater( () -> {
                stopStage();});
        }
        else if (model.isEndGame()) {
            controlAnimation.stopAnimation();
            Platform.runLater( () -> {endGame();});
        }

        inUpdate = false;
    }

    /* ***************************************
       HELPERS METHODS
    **************************************** */

    /**
     * Get the look of a given element
     * @param element the element for which the look is asked.
     * @return an ElementLook object that is the look of the element
     *
     */
    public ElementLook getElementLook(GameElement element) {
        return mapElementLook.get(element);
    }

    public void removeElementLookFromContainerLookCell(ContainerElement container, ElementLook look, int row, int col) {
        Logger.trace(frameNumber+" - remove element look ["+look+" ] from "+row+","+col);
        // get the container look from the container element where the element was removed
        ContainerLook containerLook = (ContainerLook) getElementLook(container);
        // add the look of the element to the inner looks of the container look =>
        // the innerLayout of the container look now manages the element look location.
        containerLook.removeInnerLook(look, row, col);
    }

    public void putElementLookToContainerLookCell(ContainerElement container, ElementLook look, int row, int col) {

        Logger.trace(frameNumber+" - put element look ["+look+"] in "+row+","+col);
        // get the look of the container element
        ContainerLook containerLook = (ContainerLook) getElementLook(container);
        // add the look of the element to the inner looks of the container look =>
        // the innerLayout of the container look now manages the element look location.
        containerLook.addInnerLook(look, row, col);
    }

    public void moveElementLookToContainerLookCell(GameElement element, ElementLook look, int rowSrc, int colSrc, int rowDest, int colDest) {
        Logger.trace(frameNumber+" - move element look ["+look+"] from "+rowSrc+","+colSrc+ " to "+rowDest+","+colDest);
        // NB: element may have been removed from a container but not put in another one
        // in this case, do nothing
        if (element.getContainer() == null) return;
        // get the look of the container element
        ContainerLook containerLook = (ContainerLook) getElementLook(element.getContainer());
        // move the look of the element within the layout of the container look.
        containerLook.moveInnerLook(look, rowSrc, colSrc, rowDest, colDest);

    }

    /**
     * Get all visible and clickable elements that are at a given point in the scene coordinate space.
     * @param point the coordinate of a point
     * @return A list of game element
     */
    public List<GameElement> elementsAt(Coord2D point) {
        List<GameElement> list = new ArrayList<>();
        for(GameElement element : model.getElements()) {
            if ((element.isVisible()) && (element.isClickable())) {
                ElementLook look = mapElementLook.get(element);
                if ((look != null) && (look.isPointWithin(point))) {
                    list.add(element);
                }
            }
        }
        return list;
    }
}
