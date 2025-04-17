package model;

import boardifier.control.Logger;
import boardifier.model.GameStageModel;
import boardifier.model.ContainerElement;

import java.util.ArrayList;
import java.util.List;
import java.awt.*;

/**
 * Hole main board represent the element where pawns are put when played
 * Thus, a simple ContainerElement with 3 rows and 3 column is needed.
 * Nevertheless, in order to "simplify" the work for the controller part,
 * this class also contains method to determine all the valid cells to put a
 * pawn with a given value.
 */
public class HoleBoard extends ContainerElement {
    public HoleBoard(int x, int y, GameStageModel gameStageModel) {
        // call the super-constructor to create a 3x3 grid, named "holeboard", and in x,y in space
        super("holeboard", x, y, 3 , 3, gameStageModel);
    }

    public void setValidCells(int number) {
        Logger.debug("called",this);
        resetReachableCells(false);
        List<Point> valid = computeValidCells(number);
        if (valid != null) {
            for(Point p : valid) {
                reachableCells[p.y][p.x] = true;
            }
        }
    }

    public List<Point> computeValidCells(int number) {
        List<Point> lst = new ArrayList<>();
        Pawn p = null;
        // if the grid is empty, is it the first turn and thus, all cells are valid
        if (isEmpty()) {
            // i are rows
            for(int i=0;i<3;i++) {
                // j are cols
                for (int j = 0; j < 3; j++) {
                    // cols is in x direction and rows are in y direction, so create a point in (j,i)
                    lst.add(new Point(j,i));
                }
            }
            return lst;
        }
        // else, take each empty cell and check if it is valid
        for(int i=0;i<3;i++) {
            for(int j=0;j<3;j++) {
                if (isEmptyAt(i,j)) {
                    // check adjacence in row-1
                    if (i-1 >= 0) {
                        if (j-1>=0) {
                            p = (Pawn)getElement(i-1,j-1);

                            // check if same parity
                            if ((p != null) && ( p.getNumber()%2 == number%2)) {
                                lst.add(new Point(j,i));
                                continue; // go to the next point
                            }
                        }
                        p = (Pawn)getElement(i-1,j);
                        // check if different parity
                        if ((p != null) && ( p.getNumber()%2 != number%2)) {
                            lst.add(new Point(j,i));
                            continue; // go to the next point
                        }
                        if (j+1<=2) {
                            p = (Pawn)getElement(i-1,j+1);
                            // check if same parity
                            if ((p != null) && ( p.getNumber()%2 == number%2)) {
                                lst.add(new Point(j,i));
                                continue; // go to the next point
                            }
                        }
                    }
                    // check adjacence in row+1
                    if (i+1 <= 2) {
                        if (j-1>=0) {
                            p = (Pawn)getElement(i+1,j-1);
                            // check if same parity
                            if ((p != null) && ( p.getNumber()%2 == number%2)) {
                                lst.add(new Point(j,i));
                                continue; // go to the next point
                            }
                        }
                        p = (Pawn)getElement(i+1,j);
                        // check if different parity
                        if ((p != null) && ( p.getNumber()%2 != number%2)) {
                            lst.add(new Point(j,i));
                            continue; // go to the next point
                        }
                        if (j+1<=2) {
                            p = (Pawn)getElement(i+1,j+1);
                            // check if same parity
                            if ((p != null) && ( p.getNumber()%2 == number%2)) {
                                lst.add(new Point(j,i));
                                continue; // go to the next point
                            }
                        }
                    }
                    // check adjacence in row
                    if (j-1>=0) {
                        p = (Pawn)getElement(i,j-1);
                        // check if different parity
                        if ((p != null) && ( p.getNumber()%2 != number%2)) {
                            lst.add(new Point(j,i));
                            continue; // go to the next point
                        }
                    }
                    if (j+1<=2) {
                        p = (Pawn)getElement(i,j+1);
                        // check if different parity
                        if ((p != null) && ( p.getNumber()%2 != number%2)) {
                            lst.add(new Point(j,i));
                            continue; // go to the next point
                        }

                    }
                }
            }
        }
        return lst;
    }
}
