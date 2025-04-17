package boardifier.model;

import boardifier.model.animation.AnimationStep;

import java.util.ArrayList;
import java.util.List;


/**
 * A basic sprite element.
 *
 *  A Sprite is an element that is moved in x,y directions according to a speed in these both directions.
 *  Since at each frame, update() is called by the ControllerAnimation, the Sprite is continuously moving
 *  in the same direction until xSpeed,ySpeed are changed. At the same time, the sprite aspect can change cyclically
 *  every framesPerFaceChange. Each aspect is called a "face", and different faces of the same sprite can be set of images
 *  or a set of shapes (see SpriteImageLook and SpriteDrawnLook).
 *
 *  The move can be stopped by calling stopMoving() or when an obstacle is encountered. When the sprite is falling,
 *  the default behaviour is to nullify xSpeed.
 *
 *  The sprite may also be animated, which is in this class supersede the process of moving the sprite.
 *
 *  Since the behaviour of a sprite a totally game-dependent, the update() method should be overridden in subclasses to
 *  reflect the desired sprite behavior.
 */
public class SpriteElement extends GameElement {

    // the number of different faces of this element.
    protected int nbFaces;
    /* a list of face indexes to use
       by default, this list is initialized to 0, 1, ..., nbFaces-1 in order to
       follow and show in sequence all faces. But it can be set to any other valid
       sequence of any size.
     */
    protected List<Integer> faceIndexes;
    // the index to select an entry in faceIndexes.
    // It is not used directly but to determine the index of the current face, i.e. faceIndexes.get(currentIndex)
    protected int currentIndex;

    // the state values
    public final static int SPRITE_STATE_IDLE = 0;
    public final static int SPRITE_STATE_MOVING = 1;
    public final static int SPRITE_STATE_FALLING = 2;
    public final static int SPRITE_STATE_COLLIDE = 3;
    public final static int SPRITE_STATE_JUMPING = 4;
    // convenient values for some cases of move.
    public final static int MOVE_NONE = -1;
    public final static int MOVE_RIGHT = 0;
    public final static int MOVE_UP = 1;
    public final static int MOVE_LEFT = 2;
    public final static int MOVE_DOWN = 3;
    // the current state
    protected int state;
    // the current x,y speed
    protected double xSpeed;
    protected double ySpeed;
    // allows to setup the number of frames to pass before changing the current image index
    protected int framesPerFaceChange;

    // frame counter, related to framesPerLookChange
    protected int frameCounter;

    public SpriteElement(int nbFaces, GameStageModel gameStageModel) {
        super(gameStageModel, ElementTypes.getType("sprite"));
        this.nbFaces = nbFaces;
        currentIndex = 0;
        faceIndexes = new ArrayList<>();
        // by default, use all faces sequentially
        for (int i=0;i<nbFaces;i++) faceIndexes.add(i);
        // enforce the  fact that the look changed to get the first display of this element.
        // otherwise it won't show until something change its state.
        addChangeFaceEvent();

        state = SPRITE_STATE_IDLE;
        xSpeed = 0;
        ySpeed = 0;
        framesPerFaceChange = 0; // no change of appearance by default
        frameCounter = 0;
    }

    public int getNbFaces() {
        return nbFaces;
    }

    public List<Integer> getFaceIndexes() {
        return faceIndexes;
    }

    public void setFaceIndexes(List<Integer> faceIndexes) {
        this.faceIndexes = faceIndexes;
    }

    public int getCurrentFaceIndex() {
        return faceIndexes.get(currentIndex);
    }

    public void setCurrentIndex(int newIndex) {
        if (newIndex != currentIndex) {
            currentIndex = newIndex;
            addChangeFaceEvent();
        }
    }

    public int getState() {
        return state;
    }
    public void setState(int state) {
        this.state = state;
    }

    public void setFramesPerFaceChange(int framesPerFaceChange) {
        this.framesPerFaceChange = framesPerFaceChange;
    }

    public double getxSpeed() {
        return xSpeed;
    }
    public void setxSpeed(double xSpeed) {
        this.xSpeed = xSpeed;
    }

    public double getySpeed() {
        return ySpeed;
    }
    public void setySpeed(double ySpeed) {
        this.ySpeed = ySpeed;
    }

    public void stopMoving(){
        state = SPRITE_STATE_IDLE;
        xSpeed = 0;
        ySpeed = 0;
    }
    public void startMoving(){
        state = SPRITE_STATE_MOVING;
    }
    public void startFalling(){
        state = SPRITE_STATE_FALLING;
        xSpeed = 0;
    }

    /**
     * Update the sprite state.
     * Since the behaviour of a sprite is totally dependent of the game (some collisions may
     * stop it, some others don't), this method only updates what can be done for sure
     * i.e. processing a started animation or the frame counter, and moving the sprite
     * if it moving/falling.
     * This method SHOULD BE OVERRIDDEN in subclasses to fit with the desired behavior.
     */
    public void update() {

        updateAnimation();

        updateLocation();

        // do not update the index if an animation is already running
        if ((animation == null) && (framesPerFaceChange > 0)){
            updateIndex();
        }
    }

    public void updateAnimation() {
        if (animation != null) {
            AnimationStep step = animation.next();
            if (step != null) {
                if (animation.getName().startsWith("move")) {
                    setLocation(step.getInt(0), step.getInt(1));
                } else if (animation.getName().startsWith("look")) {
                    setCurrentIndex(step.getInt(0));
                }
            }
            else {
                animation = null;
            }
        }
    }

    public void updateLocation() {
        if ((state == SPRITE_STATE_FALLING) || (state == SPRITE_STATE_MOVING)) {
            relativeMove(xSpeed, ySpeed);
        }
    }

    /**
     * Default method to update the index of faces.
     * This method just cycles over the possible faces. It can be overridden in subclasses to
     * change this behaviour
     */
    public void updateIndex() {
        frameCounter++;
        if (frameCounter == framesPerFaceChange) {
            setCurrentIndex((currentIndex + 1) % faceIndexes.size());
            frameCounter = 0;
        }
    }


}
