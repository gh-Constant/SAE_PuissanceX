package view.console;

import boardifier.model.GameElement;
import boardifier.view.GridLook;
import model.PuissanceXBoard;

public class PuissanceXBoardLook extends GridLook {
    
    public PuissanceXBoardLook(GameElement element) {
        super(2, 4, element, -1, 1); // Hauteur 2, largeur 4, profondeur -1, bordure 1
        setVerticalAlignment(ALIGN_MIDDLE);
        setHorizontalAlignment(ALIGN_CENTER);
    }
    
    @Override
    protected void renderBorders() {
        // TODO: Dessiner les bordures du plateau
        // Utiliser des caractères comme '|', '-', '+' pour les bordures
    }
    
    @Override
    public void onReachableChange() {
        // TODO: Mettre en évidence les colonnes où un jeton peut être placé
    }
}