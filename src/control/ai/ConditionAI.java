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

        PuissanceXModel model = (PuissanceXModel) this.model;
        PuissanceXStageModel stageModel = (PuissanceXStageModel) model.getGameStage();
        PuissanceXBoard board = stageModel.getBoard();
        SimplifyBoard simpBoard = new SimplifyBoard(board);
        int adversaryId = (model.getIdPlayer() + 1) % 2;
        int iaId = model.getIdPlayer();

    
        // Trouve toutes les colonnes encore jouables
        List<Integer> availableCols = new ArrayList<>();
        for (int col = 0; col < board.getNbCols(); col++) {
            if (!board.isColumnFull(col)) {
                availableCols.add(col);
            }
        }


        /* 
        *   Regarde si l'IA peut gagner
        *      Si l'IA peut gagner, il place le jeton
        *      Sinon continue
        */


        for (int col : availableCols) {
            int row = board.getFirstEmptyRow(col);

            // Temporairement placer le pion pour vérifier 
            simpBoard.add(col, iaId);

            // Vérifier si ce placement crée une victoire
            if (simpBoard.checkWin(row, col, model.getWinCondition())) {
                System.out.println("AI can win in column " + col);
                PuissanceXDisk disk = new PuissanceXDisk(iaId, stageModel);
                ActionList actions = ActionFactory.generatePutInContainer(model, disk, "board", row, col);
                actions.setDoEndOfTurn(true);
                return actions;
            }

            // Retirer le pion de test
            simpBoard.suppr(col);

            /* 
            *   Regarde si l'adversaire peut gagner
            *      Si l'adversaire peut gagner, il le bloque
            *      Sinon continue
            */

            // Simuler un coup de l'adversaire
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
            PuissanceXDisk disk = new PuissanceXDisk(iaId, stageModel);
            ActionList actions = ActionFactory.generatePutInContainer(model, disk, "board", row, chosenCol);
            actions.setDoEndOfTurn(true);
            return actions;
        }

        /*  Logique :
        *   
        *   - Évite de donner une victoire à l'adversaire au prochain tour
        *   - Priorité aux colonnes qui permettent un alignement de 3
        *   - Priorité aux colonnes au centre
        *   
        */

        // Évite de donner une victoire à l'adversaire au prochain tour
        List<Integer> safeMoves = new ArrayList<>(availableCols);
        for (int col : availableCols) {
            int row = board.getFirstEmptyRow(col);
            if (row > 0) { // Vérifie s'il y a une place au-dessus
                simpBoard.add(col, iaId);
                simpBoard.add(col, adversaryId); // Simule le coup suivant de l'adversaire
                
                if (simpBoard.checkWin(row-1, col, adversaryId)) {
                    safeMoves.remove(Integer.valueOf(col));
                }
                
                simpBoard.suppr(col);
                simpBoard.suppr(col);
            }
        }

        // Priorité aux colonnes qui permettent un alignement de 3
        int bestCol = -1;
        int maxAlign = -1;

        for (int col : safeMoves.isEmpty() ? availableCols : safeMoves) {
            int row = board.getFirstEmptyRow(col);
            simpBoard.add(col, iaId);
            
            int alignCount = countPotentialAligns(simpBoard, row, col, iaId);
            if (alignCount > maxAlign) {
                maxAlign = alignCount;
                bestCol = col;
            }
            
            simpBoard.suppr(col);
        }

        if (bestCol != -1) {
            return getActions(bestCol);
        }


        // Priorité aux colonnes au centre
        ActionList centerAction = playCenter(availableCols, board);
        if (centerAction != null) {
            return centerAction;
        }


        // Si aucune stratégie précédente n'a fonctionné, joue aléatoirement
        if (!safeMoves.isEmpty()) {
            return getActions(safeMoves.get(new Random().nextInt(safeMoves.size())));
        }

        return null;
    }


    private int countPotentialAligns(SimplifyBoard board, int row, int col, int playerId) {
        int count = 0;
    
        // Vérifie les alignements horizontaux
        for (int c = Math.max(0, col-2); c <= Math.min(board.getNbCols()-3, col); c++) {
            int align = 0;
            for (int i = 0; i < 3; i++) {
                if (board.get(row, c+i) == playerId) {
                    align++;
                }
            }
            if (align >= 2) count++;
        }
        
        // Vérifie les alignements verticaux
        if (row <= board.getNbRows()-3) {
            int align = 0;
            for (int r = row; r < row+3; r++) {
                if (board.get(r, col) == playerId) {
                    align++;
                }
            }
            if (align >= 2) count++;
        }
        
        // Vérifie la diagonale 1
        for (int r = Math.min(row+2, board.getNbRows()-1), c = Math.max(0, col-2); 
             r >= 0 && c <= Math.min(board.getNbCols()-3, col); 
             r--, c++) {
            int align = 0;
            for (int i = 0; i < 3; i++) {
                if (r-i >= 0 && c+i < board.getNbCols() && board.get(r-i, c+i) == playerId) {
                    align++;
                }
            }
            if (align >= 2) count++;
        }
        
        // Vérifie la diagonale 2
        for (int r = Math.max(0, row-2), c = Math.max(0, col-2); 
             r <= board.getNbRows()-3 && c <= Math.min(board.getNbCols()-3, col); 
             r++, c++) {
            int align = 0;
            for (int i = 0; i < 3; i++) {
                if (r+i < board.getNbRows() && c+i < board.getNbCols() && board.get(r+i, c+i) == playerId) {
                    align++;
                }
            }
            if (align >= 2) count++;
        }
    
        return count;
    }

    private ActionList playCenter(List<Integer> availableCols, PuissanceXBoard board) {
        int centerCol = board.getNbCols() / 2;
        if (availableCols.contains(centerCol)) {
            return getActions(centerCol);
        }
        return null;
    }
}
