package control.ai;

import boardifier.control.Controller;
import boardifier.model.Model;
import boardifier.model.action.ActionList;
import boardifier.control.ActionFactory;
import control.PuissanceXDecider;
import model.PuissanceXModel;
import model.PuissanceXStageModel;
import model.Board;
import model.Disk;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MinimaxAI extends PuissanceXDecider {

    private static final int MAX_DEPTH = 1; // Default search depth
    private static final int WIN_SCORE = 100000;
    private static final int LOSE_SCORE = -100000;
    private static final int DRAW_SCORE = 0;
    private Random random = new Random();
    private int dynamicMaxDepth; // To allow dynamic depth setting

    public MinimaxAI(Model model, Controller control) {
        super(model, control);
        this.dynamicMaxDepth = MAX_DEPTH; // Initialize with default
    }

    // Optional: Allow setting depth dynamically, e.g., for difficulty levels
    public MinimaxAI(Model model, Controller control, int searchDepth) {
        super(model, control);
        this.dynamicMaxDepth = Math.max(1, searchDepth); // Ensure depth is at least 1
    }


    @Override
    public ActionList decide() {
        PuissanceXModel pModel = (PuissanceXModel) this.model;
        PuissanceXStageModel stageModel = (PuissanceXStageModel) pModel.getGameStage();
        Board board = stageModel.getBoard();
        int aiPlayerId = pModel.getIdPlayer();
        // Determine opponent ID. Assuming player IDs are 0 and 1.
        int opponentId = (aiPlayerId == 0) ? 1 : 0;

        long startTime = System.currentTimeMillis();
        int bestCol = findBestMove(stageModel, board, aiPlayerId, opponentId);
        long endTime = System.currentTimeMillis();
        System.out.println("Minimax AI (depth " + dynamicMaxDepth + ") decision took: " + (endTime - startTime) + "ms");

        if (bestCol == -1) {
            // This case should ideally be handled if no moves are possible (e.g. board is full)
            // or if an unexpected error occurred. Fallback to a random valid move.
            System.out.println("Minimax AI couldn't determine a best move, resorting to a random valid move.");
            List<Integer> availableCols = new ArrayList<>();
            for (int col = 0; col < board.getNbCols(); col++) {
                if (!board.isColumnFull(col)) {
                    availableCols.add(col);
                }
            }
            if (availableCols.isEmpty()) {
                // No moves possible, should be a draw or already ended.
                // Return an empty action list or handle as game end.
                return new ActionList();
            }
            bestCol = availableCols.get(random.nextInt(availableCols.size()));
        }

        int row = board.getFirstEmptyRow(bestCol);
        // Ensure row is valid before proceeding
        if (row == -1) { // Column is somehow full, should have been caught by findBestMove or isColumnFull
            System.out.println("Error: Minimax chose column " + bestCol + " but it's full or invalid.");
            // Fallback to first available column if any
            for (int c = 0; c < board.getNbCols(); c++) {
                if (!board.isColumnFull(c)) {
                    bestCol = c;
                    row = board.getFirstEmptyRow(bestCol);
                    break;
                }
            }
            if (row == -1) return new ActionList(); // No move possible
        }

        Disk disk = new Disk(aiPlayerId, stageModel);
        ActionList actions = ActionFactory.generatePutInContainer(pModel, disk, "board", row, bestCol);
        actions.setDoEndOfTurn(true);

        System.out.println("Minimax AI player " + (aiPlayerId + 1) + " chooses column " + bestCol);
        return actions;
    }

    private int findBestMove(PuissanceXStageModel stageModel, Board board, int aiPlayerId, int opponentId) {
        int bestScore = Integer.MIN_VALUE;
        List<Integer> bestCols = new ArrayList<>(); // To store columns with the same best score

        for (int col = 0; col < board.getNbCols(); col++) {
            if (!board.isColumnFull(col)) {
                int row = board.getFirstEmptyRow(col);
                Disk tempDisk = makeMove(board, col, aiPlayerId, stageModel);

                // Depth is dynamicMaxDepth - 1 because the first move is made here
                int currentScore = minimax(stageModel, board, dynamicMaxDepth - 1, row, col, aiPlayerId, false, Integer.MIN_VALUE, Integer.MAX_VALUE, aiPlayerId, opponentId);

                undoMove(board, tempDisk, row, col);

                if (currentScore > bestScore) {
                    bestScore = currentScore;
                    bestCols.clear();
                    bestCols.add(col);
                } else if (currentScore == bestScore) {
                    bestCols.add(col);
                }
            }
        }

        if (bestCols.isEmpty()) {
            return -1; // No valid move found
        }
        // Pick a random move from the best moves to introduce variability
        return bestCols.get(random.nextInt(bestCols.size()));
    }

    private int minimax(PuissanceXStageModel stageModel, Board board, int depth,
                        int lastPlayedRow, int lastPlayedCol, int playerWhoMadeLastMoveId,
                        boolean isMaximizingPlayerTurn, int alpha, int beta,
                        int aiPlayerId, int opponentId) {

        // Check terminal states based on the last move
        if (stageModel.checkWin(lastPlayedRow, lastPlayedCol, playerWhoMadeLastMoveId)) {
            if (playerWhoMadeLastMoveId == aiPlayerId) {
                return WIN_SCORE + depth; // Prefer faster wins
            } else {
                return LOSE_SCORE - depth; // Prefer slower losses
            }
        }

        if (stageModel.isBoardFull()) {
            return DRAW_SCORE;
        }

        if (depth == 0) {
            return evaluateBoard(stageModel, board, aiPlayerId, opponentId);
        }

        if (isMaximizingPlayerTurn) { // AI's turn (Maximizer)
            int maxEval = Integer.MIN_VALUE;
            for (int col = 0; col < board.getNbCols(); col++) {
                if (!board.isColumnFull(col)) {
                    int row = board.getFirstEmptyRow(col);
                    Disk tempDisk = makeMove(board, col, aiPlayerId, stageModel);
                    int eval = minimax(stageModel, board, depth - 1, row, col, aiPlayerId, false, alpha, beta, aiPlayerId, opponentId);
                    undoMove(board, tempDisk, row, col);
                    maxEval = Math.max(maxEval, eval);
                    alpha = Math.max(alpha, eval);
                    if (beta <= alpha) {
                        break; // Beta cut-off
                    }
                }
            }
            return maxEval;
        } else { // Opponent's turn (Minimizer)
            int minEval = Integer.MAX_VALUE;
            for (int col = 0; col < board.getNbCols(); col++) {
                if (!board.isColumnFull(col)) {
                    int row = board.getFirstEmptyRow(col);
                    Disk tempDisk = makeMove(board, col, opponentId, stageModel);
                    int eval = minimax(stageModel, board, depth - 1, row, col, opponentId, true, alpha, beta, aiPlayerId, opponentId);
                    undoMove(board, tempDisk, row, col);
                    minEval = Math.min(minEval, eval);
                    beta = Math.min(beta, eval);
                    if (beta <= alpha) {
                        break; // Alpha cut-off
                    }
                }
            }
            return minEval;
        }
    }

    private Disk makeMove(Board board, int col, int playerId, PuissanceXStageModel stageModel) {
        int row = board.getFirstEmptyRow(col);
        if (row != -1) {
            Disk disk = new Disk(playerId, stageModel); // Pass the stage model
            board.addElement(disk, row, col, false); // New call with doEvent = false
            return disk; // Return the disk that was placed
        }
        return null; // Column was full or invalid
    }

    private void undoMove(Board board, Disk disk, int row, int col) {
        board.removeElement(disk, false); // New call with doEvent = false
    }

    private int evaluateBoard(PuissanceXStageModel stageModel, Board board, int aiPlayerId, int opponentId) {
        int score = 0;
        int winCondition = stageModel.getWinCondition();

        // Evaluate center column control (more valuable)
        int centerCol = board.getNbCols() / 2;
        for (int r = 0; r < board.getNbRows(); r++) {
            if (!board.getElements(r, centerCol).isEmpty()) {
                Disk d = (Disk) board.getElements(r, centerCol).get(0);
                if (d.getPlayerId() == aiPlayerId) {
                    score += 3;
                } else if (d.getPlayerId() == opponentId) {
                    score -= 3;
                }
            }
        }

        // Evaluate horizontal, vertical, and diagonal opportunities
        score += evaluateLines(board, aiPlayerId, opponentId, winCondition);

        return score;
    }

    private int evaluateLines(Board board, int aiPlayerId, int opponentId, int winCondition) {
        int score = 0;
        int rows = board.getNbRows();
        int cols = board.getNbCols();

        // Horizontal check
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c <= cols - winCondition; c++) {
                score += scoreSequence(board, r, c, 0, 1, winCondition, aiPlayerId, opponentId);
            }
        }

        // Vertical check
        for (int c = 0; c < cols; c++) {
            for (int r = 0; r <= rows - winCondition; r++) {
                score += scoreSequence(board, r, c, 1, 0, winCondition, aiPlayerId, opponentId);
            }
        }

        // Diagonal (bottom-left to top-right)
        for (int r = winCondition - 1; r < rows; r++) {
            for (int c = 0; c <= cols - winCondition; c++) {
                score += scoreSequence(board, r, c, -1, 1, winCondition, aiPlayerId, opponentId);
            }
        }

        // Diagonal (top-left to bottom-right)
        for (int r = 0; r <= rows - winCondition; r++) {
            for (int c = 0; c <= cols - winCondition; c++) {
                score += scoreSequence(board, r, c, 1, 1, winCondition, aiPlayerId, opponentId);
            }
        }
        return score;
    }

    private int scoreSequence(Board board, int r, int c, int dr, int dc, int len, int aiPlayerId, int opponentId) {
        int aiCount = 0;
        int opponentCount = 0;

        for (int i = 0; i < len; i++) {
            if (!board.getElements(r + i * dr, c + i * dc).isEmpty()) {
                Disk disk = (Disk) board.getElements(r + i * dr, c + i * dc).get(0);
                if (disk.getPlayerId() == aiPlayerId) {
                    aiCount++;
                } else if (disk.getPlayerId() == opponentId) {
                    opponentCount++;
                }
            }
        }

        if (aiCount > 0 && opponentCount == 0) { // AI has a potential line
            if (aiCount == len -1) return 100; // Threatens win
            if (aiCount == len -2) return 10;
            return 1;
        } else if (opponentCount > 0 && aiCount == 0) { // Opponent has a potential line
            if (opponentCount == len-1) return -100; // Must block
            if (opponentCount == len-2) return -10;
            return -1;
        }
        return 0; // Empty or mixed
    }
}