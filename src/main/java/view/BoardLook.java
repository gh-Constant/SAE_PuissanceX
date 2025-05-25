package view;

import boardifier.view.ClassicBoardLook;
import boardifier.view.ConsoleColor;
import model.PuissanceXBoard;

public class BoardLook extends ClassicBoardLook {
    
    public BoardLook(PuissanceXBoard board) {
        // Create a classic board look with:
        // - cells of size 3x5 (height x width)
        // - depth 0
        // - border width 1
        // - show coordinates false (nous gérerons l'affichage nous-mêmes)
        super(3, 5, board, 0, 1, false);
        
        // Set cell alignments to center for better visual appearance
        setVerticalAlignment(ALIGN_MIDDLE);
        setHorizontalAlignment(ALIGN_CENTER);
    }

    @Override
    protected void renderBorders() {
        // Utiliser le rendu de bordures par défaut
        super.renderBorders();
    }
    
    @Override
    public void render() {
        // Rendre le plateau normalement sans les numéros
        super.render();
    }
    
    /**
     * Méthode publique pour afficher les numéros de colonnes
     * Sera appelée depuis PuissanceXStageView après le rendu du plateau
     */
    public void renderColumnNumbers() {
        PuissanceXBoard board = (PuissanceXBoard) element;
        int nbCols = board.getNbCols();
        
        // Espacement initial pour aligner avec les bordures du plateau
        System.out.print(" ");
        
        for (int col = 0; col < nbCols; col++) {
            String colNumber = String.valueOf(col + 1);
            // Chaque cellule fait 5 caractères + 1 pour la bordure = 6 caractères
            System.out.print(ConsoleColor.CYAN + "  " + colNumber + "  " + ConsoleColor.RESET);
            if (col < nbCols - 1) {
                System.out.print("║"); // Utiliser le même caractère de bordure
            }
        }
        System.out.println(); // Nouvelle ligne après les numéros
    }
}