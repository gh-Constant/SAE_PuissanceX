package view;

import boardifier.view.ElementLook;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import model.PuissanceXDisk;

/**
 * Simple JavaFX-compatible disk look for PuissanceX.
 * This is a minimal implementation to avoid null pointer exceptions.
 */
public class SimpleDiskLook extends ElementLook {
    
    private Circle circle;
    
    public SimpleDiskLook(PuissanceXDisk disk) {
        super(disk);
        
        // Create a simple circle to represent the disk
        circle = new Circle(50); // radius of 50 (increased for much larger cells)
        
        // Set color based on player
        if (disk.getPlayerId() == 0) {
            circle.setFill(Color.RED);
        } else {
            circle.setFill(Color.YELLOW);
        }
        
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(2);
        
        addShape(circle);
    }
    
    @Override
    public void render() {
        // Update the circle position if needed
        PuissanceXDisk disk = (PuissanceXDisk) getElement();
        
        // Update color in case it changed
        if (disk.getPlayerId() == 0) {
            circle.setFill(Color.RED);
        } else {
            circle.setFill(Color.YELLOW);
        }
    }
    
    @Override
    public void onFaceChange() {
        render();
    }
}
