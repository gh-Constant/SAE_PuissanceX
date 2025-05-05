package model;

import boardifier.model.GameStageModel;
import boardifier.model.ContainerElement;
import java.util.List;
import java.awt.Point;

public class PuissanceXBoard extends ContainerElement {
    private int rows;
    private int cols;
    private int winCondition;
    
    public PuissanceXBoard(int x, int y, int rows, int cols, int winCondition, GameStageModel gameStageModel) {
        super("puissancexboard", x, y, rows, cols, gameStageModel);
        
        // Store dimensions and win condition
        this.rows = rows;
        this.cols = cols;
        this.winCondition = winCondition;
        
        // Initialize the grid (already done by ContainerElement constructor)
    }
    
    /**
     * Gets the number of rows in the board.
     * @return The number of rows
     */
    public int getRows() {
        return rows;
    }
    
    /**
     * Gets the number of columns in the board.
     * @return The number of columns
     */
    public int getCols() {
        return cols;
    }
    
    /**
     * Gets the win condition (number of discs in a row needed to win).
     * @return The win condition
     */
    public int getWinCondition() {
        return winCondition;
    }
    
    /**
     * Checks if a column is valid for placing a disc.
     * @param col The column to check
     * @return true if a disc can be placed in this column, false otherwise
     */
    public boolean isValidColumn(int col) {
        // TODO: Vérifier si la colonne est valide
        // 1. Vérifier si la colonne est dans les limites du plateau
        // 2. Vérifier si la colonne n'est pas pleine
        return false;
    }
    
    /**
     * Finds the first available row in a column.
     * @param col The column to check
     * @return The row index, or -1 if the column is full
     */
    public int getFirstAvailableRow(int col) {
        // TODO: Trouver la première ligne disponible dans une colonne
        // 1. Parcourir la colonne de bas en haut
        // 2. Retourner l'indice de la première cellule vide
        return -1;
    }
    
    /**
     * Checks if there is a winner starting from a specific position.
     * @param row The row of the last placed disc
     * @param col The column of the last placed disc
     * @param playerId The ID of the player who placed the disc
     * @return true if the player has won, false otherwise
     */
    public boolean checkWin(int row, int col, int playerId) {
        // TODO: Vérifier s'il y a un gagnant
        // 1. Vérifier l'alignement horizontal
        // 2. Vérifier l'alignement vertical
        // 3. Vérifier l'alignement diagonal (/)
        // 4. Vérifier l'alignement diagonal (\)
        return false;
    }
    
    /**
     * Checks if the board is full (draw).
     * @return true if the board is full, false otherwise
     */
    public boolean isFull() {
        // TODO: Vérifier si la grille est pleine
        // 1. Parcourir toutes les colonnes
        // 2. Vérifier si au moins une colonne n'est pas pleine
        return false;
    }
    
    /**
     * Sets the valid columns where a disc can be placed.
     */
    public void setValidColumns() {
        // TODO: Mettre à jour les colonnes valides
        // 1. Réinitialiser les cellules atteignables
        // 2. Marquer les colonnes non pleines comme atteignables
    }
}
