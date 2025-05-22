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
    private Tree root;
    public static final int WIN_SCORE = 1_000_000_000;
    public static final int DEFAULT_DEPTH = 1;

    public static int COUNT_OPERATIONS = 0;

    public Minimax(Model model, Controller control) {
        super(model, control);
    }


    @Override
    public ActionList decide() {
        PuissanceXStageModel stageModel = (PuissanceXStageModel) model.getGameStage();
        PuissanceXBoard board = stageModel.getBoard();

        this.root = new Tree(new SimplifyBoard(board), board.getNbRows(), board.getNbCols(), model.getIdPlayer() == 1, -1, stageModel.getWinCondition());


        int depth = DEFAULT_DEPTH;
        COUNT_OPERATIONS = 0;
        this.root.mimimax(depth, -WIN_SCORE, WIN_SCORE);


        /*
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < 3000) {
            this.root.mimimax((PuissanceXModel) this.model, this.control, depth, -Minimax.WIN_SCORE, Minimax.WIN_SCORE);
            depth += 1;
        }
        */
        Tree BestChild = this.root.getBestChildren();

        Logger.info(COUNT_OPERATIONS + " operations");
        Logger.info("player : " + model.getIdPlayer());
        Logger.info("Score : " + BestChild.getScore());


        return this.getActions(BestChild.getActionCol());
    }
}


class Tree {
    private final int winCondition;
    private final int id;
    private float score;
    private final boolean isMaximizing;

    private SimplifyBoard board;
    private final int nbRows;
    private final int nbCols;

    private Tree[] children;
    private Tree bestChild;

    private final int actionCol;

    private boolean isLeaf;


    public Tree(SimplifyBoard board, int nbRows, int nbCols, boolean isMaximizing, int actionCol, int winCondition) {
        this.board = board;
        this.nbRows = nbRows;
        this.nbCols = nbCols;

        this.isMaximizing = isMaximizing;
        this.id = isMaximizing ? 1 : 0;
        this.children = new Tree[nbCols];
        this.bestChild = null;
        this.actionCol = actionCol;

        if (this.isMaximizing) {
            this.score = -Minimax.WIN_SCORE;
        } else {
            this.score = Minimax.WIN_SCORE;
        }
        this.winCondition = winCondition;
        this.isLeaf = false;
    }



    public Tree getBestChildren() {
        return this.bestChild;
    }


    public float mimimax(int depth,
                         float alpha, float beta // value should be more than x or less than x to be worth calculate
    ) {
        boolean isWinMove = false;
        Minimax.COUNT_OPERATIONS++;

        if (this.actionCol != -1) {
            isWinMove = this.play();
        }


        if (depth <= 0 || isWinMove || board.isFull()) {
            this.isLeaf = true;

            this.evaluate(isWinMove);
            return this.score;
        }


        this.isLeaf = false;

        for (int i = 0; i < this.nbCols; i++) {
            if (board.isColumnFull(i)) { // TODO
                continue;
            }
            this.children[i] = new Tree(this.board, this.nbRows , this.nbCols, !this.isMaximizing, i, this.winCondition);
            float childScore = this.children[i].mimimax(depth - 1, alpha, beta);
            this.children[i].back();


            if (this.isMaximizing) {
                alpha = Math.max(alpha, childScore);
                if (bestChild == null || childScore > this.bestChild.getScore()) {
                    this.score = childScore;
                    this.bestChild = this.children[i];
                }
            } else {
                beta = Math.min(beta, childScore);
                if (bestChild == null || childScore < this.bestChild.getScore()) {
                    this.score = childScore;
                    this.bestChild = this.children[i];
                }
            }

            if (alpha > beta) {
                return this.score;
            }
        }

        return this.score;
    }

    public boolean play() {
        this.board.add(this.actionCol, this.id);
        return board.checkWin(this.actionCol, this.winCondition);
    }

    public void back() {
        this.board.suppr(this.actionCol);
    }

    public int getActionCol() {
        return this.actionCol;
    }

    private float evaluateAlign(int row, int col) { // TODO: improve
        float score = 0;

        for (int i = winCondition; i > 1; i--) {
            if (board.checkWin(row, col, i)) {
                score += ((float) Math.pow(5, i));
            }
        }

        return score;
    }

    public void evaluate(boolean isWinMove) {
        if (isWinMove) {
            if (this.isMaximizing) {
                this.score = Minimax.WIN_SCORE - board.getNbDisk();
            } else {
                this.score = -Minimax.WIN_SCORE + board.getNbDisk();
            }
            return;
        }

        float center = ((float) this.nbCols) / 2;


        float score = 0;

        for (int col = 0; col < nbCols; col++) {
            float note = (1 - (Math.abs(center - col) / center)) * 5; // center disk are better

            for (int row = 0; row < nbRows; row++) {
                if (board.get(row, col) == -1) {
                    continue;
                }
                if (board.get(row, col) == this.id) {
                    score += note;
                    score += this.evaluateAlign(row, col);
                } else {
                    score -= note;
                    score -= this.evaluateAlign(row, col);
                }
            }
        }

        this.score = score;
    }

    public float getScore() {
        return this.score;
    }
}