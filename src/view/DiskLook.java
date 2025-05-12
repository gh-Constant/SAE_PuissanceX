package view;

import boardifier.view.ConsoleColor;
import boardifier.view.ElementLook;
import model.Disk;

public class DiskLook extends ElementLook {
    public DiskLook(Disk disk) {
        super(disk, 3, 5);
        setAnchorType(ANCHOR_TOPLEFT);
        render();
    }

    @Override
    public void render() {
        Disk disk = (Disk) element;
        setSize(getWidth(), getHeight());

        String color;
        String symbol;
        if (disk.getPlayerId() == 0) {
            color = ConsoleColor.RED_BACKGROUND;
            symbol = ConsoleColor.RED_BOLD + "●" + ConsoleColor.RESET;
        } else {
            color = ConsoleColor.YELLOW_BACKGROUND;
            symbol = ConsoleColor.YELLOW_BOLD + "●" + ConsoleColor.RESET;
        }

        // Create a more visually appealing disk
        for(int i = 0; i < getHeight(); i++) {
            for(int j = 0; j < getWidth(); j++) {
                if (i == getHeight()/2 && j == getWidth()/2) {
                    shape[i][j] = symbol;
                } else if ((i == getHeight()/2 - 1 || i == getHeight()/2 + 1) && j == getWidth()/2) {
                    // Add a subtle highlight/shadow effect
                    shape[i][j] = color + " " + ConsoleColor.RESET;
                } else {
                    shape[i][j] = color + " " + ConsoleColor.RESET;
                }
            }
        }
    }
}