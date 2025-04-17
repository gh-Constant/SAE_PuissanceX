package boardifier.model.animation;

public class AnimationState {
    // the possibles states of the animation : started, paused, no animation
    public static final int STATE_ANIMATION_NO = 0;
    public static final int STATE_ANIMATION_STARTED = 1;
    public static final int STATE_ANIMATION_PAUSED = 2;
    // the current state of an animation
    protected int state;

    public AnimationState() {
        state = STATE_ANIMATION_NO;
    }

    public int getState() {
        return state;
    }
    public boolean isStarted() {
        return state == STATE_ANIMATION_STARTED;
    }
    public boolean isPaused() {
        return state == STATE_ANIMATION_PAUSED;
    }
    public boolean isOff() {
        return state == STATE_ANIMATION_NO;
    }

    public synchronized void start() {
        state = STATE_ANIMATION_STARTED;
    }
    public synchronized void pause() {
        state = STATE_ANIMATION_PAUSED;
    }
    public synchronized void resume() {
        state = STATE_ANIMATION_STARTED;
    }
    public synchronized void stop() {
        state = STATE_ANIMATION_NO;
        notifyAll();
    }
    public synchronized void waitStop() {
        while (state != STATE_ANIMATION_NO) {
            try {
                wait();
            }
            catch(InterruptedException e) {}
        }
    }
}
