package view.console;

import boardifier.model.ContainerElement;
import boardifier.view.GridLook;
import boardifier.view.ConsoleColor;
import model.PuissanceXPot;

public class PuissanceXPotLook extends GridLook {
    
    private int playerIndex;
    
    public PuissanceXPotLook(ContainerElement element, int playerIndex) {
        super(1, 2, element, -1, 1); // Hauteur 1, largeur 2, profondeur -1, bordure 1
        setVerticalAlignment(ALIGN_MIDDLE);
        setHorizontalAlignment(ALIGN_CENTER);
        this.playerIndex = playerIndex;
    }
    
    @Override
    protected void renderBorders() {
        // Dessiner les bordures du pot avec la couleur du joueur
        String borderColor = (playerIndex == 0) ? 
                ConsoleColor.YELLOW : ConsoleColor.RED;
        
        // Bordure supérieure
        for (int j = 0; j < width; j++) {
            shape[0][j] = borderColor + "─" + ConsoleColor.RESET;
        }
        
        // Coins supérieurs
        shape[0][0] = borderColor + "┌" + ConsoleColor.RESET;
        shape[0][width-1] = borderColor + "┐" + ConsoleColor.RESET;
        
        // Bordures latérales
        for (int i = 1; i < height-1; i++) {
            shape[i][0] = borderColor + "│" + ConsoleColor.RESET;
            shape[i][width-1] = borderColor + "│" + ConsoleColor.RESET;
        }
        
        // Bordure inférieure
        for (int j = 0; j < width; j++) {
            shape[height-1][j] = borderColor + "─" + ConsoleColor.RESET;
        }
        
        // Coins inférieurs
        shape[height-1][0] = borderColor + "└" + ConsoleColor.RESET;
        shape[height-1][width-1] = borderColor + "┘" + ConsoleColor.RESET;
    }
    
    @Override
    public void onReachableChange() {
        // Mettre en évidence le pot lorsqu'il est atteignable
        PuissanceXPot pot = (PuissanceXPot)element;
        int row = 0;
        int col = 0;
        
        if (pot.isCellReachable(row, col)) {
            renderBorders(); // Redessiner les bordures normales d'abord
            
            // Ajouter un indicateur visuel pour montrer que le pot est atteignable
            String highlightColor = (playerIndex == 0) ? 
                    ConsoleColor.YELLOW_BRIGHT : ConsoleColor.RED_BRIGHT;
            
            // Modifier les coins pour indiquer que le pot est atteignable
            shape[0][0] = highlightColor + "┏" + ConsoleColor.RESET;
            shape[0][width-1] = highlightColor + "┓" + ConsoleColor.RESET;
            shape[height-1][0] = highlightColor + "┗" + ConsoleColor.RESET;
            shape[height-1][width-1] = highlightColor + "┛" + ConsoleColor.RESET;
        }
    }
}