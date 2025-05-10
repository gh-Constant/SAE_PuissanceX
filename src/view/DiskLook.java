package view;

import boardifier.view.ElementLook;
import boardifier.view.ConsoleColor;
import model.Disk;

public class DiskLook extends ElementLook {
    
    public DiskLook(Disk disk) {
        super(disk, 1, 1); // A disk occupies 1x1 character
        render();
    }
    
    @Override
    public void render() {
        Disk disk = (Disk) element;
        
        // Player 1 is typically red, Player 2 is typically yellow
        String diskSymbol;
        if (disk.getPlayerId() == 0) {
            diskSymbol = ConsoleColor.RED_BACKGROUND + " " + ConsoleColor.RESET;
        } else {
            diskSymbol = ConsoleColor.YELLOW_BACKGROUND + " " + ConsoleColor.RESET;
        }
        
        shape[0][0] = diskSymbol;
    }
}