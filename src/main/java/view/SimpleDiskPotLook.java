package view;

import boardifier.view.GridLook;
import javafx.scene.paint.Color;
import model.PuissanceXDiskPot;

/**
 * Simple JavaFX-compatible disk pot look for PuissanceX.
 * This is a minimal implementation to avoid null pointer exceptions.
 */
public class SimpleDiskPotLook extends GridLook {
    
    public SimpleDiskPotLook(PuissanceXDiskPot pot, Color color) {
        // Create a grid look with JavaFX-compatible parameters:
        // rowHeight, colWidth, element, depth, borderWidth, borderColor
        super(30, // row height
              30, // col width
              pot, // pot element
              0, // depth
              1, // border width
              color); // border color
    }
    
    @Override
    protected void render() {
        super.render();
    }
    
    @Override
    public void onFaceChange() {
        // Simple implementation - just call parent if it exists
        try {
            super.onFaceChange();
        } catch (Exception e) {
            // Ignore any errors in the parent implementation
        }
    }
}
