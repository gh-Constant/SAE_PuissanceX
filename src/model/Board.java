package model;

import boardifier.model.ContainerElement;
import boardifier.model.GameStageModel;
import boardifier.model.ElementTypes;

public class Board extends ContainerElement {
    
    static {
        ElementTypes.register("board", 51); // Register a unique type for the board
    }
    
    public Board(int rows, int cols, GameStageModel gameStageModel) {
        super("board", 0, 0, rows, cols, gameStageModel);
        this.type = ElementTypes.getType("board");
    }
    
    // Method to check if a column is full
    public boolean isColumnFull(int col) {
        if (col < 0 || col >= getNbCols()) return true;
        
        // In Puissance X, the top row (row 0) being empty means the column isn't full
        return !getElements(0, col).isEmpty();
    }
    
    // Method to find the first empty row in a column (for dropping a disk)
    public int getFirstEmptyRow(int col) {
        if (col < 0 || col >= getNbCols()) return -1;
        
        for (int row = getNbRows() - 1; row >= 0; row--) {
            if (getElements(row, col).isEmpty()) {
                return row;
            }
        }
        return -1; // Column is full
    }
}