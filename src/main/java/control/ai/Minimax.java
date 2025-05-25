package control.ai;

import boardifier.control.Controller;
import boardifier.control.Logger;
import boardifier.model.Model;
import boardifier.model.action.ActionList;
import control.PuissanceXDecider;
import control.SimplifyBoard;
import model.PuissanceXBoard;
import model.PuissanceXStageModel;

import java.util.ArrayList;
import java.util.List;

public class Minimax extends PuissanceXDecider {
    public static final float WIN_SCORE = 100_000_000;
    public static final int DEFAULT_DEPTH = 10;

    private int WIN_CONDITION = 4;
    private int COUNT_OPERATIONS = 0;

    private SimplifyBoard board;
    List<Integer> DEFAULT_SEARCH_ORDER;

    public Minimax(Model model, Controller control) {
        super(model, control);
    }


    @Override
    public ActionList decide() {
        long startTime = System.nanoTime();

        PuissanceXStageModel stageModel = (PuissanceXStageModel) model.getGameStage();
        PuissanceXBoard boardX = stageModel.getBoard();
        this.board = new SimplifyBoard(boardX);

        WIN_CONDITION = stageModel.getWinCondition();
        COUNT_OPERATIONS = 0;
        calculateDefaultSearchOrder();

        int col = -1;
        float bestScore = -WIN_SCORE;

        final int id = model.getIdPlayer();
        float[] scores = new float[board.getNbCols()];

        for (int c : DEFAULT_SEARCH_ORDER) {
            if (board.isColumnFull(c)) {
                continue;
            }

            makeMove(c, id);
            scores[c] = -negamax(DEFAULT_DEPTH-1, 1-id, c, -WIN_SCORE, WIN_SCORE);
            if (col == -1 || scores[c] > bestScore) {
                bestScore = scores[c];
                col = c;
            }
            undoMove(c);
        }

        long endTime = System.nanoTime();

        System.out.printf("%.0f", scores[0]);
        for (int i = 1; i < board.getNbCols(); i++) {
            System.out.printf(" : %.0f", scores[i]);
        }
        System.out.println();

        Logger.info("AI player " + id + " selected column: " + col + " with score: " + scores[col]);
        Logger.info(COUNT_OPERATIONS + " operations en " + (endTime - startTime) / 1_000_000 + " ms");

        return this.getActions(col);
    }

    public float negamax(int depth, int id, int col,
                         float alpha, float beta // value should be more than x or less than x to be worth calculate
    ) {
        COUNT_OPERATIONS++;

        if (board.checkWin(col, WIN_CONDITION)) {
            return -(WIN_SCORE + depth);
        }

        if (board.isFull()) {
            return 0;
        }

        if (depth <= 0) {
            return this.evaluate(id);
        }

        float score = -(WIN_SCORE - board.getNbDisk());

        for (int c : DEFAULT_SEARCH_ORDER) {
            if (c == -1 || board.isColumnFull(c)) {
                continue;
            }

            makeMove(c, id);
            score = Math.max(score, -negamax(depth - 1, 1 - id, c, -beta, -alpha));
            undoMove(c);


            alpha = Math.max(alpha, score);

            if (alpha >= beta) {
                break;
            }
        }

        return score;
    }

    private void calculateDefaultSearchOrder() {
        DEFAULT_SEARCH_ORDER = new ArrayList<>();

        int nbCols = this.board.getNbCols();
        int center = nbCols / 2;

        DEFAULT_SEARCH_ORDER.add(center);
        if (nbCols % 2 != 0) {
            for (int i = 1; i <= center; i++) {
                DEFAULT_SEARCH_ORDER.add(center - i);
                DEFAULT_SEARCH_ORDER.add(center + i);
            }
        } else {
            DEFAULT_SEARCH_ORDER.add(center - 1);
            for (int i = 1; i < center; i++) {
                DEFAULT_SEARCH_ORDER.add(center - 1 - i);
                DEFAULT_SEARCH_ORDER.add(center + i);
            }
        }
    }


    public void makeMove(int col, int id) {
        this.board.add(col, id);
    }

    public void undoMove(int col) {
        this.board.suppr(col);
    }


    private float evaluateAlign(int row, int col) { // TODO: improve
        float score = 0;

        for (int i = WIN_CONDITION; i > 1; i--) {
            if (board.checkWin(row, col, i)) {
                score += ((float) Math.pow(3, i)) ;
            }
        }

        return score;
    }

    public float evaluate(int id) {
        float center = ((float) this.board.getNbCols()) / 2.0f - 0.5f;


        float score = 0;

        for (int col = 0; col < this.board.getNbCols(); col++) {
            float colWeight = (1.0f - Math.abs(center - col) / center) * 31.0f; // center disk are better

            for (int row = 0; row < this.board.getNbRows(); row++) {
                if (board.get(row, col) == -1) {
                    continue;
                }
                if (board.get(row, col) == id) {
                    score += colWeight;
                    score += this.evaluateAlign(row, col);
                } else {
                    score -= colWeight;
                    score -= this.evaluateAlign(row, col);
                }
            }
        }
        return score;
    }
}