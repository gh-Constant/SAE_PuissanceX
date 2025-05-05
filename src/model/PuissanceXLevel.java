package model;

import boardifier.control.Logger;
import boardifier.model.GameStageModel;
import boardifier.model.Model;
import boardifier.model.StageElementsFactory;
import boardifier.model.TextElement;

public class PuissanceXLevel extends GameStageModel {
    // Éléments du jeu
    private PuissanceXBoard board;
    private PuissanceXPot[] playerPots;
    private Disc[][] playerDiscs;
    private TextElement playerName;
    
    // Variables d'état
    private int rows;
    private int cols;
    private int winCondition;
    
    public PuissanceXLevel(String name, Model model) {
        super(name, model);
        
        // TODO: Initialisation
        // 1. Récupérer les paramètres du jeu (rows, cols, winCondition)
        // 2. Initialiser les tableaux pour les pots et les jetons
        // 3. Créer l'élément texte pour afficher le joueur actif
    }
    
    /**
     * Gets the game board
     * @return The game board
     */
    public PuissanceXBoard getBoard() {
        return board;
    }
    
    /**
     * Sets the game board
     * @param board The game board
     */
    public void setBoard(PuissanceXBoard board) {
        this.board = board;
    }
    
    /**
     * Gets the pot for a specific player
     * @param playerId The player ID
     * @return The player's pot
     */
    public PuissanceXPot getPlayerPot(int playerId) {
        return playerPots[playerId];
    }
    
    /**
     * Gets all player pots
     * @return Array of player pots
     */
    public PuissanceXPot[] getPlayerPots() {
        return playerPots;
    }
    
    /**
     * Sets the player pots
     * @param playerPots Array of player pots
     */
    public void setPlayerPots(PuissanceXPot[] playerPots) {
        this.playerPots = playerPots;
    }
    
    /**
     * Gets the discs for a specific player
     * @param playerId The player ID
     * @return Array of player's discs
     */
    public Disc[] getPlayerDiscs(int playerId) {
        return playerDiscs[playerId];
    }
    
    /**
     * Gets all player discs
     * @return 2D array of player discs
     */
    public Disc[][] getPlayerDiscs() {
        return playerDiscs;
    }
    
    /**
     * Sets the player discs
     * @param playerDiscs 2D array of player discs
     */
    public void setPlayerDiscs(Disc[][] playerDiscs) {
        this.playerDiscs = playerDiscs;
    }
    
    /**
     * Gets the player name text element
     * @return The player name text element
     */
    public TextElement getPlayerName() {
        return playerName;
    }
    
    /**
     * Sets the player name text element
     * @param playerName The player name text element
     */
    public void setPlayerName(TextElement playerName) {
        this.playerName = playerName;
    }
    
    /**
     * Gets the number of rows in the board
     * @return The number of rows
     */
    public int getRows() {
        return rows;
    }
    
    /**
     * Gets the number of columns in the board
     * @return The number of columns
     */
    public int getCols() {
        return cols;
    }
    
    /**
     * Gets the win condition
     * @return The win condition
     */
    public int getWinCondition() {
        return winCondition;
    }
    
    @Override
    public StageElementsFactory getDefaultElementFactory() {
        return new PuissanceXStageFactory(this);
    }
}