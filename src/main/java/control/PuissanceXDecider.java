package control;

import boardifier.control.ActionFactory;
import boardifier.control.Decider;
import boardifier.model.GameElement;
import boardifier.model.Model;
import boardifier.control.ActionFactory;
import boardifier.control.Controller;
import model.PuissanceXBoard;
import model.PuissanceXDisk;
import model.PuissanceXModel;
import model.PuissanceXStageModel;
import boardifier.model.action.ActionList;
import model.PuissanceXStageModel;

import java.util.ArrayList;
import java.util.List;

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
}