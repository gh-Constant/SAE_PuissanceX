package view.console;

import boardifier.model.ContainerElement;
import boardifier.view.GridLook;

public class PuissanceXPotLook extends GridLook {
    
    public PuissanceXPotLook(ContainerElement element, int playerIndex) {
        super(1, 2, element, -1, 1); // Hauteur 1, largeur 2, profondeur -1, bordure 1
        setVerticalAlignment(ALIGN_MIDDLE);
        setHorizontalAlignment(ALIGN_CENTER);
    }
    
    @Override
    protected void renderBorders() {
        // TODO: Dessiner les bordures du pot
    }
}