package boardifier.model.action;

import boardifier.model.ContainerElement;
import boardifier.model.Coord2D;
import boardifier.model.GameElement;
import boardifier.model.Model;
import boardifier.model.animation.AnimationTypes;
import boardifier.model.animation.LinearMoveAnimation;
import boardifier.model.animation.MoveAnimation;
import boardifier.model.animation.WaitAnimation;


public class RemoveFromContainerAction extends GameAction {

    // construct an action with an animation
    public RemoveFromContainerAction(Model model, GameElement element) {
        super(model, element, AnimationTypes.WAIT_FRAMES);
        animateBeforeExecute = false;
    }

    public void execute() {
        // if the element is not within a container, do nothing
        if (element.getContainer() == null) return;
        element.waitForContainerOpEnd();
        element.getContainer().removeElement(element);
        onEndCallback.execute();
    }


    public void createAnimation() {
    }
}
