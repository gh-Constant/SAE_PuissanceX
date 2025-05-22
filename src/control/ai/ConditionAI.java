package control.ai;

import boardifier.model.Model;
import boardifier.control.Controller;
import boardifier.model.action.ActionList;
import boardifier.control.ActionFactory;
import model.PuissanceXDisk;
import model.PuissanceXModel;
import model.PuissanceXStageModel;
import model.PuissanceXBoard;
import control.SimplifyBoard;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ConditionAI extends control.PuissanceXDecider {

    public ConditionAI(Model model, Controller control) {
        super(model, control);
    }

    @Override
    public ActionList decide() {

        // Regarde si l'IA peut gagner
        //      Si l'IA peut gagner, il place le jeton
        //      Sinon continue
        PuissanceXModel model = (PuissanceXModel) this.model;
        PuissanceXStageModel stageModel = (PuissanceXStageModel) model.getGameStage();
        PuissanceXBoard board = stageModel.getBoard();
        SimplifyBoard simpBoard = new SimplifyBoard(board);

        // Trouve toutes les colonnes encore jouables
        List<Integer> availableCols = new ArrayList<>();
        for (int col = 0; col < board.getNbCols(); col++) {
            if (!board.isColumnFull(col)) {
                availableCols.add(col);
            }
        }

        for (int col : availableCols) {
            int row = board.getFirstEmptyRow(col);

            // Temporairement placer le pion pour vérifier 
            simpBoard.add(col, model.getIdPlayer());

            // Vérifier si ce placement crée une victoire
            if (simpBoard.checkWin(row, col, model.getWinCondition())) {
                System.out.println("AI can win in column " + col);
                PuissanceXDisk disk = new PuissanceXDisk(model.getIdPlayer(), stageModel);
                ActionList actions = ActionFactory.generatePutInContainer(model, disk, "board", row, col);
                actions.setDoEndOfTurn(true);
                return actions;
            }

            // Retirer le pion de test
            simpBoard.suppr(col);

            // Regarde si l'adversaire peut gagner
            //      Si l'adversaire peut gagner, il le bloque
            //      Sinon continue

            // Simuler un coup de l'adversaire
            int adversaryId = (model.getIdPlayer() + 1) % 2;
            simpBoard.add(col, adversaryId);

            // Vérifier si l'adversaire peut gagner
            if (simpBoard.checkWin(row, col, model.getWinCondition())) {
                System.out.println("Blocking opponent win in column " + col);
                return getActions(col); 
            }

            // Retirer le pion de test de l'adversaire
            simpBoard.suppr(col);
        }

        // Si l'IA ne peut pas gagner, il place un jeton de manière aléatoire
        if (!availableCols.isEmpty()) {
            int chosenCol = availableCols.get(new Random().nextInt(availableCols.size()));
            int row = board.getFirstEmptyRow(chosenCol);
            PuissanceXDisk disk = new PuissanceXDisk(model.getIdPlayer(), stageModel);
            ActionList actions = ActionFactory.generatePutInContainer(model, disk, "board", row, chosenCol);
            actions.setDoEndOfTurn(true);
            return actions;
        }

        // Logique pas encore developpé
        return null;
    }
}
