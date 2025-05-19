package view;

import boardifier.view.ClassicBoardLook;
import model.PuissanceXBoard;

public class BoardLook extends ClassicBoardLook {
    
    public BoardLook(PuissanceXBoard board) {
        // Create a classic board look with:
        // - cells of size 3x5 (height x width)
        // - depth 0
        // - border width 1
        // - show coordinates true
        super(3, 5, board, 0, 1, true);
        
        // Set cell alignments to center for better visual appearance
        setVerticalAlignment(ALIGN_MIDDLE);
        setHorizontalAlignment(ALIGN_CENTER);
    }

    @Override
    protected void renderBorders() {
        // Use the default border rendering from ClassicBoardLook
        super.renderBorders();
    }
}