package view;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import model.PuissanceXDisk;

public class DiskLookFX {

    private Circle diskCircle;

    public DiskLookFX(PuissanceXDisk disk) {
        diskCircle = new Circle(20);
        if (disk.getPlayerId() == 0) {
            diskCircle.setFill(Color.RED);
        } else {
            diskCircle.setFill(Color.YELLOW);
        }
    }

    public Circle getDiskCircle() {
        return diskCircle;
    }
}