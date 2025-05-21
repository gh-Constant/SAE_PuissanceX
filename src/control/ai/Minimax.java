package control.ai;

import boardifier.control.ActionFactory;
import boardifier.control.ActionPlayer;
import boardifier.control.Controller;
import boardifier.control.Logger;
import boardifier.model.Model;
import boardifier.model.Player;
import boardifier.model.action.ActionList;
import control.PuissanceXDecider;
import model.PuissanceXBoard;
import model.PuissanceXDisk;
import model.PuissanceXModel;
import model.PuissanceXStageModel;

public class Minimax extends PuissanceXDecider {
    private Tree root;
    public static final int WIN_SCORE = 1_000_000;
    public static final int WIN_ALIGN_SCORE = 10_000;

    public static int COUNT_OPERATIONS = 0;

    public Minimax(Model model, Controller control) {
        super(model, control);
    }


    @Override
    public ActionList decide() {
        PuissanceXStageModel stageModel = (PuissanceXStageModel) model.getGameStage();
        PuissanceXBoard board = stageModel.getBoard();

        this.root = new Tree(board.getNbCols(), true, -1);


        int depth = 1;
        COUNT_OPERATIONS = 0;
        this.root.mimimax((PuissanceXModel) this.model, this.control, depth, -Minimax.WIN_SCORE, Minimax.WIN_SCORE);

        /*
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < 3000) {
            this.root.mimimax((PuissanceXModel) this.model, this.control, depth, -Minimax.WIN_SCORE, Minimax.WIN_SCORE);
            depth += 1;
        }
        */
        Logger.info(COUNT_OPERATIONS + " operations");

        int chosenCol = this.root.getBestChildren().getActionCol();

        return this.getActions(chosenCol);
    }
}


class Score {
    enum EvaluationType {
        EQUAL_TO,
        LESS_THAN,
        MORE_THAN,
        UNDEFINED
    }
    public float evaluationValue;
    public EvaluationType evaluationType;

    public Score() {
        this.evaluationType = EvaluationType.UNDEFINED;
        this.evaluationValue = 0.0f;
    }

    public Score(float evaluationValue) {
        this.evaluationValue = evaluationValue;
        this.evaluationType = EvaluationType.EQUAL_TO;
    } 

}

class Tree {
    private Score score;
    private final boolean isMaximizing;

    private final int nbCols;
    private Tree[] children;

    private final int actionCol;

    private boolean isLeaf;


    public Tree(int nbCols, boolean isMaximizing, int actionCol) {
        this.nbCols = nbCols;
        this.isMaximizing = isMaximizing;
        this.children = new Tree[nbCols];
        this.actionCol = actionCol;
        this.score = new Score();
        this.isLeaf = false;
    }



    public Tree getBestChildren() {
        Tree bestChild = null;


        for (int i = 0; i < nbCols; i++) {
            Tree child = children[i];
            if (child == null) {
                continue;
            }
            if (bestChild == null) {
                bestChild = child;
            } else if (this.isMaximizing) {
                if (child.score.evaluationValue > bestChild.score.evaluationValue) {
                    bestChild = child;
                    bestChild.score.evaluationValue = child.score.evaluationValue;
                }
            } else {
                if (child.score.evaluationValue < bestChild.score.evaluationValue) {
                    bestChild = child;
                    bestChild.score.evaluationValue = child.score.evaluationValue;
                }
            }

        }

        return bestChild;
    }

    public boolean isCompleted() {
        for (int i = 0; i < nbCols; i++) {
            Tree child = children[i];
            if (child == null) {
                return false;
            }
        }
        return true;
    }

    public void calculateBestScore(PuissanceXModel model) {
        if (this.isCompleted()) {
            if (this.isMaximizing) {
                this.score.evaluationType = Score.EvaluationType.MORE_THAN;
            } else {
                this.score.evaluationType = Score.EvaluationType.LESS_THAN;
            }
        }

        Tree bestChild = this.getBestChildren();
        if (bestChild == null) {
            this.score = new Score(Tree.evaluate(model));
        } else {
            this.score.evaluationValue = bestChild.score.evaluationValue;
        }
    }

    public Score getScore() {
        return this.score;
    }

    public Score mimimax(PuissanceXModel model, Controller control, int depth,
                         float alpha, float beta // value should be more than x or less than x to be worth calculate
    ) {

        PuissanceXStageModel stageModel = (PuissanceXStageModel) model.getGameStage();
        PuissanceXBoard board = stageModel.getBoard();

        boolean isWinMove = false;
        Minimax.COUNT_OPERATIONS++;
        int actionRow = board.getFirstEmptyRow(this.actionCol);

        if (this.actionCol != -1) {
            isWinMove = Tree.play(model, control, actionRow, this.actionCol);
        }


        if (depth <= 0 || isWinMove || board.isFull()) {
            this.isLeaf = true;
            this.score.evaluationType = Score.EvaluationType.EQUAL_TO;
            this.score.evaluationValue = Tree.evaluate(model);
            return this.score;
        }


        this.isLeaf = false;

        for (int i = 0; i < this.nbCols; i++) {
            if (board.isColumnFull(i)) {
                continue;
            }
            this.children[i] = new Tree(this.nbCols, !this.isMaximizing, i);
            Score childScore = this.children[i].mimimax(model, control, depth - 1, alpha, beta);
            this.children[i].cancelAction(model, control, actionRow, this.actionCol);

            if (this.isMaximizing) {
                alpha = Math.max(alpha, childScore.evaluationValue);
            } else {
                beta = Math.min(beta, childScore.evaluationValue);
            }
            // Logger.debug(alpha + "," + beta + " --> " + childScore.evaluationValue);

            if (alpha >= beta) {
                break;
            }

        }


        calculateBestScore(model);

        return this.score;
    }

    public int getActionCol() {
        return this.actionCol;
    }




    public static boolean play(PuissanceXModel model, Controller control, int row, int col) {
        
    }

    public void cancelAction(PuissanceXModel model, Controller control, int row, int col) {

    }

    private static float evaluateAlign(PuissanceXModel model, int row, int col, int id) {
        PuissanceXStageModel stageModel = (PuissanceXStageModel) model.getGameStage();
        int winCondition = stageModel.getWinCondition();

        float score = 0;
        if (stageModel.checkWin(row, col, id)) {
            score += Minimax.WIN_SCORE * winCondition;
            System.out.print(score + " - ");
        }

        for (int i = stageModel.getWinCondition(); i > 1; i--) {
            stageModel.setWinCondition(i);
            if (stageModel.checkWin(row, col, id)) {
                score += i * Minimax.WIN_ALIGN_SCORE / ((float) Math.pow(10, (winCondition-i+1)));
            }
        }

        stageModel.setWinCondition(winCondition);
        return score;
    }

    public static float evaluate(PuissanceXModel model) { // TODO: need to be improved
        PuissanceXStageModel stageModel = (PuissanceXStageModel) model.getGameStage();
        PuissanceXBoard board = stageModel.getBoard();

        int nbCols = board.getNbCols();
        float center = ((float) nbCols) / 2;
        int nbRows = board.getNbRows();

        int PLAYER_ID, AI_ID;
        if (model.getPlayers().getFirst().getType() == Player.HUMAN) {
            PLAYER_ID = 0;
            AI_ID = 1;
        } else {
            PLAYER_ID = 1;
            AI_ID = 0;
        }

        float score = 0;

        for (int col = 0; col < nbCols; col++) {
            float note = Math.abs(center - col) / center;

            for (int row = 0; row < nbRows; row++) {
                if (board.getElements(row, col).isEmpty() || !(board.getElements(row, col).getFirst() instanceof PuissanceXDisk)) {
                    continue;
                }
                if (((PuissanceXDisk) board.getElements(row, col).getFirst()).getPlayerId() == AI_ID) {
                    score += note;
                    score += Tree.evaluateAlign(model, row, col, AI_ID);
                } else if (((PuissanceXDisk) board.getElements(row, col).getFirst()).getPlayerId() == PLAYER_ID) {
                    score -= note;
                    score -= Tree.evaluateAlign(model, row, col, PLAYER_ID);
                }
            }
        }

        return score;
    }

}