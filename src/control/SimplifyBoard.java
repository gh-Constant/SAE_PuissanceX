package control;

import boardifier.model.GameElement;
import model.PuissanceXBoard;
import model.PuissanceXDisk;

import java.util.List;

public class SimplifyBoard {
    private int [][] board;
    private final int nbRows;
    private final int nbCols;
    private int nbDisk;

    /**
     * Constructs a simplified integer-based board representation from a given PuissanceXBoard.
     *
     * Initializes the board state by mapping each cell to the corresponding player ID or -1 if empty,
     * and counts the total number of disks present.
     */
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
                    this.board[row][col] = ((PuissanceXDisk) elt.getFirst()).getPlayerId();
                    this.nbDisk++;
                }
            }
        }
    }

    /**
     * Creates a deep copy of the specified SimplifyBoard, duplicating its board state and metadata.
     *
     * @param other the SimplifyBoard instance to copy
     */
    public SimplifyBoard(SimplifyBoard other) {
        this.nbRows = other.nbRows;
        this.nbCols = other.nbCols;
        this.nbDisk = other.nbDisk;
        this.board = new int[this.nbRows][this.nbCols];

        for (int row = 0; row < this.nbRows; row++) {
            System.arraycopy(other.board[row], 0, this.board[row], 0, this.nbCols);
        }
    }

    /****
     * Creates and returns a deep copy of this `SimplifyBoard` instance.
     *
     * @return a new `SimplifyBoard` object with identical board state and metadata
     */
    public SimplifyBoard copy() {
        return new SimplifyBoard(this);
    }

    /**
     * Determines whether the board is completely full, with no empty cells in any column.
     *
     * @return {@code true} if all columns are full; {@code false} otherwise
     */
    public boolean isFull() {
        for (int col = 0; col < this.nbCols; col++) {
            if (!this.isColumnFull(col)) {
                return false;
            }
        }
        return true;
    }

    /****
     * Determines whether the specified column is full.
     *
     * @param col the index of the column to check
     * @return true if the top cell of the column is occupied; false if there is at least one empty cell
     */
    public boolean isColumnFull(int col) {
        return this.board[0][col] != -1;
    }

    /****
     * Places a disk with the specified player ID into the lowest available cell of the given column.
     *
     * @param col the column index where the disk should be added
     * @param id the player ID to assign to the placed disk
     */
    public void add(int col, int id) {
        for (int row = this.nbRows - 1; row >= 0; row--) {
            if (this.board[row][col] == -1) {
                this.board[row][col] = id;
                nbDisk++;
                return;
            }
        }
    }


    /****
     * Removes the topmost disk from the specified column.
     *
     * Decrements the disk count if a disk is removed. If the column is empty, no action is taken.
     *
     * @param col the index of the column from which to remove the disk
     */
    public void suppr(int col) {
        for (int row = 0; row < this.nbRows; row++) {
            if (this.board[row][col] != -1) {
                this.board[row][col] = -1;
                nbDisk--;
                return;
            }
        }
    }


    /****
     * Returns the player ID at the specified cell.
     *
     * @param row the row index of the cell
     * @param col the column index of the cell
     * @return the player ID at the given cell, or -1 if the cell is empty
     */
    public int get(int row, int col) {
        return this.board[row][col];
    }

    /**
     * Determines if a winning condition is met in the specified column.
     *
     * Scans the given column from bottom to top to find the first non-empty cell,
     * then checks if placing a disk there results in a win according to the specified win condition.
     *
     * @param col the column index to check for a win
     * @param winCondition the number of consecutive disks required to win
     * @return true if a win is detected starting from the first non-empty cell in the column; false otherwise
     */
    public boolean checkWin(int col, int winCondition) {
        for (int row = 0; row < this.nbRows; row++) {
            if (this.board[row][col] != -1) {
                return this.checkWin(row, col, winCondition);
            }
        }
        return false;
    }

    /**
     * Determines if a winning condition is met starting from the specified cell.
     *
     * Checks for a sequence of identical player IDs of the required length in horizontal, vertical, and both diagonal directions from the given cell.
     *
     * @param row the row index of the starting cell
     * @param col the column index of the starting cell
     * @param winCondition the number of consecutive disks required to win
     * @return true if a winning sequence is found, false otherwise
     */
    public boolean checkWin(int row, int col, int winCondition) {
        // Check horizontal, vertical, and diagonal lines
        return checkHorizontal(row, col, winCondition) || 
               checkVertical(row, col, winCondition) || 
               checkDiagonal1(row, col, winCondition) || 
               checkDiagonal2(row, col, winCondition);
    }
    
    /**
     * Checks if there are enough consecutive disks horizontally from the specified cell to meet the win condition.
     *
     * @param row the row index of the starting cell
     * @param col the column index of the starting cell
     * @param winCondition the number of consecutive disks required to win
     * @return true if the horizontal sequence meets or exceeds the win condition, false otherwise
     */
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
    
    /**
     * Checks if there are enough consecutive disks vertically from the specified cell to meet the win condition.
     *
     * @param row the starting row index
     * @param col the column index
     * @param winCondition the required number of consecutive disks to win
     * @return true if a vertical sequence of the player's disks meets or exceeds the win condition, false otherwise
     */
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
    
    /**
     * Checks if there is a winning sequence of disks matching the player ID along the up-left to down-right diagonal starting from the specified cell.
     *
     * @param row the row index of the starting cell
     * @param col the column index of the starting cell
     * @param winCondition the number of consecutive disks required to win
     * @return true if a winning sequence is found along this diagonal, false otherwise
     */
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
    
    /**
     * Checks if there is a winning sequence of disks matching the player ID along the diagonal from top-right to bottom-left starting at the specified cell.
     *
     * @param row the starting row index
     * @param col the starting column index
     * @param winCondition the required number of consecutive disks to win
     * @return true if a winning sequence is found along this diagonal, false otherwise
     */
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

    /**
     * Returns the number of disks currently placed on the board.
     *
     * @return the total count of disks on the board
     */
    public int getNbDisk() {
        return nbDisk;
    }

    /**
     * Returns the number of rows in the board.
     *
     * @return the total number of rows
     */
    public int getNbRows() {
        return nbRows;
    }

    /****
     * Returns the number of columns in the board.
     *
     * @return the total number of columns
     */
    public int getNbCols() {
        return nbCols;
    }
}
