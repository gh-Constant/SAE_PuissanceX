package model;

import boardifier.model.ContainerElement;
import boardifier.model.ElementTypes;
import boardifier.model.GameStageModel;

import java.util.ArrayList;
import java.util.List;

public class PuissanceXBoard extends ContainerElement {

    public static final String BOARD_NAME = "board";

    static {
        ElementTypes.register(BOARD_NAME, 51); // Register a unique type for the board
    }
    
    public PuissanceXBoard(int rows, int cols, GameStageModel gameStageModel) {
        super(BOARD_NAME, 0, 0, rows, cols, gameStageModel);
        this.type = ElementTypes.getType(BOARD_NAME);
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

    public List<Integer> getAvailableCol() {
        List<Integer> availableCols = new ArrayList<>();
        for (int col = 0; col < this.getNbCols(); col++) {
            if (!this.isColumnFull(col)) {
                availableCols.add(col);
            }
        }
        return availableCols;
    }

    public boolean isFull() {
        return this.getAvailableCol().isEmpty();
    }
}