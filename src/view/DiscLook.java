package view;

import boardifier.model.GameElement;
import boardifier.view.ElementLook;

public class DiscLook extends ElementLook {
    public DiscLook(GameElement element) {
        super(element);
        // Définition de l'apparence
    }
    
    @Override
    public void onSelectionChange() {
        // Gestion de l'apparence lors de la sélection
    }
    
    @Override
    public void onPutInContainer() {
        // Gestion de l'apparence lors du placement
    }
}