package model;

import boardifier.model.Model;

public class PuissanceXModel extends Model {
    private int boardRows;
    private int boardCols;
    private int winCondition;
    
    public PuissanceXModel() {
        super();
        // Default values
        this.boardRows = 6;
        this.boardCols = 7;
        this.winCondition = 4;
    }
    
    public void setBoardRows(int boardRows) {
        this.boardRows = boardRows;
    }
    
    public int getBoardRows() {
        return boardRows;
    }
    
    public void setBoardCols(int boardCols) {
        this.boardCols = boardCols;
    }
    
    public int getBoardCols() {
        return boardCols;
    }
    
    public void setWinCondition(int winCondition) {
        this.winCondition = winCondition;
    }
    
    public int getWinCondition() {
        return winCondition;
    }
}
