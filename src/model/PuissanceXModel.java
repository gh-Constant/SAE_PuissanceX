package model;

import boardifier.model.Model;

public class PuissanceXModel extends Model {
    private int boardRows;
    private int boardCols;
    private int winCondition;
    private PuissanceXDiskPot player1Pot; // Red pot
    private PuissanceXDiskPot player2Pot; // Yellow pot

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

    public PuissanceXDiskPot getPlayer1Pot() {
        return player1Pot;
    }

    public void setPlayer1Pot(PuissanceXDiskPot player1Pot) {
        this.player1Pot = player1Pot;
    }

    public PuissanceXDiskPot getPlayer2Pot() {
        return player2Pot;
    }

    public void setPlayer2Pot(PuissanceXDiskPot player2Pot) {
        this.player2Pot = player2Pot;
    }
}
