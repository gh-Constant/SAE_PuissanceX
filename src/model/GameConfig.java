package model;

public class GameConfig {
    private static int boardRows = 6;
    private static int boardCols = 7;
    private static int winCondition = 4;
    
    public static void setConfig(int rows, int cols, int win) {
        boardRows = rows;
        boardCols = cols;
        winCondition = win;
    }
    
    public static int getBoardRows() {
        return boardRows;
    }
    
    public static int getBoardCols() {
        return boardCols;
    }
    
    public static int getWinCondition() {
        return winCondition;
    }
}