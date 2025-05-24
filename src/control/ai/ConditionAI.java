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

    /**
     * Constructs a ConditionAI instance for the Connect Four-like game.
     *
     * Initializes the AI with the provided game model and controller.
     */
    public ConditionAI(Model model, Controller control) {
        super(model, control);
    }

    /**
     * Determines and returns the AI's next move based on prioritized strategies for a Connect Four-like game.
     *
     * The AI first checks for immediate winning moves, then blocks the opponent's potential wins. If neither is possible, it avoids moves that would allow the opponent to win on their next turn, prioritizes columns that enable alignments of three disks, prefers the center column, and finally selects a random safe or available move if no strategic options are found.
     *
     * @return an ActionList representing the chosen move, or null if no moves are possible
     */
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

    /**
     * Determines the column from the provided list that offers the highest potential for the AI to create alignments of three disks.
     *
     * Simulates placing a disk in each candidate column, evaluates the resulting alignment potential, and returns the column index with the greatest alignment opportunity.
     *
     * @param colsToCheck list of candidate columns to evaluate
     * @param board the current game board
     * @param simpBoard a simplified board used for move simulation
     * @param iaId the AI player's identifier
     * @param maxAlign the current maximum alignment count found
     * @param bestCol the current best column index
     * @return the column index with the highest potential alignment for the AI
     */
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

    /**
     * Filters out columns that would allow the opponent to win immediately on their next turn.
     *
     * Simulates placing a disk in each available column, followed by an opponent move in the same column.
     * Removes columns from the list if this sequence results in an opponent victory.
     *
     * @param availableCols list of columns currently available for play
     * @return a list of columns considered safe from immediate opponent victory
     */
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

    /**
     * Generates an action to block the opponent's immediate winning move in the specified column.
     *
     * Simulates the opponent placing a disk in the given column. If this results in a win for the opponent,
     * returns an action for the AI to place its own disk in that column to block the win. Returns null if no block is needed.
     *
     * @param col the column to check for an opponent's winning move
     * @param simpBoard the simplified board used for simulation
     * @param adversaryId the ID of the opponent player
     * @param iaId the ID of the AI player
     * @param row the row where the disk would land in the specified column
     * @param model the game model
     * @param stageModel the stage model for disk creation
     * @return an action list to block the opponent's win, or null if blocking is not required
     */
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

    /**
     * Generates an action to place a disk in the specified column if it results in an immediate win for the AI.
     *
     * Temporarily simulates placing the AI's disk in the given column and checks for a winning condition. If the move results in victory, returns the corresponding action list; otherwise, returns null.
     *
     * @param col the column to test for a winning move
     * @param simpBoard the simplified board used for simulation
     * @param iaId the AI player's identifier
     * @param row the row where the disk would be placed
     * @param model the game model
     * @param stageModel the stage model for disk creation
     * @return an action list to place the disk and end the turn if the move wins the game, or null if not a winning move
     */
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


    /**
     * Returns a list of column indices that are not full and are available for a move.
     *
     * @param board the current game board
     * @return a list of playable column indices
     */
    private static List<Integer> getCols(PuissanceXBoard board) {
        List<Integer> availableCols = new ArrayList<>();
        for (int col = 0; col < board.getNbCols(); col++) {
            if (!board.isColumnFull(col)) {
                availableCols.add(col);
            }
        }
        return availableCols;
    }


    /**
     * Counts the number of potential alignments of three disks for the specified player at the given position.
     *
     * Evaluates horizontal, vertical, and both diagonal directions to determine how many alignments of three could be formed by placing a disk at the specified row and column.
     *
     * @param row the row index to evaluate
     * @param col the column index to evaluate
     * @param playerId the ID of the player for whom to count potential alignments
     * @return the total number of potential alignments of three disks at the specified position, or 0 if the move is invalid
     */
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
    
    
    /**
     * Counts the number of potential horizontal alignments of at least two disks for the specified player
     * in any sequence of three consecutive columns that includes the given position.
     *
     * @param row the row index to evaluate
     * @param col the column index to center the check around
     * @param playerId the ID of the player whose alignments are counted
     * @return the number of horizontal three-cell segments containing at least two of the player's disks
     */
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
    
    /**
     * Counts the number of potential vertical alignments of at least two disks for the specified player
     * in a sequence of three cells starting from the given position.
     *
     * @param row the starting row index
     * @param col the column index
     * @param playerId the ID of the player to check alignments for
     * @return the number of vertical alignments of at least two disks in a three-cell segment
     */
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
    
    /**
     * Counts the number of potential diagonal alignments (top-left to bottom-right) of at least two disks for the specified player within sequences of three cells.
     *
     * @param board the simplified game board
     * @param row the row index to start checking from
     * @param col the column index to start checking from
     * @param playerId the ID of the player to check alignments for
     * @return the number of diagonal sequences of three cells containing at least two of the player's disks
     */
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
    
    /**
     * Counts the number of potential alignments of at least two disks for the specified player
     * in all bottom-left to top-right diagonal sequences of length three that include the given position.
     *
     * @param row the row index of the position to check
     * @param col the column index of the position to check
     * @param playerId the ID of the player whose alignments are counted
     * @return the number of diagonal sequences of three containing at least two of the player's disks
     */
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

    /**
     * Returns an action to play in the center column if it is available.
     *
     * @param availableCols list of columns that can be played
     * @param board the current game board
     * @return an action to play in the center column, or null if the center is not available
     */
    private ActionList playCenter(List<Integer> availableCols, PuissanceXBoard board) {
        int centerCol = board.getNbCols() / 2;
        if (availableCols.contains(centerCol)) {
            return getActions(centerCol);
        }
        return null;
    }

    /**
     * Checks whether the specified row and column represent a valid move on the board.
     *
     * A move is valid if the row and column are within the board's bounds and the column is not full.
     *
     * @param board the simplified board representation
     * @param row the row index to check
     * @param col the column index to check
     * @return true if the move is within bounds and the column is not full; false otherwise
     */
    private boolean isValidMove(SimplifyBoard board, int row, int col) {
        return row >= 0 && row < board.getNbRows() && 
               col >= 0 && col < board.getNbCols() && 
               !board.isColumnFull(col);
    }

    /**
     * Selects a default move by prioritizing the center column, then a random safe move, and finally any random available move.
     *
     * @param safeMoves list of columns considered safe from immediate opponent victory
     * @param availableCols list of all columns that are currently playable
     * @param board the current game board
     * @return an action to place a disk in the chosen column, or null if no moves are possible
     */
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
