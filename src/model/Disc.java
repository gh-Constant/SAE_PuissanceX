package model;

import boardifier.model.ElementTypes;
import boardifier.model.GameElement;
import boardifier.model.GameStageModel;

public class Disc extends GameElement {
    public static final int DISC_PLAYER1 = 0;
    public static final int DISC_PLAYER2 = 1;
    
    private int color;
    
    public Disc(int color, GameStageModel gameStageModel) {
        super(gameStageModel);
        //TODO Enregistrement du type et initialisation
    }
    
    // Getters
}