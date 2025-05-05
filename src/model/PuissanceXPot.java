package model;

import boardifier.model.GameStageModel;
import boardifier.model.ContainerElement;

public class PuissanceXPot extends ContainerElement {
    public PuissanceXPot(int x, int y, int nbDiscs, GameStageModel gameStageModel) {
        super("discpot", x, y, 1, nbDiscs, gameStageModel);
    }
}