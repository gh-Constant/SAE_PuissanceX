package boardifier.model.action;

import boardifier.control.Logger;
import boardifier.model.ContainerElement;
import boardifier.model.Coord2D;
import boardifier.model.GameElement;
import boardifier.model.Model;
import boardifier.model.animation.AnimationTypes;
import boardifier.model.animation.LinearMoveAnimation;
import boardifier.model.animation.MoveAnimation;


public class MoveWithinContainerAction extends GameAction {

    protected int rowDest;
    protected int colDest;
    protected double xDest;
    protected double yDest;
    protected double factor; // a speed in pixel/ms or the whole duration, see LinearMoveAnimation

    // construct an action with an animation
    public MoveWithinContainerAction(Model model, GameElement element, int rowDest, int colDest, String animationName, double xDest, double yDest, double factor) {
        super(model, element, animationName);

        this.rowDest = rowDest;
        this.colDest = colDest;
        this.xDest = xDest;
        this.yDest = yDest;
        this.factor = factor;
    }

    public MoveWithinContainerAction(Model model, GameElement element, int rowDest, int colDest) {
        this(model, element, rowDest, colDest, AnimationTypes.NONE, 0,0, 0);
    }

    public int getRowDest() {
        return rowDest;
    }

    public int getColDest() {
        return colDest;
    }

    public void execute() {
        ContainerElement container = element.getContainer();
        if (container != null) {

            container.moveElement(element, rowDest, colDest);
        }
        onEndCallback.execute();
    }

    protected void createAnimation() {
        animation = null;
        // only create an animation of type move/xxx
        if (animationName.startsWith("move")) {

            if (element.getContainer() == null) {
                Logger.trace("cannot create a move animation for an element that is not in a container");
                return;
            }
            Coord2D endLoc = new Coord2D(xDest, yDest);
            // create animation to visualize this movement
            if (animationType == AnimationTypes.MOVETELEPORT_VALUE) {
                animation = new MoveAnimation(model, element.getLocation(), endLoc);
            } else if ((animationType == AnimationTypes.MOVELINEARPROP_VALUE) ||
                    (animationType == AnimationTypes.MOVELINEARCST_VALUE)) {
                animation = new LinearMoveAnimation(model, element.getLocation(), endLoc, animationType, factor);
            }
        }
    }
}
