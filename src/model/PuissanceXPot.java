package model;

import boardifier.model.GameStageModel;
import boardifier.model.ContainerElement;

public class PuissanceXPot extends ContainerElement {
    
    // Identifiant du joueur propriétaire du pot
    private int playerId;
    private int nbDiscs;
    
    public PuissanceXPot(int x, int y, int nbDiscs, GameStageModel gameStageModel, int playerId) {
        super("discpot", x, y, 1, nbDiscs, gameStageModel);
        
        this.playerId = playerId;
        this.nbDiscs = nbDiscs;
    }

    public int getPlayerId() {
        return playerId;
    }

    public int getNbDiscs() {
        return nbDiscs;
    }

    public boolean isDiscAvailable() {
        return !this.isEmpty();
    }

    public Disc getNextDisc() {
        if (this.isEmpty()) {
            return null;
        }
        Disc newDisc = new Disc(0, 0, playerId, this.gameStageModel);
        this.removeElement(newDisc);
        return newDisc;
    }
}
