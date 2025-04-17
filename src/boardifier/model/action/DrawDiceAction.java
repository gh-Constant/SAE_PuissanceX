package boardifier.model.action;

import boardifier.model.*;
import boardifier.model.animation.AnimationTypes;
import boardifier.model.animation.CyclicFaceAnimation;

import java.util.Calendar;
import java.util.Random;

public class DrawDiceAction extends GameAction {

    private static Random loto = new Random(Calendar.getInstance().getTimeInMillis());
    /**
     * The number of cycles of random draw
     * This value multiplied by the number of sides of the dice gives the number of randomly generated values.
     * Each random value is drawn between 0 and nbSide-1.
     */
    int nbCycles;
    /**
     * The time in ms between two changes of the face of the dice.
     */
    int waitBetweenSides;

    /**
     * constructor to draw a dice without an animation.
     * @param model
     * @param element
     */
    public DrawDiceAction(Model model, GameElement element) {
        this(model, element, 0, 0);
        animationType = AnimationTypes.getType("none");
    }

    /**
     * constructor to draw a dice with an animation.
     * @param model
     * @param element
     * @param nbCycles
     * @param waitBetweenSides
     */
    public DrawDiceAction(Model model, GameElement element, int nbCycles, int waitBetweenSides) {
        super(model, element, "look/random");
        this.nbCycles = nbCycles;
        this.waitBetweenSides = waitBetweenSides;
    }

    public void execute() {
        if (element.getType() != ElementTypes.getType("dice")) return;
        if (animation == null) {
            DiceElement d = (DiceElement)element;
            d.setCurrentValue(1+loto.nextInt(d.getNbSides()));
        }
        onEndCallback.execute();
    }

    protected void createAnimation() {
        animation = null;
        if (element.getType() != ElementTypes.getType("dice")) return;
        DiceElement d = (DiceElement)element;
        animation = new CyclicFaceAnimation(model, d.getNbSides(), nbCycles, waitBetweenSides, true);
    }
}
