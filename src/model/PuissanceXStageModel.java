package model;

import boardifier.model.GameStageModel;
import boardifier.model.Model;
import boardifier.model.StageElementsFactory;
import boardifier.model.TextElement;

public class PuissanceXStageModel extends GameStageModel {
    private PuissanceXBoard board;
    private TextElement playerText;
    private int winCondition;
    
    public PuissanceXStageModel(String name, Model model) {
        super(name, model);
        // Get win condition from the model if it's a PuissanceXModel
        if (model instanceof PuissanceXModel) {
            this.winCondition = ((PuissanceXModel) model).getWinCondition();
        } else {
            // Default win condition is 4
            this.winCondition = 4;
        }
    }
    
    public void setWinCondition(int winCondition) {
        this.winCondition = winCondition;
    }
    
    public int getWinCondition() {
        return winCondition;
    }
    
    public PuissanceXBoard getBoard() {
        return board;
    }
    
    public void setBoard(PuissanceXBoard board) {
        this.board = board;
        addContainer(board);
    }
    
    public TextElement getPlayerText() {
        return playerText;
    }
    
    public void setPlayerText(TextElement playerText) {
        this.playerText = playerText;
        addElement(playerText);
    }
    
    @Override
    public StageElementsFactory getDefaultElementFactory() {
        return new PuissanceXStageFactory(this);
    }
    
    // Method to check for a win
    public boolean checkWin(int row, int col, int playerId) {
        // Check horizontal, vertical, and diagonal lines
        return checkHorizontal(row, col, playerId) || 
               checkVertical(row, col, playerId) || 
               checkDiagonal1(row, col, playerId) || 
               checkDiagonal2(row, col, playerId);
    }
    
    // Helper methods for win checking
    private boolean checkHorizontal(int row, int col, int playerId) {
        int count = 0;
        // Check to the left
        for (int c = col; c >= 0; c--) {
            if (isDiskOfPlayer(row, c, playerId)) count++;
            else break;
        }
        // Check to the right
        for (int c = col + 1; c < board.getNbCols(); c++) {
            if (isDiskOfPlayer(row, c, playerId)) count++;
            else break;
        }
        return count >= winCondition;
    }
    
    private boolean checkVertical(int row, int col, int playerId) {
        int count = 0;
        // Check upward
        for (int r = row; r >= 0; r--) {
            if (isDiskOfPlayer(r, col, playerId)) count++;
            else break;
        }
        // Check downward
        for (int r = row + 1; r < board.getNbRows(); r++) {
            if (isDiskOfPlayer(r, col, playerId)) count++;
            else break;
        }
        return count >= winCondition;
    }
    
    private boolean checkDiagonal1(int row, int col, int playerId) {
        int count = 0;
        // Check up-left
        for (int r = row, c = col; r >= 0 && c >= 0; r--, c--) {
            if (isDiskOfPlayer(r, c, playerId)) count++;
            else break;
        }
        // Check down-right
        for (int r = row + 1, c = col + 1; r < board.getNbRows() && c < board.getNbCols(); r++, c++) {
            if (isDiskOfPlayer(r, c, playerId)) count++;
            else break;
        }
        return count >= winCondition;
    }
    
    private boolean checkDiagonal2(int row, int col, int playerId) {
        int count = 0;
        // Check up-right
        for (int r = row, c = col; r >= 0 && c < board.getNbCols(); r--, c++) {
            if (isDiskOfPlayer(r, c, playerId)) count++;
            else break;
        }
        // Check down-left
        for (int r = row + 1, c = col - 1; r < board.getNbRows() && c >= 0; r++, c--) {
            if (isDiskOfPlayer(r, c, playerId)) count++;
            else break;
        }
        return count >= winCondition;
    }
    
    private boolean isDiskOfPlayer(int row, int col, int playerId) {
        if (board.getElements(row, col).isEmpty()) return false;
        if (board.getElements(row, col).get(0) instanceof PuissanceXDisk) {
            PuissanceXDisk puissanceXDisk = (PuissanceXDisk) board.getElements(row, col).get(0);
            return puissanceXDisk.getPlayerId() == playerId;
        }
        return false;
    }
    
    // Check if the board is full (draw)
    public boolean isBoardFull() {
        for (int col = 0; col < board.getNbCols(); col++) {
            if (!board.isColumnFull(col)) return false;
        }
        return true;
    }
}
