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
        // Fond bleu vif pour tout le plateau
        javafx.scene.shape.Rectangle background = new javafx.scene.shape.Rectangle(
            colWidth * nbCols + 2 * frameWidth + 40,
            rowHeight * nbRows + 2 * frameWidth + 40,
            Color.BLUE
        );
        background.setX(gapXToCells - frameWidth - 20);
        background.setY(gapYToCells - frameWidth - 20);
        addShape(background);

        // Cadre autour du plateau
        javafx.scene.shape.Rectangle frame = new javafx.scene.shape.Rectangle(
            colWidth * nbCols + 2 * frameWidth,
            rowHeight * nbRows + 2 * frameWidth,
            Color.BLUE
        );
        frame.setX(gapXToCells - frameWidth);
        frame.setY(gapYToCells - frameWidth);
        frame.setStroke(frameColor);
        frame.setStrokeWidth(frameWidth);
        addShape(frame);

        // Cercles noirs pour les emplacements vides
        double radius = colWidth * 0.4;
        for (int i = 0; i < nbRows; i++) {
            for (int j = 0; j < nbCols; j++) {
                javafx.scene.shape.Circle hole = new javafx.scene.shape.Circle(
                    gapXToCells + j * colWidth + colWidth / 2,
                    gapYToCells + i * rowHeight + rowHeight / 2,
                    radius,
                    Color.BLACK
                );
                hole.setStroke(Color.DEEPSKYBLUE);
                hole.setStrokeWidth(4);
                addShape(hole);
            }
        }
    }
    
    @Override
    public void onFaceChange() {
        // Simple implementation - just call parent
        super.onFaceChange();
    }
}
