package control.ai;

import boardifier.control.Controller;
import boardifier.control.Logger;
import boardifier.model.Model;
import boardifier.model.action.ActionList;
import control.PuissanceXDecider;
import control.SimplifyBoard;
import model.PuissanceXBoard;
import model.PuissanceXStageModel;

public class Minimax extends PuissanceXDecider {
    public static final float WIN_SCORE = 100_000_000;
    public static final int DEFAULT_DEPTH = 8;

    private int WIN_CONDITION = 4;
    private int COUNT_OPERATIONS = 0;

    private SimplifyBoard board;

    public Minimax(Model model, Controller control) {
        super(model, control);
    }


    @Override
    public ActionList decide() {
        int col = solve(DEFAULT_DEPTH);

        /*
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < 3000) {
            this.root.mimimax((PuissanceXModel) this.model, this.control, depth, -Minimax.WIN_SCORE, Minimax.WIN_SCORE);
            depth += 1;
        }
        */

        Logger.info(COUNT_OPERATIONS + " operations");
        Logger.info("player : " + model.getIdPlayer());
        // Logger.info("Score : " + BestChild.getScore());

        return this.getActions(col);
    }


    public int solve(int depth) {
        PuissanceXStageModel stageModel = (PuissanceXStageModel) model.getGameStage();
        PuissanceXBoard boardX = stageModel.getBoard();
        this.board = new SimplifyBoard(boardX);

        WIN_CONDITION = stageModel.getWinCondition();
        COUNT_OPERATIONS = 0;

        final int id = model.getIdPlayer();
        float score = 0;
        int col = -1;

        for (int i = 0; i < board.getNbCols(); i++) {
            if (board.isColumnFull(i)) {
                continue;
            }

            if (play(i, id)) {
                score = WIN_SCORE - board.getNbDisk();
                col = i;
                break;
            }

            float s = mimimax(depth, id, -WIN_SCORE, WIN_SCORE);
            if (col == -1 || s > score) {
                score = s;
                col = i;
            }
        }


        Logger.info("AI player " + id + " selected column: " + col + " with score: " + score);
        return col;
    }

    public float mimimax(int depth, int id,
                         float alpha, float beta // value should be more than x or less than x to be worth calculate
    ) {
        COUNT_OPERATIONS++;

        if (board.isFull()) {
            return 0;
        }

        if (depth <= 0) {
            return this.evaluate(id);
        }

        float score = -Minimax.WIN_SCORE;

        for (int i = 0; i < this.board.getNbCols(); i++) {
            if (board.isColumnFull(i)) {
                continue;
            }

            if (play(i, id)) {
                score = Minimax.WIN_SCORE - board.getNbDisk();
                back(i);
                break;
            } else {
                score = Math.max(score, -mimimax(depth - 1, 1 - id, -beta, -alpha));
            }
            back(i);

            if (score >= beta) {
                break;
            }

            alpha = Math.max(alpha, score);

        }

        return score;
    }

    public boolean play(int col, int id) {
        this.board.add(col, id);
        return board.checkWin(col, WIN_CONDITION);
    }

    public void back(int col) {
        this.board.suppr(col);
    }


    private float evaluateAlign(int row, int col) { // TODO: improve
        float score = 0;

        for (int i = WIN_CONDITION; i > 1; i--) {
            if (board.checkWin(row, col, i)) {
                score += ((float) Math.pow(3, i));
            }
        }

        return score;
    }

    public float evaluate(int id) {
        float center = ((float) this.board.getNbCols()) / 2;


        float score = 0;

        for (int col = 0; col < this.board.getNbCols(); col++) {
            float note = (0.5f - (Math.abs(center - col) / center)) * 5; // center disk are better

            for (int row = 0; row < this.board.getNbRows(); row++) {
                if (board.get(row, col) == -1) {
                    continue;
                }
                if (board.get(row, col) == id) {
                    score += note;
                    score += this.evaluateAlign(row, col);
                } else {
                    score -= note;
                    score -= this.evaluateAlign(row, col);
                }
            }
        }

        return score;
    }
}