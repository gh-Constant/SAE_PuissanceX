package model;

import boardifier.model.GameStageModel;
import boardifier.model.StageElementsFactory;
import boardifier.model.TextElement;

public class PuissanceXStageFactory extends StageElementsFactory {
    private PuissanceXLevel stageModel;
    
    public PuissanceXStageFactory(GameStageModel gameStageModel) {
        super(gameStageModel);
        stageModel = (PuissanceXLevel)gameStageModel;
    }
    
    @Override
    public void setup() {
        //TODO Création de tous les éléments du jeu
    }
}