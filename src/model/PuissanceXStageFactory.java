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
        
        // Calculate positions for the pots
        int boardWidth = model.getBoardCols() * 5;
        int boardHeight = model.getBoardRows() * 3;

        // Red disk pot (Player 1) - positioned to the right of the board, upper position
        PuissanceXDiskPot redPot = new PuissanceXDiskPot(boardWidth * 2, boardHeight/3, stageModel);
        stageModel.addElement(redPot); // Add to stage model to make it visible
        model.setPlayer1Pot(redPot);

        // Yellow disk pot (Player 2) - positioned to the right of the board, lower position
        PuissanceXDiskPot yellowPot = new PuissanceXDiskPot(boardWidth * 2 + 10, boardHeight/3, stageModel);
        stageModel.addElement(yellowPot); // Add to stage model to make it visible
        model.setPlayer2Pot(yellowPot);
    }
}