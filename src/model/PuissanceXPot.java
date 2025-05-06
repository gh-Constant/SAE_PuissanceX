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

    /**
     * Checks if a cell in the pot is reachable (can be interacted with).
     * @param row The row of the cell
     * @param col The column of the cell
     * @return true if the cell is reachable, false otherwise
     */
    public boolean isCellReachable(int row, int col) {
        // Check if the cell is within bounds
        if (row < 0 || row >= 1 || col < 0 || col >= 1) return false;
        // A cell is reachable if it's marked as reachable in the reachableCells array
        return canReachCell(row, col);
    }

    /**
     * Checks if the pot has any discs available.
     * @return true if there are discs available, false otherwise
     */
    public boolean isDiscAvailable() {
        return !this.isEmpty();
    }
}
