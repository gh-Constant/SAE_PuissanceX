package model;

import boardifier.model.ContainerElement;
import boardifier.model.ElementTypes;
import boardifier.model.GameElement;
import boardifier.model.GameStageModel;

public class Board extends ContainerElement {
    
    static {
        ElementTypes.register("board", 51); // Register a unique type for the board
    }
    
    public Board(int rows, int cols, GameStageModel gameStageModel) {
        super("board", 0, 0, rows, cols, gameStageModel);
        this.type = ElementTypes.getType("board");
    }

    // Modified original override to call the new version with events enabled
    @Override
    public void addElement(GameElement element, int row, int col) {
        this.addElement(element, row, col, true); // Default behavior is to trigger events
    }

    // New method with doEvent flag
    public void addElement(GameElement element, int row, int col, boolean doEvent) {
        if ((row >= nbRows) || (col >= nbCols) || (row < 0) || (col < 0) ) return;
        int r = row;
        int c = col;
        // if row,col corresponds to a covered cell, get the origin
        if ((rowSpans[row][col] < 1) && (colSpans[row][col] < 1)) {
            r = -rowSpans[row][col];
            c = -colSpans[row][col];
        }
        grid[r][c].add(element);
        element.setContainer(this);

        if (doEvent) {
            // signal that element has changed of grid => the inner layout has to take ownership of the element look, which is initiated by the controller
            element.addPutInContainerEvent(this, row, col);
            // signal the stage model that element is put in grid so that callback can be exectued
            gameStageModel.putInContainer(element, this, row, col);
        }
    }

    // Override standard removeElement to call new version with events enabled
    @Override
    public void removeElement(GameElement element) {
        this.removeElement(element, true); // Default behavior is to trigger events
    }

    // New method with doEvent flag (finds element first)
    public void removeElement(GameElement element, boolean doEvent) {
        int[] coords = getElementCell(element);
        if (coords != null) {
            this.removeElement(element, coords[0], coords[1], doEvent);
        }
    }

    // New method with doEvent flag (for specific coordinates)
    // This mimics the private ContainerElement.removeElement(GameElement, int, int) but is public and respects doEvent
    public void removeElement(GameElement element, int row, int col, boolean doEvent) {
        if ((row >= nbRows) || (col >= nbCols) || (row < 0) || (col < 0)) return;
        int r = row;
        int c = col;
        // if row,col corresponds to a covered cell, get the origin
        if ((rowSpans[row][col] < 1) && (colSpans[row][col] < 1)) {
            r = -rowSpans[row][col];
            c = -colSpans[row][col];
        }
        // Ensure the element exists in the target cell before trying to remove
        if ((grid[r][c].isEmpty()) || (!grid[r][c].contains(element))) return;
        
        // Use r,c for removal as per span logic, consistent with addElement
        grid[r][c].remove(element); 
        element.setContainer(null);

        if (doEvent) {
            // signal that element has changed of grid => the inner layout has to take ownership of the element look, which is initiated by the controller
            element.addRemoveFromContainerEvent(this, row, col);
            // signal the stage model that element is removed from grid so that callback can be executed
            gameStageModel.removedFromContainer(element, this, row, col);
        }
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