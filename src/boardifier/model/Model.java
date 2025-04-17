package boardifier.model;

import java.util.ArrayList;
import java.util.List;

public class Model {

    protected int state; // the game state
    protected final static int STATE_INIT = 1; // e.g. the intro screen
    protected final static int STATE_PLAY = 2; // when player is playing within a stage
    protected final static int STATE_PAUSED = 3; // when game is paused
    protected final static int STATE_ENDSTAGE = 4; // when current level  has ended
    protected final static int STATE_ENDGAME = 5; // when game has ended
    // and so on

    // the following boolean are used to switch on/off capture of events, notably, during animations.
    protected boolean captureMouseEvent;
    protected boolean captureKeyEvent;
    protected boolean captureActionEvent;

    // time related attributes
    /* NB : we can set the framerate by specifying a gap (cf. frameGap)
       between two calls to methods that are used to update the position of
       objects in the scene.
       CAUTION : in order to achieve a smooth animation, the update methods must take less time to compute the new state/location
       of objects than the value of frameGap.
     */
    protected long frameGap;
    protected long lastFrame; // used to regulate FPS

    protected GameStageModel gameStageModel; // the current game stage
    // the players
    protected List<Player> players;
    // the id of the current player
    protected int idPlayer;
    // the id of the winner
    protected int idWinner;
    // the coordinates of the last click (may be used in update() to do something implied by a mouse click)
    // NB : the last key stroke is store in players (useful in case there are several players)
    protected Coord2D lastClick;


    public Model(long frameGap) {
        state = STATE_INIT;
        captureKeyEvent = false;
        captureMouseEvent = false;
        captureActionEvent = false;
        lastFrame = -1;
        players = new ArrayList<>();
        idPlayer = 0;
        idWinner = -1;
        lastClick = new Coord2D(-1,-1);
        gameStageModel = null;

        this.frameGap = frameGap;
    }

    public Model() {
        this(10000000); // 10000000ns = 10ms
    }

    public void reset() {
        state = STATE_INIT;
        captureKeyEvent = false;
        captureMouseEvent = false;
        captureActionEvent = false;
        lastFrame = -1;
        // do not clear players, since it is not initialized in createElements
        idPlayer = 0;
        idWinner = -1;
        lastClick = new Coord2D(-1,-1);
        gameStageModel = null;
        for(Player p : players) {
            p.reset();
        }
    }

    public boolean isCaptureMouseEvent() {
        return captureMouseEvent;
    }
    public void toggleCaptureMouseEvent() {
        captureMouseEvent = !captureMouseEvent;
    }
    public void setCaptureMouseEvent(boolean captureMouseEvent) {
        this.captureMouseEvent = captureMouseEvent;
    }

    public boolean isCaptureKeyEvent() {
        return captureKeyEvent;
    }
    public void toggleCaptureKeyEvent() {
        captureKeyEvent = !captureKeyEvent;
    }

    public void setCaptureKeyEvent(boolean captureKeyEvent) {
        this.captureKeyEvent = captureKeyEvent;
    }

    public boolean isCaptureActionEvent() {
        return captureActionEvent;
    }
    public void toggleCaptureActionEvent() {
        captureActionEvent = !captureActionEvent;
    }

    public void setCaptureActionEvent(boolean captureActionEvent) {
        this.captureActionEvent = captureActionEvent;
    }

    public void toggleCaptureEvents() {
        toggleCaptureKeyEvent();
        toggleCaptureActionEvent();
        toggleCaptureMouseEvent();
    }
    public void setCaptureEvents(boolean state) {
        setCaptureMouseEvent(state);
        setCaptureKeyEvent(state);
        setCaptureActionEvent(state);
    }

    public long getFrameGap() {
        return frameGap;
    }
    public void setFrameGap(long frameGap) {
        this.frameGap = frameGap;
    }
    public int getFrameGapInMs() {
        return (int)(frameGap/1000000);
    }

    public long getLastFrame() {
        return lastFrame;
    }
    public void setLastFrame(long lastFrame) {
        this.lastFrame = lastFrame;
    }

    public GameStageModel getGameStage() {
        return gameStageModel;
    }

    public void setGameStage(GameStageModel gameStageModel) {
        this.gameStageModel = gameStageModel;
    }

    public void startGame(GameStageModel gameStageModel)  {
        startStage(gameStageModel);
    }
    public void startStage(GameStageModel gameStageModel)  {
        reset();
        this.gameStageModel = gameStageModel;
        state = STATE_PLAY;
        setCaptureEvents(true);
    }

    public boolean isStageStarted() {
        return state == STATE_PLAY;
    }
    public void pauseGame() {
        state = STATE_PAUSED;
        setCaptureActionEvent(false);
        setCaptureMouseEvent(false);
    }
    public void resumeGame() {
        state = STATE_PLAY;
        setCaptureActionEvent(true);
        setCaptureMouseEvent(true);
    }

    public void stopStage() {
        state = STATE_ENDSTAGE;
    }
    public boolean isEndStage() {
        return state == STATE_ENDSTAGE;
    }


    public boolean isEndGame() {
        return state == STATE_ENDGAME;
    }
    public void stopGame() {
        state = STATE_ENDGAME;
    }

    public List<Player> getPlayers() {
        return players;
    }
    public void addHumanPlayer(String name) {
        players.add(Player.createHumanPlayer(name));
    }
    public void addComputerPlayer(String name) {
        players.add(Player.createComputerPlayer(name));
    }

    public int getIdPlayer() {
        return idPlayer;
    }
    public void setIdPlayer(int idPlayer) {
        this.idPlayer = idPlayer;
    }
    public Player getCurrentPlayer() {
        return players.get(idPlayer);
    }
    public String getCurrentPlayerName() {
        return players.get(idPlayer).getName();
    }
    public void setNextPlayer() {
        idPlayer = (idPlayer+1)%players.size();
    }

    public int getIdWinner() {
        return idWinner;
    }
    public void setIdWinner(int idWinner) {
        this.idWinner = idWinner;
    }

    public String getPlayerLastKey(int idPlayer) {
        return players.get(idPlayer).getLastKeyPressed();
    }
    public boolean isPlayerKeyPressed(int idPlayer, String name) {
        return players.get(idPlayer).isKeyPressed(name);
    }
    public void addPlayerKeyPressed(int idPlayer, String name) {
        players.get(idPlayer).addKeyPressed(name);
    }
    public void removePlayerKeyPressed(int idPlayer, String name) {
        players.get(idPlayer).removeKeyPressed(name);
    }
    public Coord2D getLastClick() {
        return lastClick;
    }
    public void setLastClick(Coord2D lastClick) {
        this.lastClick = lastClick;
    }

    /* *********************************************
       TRAMPOLINE METHODS
       NB: gain access to the current game stage
     ********************************************* */
    public List<GameElement> getElements() {
        if (gameStageModel == null) return null;
        return gameStageModel.getElements();
    }
    public List<GameElement> elementsByType(int type) {
        if (gameStageModel == null) return null;
        return gameStageModel.elementsByType(type);
    }
    public List<ContainerElement> getContainers() {
        if (gameStageModel == null) return null;
        return gameStageModel.getContainers();
    }

    public ContainerElement getContainer(String name) {
        for(ContainerElement container : getContainers()) {
            if (container.name.equals(name)) return container;
        }
        return null;
    }
    public List<GameElement> getSelected() {
        if (gameStageModel == null) return null;
        return gameStageModel.getSelected();
    }

    // method called by GameElement when they are selected to keep track of all selected elements (if needed)
    public void setSelected(GameElement element, boolean selected) {
        if (gameStageModel == null) return;
        gameStageModel.setSelected(element, selected);
    }

    public void unselectAll() {
        if (gameStageModel == null) return;
        gameStageModel.unselectAll();
    }
}
