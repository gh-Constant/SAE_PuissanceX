package view.console;

import boardifier.model.ContainerElement;
import boardifier.view.ClassicBoardLook;
import boardifier.view.ConsoleColor;
import model.PuissanceXBoard;

/**
 * PuissanceXBoardLook defines the visual representation of the Puissance X game board in console mode.
 * It extends ClassicBoardLook to create a grid with borders and coordinates.
 */
public class PuissanceXBoardLook extends ClassicBoardLook {
    
    /**
     * Creates a new PuissanceXBoardLook for the given board.
     * 
     * @param board The PuissanceXBoard to create a look for
     */
    public PuissanceXBoardLook(ContainerElement board) {
        // Use 1 row height, 2 column width, depth 0, border width 1, and show coordinates
        super(1, 2, board, 0, 1, true);
    }
    
    @Override
    protected void renderBorders() {
        // First call the parent method to draw the basic borders
        super.renderBorders();
        
        // Add blue color to the borders to make the board stand out
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                String cell = shape[i][j];
                if (cell != null && !cell.equals(" ")) {
                    // Check if the cell contains a border character
                    if (cell.equals("─") || cell.equals("│") || 
                        cell.equals("┌") || cell.equals("┐") || 
                        cell.equals("└") || cell.equals("┘") || 
                        cell.equals("┬") || cell.equals("┴") || 
                        cell.equals("├") || cell.equals("┤") || 
                        cell.equals("┼")) {
                        shape[i][j] = ConsoleColor.BLUE + cell + ConsoleColor.RESET;
                    }
                }
            }
        }
    }

    public void onReachableChange() {
        PuissanceXBoard board = (PuissanceXBoard)element;
        
        // Highlight reachable cells (columns where discs can be dropped)
        for (int col = 0; col < board.getNbCols(); col++) {
            if (board.isColumnPlayable(col)) {
                // Highlight the top cell of each playable column
                int row = 0;
                if (board.isCellReachable(row, col)) {
                    // Get the position in the shape array for this cell
                    int shapeRow = innersTop + (int)(row * rowHeight + rowHeight/2);
                    int shapeCol = innersLeft + (int)(col * colWidth + colWidth/2);
                    
                    // Add a visual indicator for reachable cells
                    shape[shapeRow][shapeCol] = ConsoleColor.CYAN + "▼" + ConsoleColor.RESET;
                }
            }
        }
    }
    
    /**
     * Helper method to check if a column is playable
     * This would typically be in the PuissanceXBoard class, but we're adding it here for completeness
     */
    private boolean isColumnPlayable(int col) {
        PuissanceXBoard board = (PuissanceXBoard)element;
        // A column is playable if its top cell is empty
        return board.isCellEmpty(0, col);
    }
}