package view;

import boardifier.view.GridLook;
import model.Board;

public class BoardLook extends GridLook {
    
    public BoardLook(Board board) {
        // Create a grid look with cells of size 1x1, with depth 0, and border width 1
        super(1, 1, board, 0, 1);
    }

    @Override
    protected void renderBorders() {
        // Use the default border rendering from GridLook
        super.renderBorders();
    }
}
