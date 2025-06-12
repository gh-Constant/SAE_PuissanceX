package view;

import boardifier.view.ClassicBoardLook;
import javafx.scene.paint.Color;
import model.PuissanceXBoard;

/**
 * Simple JavaFX-compatible board look for PuissanceX.
 * This is a minimal implementation to avoid null pointer exceptions.
 */
public class SimpleBoardLook extends ClassicBoardLook {
    
    public SimpleBoardLook(PuissanceXBoard board) {
        // Create a classic board look with JavaFX-compatible parameters:
        // cellSize, element, depth, evenColor, oddColor, borderWidth, borderColor, frameWidth, frameColor, showCoords
        super(120, // cell size (increased to match PuissanceXRootPane)
              board, // board element
              0, // depth
              Color.LIGHTBLUE, // even color
              Color.WHITE, // odd color
              3, // border width
              Color.BLACK, // border color
              5, // frame width
              Color.DARKGRAY, // frame color
              false); // show coordinates
    }
    
    @Override
    public void render() {
        super.render();
    }
    
    @Override
    public void onFaceChange() {
        // Simple implementation - just call parent
        super.onFaceChange();
    }
}
