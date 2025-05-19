package model;

import boardifier.model.GameStageModel;
import boardifier.model.StageElementsFactory;
import boardifier.model.TextElement;

public class PuissanceXStageFactory extends StageElementsFactory {
    private PuissanceXStageModel stageModel;
    
    public PuissanceXStageFactory(PuissanceXStageModel stageModel) {
        super(stageModel);
        this.stageModel = stageModel;
    }
    
    @Override
    public void setup() {
        // Get the model and cast it to PuissanceXModel to access board dimensions
        PuissanceXModel model = (PuissanceXModel) stageModel.getModel();
        
        // Create the board
        PuissanceXBoard board = new PuissanceXBoard(model.getBoardRows(),
                               model.getBoardCols(), 
                               stageModel);
        stageModel.setBoard(board);
        
        // Create the player text element
        TextElement playerText = new TextElement("Current player: " + 
                                               stageModel.getModel().getCurrentPlayerName(), 
                                               stageModel);
        playerText.setLocation(0, 0);
        stageModel.setPlayerText(playerText);
    }
}