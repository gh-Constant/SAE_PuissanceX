package boardifier.control;

import boardifier.model.*;
import boardifier.view.*;

import java.util.HashMap;
import java.util.Map;

public abstract class Controller {
    protected Model model;
    protected View view;
    protected String firstStageName;
    protected Map<GameElement, ElementLook> mapElementLook;
    private long frameNumber;
    private boolean stopProcessEvents; // declared here because must be modified from with lambda

    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;
        firstStageName = "";
        frameNumber = 0;
    }

    public void setFirstStageName(String firstStageName) {
        this.firstStageName = firstStageName;
    }

    public void startGame() throws GameException {
        if (firstStageName.isEmpty()) throw new GameException("The name of the first stage have not been set. Abort");
        Logger.trace("START THE GAME");
        startStage(firstStageName);
    }

    /**
     * defines what must be done during a stage
     */
    public abstract void stageLoop();

    /**
     * Start a stage of the game.
     * This method MUST NOT BE called directly, except in the endStage() overridden method.*
     *
     * @param stageName The name of the stage, as registered in the StageFactory.
     * @throws GameException
     */
    protected void startStage(String stageName) throws GameException {
        if (model.isStageStarted()) stopStage();
        Logger.trace("START STAGE " + stageName);
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
        for (GameElement element : gameStageModel.getElements()) {
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

    }

    private void processEvents() {
        // first, process container related events
        processContainerEvents();
        updateElements(); // in case of some element override update()
        processLookEvents();
    }

    private void processContainerEvents() {
        Logger.trace(" called", this);

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
                } else if (e.isInContainerEvent()) {
                    queue.removeEvent(i--);
                    ContainerElement container = (ContainerElement) e.getParameter(0);
                    int row = (Integer) e.getParameter(1);
                    int col = (Integer) e.getParameter(2);
                    putElementLookToContainerLookCell(container, v, row, col);
                } else if (e.isMoveInContainerEvent()) {
                    queue.removeEvent(i--);
                    int rowSrc = (Integer) e.getParameter(0);
                    int colSrc = (Integer) e.getParameter(1);
                    int rowDest = (Integer) e.getParameter(2);
                    int colDest = (Integer) e.getParameter(3);
                    moveElementLookToContainerLookCell(k, v, rowSrc, colSrc, rowDest, colDest);
                }
            }
        });

    }

    private void updateElements() {
        Logger.trace(" called", this);
        // for each element : process all event and then update
        mapElementLook.forEach((k,v) -> {
            // update the model of the element, in case of there is really something to do (see comment before GameElement.update())
            k.update();
        });
    }

    private void processLookEvents() {
        Logger.trace(" called", this);

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
        //System.out.println("END THE GAME");
        if (model.getIdWinner() != -1) {
            System.out.println(model.getPlayers().get(model.getIdWinner()).getName() + " wins");
        } else {
            System.out.println("Draw game");
        }

    }

    public void update() {
        frameNumber++;
        // process all events & updates
        processEvents();
        // update the view
        view.update();
    }

    /* ***************************************
       HELPERS METHODS
    **************************************** */

    /**
     * Get the look of a given element
     *
     * @param element the element for which the look is asked.
     * @return an ElementLook object that is the look of the element
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
}