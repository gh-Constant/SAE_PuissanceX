package control.ai;

import boardifier.model.Model;
import boardifier.control.Controller;
import boardifier.model.action.ActionList;
import boardifier.control.ActionFactory;
import control.PuissanceXDecider;
import model.PuissanceXDisk;
import model.PuissanceXModel;
import model.PuissanceXStageModel;
import model.PuissanceXBoard;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomAIDecider extends PuissanceXDecider {
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
        List<Integer> availableCols = board.getAvailableCol();

        if (board.isFull()) {
            return null; // No valid moves
        }

        // Pick a random column
        int chosenCol = availableCols.get(random.nextInt(availableCols.size()));

        return this.getActions(chosenCol);
    }
}