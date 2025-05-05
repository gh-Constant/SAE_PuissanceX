package view;

import boardifier.model.GameElement;
import boardifier.view.GridLook;

public class PuissanceXBoardLook extends GridLook {
    public PuissanceXBoardLook(GameElement element) {
        super(element);
        // Configuration de l'apparence du plateau
    }
    
    @Override
    public void onReachableChange() {
        // Mise à jour visuelle des cellules accessibles
    }
}