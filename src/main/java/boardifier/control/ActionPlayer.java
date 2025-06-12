package boardifier.control;

import boardifier.model.*;
import boardifier.model.animation.Animation;
import boardifier.model.action.ActionList;
import boardifier.model.action.GameAction;
import javafx.application.Platform;
import java.util.List;


public class ActionPlayer extends Thread {

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

    public void run() {
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

        /* now check if call to nextPlayer() must be done, but only if not at the end of the stage/game
          NB: In some games, depending on the state of the stage, nextPlayer() will not really pass
          the hand to another player. In such case, the current player can play another turn.
         */
        if ((!model.isEndStage()) && (!model.isEndGame()) && (actions.mustDoEndOfTurn())) {
            Platform.runLater( () -> {control.endOfTurn();});
        }
    }

    private void playActions(ActionList actions) {
        // loop over all action packs
        int idPack = 0;
        for(List<GameAction> actionPack : actions.getActions()) {
            
            Logger.trace("playing pack "+idPack);

            // step 1 : execute action that must be placed before animation
            for(int i=0;i<actionPack.size();i++) {
                GameAction action = actionPack.get(i);
                if (!action.isAnimateBeforeExecute()) {
                    action.execute();
                }
            }

            // step 2 : start animations of the same pack
            Animation[] animations = new Animation[actionPack.size()];
            for(int i=0;i<actionPack.size();i++) {
                GameAction action = actionPack.get(i);
                animations[i] = action.setupAnimation();
                if (animations[i] != null) {
                    Logger.trace("starting animation "+i);
                    animations[i].start();
                }

            }
            // step 3 : wait for the end of all animations
            for(int i=0;i<actionPack.size();i++) {
                if (animations[i] != null) {
                    animations[i].getAnimationState().waitStop();
                    Logger.trace("end of animation "+i);
                }
            }

            // step 4 : execute action that must be placed after aniamtion
            for(int i=0;i<actionPack.size();i++) {
                GameAction action = actionPack.get(i);
                if (action.isAnimateBeforeExecute()) {
                    action.execute();
                }
            }

            // last enable event capture
            idPack++;
        }
    }
}
