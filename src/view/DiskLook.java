package view;

import boardifier.view.ConsoleColor;
import boardifier.view.ElementLook;
import model.Disk;

public class DiskLook extends ElementLook {
    
    public DiskLook(Disk disk) {
        // A disk now occupies 3x5 characters to match the board cell size
        super(disk, 3, 5);
        // Set anchor to top-left to match the grid cell positioning
        setAnchorType(ANCHOR_TOPLEFT);
        render();
    }
    
    @Override
    public void render() {
        Disk disk = (Disk) element;
        
        // Define colors and styles for each player
        String color;
        String symbol;
        if (disk.getPlayerId() == 0) {
            color = ConsoleColor.RED_BACKGROUND;
            symbol = ConsoleColor.RED_BOLD + "O" + ConsoleColor.RESET;
        } else {
            color = ConsoleColor.YELLOW_BACKGROUND;
            symbol = ConsoleColor.YELLOW_BOLD + "O" + ConsoleColor.RESET;
        }
        
        // Fill the 3x5 space with the colored background and symbol
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 5; j++) {
                if (i == 1 && j == 2) {
                    shape[i][j] = symbol; // Center symbol
                } else {
                    shape[i][j] = color + " " + ConsoleColor.RESET; // Background color
                }
            }
        }
    }
}