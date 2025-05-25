package model;

import boardifier.model.ContainerElement;
import boardifier.model.ElementTypes;
import boardifier.model.GameStageModel;

public class PuissanceXDiskPot extends ContainerElement {
    public static final String POT_NAME = "diskpot";
    
    static {
        ElementTypes.register(POT_NAME, 52); // Register a unique type for the pot
    }
    
    public PuissanceXDiskPot(int x, int y, GameStageModel gameStageModel) {
        super(POT_NAME, x, y, 3, 1, gameStageModel);
        this.type = ElementTypes.getType(POT_NAME);
        setLocation(x, y);
    }
}
