package view.javafx;

import boardifier.model.GameElement;
import boardifier.view.GridLook;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import model.PuissanceXBoard;

public class PuissanceXBoardLook extends GridLook {
    
    private GridPane gridPane;
    
    public PuissanceXBoardLook(GameElement element) {
        super(element);
        gridPane = new GridPane();
        // TODO: Configurer le GridPane
    }
    
    @Override
    public void onReachableChange() {
        // TODO: Mettre en évidence les colonnes où un jeton peut être placé
    }
    
    // TODO: Implémenter les méthodes pour dessiner le plateau
    // Créer des cellules avec des Rectangle et des Circle pour les emplacements
}