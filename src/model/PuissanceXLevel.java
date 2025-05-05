package model;

import boardifier.model.GameStageModel;
import boardifier.model.Model;
import boardifier.model.StageElementsFactory;
import boardifier.model.TextElement;

public class PuissanceXLevel extends GameStageModel {
    // Éléments du jeu
    private PuissanceXBoard board;
    private PuissanceXPot[] playerPots;
    private Disc[][] playerDiscs;
    private TextElement playerName;
    
    // Variables d'état
    private int rows;
    private int cols;
    private int winCondition;
    
    public PuissanceXLevel(String name, Model model) {
        super(name, model);
        // Initialisation et callbacks
    }
    
    // Getters et setters
    
    @Override
    public StageElementsFactory getDefaultElementFactory() {
        return new PuissanceXStageFactory(this);
    }
}
