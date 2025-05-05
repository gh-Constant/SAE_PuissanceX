package view.javafx;

import boardifier.model.GameElement;
import boardifier.view.ElementLook;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import model.Disc;

public class DiscLook extends ElementLook {
    
    private Circle circle;
    
    public DiscLook(GameElement element) {
        super(element);
        // TODO: Initialiser le cercle représentant le jeton
    }
    
    @Override
    public void onSelectionChange() {
        // TODO: Changer l'apparence quand le jeton est sélectionné
        // Par exemple, ajouter un effet de surbrillance
    }
    
    @Override
    public void onPutInContainer() {
        // TODO: Ajuster l'apparence quand le jeton est placé dans un conteneur
        // Par exemple, animation de chute
    }
    
    // TODO: Implémenter les méthodes pour dessiner et animer le jeton
}