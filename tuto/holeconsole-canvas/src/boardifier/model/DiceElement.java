package boardifier.model;

public class DiceElement extends SpriteElement {

    protected int nbSides;

    public DiceElement(int nbSides, GameStageModel gameStageModel) {
        super(nbSides, gameStageModel);
        this.nbSides = nbSides;
        type = ElementTypes.getType("dice");
    }

    public int getNbSides() {
        return nbSides;
    }

    public void setCurrentValue(int value) {
        if ((value <1) || (value > nbSides)) return;
        setCurrentIndex(value-1);
    }

    public int getCurrentValue() {
        return currentIndex+1;
    }
}
