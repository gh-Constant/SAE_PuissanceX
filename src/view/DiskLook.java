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
            symbol = ConsoleColor.RED_BOLD + "O" + ConsoleColor.RESET;
        } else {
            color = ConsoleColor.YELLOW_BACKGROUND;
            symbol = ConsoleColor.YELLOW_BOLD + "O" + ConsoleColor.RESET;
        }

        for(int i = 0; i < getHeight(); i++) {
            for(int j = 0; j < getWidth(); j++) {
                if (i == getHeight()/2 && j == getWidth()/2) {
                    shape[i][j] = symbol;
                } else {
                    shape[i][j] = color + " " + ConsoleColor.RESET;
                }
            }
        }
    }
}