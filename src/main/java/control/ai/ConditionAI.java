package control.ai;

import boardifier.model.Model;
import boardifier.control.Controller;
import boardifier.model.action.ActionList;
import boardifier.control.ActionFactory;
import control.PuissanceXDecider;
import control.SimplifyBoard;
import model.PuissanceXDisk;
import model.PuissanceXModel;
import model.PuissanceXStageModel;
import model.PuissanceXBoard;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ConditionAI extends PuissanceXDecider {

    public ConditionAI(Model model, Controller control) {
        super(model, control);
    }

    @Override
    public ActionList decide() {

        /*  Logique :
         *
         *   - S'il peut gagner, gagne.
         *   - Sinon, si l'adversaire peut gagner, l'IA le bloque.
         *   - Si pas de coup a été trouvé : fait ce qu'il suit
         *   - D'abord évite de donner une victoire à l'adversaire au prochain tour
         *   - Ensuite donne la priorité aux colonnes qui permettent un alignement de 3
         *   - Puis donne la priorité aux colonnes au centre
         *   - Finalement, si aucune case n'a été choisi, joue aléatoirement.
         *
         */

        PuissanceXModel model = (PuissanceXModel) this.model;
        PuissanceXStageModel stageModel = (PuissanceXStageModel) model.getGameStage();
        PuissanceXBoard board = stageModel.getBoard();
        SimplifyBoard simpBoard = new SimplifyBoard(board);
        int adversaryId = (model.getIdPlayer() + 1) % 2;
        int iaId = model.getIdPlayer();
        
        // Trouve toutes les colonnes encore jouables
        List<Integer> availableCols = getCols(board);

        for (int col : availableCols) {
            int row = board.getFirstEmptyRow(col);

            // Regarde si l'IA peut gagner
            ActionList actions = victoryAction(col, simpBoard, iaId, row, model, stageModel);
            if (actions != null) return actions;

            // Regarde si l'adversaire peut gagner
            actions = defeatAction(col, simpBoard, adversaryId, iaId, row, model, stageModel);
            if (actions != null) return actions;
        }

        // Évite de donner une victoire à l'adversaire au prochain tour
        List<Integer> safeMoves = avoidDefeatNextTurn(availableCols, board, simpBoard, iaId, adversaryId, model);

        // Priorité aux colonnes qui permettent un alignement de 3
        int bestCol = -1;
        int maxAlign = -1;

        List<Integer> colsToCheck;
        if (safeMoves.isEmpty()) {
            colsToCheck = availableCols;
        } else {
            colsToCheck = safeMoves;
        }

        bestCol = getBestCol(colsToCheck, board, simpBoard, iaId, maxAlign, bestCol);

        if (bestCol != -1) {
            return getActions(bestCol);
        }


        // Priorité aux colonnes au centre
        ActionList centerAction = playCenter(availableCols, board);
        if (centerAction != null) {
            return centerAction;
        }


        // Si aucune stratégie précédente n'a fonctionné, joue aléatoirement
        return getDefaultMove(safeMoves, availableCols, board);
    }

    private int getBestCol(List<Integer> colsToCheck, PuissanceXBoard board, SimplifyBoard simpBoard, int iaId, int maxAlign, int bestCol) {
        for (int col : colsToCheck) {
            int row = board.getFirstEmptyRow(col);
            simpBoard.add(col, iaId);

            int alignCount = countPotentialAligns(simpBoard, row, col, iaId);
            if (alignCount > maxAlign) {
                maxAlign = alignCount;
                bestCol = col;
            }

            simpBoard.suppr(col);
        }
        return bestCol;
    }

    private static List<Integer> avoidDefeatNextTurn(List<Integer> availableCols, PuissanceXBoard board, SimplifyBoard simpBoard, int iaId, int adversaryId, PuissanceXModel model) {
        // Évite de donner une victoire à l'adversaire au prochain tour
        List<Integer> safeMoves = new ArrayList<>(availableCols);

        for (int col : availableCols) {
            int row = board.getFirstEmptyRow(col);
            
            if (row > 0 && row < board.getNbRows()) {
                simpBoard.add(col, iaId);
                simpBoard.add(col, adversaryId); // Simule le coup suivant de l'adversaire

                if (simpBoard.checkWin(row-1, col, model.getWinCondition())) {
                    safeMoves.remove(Integer.valueOf(col));
                }

                simpBoard.suppr(col);
                simpBoard.suppr(col);
            }
        }
        return safeMoves;
    }

    private ActionList defeatAction(int col, SimplifyBoard simpBoard, int adversaryId, int iaId, int row, PuissanceXModel model,  PuissanceXStageModel stageModel) {
        // Simuler un coup de l'adversaire
        simpBoard.add(col, adversaryId);

        // Vérifier si l'adversaire peut gagner
        if (simpBoard.checkWin(row, col, model.getWinCondition())) {
            PuissanceXDisk disk = new PuissanceXDisk(iaId, stageModel);
            ActionList actions = ActionFactory.generatePutInContainer(model, disk, "board", row, col);
            actions.setDoEndOfTurn(true);

            // Supprime le coup simulé de l'adversaire
            simpBoard.suppr(col);
            return actions;
        }
        return null;
    }

    private ActionList victoryAction(int col, SimplifyBoard simpBoard, int iaId, int row, PuissanceXModel model, PuissanceXStageModel stageModel) {
        // Temporairement placer le pion pour vérifier 
        simpBoard.add(col, iaId);

        // Vérifier si ce placement crée une victoire
        if (simpBoard.checkWin(row, col, model.getWinCondition())) {
            PuissanceXDisk disk = new PuissanceXDisk(iaId, stageModel);
            ActionList actions = ActionFactory.generatePutInContainer(model, disk, "board", row, col);
            actions.setDoEndOfTurn(true);

            // Supprimer le coup temporaire
            simpBoard.suppr(col);
            return actions;
        }

        return null;
    }


    private static List<Integer> getCols(PuissanceXBoard board) {
        List<Integer> availableCols = new ArrayList<>();
        for (int col = 0; col < board.getNbCols(); col++) {
            if (!board.isColumnFull(col)) {
                availableCols.add(col);
            }
        }
        return availableCols;
    }


    private int countPotentialAligns(SimplifyBoard board, int row, int col, int playerId) {
        if (!isValidMove(board, row, col)) return 0;
        
        int count = 0;
    
        // Vérifie les alignements horizontaux
        count += checkPotentialHorizontal(board, row, col, playerId);
        
        // Vérifie les alignements verticaux
        count += checkPotentialVertical(board, row, col, playerId);
        
        // Vérifie la diagonale 1
        count += checkPotentialDiagonal1(board, row, col, playerId);
        
        // Vérifie la diagonale 2
        count += checkPotentialDiagonal2(board, row, col, playerId);

        return count;
    }
    
    
    private int checkPotentialHorizontal(SimplifyBoard board, int row, int col, int playerId) {
        int count = 0;
        for (int c = Math.max(0, col-2); c <= Math.min(board.getNbCols()-3, col); c++) {
            int align = 0;
            for (int i = 0; i < 3; i++) {
                if (board.get(row, c+i) == playerId) {
                    align++;
                }
            }
            if (align >= 2) count++;
        }
        return count;
    }
    
    private int checkPotentialVertical(SimplifyBoard board, int row, int col, int playerId) {
        int count = 0;
        if (row <= board.getNbRows()-3) {
            int align = 0;
            for (int r = row; r < row+3; r++) {
                if (board.get(r, col) == playerId) {
                    align++;
                }
            }
            if (align >= 2) count++;
        }
        return count;
    }
    
    private int checkPotentialDiagonal1(SimplifyBoard board, int row, int col, int playerId) {
        int count = 0;
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
        return count;
    }
    
    private int checkPotentialDiagonal2(SimplifyBoard board, int row, int col, int playerId) {
        int count = 0;
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

    private boolean isValidMove(SimplifyBoard board, int row, int col) {
        return row >= 0 && row < board.getNbRows() && 
               col >= 0 && col < board.getNbCols() && 
               !board.isColumnFull(col);
    }

    private ActionList getDefaultMove(List<Integer> safeMoves, List<Integer> availableCols, PuissanceXBoard board) {
        // D'abord essayer le centre
        ActionList centerAction = playCenter(availableCols, board);
        if (centerAction != null) return centerAction;
        
        // Ensuite un coup sûr aléatoire
        if (!safeMoves.isEmpty()) {
            return getActions(safeMoves.get(new Random().nextInt(safeMoves.size())));
        }
        
        // En dernier recours, un coup disponible aléatoire
        if (!availableCols.isEmpty()) {
            return getActions(availableCols.get(new Random().nextInt(availableCols.size())));
        }
        
        return null;
    }
}
