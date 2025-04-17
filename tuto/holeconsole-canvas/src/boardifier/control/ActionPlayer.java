package boardifier.control;

import boardifier.model.*;
import boardifier.model.action.ActionList;
import boardifier.model.action.GameAction;
import java.util.List;


public class ActionPlayer {

    protected Controller control;
    protected Model model;
    protected Decider decider;
    protected ActionList actions;
    protected ActionList preActions;

    public ActionPlayer(Model model, Controller control, Decider decider, ActionList preActions) {
        this.model = model;
        this.control = control;
        this.actions = null;
        this.decider = decider;
        this.preActions = preActions;
    }

    public ActionPlayer(Model model, Controller control, ActionList actions) {
        this.model = model;
        this.control = control;
        this.actions = actions;
        this.decider = null;
        this.preActions = null;
    }

    public void start() {
        // first disable event capture
        model.setCaptureEvents(false);

        if (preActions != null) {
            playActions(preActions);
        }
        // if there is a decider, decide what to do
        if (decider != null) {
            actions = decider.decide();
        }

        playActions(actions);

        model.setCaptureEvents(true);

    }

    private void playActions(ActionList actions) {
        // loop over all action packs
        int idPack = 0;
        for(List<GameAction> actionPack : actions.getActions()) {
            System.out.println("playing pack "+idPack);
            // step 4 : do the real actions, based on action.type
            for(GameAction action : actionPack) {
                action.execute();
            }
            // last enable event capture
            idPack++;
        }
    }
}
