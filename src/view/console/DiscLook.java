package view.console;

import boardifier.model.GameElement;
import boardifier.view.ElementLook;
import boardifier.view.ConsoleColor;
import model.Disc;

public class DiscLook extends ElementLook {
    
    public DiscLook(GameElement element) {
        super(element, 1, 1); // Taille 1x1 pour un jeton en mode console
    }
    
    @Override
    protected void render() {
        Disc disc = (Disc)element;
        String discColor = disc.getColor();
        
        if (discColor.equals(Disc.COLOR_PLAYER1)) {
            shape[0][0] = ConsoleColor.YELLOW + "O" + ConsoleColor.RESET;
        } 
        else if (discColor.equals(Disc.COLOR_PLAYER2)) {
            shape[0][0] = ConsoleColor.RED + "O" + ConsoleColor.RESET;
        }
    }
    
    @Override
    public void onSelectionChange() {
        Disc disc = (Disc)element;
        String discColor = disc.getColor();
        
        if (element.isSelected()) {
            if (discColor.equals(Disc.COLOR_PLAYER1)) {
                shape[0][0] = ConsoleColor.YELLOW_BACKGROUND + "O" + ConsoleColor.RESET;
            } 
            else if (discColor.equals(Disc.COLOR_PLAYER2)) {
                shape[0][0] = ConsoleColor.RED_BACKGROUND + "O" + ConsoleColor.RESET;
            }
        } 
        else {
            render(); // Reset to normal appearance
        }
    }

    public void onPutInContainer() {
        render(); // Update appearance when placed in container
    }
}