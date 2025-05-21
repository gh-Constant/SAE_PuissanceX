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
            board.putDisk(row, col, model.getIdPlayer());
            
            // Vérifier si ce placement crée une victoire
            if (stageModel.checkWin(row, col, model.getIdPlayer())) {
                // Retirer le pion de test
                board.removeDisk(row, col);
                
                System.out.println("AI can win in column " + col);
                PuissanceXDisk disk = new PuissanceXDisk(model.getIdPlayer(), stageModel);
                ActionList actions = ActionFactory.generatePutInContainer(model, disk, "board", row, col);
                actions.setDoEndOfTurn(true);
                return actions;
            }
            
            // Retirer le pion de test si pas de victoire
            board.removeDisk(row, col);

            // Regarde si l'adversaire peut gagner
            //      Si l'adversaire peut gagner, il le bloque
            //      Sinon continue


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