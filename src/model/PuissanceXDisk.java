package model;

import boardifier.model.GameElement;
import boardifier.model.GameStageModel;

public class PuissanceXDisk extends GameElement {
    private int playerId;
    
    public PuissanceXDisk(int playerId, GameStageModel gameStageModel) {
        super(gameStageModel);
        this.playerId = playerId;
    }
    
    public int getPlayerId() {
        return playerId;
    }
}