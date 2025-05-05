package model;

import boardifier.model.GameStageModel;
import boardifier.model.ContainerElement;
import java.util.List;
import java.awt.Point;

public class PuissanceXBoard extends ContainerElement {
    private int rows;
    private int cols;
    private int winCondition;
    
    public PuissanceXBoard(int x, int y, int rows, int cols, int winCondition, GameStageModel gameStageModel) {
        super("puissancexboard", x, y, rows, cols, gameStageModel);
        this.rows = rows;
        this.cols = cols;
        this.winCondition = winCondition;
    }
    
    // TODO: Méthodes pour la gestion du jeu
    // 1. Vérifier si une colonne est valide pour placer un jeton
    // 2. Trouver la première ligne disponible dans une colonne
    // 3. Vérifier s'il y a un gagnant (alignements horizontaux, verticaux, diagonaux)
    // 4. Vérifier si la grille est pleine (match nul)


    // Methods

    public boolean isColValid(int col) {
        return this.isEmptyAt(rows, col);
    }

    public int getFirstAvailableRowInCol(int col) {
        for (int row = rows - 1; row >= 0; --row) {
            if (this.isEmptyAt(row, col)) {
                return row;
            }
        }
        return -1;
    }

    public boolean verifHorizontal(){
        for (int row = 0; row < rows; row++) {
            int count = 0;
            int prevElement = -1;
            for (int col = 0; col < cols; col++) {
                if (this.isEmptyAt(row, col)) {
                    break;
                }
                if (this.getElement(row, col).getType() == prevElement) {
                    count++;
                } else {
                    count = 1;
                    prevElement = this.getElement(row, col).getType();
                }
                if (count == winCondition) return true;
            }
        }
        return false;
    }

    public boolean verifVertical(){
        for (int col = 0; col < cols; col++) {
            int count = 0;
            int prevElement = -1;
            for (int row = 0; row < rows; row++) {
                if (this.isEmptyAt(row, col)) {
                    break;
                }
                if (this.getElement(row, col).getType() == prevElement) {
                    count++;
                } else {
                    count = 1;
                    prevElement = this.getElement(row, col).getType();
                }
                if (count == winCondition) return true;
            }
        }
        return false;
    }

    public boolean verifDiagonal(){
        for (int row = 0; row <= rows - winCondition; ++row) {
            int count = 0;
            int prevElement = -1;
            for (int col = 0; col <= cols - winCondition; col++) {
                if (this.isEmptyAt(row + count, col + count)) break;
                if (this.getElement(row + count, col + count).getType() == prevElement) {
                    count++;
                } else {
                    count = 1;
                    prevElement = this.getElement(row + count, col + count).getType();
                }
                if (count == winCondition) return true;
            }
        }
        for (int row = 0; row <= rows - winCondition; ++row) {
            int count = 0;
            int prevElement = -1;
            for (int col = cols - 1; col >= winCondition - 1; col--) {
                if (this.isEmptyAt(row + count, col - count)) break;
                if (this.getElement(row + count, col - count).getType() == prevElement) {
                    count++;
                } else {
                    count = 1;
                    prevElement = this.getElement(row + count, col - count).getType();
                }
                if (count == winCondition) return true;
            }
        }
        return false;
    }



}
