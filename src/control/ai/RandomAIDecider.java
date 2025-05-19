package control.ai;

import boardifier.model.Model;
import boardifier.control.Controller;
import boardifier.model.action.ActionList;
import boardifier.control.ActionFactory;
import model.PuissanceXDisk;
import model.PuissanceXModel;
import model.PuissanceXStageModel;
import model.PuissanceXBoard;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomAIDecider extends control.PuissanceXDecider {
    private Random random;
    
    public RandomAIDecider(Model model, Controller control) {
        super(model, control);
        this.random = new Random();
    }

    @Override
    public ActionList decide() {
        // Add a small delay to make AI moves more visible to the user
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // Ignore interruption
        }
        
        PuissanceXModel model = (PuissanceXModel) this.model;
        PuissanceXStageModel stageModel = (PuissanceXStageModel) model.getGameStage();
        PuissanceXBoard board = stageModel.getBoard();

        // Find all columns that are not full
        List<Integer> availableCols = new ArrayList<>();
        for (int col = 0; col < board.getNbCols(); col++) {
            if (!board.isColumnFull(col)) {
                availableCols.add(col);
            }
        }

        if (availableCols.isEmpty()) {
            return null; // No valid moves
        }

        // Pick a random column
        int chosenCol = availableCols.get(random.nextInt(availableCols.size()));
        int row = board.getFirstEmptyRow(chosenCol);

        // Create a new disk for the current player
        int currentPlayer = model.getIdPlayer();
        PuissanceXDisk puissanceXDisk = new PuissanceXDisk(currentPlayer, stageModel);

        // Create the action to put the disk in the chosen column
        ActionList actions = ActionFactory.generatePutInContainer(model, puissanceXDisk, "board", row, chosenCol);
        actions.setDoEndOfTurn(true);
        
        System.out.println("AI player " + (currentPlayer + 1) + " chooses column " + chosenCol);
        
        return actions;
    }
}