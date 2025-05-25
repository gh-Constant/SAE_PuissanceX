package boardifier.model.action;

import java.util.ArrayList;
import java.util.List;

public class ActionList {

    protected  List<List<GameAction>> actions;
    protected List<GameAction> currentPack;
    protected boolean doEndOfTurn; // if true ActionPlayer will trigger a nextPlayer event after all action have been played

    public ActionList() {
        this(false);
    }

    public ActionList(boolean doEndOfTurn) {
        actions = new ArrayList<>();
        currentPack = null;
        this.doEndOfTurn = doEndOfTurn;
    }

    public void setDoEndOfTurn(boolean doEndOfTurn) {
        this.doEndOfTurn = doEndOfTurn;
    }

    public void addActionPack() {
        currentPack = new ArrayList<>();
        actions.add(currentPack);
    }

    public void addSingleAction(GameAction gameAction) {
        List<GameAction> list = new ArrayList<>();
        list.add(gameAction);
        actions.add(list);
    }

    public void addAll(ActionList list) {
        actions.addAll(list.getActions());
    }

    public void addPackAction(GameAction gameAction) {
        currentPack.add(gameAction);
    }

    public List<List<GameAction>> getActions() {
        return actions;
    }

    public boolean mustDoEndOfTurn() {
        return doEndOfTurn;
    }
}
