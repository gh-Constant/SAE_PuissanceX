package boardifier.model.action;

import boardifier.model.GameElement;
import boardifier.model.Model;
import boardifier.model.animation.Animation;
import boardifier.model.animation.AnimationCallback;
import boardifier.model.animation.AnimationTypes;

public abstract class GameAction {
    protected Model model;
    protected GameElement element;
    protected String animationName;
    protected int animationType;
    protected Animation animation;
    protected boolean animateBeforeExecute;

    // the onEnd callbak
    ActionCallback onEndCallback;

    public GameAction(Model model, GameElement element, String animationName) {
        this.model = model;
        this.element = element;
        if (AnimationTypes.isValid(animationName)) {
            this.animationName = animationName;
        }
        else {
            this.animationName = "none";
        }
        animationType = AnimationTypes.getType(animationName);
        animation = null;
        onEndCallback = () -> {};
        animateBeforeExecute = true; // by default the animation is played before the execution of the action
    }

    public boolean isAnimateBeforeExecute() {
        return animateBeforeExecute;
    }

    public void setAnimateBeforeExecute(boolean animateBeforeExecute) {
        this.animateBeforeExecute = animateBeforeExecute;
    }

    public GameElement getElement() {
        return element;
    }

    public void setElement(GameElement element) {
        this.element = element;
    }

    public Animation getAnimation() {
        return animation;
    }
    /**
     * create the animation associated to this action.
     * This method must be overridden in subclasses and called at the end
     * of their constructors
     */
    protected abstract void createAnimation();
    /**
     * initialize the animation.
     * Used in the ActionPlayer thread to prepare the animation before it is started
     */
    public Animation setupAnimation() {
        // first create the animation, if it is possible
        createAnimation();
        if (animation == null) return null;
        animation.computeSteps();
        element.setAnimation(animation);
        return  animation;
    }

    public void onAnimationEnd(AnimationCallback callback) {
        if (animation != null) {
            animation.onEnd(callback);
        }
    }

    public void onActionEnd(ActionCallback callback) {
        onEndCallback = callback;
    }
    public abstract void execute();
}
