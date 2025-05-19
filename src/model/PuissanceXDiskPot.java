package model;

import boardifier.model.GameStageModel;
import boardifier.model.ContainerElement;

public class PuissanceXDiskPot extends ContainerElement {
    public PuissanceXDiskPot(int x, int y, GameStageModel gameStageModel) {
        // call the super-constructor to create a 4x1 grid, named "pawnpot", and in x,y in space
        super("diskpot", x, y, 4, 1, gameStageModel);
    }
}
