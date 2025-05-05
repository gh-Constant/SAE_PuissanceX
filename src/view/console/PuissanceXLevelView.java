package view.console;

import boardifier.model.GameStageModel;
import boardifier.view.GameStageView;
import model.PuissanceXLevel;
import model.PuissanceXBoard;
import model.Disc;
import model.PuissanceXPot;

public class PuissanceXLevelView extends GameStageView {
    
    public PuissanceXLevelView(String name, GameStageModel gameStageModel) {
        super(name, gameStageModel);
    }
    
    @Override
    public void createLooks() {
        PuissanceXLevel level = (PuissanceXLevel)gameStageModel;
        
        // TODO: Créer le look du plateau (PuissanceXBoardLook)
        
        // TODO: Créer les looks des pots de jetons (PuissanceXPotLook)
        
        // TODO: Créer les looks des jetons (DiscLook)
        
        // TODO: Créer les looks des éléments textuels (nom du joueur, etc.)
    }
}