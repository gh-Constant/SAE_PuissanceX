package model;

import boardifier.model.GameStageModel;
import boardifier.model.ContainerElement;

public class PuissanceXPot extends ContainerElement {
    
    // Identifiant du joueur propriétaire du pot
    private int playerId;
    private int nbDiscs;
    private Disc[] discs;
    
    public PuissanceXPot(int x, int y, int nbDiscs, GameStageModel gameStageModel, int playerId) {
        super("discpot", x, y, 1, nbDiscs, gameStageModel);
        
        this.playerId = playerId;
        this.nbDiscs = nbDiscs;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public void setDiscs(Disc[] discs) {
        this.discs = discs;
    }

    public Disc[] getDiscs() {
        return discs;
    }

    public void addDisc(Disc disc) {
        this.addElement(disc, 0, this.discs.length);
    }

    public void removeDisc(Disc disc) {
        this.removeElement(disc);
    }

    public int getNbDiscs() {
        return nbDiscs;
    }

    public boolean isDiscAvailable() {
        return !this.isEmpty();
    }
}
