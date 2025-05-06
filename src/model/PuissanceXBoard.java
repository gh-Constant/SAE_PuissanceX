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
        this.rows = rows;
        this.cols = cols;
        this.winCondition = winCondition;
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
        return this.isEmptyAt(rows, col);
    }
    
    /**
     * Finds the first available row in a column.
     * @param col The column to check
     * @return The row index, or -1 if the column is full
     */
    public int getFirstAvailableRow(int col) {
        for (int row = rows - 1; row >= 0; --row) {
            if (this.isEmptyAt(row, col)) {
                return row;
            }
        }
        return -1;
    }

    /**
     * Checks if there is a winner starting from a specific position horizontaly.
     * @param row The row of the last placed disc
     * @param col The column of the last placed disc
     * @param playerId The ID of the player who placed the disc
     * @return true if the player has won, false otherwise
     */
    public boolean checkHorizontal(int row, int col, int playerId) {
        int count = 0;
        int prevElement = -1;
        for (int i = 0; i < cols; i++) {
            if (this.isEmptyAt(row, i)) {
                break;
            }
            if (this.getElement(row, i).getType() == prevElement) {
                count++;
            } else {
                count = 1;
                prevElement = this.getElement(row, i).getType();
            }
            if (count == winCondition && this.getElement(row, i).getType() == playerId) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if there is a winner starting from a specific position verticaly.
     * @param row The row of the last placed disc
     * @param col The column of the last placed disc
     * @param playerId The ID of the player who placed the disc
     * @return true if the player has won, false otherwise
     */
    public boolean checkVertical(int row, int col, int playerId) {
        int count = 0;
        int prevElement = -1;
        for (int i = 0; i < rows; i++) {
            if (this.isEmptyAt(i, col)) {
                break;
            }
            if (this.getElement(i, col).getType() == prevElement) {
                count++;
            } else {
                count = 1;
                prevElement = this.getElement(i, col).getType();
            }
            if (count == winCondition && this.getElement(i, col).getType() == playerId) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if there is a winner starting from a specific position diagonally (/ direction).
     * @param row The row of the last placed disc
     * @param col The column of the last placed disc
     * @param playerId The ID of the player who placed the disc
     * @return true if the player has won, false otherwise
     */
    public boolean checkDiagonal1(int row, int col, int playerId) {
        int count = 0;
        int prevElement = -1;
        int r = row;
        int c = col;

        while (r >= 0 && c >= 0) {
            if (this.isEmptyAt(r, c)) {
                break;
            }
            if (this.getElement(r, c).getType() == prevElement) {
                count++;
            } else {
                count = 1;
                prevElement = this.getElement(r, c).getType();
            }
            if (count == winCondition && this.getElement(r, c).getType() == playerId) {
                return true;
            }
            r--;
            c--;
        }

        r = row + 1;
        c = col + 1;
        while (r < rows && c < cols) {
            if (this.isEmptyAt(r, c)) break;
            if (this.getElement(r, c).getType() == prevElement) {
                count++;
            } else {
                count = 1;
                prevElement = this.getElement(r, c).getType();
            }
            if (count == winCondition && this.getElement(r, c).getType() == playerId) {
                return true;
            }
            r++;
            c++;
        }

        return false;
    }

    /**
     * Checks if there is a winner starting from a specific position diagonally (\ direction).
     * @param row The row of the last placed disc
     * @param col The column of the last placed disc
     * @param playerId The ID of the player who placed the disc
     * @return true if the player has won, false otherwise
     */
    public boolean checkDiagonal2(int row, int col, int playerId) {
        int count = 0;
        int prevElement = -1;

        int r = row;
        int c = col;
        while (r >= 0 && c < cols) {
            if (this.isEmptyAt(r, c)) break;
            if (this.getElement(r, c).getType() == prevElement) {
                count++;
            } else {
                count = 1;
                prevElement = this.getElement(r, c).getType();
            }
            if (count == winCondition && this.getElement(r, c).getType() == playerId) {
                return true;
            }
            r--;
            c++;
        }

        r = row + 1;
        c = col - 1;
        while (r < rows && c >= 0) {
            if (this.isEmptyAt(r, c)) break;
            if (this.getElement(r, c).getType() == prevElement) {
                count++;
            } else {
                count = 1;
                prevElement = this.getElement(r, c).getType();
            }
            if (count == winCondition && this.getElement(r, c).getType() == playerId) {
                return true;
            }
            r++;
            c--;
        }

        return false;
    }
    
    /**
     * Checks if there is a winner starting from a specific position.
     * @param row The row of the last placed disc
     * @param col The column of the last placed disc
     * @param playerId The ID of the player who placed the disc
     * @return true if the player has won, false otherwise
     */
    public boolean checkWin(int row, int col, int playerId) {
        return this.checkHorizontal(row, col, playerId) 
                || this.checkVertical(row, col, playerId) 
                || this.checkDiagonal1(row, col, playerId) 
                || this.checkDiagonal2(row, col, playerId);
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

    /**
     * Checks if a column is playable (can accept a disc).
     * @param col The column to check
     * @return true if a disc can be placed in this column, false otherwise
     */
    public boolean isColumnPlayable(int col) {
        // A column is playable if it's within bounds and the top cell is empty
        if (col < 0 || col >= cols) return false;
        return isEmptyAt(0, col);
    }

    /**
     * Checks if a cell is empty.
     * @param row The row of the cell
     * @param col The column of the cell
     * @return true if the cell is empty, false otherwise
     */
    public boolean isCellEmpty(int row, int col) {
        // Check if the cell is within bounds
        if (row < 0 || row >= rows || col < 0 || col >= cols) return false;
        // A cell is empty if there's no element at that position
        return isEmptyAt(row, col);
    }

    /**
     * Checks if a cell is reachable (can be interacted with).
     * @param row The row of the cell
     * @param col The column of the cell
     * @return true if the cell is reachable, false otherwise
     */
    public boolean isCellReachable(int row, int col) {
        // Check if the cell is within bounds
        if (row < 0 || row >= rows || col < 0 || col >= cols) return false;
        // A cell is reachable if it's marked as reachable in the reachableCells array
        return canReachCell(row, col);
    }

    /**
     * Gets the number of columns in the board.
     * @return The number of columns
     */
    public int getNbCols() {
        return cols;
    }

    /**
     * Gets the number of rows in the board.
     * @return The number of rows
     */
    public int getNbRows() {
        return rows;
    }
}
