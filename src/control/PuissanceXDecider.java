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

    @Override
    public ActionList decide() {
        PuissanceXModel model = (PuissanceXModel) this.model;
        // TODO: Implement decision logic
        return null;
    }


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

    public int[][] getBoard() {
        PuissanceXStageModel stageModel = (PuissanceXStageModel) model.getGameStage();
        PuissanceXBoard board = stageModel.getBoard();

        int[][] newBoard = new int[board.getNbRows()][board.getNbCols()];
        for (int row = 0; row < board.getNbRows(); row++) {
            for (int col = 0; col < board.getNbCols(); col++) {
                List<GameElement> elt = board.getElements(row, col);
                if (elt.isEmpty()) {
                    newBoard[row][col] = -1;
                } else {
                    newBoard[row][col] = ((PuissanceXDisk) elt.getFirst()).getPlayerId();
                }
            }
        }
        return newBoard;

    }
}