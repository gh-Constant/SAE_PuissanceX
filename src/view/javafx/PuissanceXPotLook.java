package view.javafx;

import boardifier.model.ContainerElement;
import boardifier.view.GridLook;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class PuissanceXPotLook extends GridLook {
    
    private HBox container;
    
    public PuissanceXPotLook(ContainerElement element, int playerIndex) {
        super(element);
        container = new HBox();
        // TODO: Configurer le conteneur
    }
    
    // TODO: Implémenter les méthodes pour dessiner le pot de jetons
}