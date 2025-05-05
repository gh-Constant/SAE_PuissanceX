package view.javafx;

import boardifier.model.GameStageModel;
import boardifier.view.GameStageView;
import javafx.scene.layout.Pane;
import model.PuissanceXLevel;

public class PuissanceXLevelView extends GameStageView {
    
    private Pane pane;
    
    public PuissanceXLevelView(String name, GameStageModel gameStageModel) {
        super(name, gameStageModel);
        pane = new Pane();
    }
    
    @Override
    public void createLooks() {
        PuissanceXLevel level = (PuissanceXLevel)gameStageModel;
        
        // TODO: Créer le look du plateau (PuissanceXBoardLook)
        
        // TODO: Créer les looks des pots de jetons (PuissanceXPotLook)
        
        // TODO: Créer les looks des jetons (DiscLook)
        
        // TODO: Créer les looks des éléments textuels (nom du joueur, etc.)
        
        // TODO: Ajouter tous les éléments visuels au pane
    }
    
    public Pane getPane() {
        return pane;
    }
}