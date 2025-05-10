package model;

import boardifier.model.GameElement;
import boardifier.model.GameStageModel;

public class Disk extends GameElement {
    private int playerId;
    
    public Disk(int playerId, GameStageModel gameStageModel) {
        super(gameStageModel);
        this.playerId = playerId;
    }
    
    public int getPlayerId() {
        return playerId;
    }
}