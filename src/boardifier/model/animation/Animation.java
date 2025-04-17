package boardifier.model.animation;

import boardifier.model.Model;

import java.util.ArrayList;
import java.util.List;

public abstract class Animation {
    protected int duration; // in milliseconds
    protected int frameGap; // CAUTION : shoud be normally boardifier.model.Model.frameGap to have an animation synchronized with the framerate
    // the state of the animation
    protected AnimationState state;
    // the step i.e. the index in animation at which the current animation is.
    protected int animationStep;
    // the list of step data
    protected List<AnimationStep> steps;

    // the type
    protected int type;
    // the onEnd callbak
    AnimationCallback onEndCallback;

    public static AnimationStep NOPStep; // used to represent an animation step without any change to do, just waiting

    // in order to take into account that an animation duration may be
    // computed, we define 2 constructors with one that do not set duration
    public Animation(Model model, int type) {
        this(model, 0, type);
        // WARNING : the dev will have to explicitly call computeSteps()
    }

    public Animation(Model model, int duration, int type) {
        this.duration = duration;
        frameGap = model.getFrameGapInMs();
        state = new AnimationState();
        animationStep = 0;
        steps = new ArrayList<>();
        if (AnimationTypes.isValid(type)) {
            this.type = type;
        } else {
            this.type = AnimationTypes.getType("none");
        }
        // setup a NOP callback
        onEndCallback = () -> {
        };

        NOPStep = new AnimationStep();
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return AnimationTypes.getName(type);
    }

    public AnimationState getAnimationState() {
        return state;
    }

    public boolean isStarted() {
        return state.isStarted();
    }

    public boolean isPaused() {
        return state.isPaused();
    }

    public boolean isOff() {
        return state.isOff();
    }

    public void start() {
        state.start();
        animationStep = 0;
    }

    public void pause() {
        state.pause();
    }

    public void resume() {
        state.resume();
    }

    public void stop() {
        state.stop();
        onEndCallback.execute();
    }

    public abstract void computeSteps();

    public AnimationStep next() {
        if (state.isOff()) {
            return null;
        } else {
            AnimationStep step = steps.get(animationStep);
            // if it is active, increment or stop, and if it is paused, do nothin.
            if (state.isStarted()) {
                if (animationStep < steps.size() - 1) {
                    animationStep++;
                } else {
                    stop();
                }
            }
            return step;
        }
    }

    public void onEnd(AnimationCallback function) {
        onEndCallback = function;
    }
}
