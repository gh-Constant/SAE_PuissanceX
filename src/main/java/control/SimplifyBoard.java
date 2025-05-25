package control;

import boardifier.model.GameElement;
import model.PuissanceXBoard;
import model.PuissanceXDisk;

import java.util.ArrayList;
import java.util.List;

public class SimplifyBoard {
    private int [][] board;
    private final int nbRows;
    private final int nbCols;
    private int nbDisk;

    public SimplifyBoard(PuissanceXBoard board) {
        this.nbRows = board.getNbRows();
        this.nbCols = board.getNbCols();
        this.board = new int[this.nbRows][this.nbCols];

        this.nbDisk = 0;

        for (int row = 0; row < board.getNbRows(); row++) {
            for (int col = 0; col < board.getNbCols(); col++) {
                List<GameElement> elt = board.getElements(row, col);
                if (elt.isEmpty()) {
                    this.board[row][col] = -1;
                } else {
                    this.board[row][col] = ((PuissanceXDisk) elt.get(0)).getPlayerId();
                    this.nbDisk++;
                }
            }
        }
    }

    public SimplifyBoard(SimplifyBoard other) {
        this.nbRows = other.nbRows;
        this.nbCols = other.nbCols;
        this.nbDisk = other.nbDisk;
        this.board = new int[this.nbRows][this.nbCols];

        for (int row = 0; row < this.nbRows; row++) {
            System.arraycopy(other.board[row], 0, this.board[row], 0, this.nbCols);
        }
    }

    public SimplifyBoard copy() {
        return new SimplifyBoard(this);
    }

    public boolean isFull() {
        for (int col = 0; col < this.nbCols; col++) {
            if (!this.isColumnFull(col)) {
                return false;
            }
        }
        return true;
    }

    public boolean isColumnFull(int col) {
        return this.board[0][col] != -1;
    }

    public void add(int col, int id) {
        for (int row = this.nbRows - 1; row >= 0; row--) {
            if (this.board[row][col] == -1) {
                this.board[row][col] = id;
                nbDisk++;
                return;
            }
        }
    }


    public void suppr(int col) {
        for (int row = 0; row < this.nbRows; row++) {
            if (this.board[row][col] != -1) {
                this.board[row][col] = -1;
                nbDisk--;
                return;
            }
        }
    }


    public int get(int row, int col) {
        return this.board[row][col];
    }
  
    public List<Integer> getDiffs(SimplifyBoard other) {
        List<Integer> diffs = new ArrayList<>();
        for (int col = 0; col < this.nbCols; col++) {
            int n = 0;
            for (int row = 0; row < this.nbRows; row++) {
                if (this.board[row][col] != other.board[row][col]) {
                    n++;
                }
            }
            diffs.add(n);
        }
        return diffs;
    }

    public boolean checkWin(int col, int winCondition) {
        for (int row = 0; row < this.nbRows; row++) {
            if (this.board[row][col] != -1) {
                return this.checkWin(row, col, winCondition);
            }
        }
        return false;
    }

    public boolean checkWin(int row, int col, int winCondition) {
        // Check horizontal, vertical, and diagonal lines
        return checkHorizontal(row, col, winCondition) || 
               checkVertical(row, col, winCondition) || 
               checkDiagonal1(row, col, winCondition) || 
               checkDiagonal2(row, col, winCondition);
    }
    
    // Helper methods for win checking
    private boolean checkHorizontal(int row, int col, int winCondition) {
        int count = 0;
        int playerId = this.board[row][col];

        for (int c = col; c >= 0; c--) {
            if (this.board[row][c] == playerId) count++;
            else break;
        }
        // Check to the right
        for (int c = col + 1; c < this.nbCols; c++) {
            if (this.board[row][c] == playerId) count++;
            else break;
        }
        return count >= winCondition;
    }
    
    private boolean checkVertical(int row, int col, int winCondition) {
        int count = 0;
        int playerId = this.board[row][col];

        // Check upward
        for (int r = row; r >= 0; r--) {
            if (this.board[r][col] == playerId) count++;
            else break;
        }
        // Check downward
        for (int r = row + 1; r < this.nbRows; r++) {
            if (this.board[r][col] == playerId) count++;
            else break;
        }
        return count >= winCondition;
    }
    
    private boolean checkDiagonal1(int row, int col, int winCondition) {
        int count = 0;
        int playerId = this.board[row][col];

        // Check up-left
        for (int r = row, c = col; r >= 0 && c >= 0; r--, c--) {
            if (this.board[r][c] == playerId) count++;
            else break;
        }
        // Check down-right
        for (int r = row + 1, c = col + 1; r < this.nbRows && c < this.nbCols; r++, c++) {
            if (this.board[r][c] == playerId) count++;
            else break;
        }
        return count >= winCondition;
    }
    
    private boolean checkDiagonal2(int row, int col, int winCondition) {
        int count = 0;
        int playerId = this.board[row][col];

        // Check up-right
        for (int r = row, c = col; r >= 0 && c < this.nbCols; r--, c++) {
            if (this.board[r][c] == playerId) count++;
            else break;
        }
        // Check down-left
        for (int r = row + 1, c = col - 1; r < this.nbRows && c >= 0; r++, c--) {
            if (this.board[r][c] == playerId) count++;
            else break;
        }
        return count >= winCondition;
    }

    public int getNbDisk() {
        return nbDisk;
    }

    public int getNbCols() {
        return nbCols;
    }

    public int getNbRows() {
        return nbRows;
    }
}
