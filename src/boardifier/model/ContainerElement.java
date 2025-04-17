package boardifier.model;

import boardifier.control.Logger;
import boardifier.view.ElementLook;

import java.util.ArrayList;
import java.util.List;

public class ContainerElement extends StaticElement {
    protected String name;
    protected int nbRows;
    protected int nbCols;
    protected List<GameElement>[][] grid;
    protected int[][] rowSpans;
    protected int[][] colSpans;

    protected boolean[][] reachableCells;

    public ContainerElement(String name, int x, int y, int nbRows, int nbCols, GameStageModel gameStageModel) {
        this(name, x, y, nbRows, nbCols, gameStageModel, ElementTypes.getType("container"));
    }

    public ContainerElement(String name, int x, int y, int nbRows, int nbCols, GameStageModel gameStageModel, int type) {
        super(x, y, gameStageModel, type);
        this.name = name;
        this.nbRows = nbRows;
        this.nbCols = nbCols;
        grid = new List[nbRows][nbCols];
        rowSpans = new int[nbRows][nbCols];
        colSpans = new int[nbRows][nbCols];
        for (int i = 0; i < nbRows; i++) {
            for (int j = 0; j < nbCols; j++) {
                grid[i][j] = new ArrayList<>();
                rowSpans[i][j] = 1;
                colSpans[i][j] = 1;
            }
        }

        reachableCells = new boolean[nbRows][nbCols];
        resetReachableCells(true, false);
    }

    // getters/setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNbRows() {
        return nbRows;
    }

    public int getNbCols() {
        return nbCols;
    }

    public boolean hasSpannings() {
        for(int i=0;i<nbRows;i++) {
            for(int j=0;j<nbCols;j++) {
                if ((rowSpans[i][j] > 1) || (colSpans[i][j] > 1)) return true;
            }
        }
        return false;
    }

    public int[][] getRowSpans() {
        return rowSpans;
    }

    public int[][] getColSpans() {
        return colSpans;
    }

    public boolean setCellSpan(int row, int col, int rowSpan, int colSpan) {
        // cannot span a cell that is already covered by another span
        if ((rowSpans[row][col] < 1) || (colSpans[row][col] < 1)) return false;
        // cannot span over less than 1 row/column
        if ((rowSpan < 1) || (colSpan < 1)) return false;
        // check if there is something to do
        if ((rowSpans[row][col] == rowSpan) && (colSpans[row][col] == colSpan)) return false;
        // check if the span is valid, i.e. not out of boundaries
        if ( ((row+rowSpan)> nbRows) || ((col+colSpan)>nbCols)) return false;

        // check if there is already a span for that cell and if it changes.
        // in that case, undo the span firstly
        if ((rowSpans[row][col] > 1) || (colSpans[row][col] > 1)) {
            clearCellSpan(row, col, false);
        }

        // get all the elements within the covered area and put them
        // in row,col cell
        for(int i=0;i<rowSpan;i++) {
            for(int j=0;j<colSpan;j++) {
                if ((i!=0) && (j!=0)) {
                    grid[row][col].addAll(grid[row+i][col+j]);
                    // for each element, generate a move event
                    for(GameElement element : grid[row+i][col+j]) {
                        element.addMoveInContainerEvent(row+i, col+j, row, col);
                    }
                    grid[row+i][col+j].clear(); // clear the cell list
                }
            }
        }
        rowSpans[row][col] = rowSpan;
        colSpans[row][col] = colSpan;
        for(int i=0;i<rowSpan;i++) {
            for(int j=0;j<colSpan;j++) {
                if ((i!=0) && (j!=0)) {
                    rowSpans[row + i][col + j] = -row;
                    colSpans[row + i][col + j] = -col;
                }
            }
        }
        // add a face event because the structure of the container changed, thus its look must also change.
        addChangeFaceEvent();
        return true;
    }

    /**
     * clearCellSpan() jsut reset a spanned zone to a set of cells with span = 1
     * BEWARE : if there are several element stored in the cell, there are still in this cell
     * after the reset.
     * @param row
     * @param col
     */
    private void clearCellSpan(int row, int col, boolean doEvent) {
        int rs = rowSpans[row][col];
        int cs = colSpans[row][col];
        for(int i=0;i<rs;i++) {
            for(int j=0;j<cs;j++) {
                rowSpans[row+i][col+j] = 1;
                colSpans[row+i][col+j] = 1;
            }
        }
        // add a face event because the structure of the container changed, thus its look must also change.
        if (doEvent) {
            addChangeFaceEvent();
        }
    }

    private void clearCellSpan(int row, int col) {
        clearCellSpan(row, col, true);
    }

    /**
     * Since boardifierconsole is in text mode, the default behaviour is to have no special rendering
     * for cells that can be reached, after for example, selecting an element that should be played.
     * This is why this method calls the general method below with a second parameter as false, to prevent
     * a ChangeFace event to be created.
     * @param state
     */
    public void resetReachableCells(boolean state) {
        resetReachableCells(state, false);
    }
    public void resetReachableCells(boolean state, boolean doEvent) {
        for (int i = 0; i < nbRows; i++) {
            for (int j = 0; j < nbCols; j++) {
                reachableCells[i][j] = state;
            }
        }
        // in some games, changing the reachable state has an impact on the grid look
        if (doEvent) {
            addChangeFaceEvent();
        }
    }

    public boolean[][] getReachableCells() {
        return reachableCells;
    }

    public void setCellReachable(int row, int col, boolean reachable) {

        if ((row >= 0) && (row < nbRows) && (col >= 0) && (col < nbCols)) {

            if (reachableCells[row][col] != reachable) {

                reachableCells[row][col] = reachable;
                // in some games, changing the reachable state has an impact on the grid look
                addChangeFaceEvent();
            }
        }
    }

    public boolean canReachCell(int row, int col) {
        if ((row >= 0) && (row < nbRows) && (col >= 0) && (col < nbCols)) {
            return reachableCells[row][col];
        }
        return false;
    }

    // reset the board by removing elements from the board AND from the elements in model
    public void reset() {
        for (int i = 0; i < nbRows; i++) {
            for (int j = 0; j < nbCols; j++) {
                grid[i][j] = new ArrayList<>();
            }
        }
    }

    public void addElement(GameElement element, int row, int col) {
        if ((row >= nbRows) || (col >= nbCols)) return;
        int r = row;
        int c = col;
        // if row,col corresponds to a covered cell, get the origin
        if ((rowSpans[row][col] < 1) && (colSpans[row][col] < 1)) {
            r = -rowSpans[row][col];
            c = -colSpans[row][col];
        }
        grid[r][c].add(element);
        element.setContainer(this);

        // signal that element has changed of grid => the inner layout has to take ownership of the element look, which is initiated by the controller
        element.addPutInContainerEvent(this, row, col);
        // signal the stage model that element is put in grid so that callback can be exectued
        gameStageModel.putInContainer(element, this, row, col);
    }

    public void removeElement(GameElement element) {
        int[] coords = getElementCell(element);
        if (coords != null) {
            removeElement(element, coords[0], coords[1]);
        }
    }

    private void removeElement(GameElement element, int row, int col) {
        if ((row >= nbRows) || (col >= nbCols)) return;
        int r = row;
        int c = col;
        // if row,col corresponds to a covered cell, get the origin
        if ((rowSpans[row][col] < 1) && (colSpans[row][col] < 1)) {
            r = -rowSpans[row][col];
            c = -colSpans[row][col];
        }
        if ((grid[r][c].isEmpty()) || (!grid[r][c].contains(element))) return;
        grid[row][col].remove(element);
        element.setContainer(null);

        // signal that element has changed of grid => the inner layout has to take ownership of the element look, which is initiated by the controller
        element.addRemoveFromContainerEvent(this, row, col);
        // signal the stage model that element is removed from grid so that callback can be executed
        gameStageModel.removedFromContainer(element, this, row, col);
    }

    /**
     * Move an element from a grid cell to another one.
     * This method has 2 modes to process the move:
     * 1) removes the element from its current cell and use putElement() to assign it to the new cell. It leads to call the callbacks once again,
     * and to set the location of the element, by default at the cell center
     * 2) just reassign the cell that owns the element. No callbacks are called and the lcoation is not updated. Useful for moving sprites owned by a grid.
     *
     * @param element the element to move
     * @param rowDest the new grid row of the element
     * @param colDest the new grid column of the element
     */
    public void moveElement(GameElement element, int rowDest, int colDest) {

        if ((rowDest >= nbRows) || (colDest >= nbCols)) return;

        int[] coords = getElementCell(element);
        if (coords == null) {
            System.out.println("ELEMENT TO MOVE IS NOT IN A CELL");
        }
        int rs = coords[0];
        int cs = coords[1];
        int rd = rowDest;
        int cd = colDest;

        // if row,col corresponds to a covered cell, get the origin
        if ((rowSpans[coords[0]][coords[1]] < 1) && (colSpans[coords[0]][coords[1]] < 1)) {
            rs = -rowSpans[coords[0]][coords[1]];
            cs = -colSpans[coords[0]][coords[1]];
        }
        if ((rowSpans[rowDest][colDest] < 1) && (colSpans[rowDest][colDest] < 1)) {
            rd = -rowSpans[rowDest][colDest];
            cd = -colSpans[rowDest][colDest];
        }

        // if the element is not already in rowDest,colDest cell: move it.
        if ((rs != rd) || (cs != cd)) {
            // WARNING : do not call removeElement() to do the job
            // because it would call the onRemove() callback for nothing
            grid[rs][cs].remove(element);
            grid[rd][cd].add(element);

            element.addMoveInContainerEvent(rs,cs,rd,cd);
            // signal the stage model that element is moved within the same grid so that callback can be exectued
            gameStageModel.movedInContainer(element, this, rd, cd);
        }
    }

    public int[] getElementCell(GameElement element) {
        for (int i = 0; i < nbRows; i++) {
            for (int j = 0; j < nbCols; j++) {
                if (grid[i][j].contains(element)) {
                    int[] tab = {i, j};
                    return tab;
                }
            }
        }
        return null;
    }

    public List<GameElement> getElements(int row, int col) {
        return grid[row][col];
    }


    /**
     * get the first element that is stored in cell row,col.
     *
     * @param row the cell row
     * @param col the cell col
     * @return the first element stored or null if there are no elements
     */
    public GameElement getFirstElement(int row, int col) {
        if (grid[row][col].size() > 0) {
            return grid[row][col].get(0);
        }
        return null;
    }

    /**
     * get the last element that is stored in cell row,col.
     *
     * @param row the cell row
     * @param col the cell col
     * @return the last element stored or null if there are no elements
     */
    public GameElement getLastElement(int row, int col) {
        if (grid[row][col].size() > 0) {
            return grid[row][col].get(grid[row][col].size() - 1);
        }
        return null;
    }

    /**
     * get the first element that is stored in cell row,col.
     * It is a "convenience" method that is an alias for getFirstElement, in case of a game
     * enforce the fact that there is no more than a single element in a cell at any time.
     *
     * @param row the cell row
     * @param col the cell col
     * @return the first element stored or null if there are no elements
     * @see ContainerElement#getFirstElement(int, int)
     */
    public GameElement getElement(int row, int col) {
        return getFirstElement(row, col);
    }

    /**
     * get the element
     *
     * @param row   the cell row
     * @param col   the cell col
     * @param index the index of the element to retrieve in the list store in cell row,col
     * @return the first element stored or null if there are no elements
     */
    public GameElement getElement(int row, int col, int index) {
        if ((grid[row][col].size() == 0) || (index < 0) || (index >= grid[row][col].size())) return null;
        return grid[row][col].get(index);
    }

    /**
     * determine if there is at least one element stored in cell row,col
     *
     * @param row the cell row
     * @param col the cell col
     * @return true if there is at least one element, otherwise false
     */
    public boolean isElementAt(int row, int col) {
        if (grid[row][col].size() > 0) return true;
        return false;
    }

    /**
     * determine if there is no element stored in the grid
     * @return true if there is no element, otherwise false
     */
    public boolean isEmpty() {
        for (int i = 0; i < nbRows; i++) {
            for (int j = 0; j < nbCols; j++) {
                if (grid[i][j].size() > 0) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * determine if there is no element stored in cell row,col
     *
     * @param row the cell row
     * @param col the cell col
     * @return true if there is no element, otherwise false
     */
    public boolean isEmptyAt(int row, int col) {
        if (grid[row][col].isEmpty()) return true;
        return false;
    }

    // test if element is within this grid
    public boolean contains(GameElement element) {
        for (int i = 0; i < nbRows; i++) {
            for (int j = 0; j < nbCols; j++) {
                if (grid[i][j].contains(element)) {
                    return true;
                }
            }
        }
        return false;
    }
}
