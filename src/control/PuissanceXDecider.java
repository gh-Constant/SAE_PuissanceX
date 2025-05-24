package control;

import boardifier.control.Decider;
import boardifier.model.Model;
import boardifier.control.ActionFactory;
import boardifier.control.Controller;
import model.PuissanceXBoard;
import model.PuissanceXDisk;
import model.PuissanceXModel;
import model.PuissanceXStageModel;
import boardifier.model.action.ActionList;

public class PuissanceXDecider extends Decider {
    public PuissanceXDecider(Model model, Controller control) {
        super(model, control);
    }

    /**
     * Determines and returns the next set of actions for the AI player.
     *
     * @return the list of actions representing the AI's move, or {@code null} if no decision is made
     */
    @Override
    public ActionList decide() {
        PuissanceXModel model = (PuissanceXModel) this.model;
        // TODO: Implement decision logic
        return null;
    }


    /**
     * Generates the list of actions required to place a disk for the current player in the specified column.
     *
     * Determines the first available row in the given column, creates a disk for the current player, and constructs an action list to place the disk at that position on the board. The action list is configured to end the player's turn after execution.
     *
     * @param column the column index where the disk should be placed
     * @return an ActionList representing the move and end-of-turn action
     */
    public ActionList getActions(int column) {
        PuissanceXStageModel stageModel = (PuissanceXStageModel) model.getGameStage();
        PuissanceXBoard board = stageModel.getBoard();

        int row = board.getFirstEmptyRow(column);

        // Create a new disk for the current player
        int currentPlayer = model.getIdPlayer();
        PuissanceXDisk puissanceXDisk = new PuissanceXDisk(currentPlayer, stageModel);

        // Create the action to put the disk in the chosen column
        ActionList actions = ActionFactory.generatePutInContainer(model, puissanceXDisk, "board", row, column);
        actions.setDoEndOfTurn(true);

        System.out.println("AI player " + (currentPlayer + 1) + " chooses column " + column);

        return actions;
    }
    
}