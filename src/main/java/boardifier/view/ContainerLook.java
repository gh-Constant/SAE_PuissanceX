package boardifier.view;

import boardifier.control.Logger;
import boardifier.model.ContainerElement;
import boardifier.model.Coord2D;
import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.List;

/**
 * ContainerLook defines a look that matches the associated ContainerElement structure,
 * with cells filled with looks that matches the GameElement that are put within the ContainerElement.
 * Cells have a transparent bakcground and no borders.
 * By default, the size of the cells is automatically computed from the size of the looks within.
 * But if it also possible to fix the size of a cell, using the dedicated constructor. In that case,
 * if looks are bigger than the cell size, they will overlap on other cells.
 * If the container has spanning cells, then ContainerLook will also use it to compute the location
 * of looks. If cell size is fixed, the size of spanning cells is just a multiple of a single cell size.
 *
 */
public class ContainerLook extends ElementLook {

    protected int nbRows;
    protected int nbCols;

    protected List<ElementLook>[][] grid; // inner looks. There may be several look in each cell

    protected int[][] verticalAlignment;
    protected int[][] horizontalAlignment;
    protected int[][] paddingTop;
    protected int[][] paddingBottom;
    protected int[][] paddingLeft;
    protected int[][] paddingRight;
    protected int innersTop; // the y coordinate of the top-left cell
    protected int innersLeft; // the x coordinate of the top-left cell
    protected int rowHeight; // if not -1, gives a fixed value for rows
    protected int colWidth; // if not -1, gives a fixed value for cols
    protected int[] rowsHeight; // used to store height of each row if rowHeight = -1
    protected int[] colsWidth; // used to store width of each col if colWidth = -1


    // alignments constant used for layout cells
    public final static int ALIGN_TOP = 0;
    public final static int ALIGN_MIDDLE = 1;
    public final static int ALIGN_BOTTOM = 2;
    public final static int ALIGN_LEFT = 0;
    public final static int ALIGN_CENTER = 1;
    public final static int ALIGN_RIGHT = 2;


    /**
     * constructor to obtain a flexible cell size
     * @param containerElement
     * @param depth
     */
    public ContainerLook(ContainerElement containerElement, int depth) {
        this(containerElement, -1, -1, depth, 0, 0);
    }

    public ContainerLook(ContainerElement containerElement, int rowHeight, int colWidth, int depth) {
        this(containerElement, rowHeight, colWidth, depth, 0, 0);
    }
    /**
     * constructor to obtain a fixed cell size.
     * @param containerElement
     * @param rowHeight
     * @param colWidth
     * @param depth
     */
    public ContainerLook(ContainerElement containerElement, int rowHeight, int colWidth, int depth, int innersTop, int innersLeft) {
        super(containerElement);
        // force sizes to coherent values
        if (rowHeight < -1) rowHeight = -1;
        else if (rowHeight == 0) rowHeight = 1;
        if (colWidth < -1) colWidth = -1;
        else if (colWidth == 0) colWidth = 1;

        this.rowHeight = rowHeight;
        this.colWidth = colWidth;
        nbRows = containerElement.getNbRows();
        nbCols = containerElement.getNbCols();

        // create the grid
        grid = new List[nbRows][nbCols];
        for (int i = 0; i < nbRows; i++) {
            for (int j = 0; j < nbCols; j++) {
                grid[i][j] = new ArrayList<>();
            }
        }

        // setup row/col sizes
        rowsHeight = new int[nbRows];
        colsWidth = new int[nbCols];
        this.rowHeight = rowHeight;
        if (rowHeight == -1) {
            for (int i = 0; i < nbRows; i++) rowsHeight[i] = 0;
        } else {
            for (int i = 0; i < nbRows; i++) rowsHeight[i] = rowHeight;
        }
        this.colWidth = colWidth;
        if (colWidth == -1) {
            for(int i=0;i<nbCols; i++) colsWidth[i] = 0;
        }
        else {
            for(int i=0;i<nbCols; i++) colsWidth[i] = colWidth;
        }

        anchorType = ANCHOR_TOPLEFT;
        this.depth = depth;


        verticalAlignment = new int[nbRows][nbCols];
        horizontalAlignment = new int[nbRows][nbCols];
        for(int i=0;i<nbRows;i++) {
            for(int j=0;j<nbCols;j++) {
                verticalAlignment[i][j] = ALIGN_TOP;
                horizontalAlignment[i][j] = ALIGN_LEFT;
            }
        }

        // by default the location of the inner element starts at (0,0) relative to the Group of the ContainerLook
        this.innersTop = innersTop;
        this.innersLeft = innersLeft;
        // by default, no padding
        this.paddingTop = new int[nbRows][nbCols];
        this.paddingBottom = new int[nbRows][nbCols];
        this.paddingLeft = new int[nbRows][nbCols];
        this.paddingRight = new int[nbRows][nbCols];
        for(int i=0;i<nbRows;i++) {
            for(int j=0;j<nbCols;j++) {
                this.paddingTop[i][j] = 0;
                this.paddingBottom[i][j] = 0;
                this.paddingLeft[i][j] = 0;
                this.paddingRight[i][j] = 0;
            }
        }
    }

    public int getInnersTop() {
        return innersTop;
    }

    public void setInnersTop(int innersTop) {
        this.innersTop = innersTop;
        updateInners();
    }

    public int getInnersLeft() {
        return innersLeft;
    }

    public void setInnersLeft(int innersLeft) {
        this.innersLeft = innersLeft;
        updateInners();
    }

    public void setInnersTopLeft(int top, int left) {
        this.innersTop = top;
        this.innersLeft = left;
        updateInners();
    }


    public void setRowHeight(int rowHeight) {
        if (this.rowHeight != rowHeight) {
            this.rowHeight = rowHeight;
            if (rowHeight == -1) {
                for (int i = 0; i < nbRows; i++) rowsHeight[i] = 0;
            } else {
                for (int i = 0; i < nbRows; i++) rowsHeight[i] = rowHeight;
            }
            updateInners();
        }
    }

    public void setColWidth(int colWidth) {
        if (this.colWidth != colWidth) {
            this.colWidth = colWidth;
            if (colWidth == -1) {
                for(int i=0;i<nbCols; i++) colsWidth[i] = 0;
            }
            else {
                for(int i=0;i<nbCols; i++) colsWidth[i] = colWidth;
            }
            updateInners();
        }
    }

    public void setVerticalAlignment(int align) {
        boolean changed = false;
        if ((align < ALIGN_TOP) || (align > ALIGN_BOTTOM)) align = ALIGN_TOP;
        for(int i=0;i<nbRows;i++) {
            for (int j = 0; j < nbCols; j++) {

                if (verticalAlignment[i][j] != align) {
                    verticalAlignment[i][j] = align;
                    changed = true;
                }
            }
        }
        if (changed) updateInners(false);
    }

    public void setCellVerticalAlignment(int row, int col, int align) {
        if ( (row> nbRows) || (col>nbCols)) return;
        if ((align < ALIGN_TOP) || (align > ALIGN_BOTTOM)) align = ALIGN_TOP;
        if (verticalAlignment[row][col] != align) {
            verticalAlignment[row][col] = align;
            updateInners(false);
        }
    }

    public void setHorizontalAlignment(int align) {
        boolean changed = false;
        if ((align < ALIGN_LEFT) || (align > ALIGN_RIGHT)) align = ALIGN_LEFT;
        for(int i=0;i<nbRows;i++) {
            for (int j = 0; j < nbCols; j++) {
                if (horizontalAlignment[i][j] != align) {
                    horizontalAlignment[i][j] = align;
                    changed = true;
                }
            }
        }
        if (changed) updateInners(false);
    }

    public void setCellHorizontalAlignment(int row, int col, int align) {
        if ( (row> nbRows) || (col>nbCols)) return;
        if ((align < ALIGN_LEFT) || (align > ALIGN_RIGHT)) align = ALIGN_LEFT;
        if (horizontalAlignment[row][col] != align) {
            horizontalAlignment[row][col] = align;
            updateInners(false);
        }
    }

    public void setPadding(int padding) {
        boolean changed = false;
        for(int i=0;i<nbRows;i++) {
            for(int j=0;j<nbCols;j++) {
                if (paddingTop[i][j] != padding) {
                    paddingTop[i][j] = padding;
                    changed = true;
                }
                if (paddingBottom[i][j] != padding) {
                    paddingBottom[i][j] = padding;
                    changed = true;
                }
                if (paddingLeft[i][j] != padding) {
                    paddingLeft[i][j] = padding;
                    changed = true;
                }
                if (paddingRight[i][j] != padding) {
                    paddingRight[i][j] = padding;
                    changed = true;
                }
            }
        }
        if (changed) updateInners();
    }
    public void setPaddingTop(int padding) {
        boolean changed = false;
        for(int i=0;i<nbRows;i++) {
            for(int j=0;j<nbCols;j++) {
                if (paddingTop[i][j] != padding) {
                    paddingTop[i][j] = padding;
                    changed = true;
                }
            }
        }
        if (changed) updateInners();
    }
    public void setPaddingBottom(int padding) {
        boolean changed = false;
        for(int i=0;i<nbRows;i++) {
            for(int j=0;j<nbCols;j++) {
                if (paddingBottom[i][j] != padding) {
                    paddingBottom[i][j] = padding;
                    changed = true;
                }
            }
        }
        if (changed) updateInners();
    }
    public void setPaddingLeft(int padding) {
        boolean changed = false;
        for(int i=0;i<nbRows;i++) {
            for(int j=0;j<nbCols;j++) {
                if (paddingLeft[i][j] != padding) {
                    paddingLeft[i][j] = padding;
                    changed = true;
                }
            }
        }
        if (changed) updateInners();
    }
    public void setPaddingRight(int padding) {
        boolean changed = false;
        for(int i=0;i<nbRows;i++) {
            for(int j=0;j<nbCols;j++) {
                if (paddingRight[i][j] != padding) {
                    paddingRight[i][j] = padding;
                    changed = true;
                }
            }
        }
        if (changed) updateInners();
    }

    public void setCellPadding(int row, int col, int padding) {
        boolean changed = false;
        if ( (row> nbRows) || (col>nbCols)) return;
        if (padding < 0) padding = 0;
        if (paddingTop[row][col] != padding) {
            paddingTop[row][col] = padding;
            changed = true;
        }
        if (paddingBottom[row][col] != padding) {
            paddingBottom[row][col] = padding;
            changed = true;
        }
        if (paddingLeft[row][col] != padding) {
            paddingLeft[row][col] = padding;
            changed = true;
        }
        if (paddingRight[row][col] != padding) {
            paddingRight[row][col] = padding;
            changed = true;
        }
        if (changed) updateInners();

    }

    public void setCellPaddingTop(int row, int col, int padding) {
        if ( (row> nbRows) || (col>nbCols)) return;
        if (padding < 0) padding = 0;
        if (paddingTop[row][col] != padding) {
            paddingTop[row][col] = padding;
            updateInners();
        }
    }
    public void setCellPaddingBottom(int row, int col, int padding) {
        if ( (row> nbRows) || (col>nbCols)) return;
        if (padding < 0) padding = 0;
        if (paddingBottom[row][col] != padding) {
            paddingBottom[row][col] = padding;
            updateInners();
        }
    }
    public void setCellPaddingLeft(int row, int col, int padding) {

        if ( (row> nbRows) || (col>nbCols)) return;
        if (padding < 0) padding = 0;
        if (paddingLeft[row][col] != padding) {
            paddingLeft[row][col] = padding;
            updateInners();
        }
    }
    public void setCellPaddingRight(int row, int col, int padding) {

        if ( (row> nbRows) || (col>nbCols)) return;
        if (padding < 0) padding = 0;
        if (paddingRight[row][col] != padding) {
            paddingRight[row][col] = padding;
            updateInners();
        }
    }

    private int getGridWidth() {
        int w = 0;
        for(int j=0;j<nbCols;j++) w += colsWidth[j];
        return w;
    }

    private int getGridHeight() {
        int h = 0;
        for(int i=0;i<nbRows;i++) h += rowsHeight[i];
        return h;
    }

    @Override
    public int getHeight() {
        if (rowHeight == -1) {
            return innersTop +getGridHeight();
        }
        return innersTop +nbRows*rowHeight;
    }

    @Override
    public int getWidth() {
        if (colWidth == -1) {
            return innersLeft +getGridWidth();
        }
        return innersLeft +nbCols*colWidth;
    }

    private int getCellLookMaxWidth(int row, int col) {
        int w = 0;
        for(ElementLook look : grid[row][col]) {
            if (look.getWidth() > w) w = look.getWidth();
        }
        return w;
    }

    private int getCellLookMaxHeight(int row, int col) {
        int h = 0;
        for(ElementLook look : grid[row][col]) {
            if (look.getHeight() > h) h = look.getHeight();
        }
        return h;
    }

    private void updateRowHeight() {

        // do nothing if rows have a fixed size
        if (rowHeight != -1) return;
        int[][] rowSpans = ((ContainerElement)element).getRowSpans();
        // first, take only non spanning looks into account
        int max = 0;
        for(int i=0;i<nbRows;i++) {
            max = 0;
            for(int j=0;j<nbCols;j++) {
                if ((!grid[i][j].isEmpty()) && (rowSpans[i][j] == 1)) {
                    int h = getCellLookMaxHeight(i,j)+ paddingTop[i][j] + paddingBottom[i][j];
                    if ( h > max) max = h;
                }
            }
            rowsHeight[i] = max;
        }
        // now adjust row height looking at spanning looks height
        for(int i=0;i<nbRows;i++) {
            for(int j=0;j<nbCols;j++) {
                // if there is a row spanning look, check if sum of height are sufficient
                if ( ((rowSpans[i][j]) > 1) && (!grid[i][j].isEmpty())) {
                    max = 0; // sum of heights
                    for(int k=0;k<rowSpans[i][j];k++) {
                        max += rowsHeight[i+k];
                    }
                    // if sum of heights is not sufficient, increase height of first row
                    int h = getCellLookMaxHeight(i,j) + paddingTop[i][j] + paddingBottom[i][j];
                    if (h > max) {
                        rowsHeight[i] += h - max;
                    }
                }
            }
        }

        Logger.trace("finished recompute row height for ["+this+"] : ");
        for(int i=0;i<nbRows;i++) Logger.trace(rowsHeight[i]+" ");
    }

    private void updateColWidth() {

        // do nothing if cols have a fixed size
        if (colWidth != -1) return;

        int[][] colSpans = ((ContainerElement)element).getColSpans();
        // first, take only non spanning looks into account
        int max = 0;
        for(int j=0;j<nbCols;j++) {
            max = 0;
            for(int i=0;i<nbRows;i++) {
                if ( ((colSpans[i][j]) == 1) && (!grid[i][j].isEmpty())) {
                int w = getCellLookMaxWidth(i,j) +paddingLeft[i][j] + paddingRight[i][j];
                if (w > max) max = w;
                }
            }
            colsWidth[j] = max;
        }
        // now adjust col width looking at spanning looks width
        for(int j=0;j<nbCols;j++) {
            for(int i=0;i<nbRows;i++) {
                // if there is a row spanning look, check if sum of height are sufficient
                if ( ((colSpans[i][j]) > 1) && (!grid[i][j].isEmpty())) {
                    max = 0; // sum of heights
                    for(int k=0;k<colSpans[i][j];k++) {
                        max += colsWidth[j+k];
                    }
                    // if sum of widths is not sufficient, increase height of first col
                    int w = getCellLookMaxWidth(i,j) + paddingLeft[i][j] + paddingRight[i][j];
                    if (w > max) {
                        colsWidth[j] += w - max;
                    }
                }
            }
        }
        Logger.trace("finished recompute cols width for ["+this+"] : ");
        for(int i=0;i<nbCols;i++) Logger.trace(colsWidth[i]+" ");

    }

    private void removeLookFromContainerLook(ElementLook look) {
        /* since look becomes parentless, it must be reattached to the RootPane
           But before that, its position relative to the RootPane must be computed
         */
        Coord2D pos = getRootPane().getRootPaneLocationFromLookLocation(look);
        // now remove the loook group from this group
        getGroup().getChildren().remove(look.getGroup());
        // move the look group to its "new" position in the RootPane
        look.moveTo(pos.getX(), pos.getY());
        getRootPane().attachLookToRootPane(look);
    }

    public void addInnerLook(ElementLook look, int row, int col) {
        int[][] rowSpans = ((ContainerElement)element).getRowSpans();
        int[][] colSpans = ((ContainerElement)element).getColSpans();

        if ((row >= nbRows) || (col >= nbCols)) return;
        int r = row;
        int c = col;
        // if row,col corresponds to a covered cell, get the origin
        if ((rowSpans[row][col] < 1) && (colSpans[row][col] < 1)) {
            r = -rowSpans[row][col];
            c = -colSpans[row][col];
        }
        grid[r][c].add(look);
        Logger.trace("added in ["+this+"] the look ["+look+"] in "+row+","+col);
        // adding the look group to this container look group
        getGroup().getChildren().add(look.getGroup());
        look.setParent(this);
        updateInners();
    }

    public void removeInnerLook(ElementLook look, int row, int col) {
        int[][] rowSpans = ((ContainerElement)element).getRowSpans();
        int[][] colSpans = ((ContainerElement)element).getColSpans();

        if ((row >= nbRows) || (col >= nbCols)) return;
        int r = row;
        int c = col;
        // if row,col corresponds to a covered cell, get the origin
        if ((rowSpans[row][col] < 1) && (colSpans[row][col] < 1)) {
            r = -rowSpans[row][col];
            c = -colSpans[row][col];
        }
        if ((grid[r][c].isEmpty()) || (!grid[r][c].contains(look))) return;

        grid[r][c].remove(look);
        removeLookFromContainerLook(look);
        look.setParent(null);
        updateInners();
    }

    public void moveInnerLook(ElementLook look, int rowSrc, int colSrc, int rowDest, int colDest) {
        int[][] rowSpans = ((ContainerElement)element).getRowSpans();
        int[][] colSpans = ((ContainerElement)element).getColSpans();

        if ((rowSrc >= nbRows) || (colSrc >= nbCols)) return;
        if ((rowDest >= nbRows) || (colDest >= nbCols)) return;
        int rs = rowSrc;
        int cs = colSrc;
        int rd = rowDest;
        int cd = colDest;

        // if row,col corresponds to a covered cell, get the origin
        if ((rowSpans[rowSrc][colSrc] < 1) && (colSpans[rowSrc][colSrc] < 1)) {
            rs = -rowSpans[rowSrc][colSrc];
            cs = -colSpans[rowSrc][colSrc];
        }
        if ((rowSpans[rowDest][colDest] < 1) && (colSpans[rowDest][colDest] < 1)) {
            rd = -rowSpans[rowDest][colDest];
            cd = -colSpans[rowDest][colDest];
        }

        if ((grid[rs][cs].isEmpty()) || (!grid[rs][cs].contains(look))) return;
        grid[rs][cs].remove(look);
        grid[rd][cd].add(look);
        updateInners();
    }

    public void updateInners() {
        updateInners(true);
    }

    public void updateInners(boolean rowColUpdate) {

        Logger.trace("called for ["+this+"]");
        if (rowColUpdate) {
            updateRowHeight();
            updateColWidth();
            Logger.trace("for ["+this+"] - change in the structure => call parent updateInners()");
            // if size changes, then other ContainerLook may also be impacted
            ContainerLook up = (ContainerLook) parent;
            // back to the top to propagate changes
            while (up != null) {
                up.updateInners();
                up = (ContainerLook) up.parent;
            }
        }
        computeInnersLocation();
    }

    @Override
    public void onFaceChange() {
        Logger.trace(" for ["+this+"] - called because of new spanning or change in reachabloe cells. Just update inners location");
        updateInners();
    }

    protected void render() {
        Logger.trace(" for ["+this+"] - this msg should not be displayed except when it is added to the gamestage. This method does nothing since there is nothing to create");
    }

    protected void computeInnersLocation() {
        int[][] rowSpans = ((ContainerElement)element).getRowSpans();
        int[][] colSpans = ((ContainerElement)element).getColSpans();
        int rowStart = 0;
        for(int i=0;i<nbRows;i++) {
            int colStart = 0;
            for(int j=0;j<nbCols;j++) {
                for(ElementLook look : grid[i][j]) {
                    // x,y are the top-left corner of the zone where the look must be put.
                    int x = colStart + paddingLeft[i][j];
                    int y = rowStart + paddingTop[i][j];
                    // xx,yy are the bottom-right corner, in case of there is no spanning
                    int w = 0;
                    for(int k=0;k<colSpans[i][j];k++) {
                        w += colsWidth[j+k];
                    }
                    int xx = colStart + w - paddingRight[i][j] - 1;
                    int h = 0;
                    for(int k=0;k<rowSpans[i][j];k++) {
                        h += rowsHeight[i+k];
                    }
                    int yy = rowStart + h - paddingBottom[i][j] - 1;

                    // modify taking alignment into account
                    if (verticalAlignment[i][j] == ALIGN_MIDDLE) {
                        y = y + (yy - y + 1 - look.getHeight()) / 2;
                    } else if (verticalAlignment[i][j] == ALIGN_BOTTOM) {
                        y = yy - look.getHeight() + 1;
                    }
                    if (horizontalAlignment[i][j] == ALIGN_CENTER) {
                        x = x + (xx - x + 1 - look.getWidth()) / 2;
                    } else if (horizontalAlignment[i][j] == ALIGN_RIGHT) {
                        x = xx - look.getWidth() + 1;
                    }
                    // move the look shape to its correct location within the rootpane
                    // knowing that x,y is the top-left corner in the local layout space
                    double lookX = innersLeft +x;
                    double lookY = innersTop +y;
                    // if anchor is at center, add size/2 so that the center of the look
                    // is at the right place.
                    if (look.getAnchorType() == ElementLook.ANCHOR_CENTER) {
                        lookX += look.getWidth()/2;
                        lookY += look.getHeight()/2;
                    }

                    if ((lookX != look.getElement().getX()) || (lookY != look.getElement().getY())) {
                        look.moveTo(lookX, lookY);
                    }

                }
                colStart += colsWidth[j];
            }
            rowStart += rowsHeight[i];
        }
    }



    public int getCellLeft(int row, int col) {
        int[][] rowSpans = ((ContainerElement)element).getRowSpans();
        int[][] colSpans = ((ContainerElement)element).getColSpans();
        // row,col cell is covered by a span => no left position
        if ((rowSpans[row][col] < 1) || (colSpans[row][col] < 1)) return -1;
        int x = 0;
        for(int j=0;j<col;j++) x+= colsWidth[j];
        return x;
    }
    public int getCellRight(int row, int col) {
        int[][] rowSpans = ((ContainerElement)element).getRowSpans();
        int[][] colSpans = ((ContainerElement)element).getColSpans();

        // row,col cell is covered by a span => no left position
        if ((rowSpans[row][col] < 1) || (colSpans[row][col] < 1)) return -1;
        int x = 0;
        for(int j=0;j<col;j++) x+= colsWidth[j];
        for(int j=0;j<colSpans[row][col];j++) x += colsWidth[col+j];

        return x-1;
    }
    public int getCellTop(int row, int col) {
        int[][] rowSpans = ((ContainerElement)element).getRowSpans();
        int[][] colSpans = ((ContainerElement)element).getColSpans();

        // row,col cell is covered by a span => no left position
        if ((rowSpans[row][col] < 1) || (colSpans[row][col] < 1)) return -1;
        int y = 0;
        for(int i=0;i<row;i++) y+= rowsHeight[i];
        return y;
    }
    public int getCellBottom(int row, int col) {
        int[][] rowSpans = ((ContainerElement)element).getRowSpans();
        int[][] colSpans = ((ContainerElement)element).getColSpans();

        // row,col cell is covered by a span => no left position
        if ((rowSpans[row][col] < 1) || (colSpans[row][col] < 1)) return -1;
        int y = 0;
        for(int i=0;i<row;i++) y+= rowsHeight[i];
        for(int i=0;i<rowSpans[row][col];i++) y += rowsHeight[row+i];
        return y-1;
    }

    public int[] getCellFromSceneLocation(Coord2D p) {
        return getCellFromSceneLocation(p.getX(), p.getY());
    }

    public int[] getCellFromSceneLocation(double x, double y) {
        // get the group node that contains the shapes/nodes of this grid and get the coordinates of p within this group
        Point2D inMyGroup = getGroup().sceneToLocal(x, y);
        int innersX = (int)inMyGroup.getX() - innersLeft;
        int innersY = (int)inMyGroup.getY() - innersTop;
        return getCellFromInnersLocation(innersX, innersY);
    }

    /**
     * get the location in the local space for an element that is already in this container
     * and that must be put in cell row,col. It is used in controllers when a MoveWithinContainerAction must be build.
     * If the look is not in this container, it returns its current location
     *
     * @param look
     * @param row
     * @param col
     * @return
     */
    public Coord2D getContainerLocationForLookFromCell(ElementLook look, int row, int col) {

        // get position in the layout local space
        Coord2D pos = getLookLocationInCell(look, row, col);
        // add the position of the inners in the container
        pos.relativeMove(innersLeft, innersTop);

        return pos;
    }

    /**
     * get the location in the RootPane space for an element that is already in the RootPane (i.e. with no parent container)
     * and that must be put in cell row,col of this container. It is used in controllers when a PutInContainerAction must be build.
     *
     * @param look
     * @param row
     * @param col
     * @return
     */
    public Coord2D getRootPaneLocationForLookFromCell(ElementLook look, int row, int col) {
        // get position in the layout local space
        Coord2D pos = getLookLocationInCell(look, row, col);
        // add the position of the inners in the container
        pos.relativeMove(innersLeft, innersTop);

        // get the gap between the RootPane group and this containerLook group.
        Point2D posGroupSrc = getRootPane().localToScene(0,0);
        Point2D posGroupDest = getGroup().localToScene(0,0);
        double gapX = posGroupDest.getX() - posGroupSrc.getX();
        double gapY = posGroupDest.getY() - posGroupSrc.getY();
        // add the gap to pos
        pos.relativeMove(gapX, gapY);

        return pos;
    }


    /**
     * computes the row,col of a cell from x,y coordinates in the local space of the inners
     * @param x
     * @param y
     * @return
     */
    public int[] getCellFromInnersLocation(double x, double y) {
        if ((x < 0) || (x >= getWidth()) || (y < 0) || (y >= getHeight())) return null;
        for(int i=0;i<nbRows; i++) {
            for(int j=0;j<nbCols;j++) {
                int x1 = getCellLeft(i,j);
                if (x1 == -1) continue;
                int x2 = getCellRight(i,j);
                int y1 = getCellTop(i,j);
                int y2 = getCellBottom(i,j);

                if ((x>=x1) && (x<=x2) && (y>= y1) && (y<=y2)) return new int[]{i,j};
            }
        }
        return null;
    }

    /**
     * get the location in the inners for a look that would b place in row,col cell
     * @param look
     * @param row
     * @param col
     * @return
     */
    public Coord2D getLookLocationInCell(ElementLook look, int row, int col) {

        int[][] rowSpans = ((ContainerElement)element).getRowSpans();
        int[][] colSpans = ((ContainerElement)element).getColSpans();

        int rowStart = 0;
        int colStart = 0;
        for(int i=0;i<row;i++) rowStart += rowsHeight[i];
        for(int j=0;j<col;j++) colStart += colsWidth[j];

        int x = colStart + paddingLeft[row][col];
        int y = rowStart + paddingTop[row][col];
        // xx,yy are the bottom-right corner, in case of there is no spanning
        int w = 0;
        for(int k=0;k<colSpans[row][col];k++) {
            w += colsWidth[col+k];
        }
        int xx = colStart + w - paddingRight[row][col] - 1;
        int h = 0;
        for(int k=0;k<rowSpans[row][col];k++) {
            h += rowsHeight[row+k];
        }
        int yy = rowStart + h - paddingBottom[row][col] - 1;

        // modify taking alignment into account
        if (verticalAlignment[row][col] == ALIGN_MIDDLE) {
            y = y + (yy - y + 1 - look.getHeight()) / 2;
        } else if (verticalAlignment[row][col] == ALIGN_BOTTOM) {
            y = yy - look.getHeight() + 1;
        }
        if (horizontalAlignment[row][col] == ALIGN_CENTER) {
            x = x + (xx - x + 1 - look.getWidth()) / 2;
        } else if (horizontalAlignment[row][col] == ALIGN_RIGHT) {
            x = xx - look.getWidth() + 1;
        }
        if (look.getAnchorType() == ElementLook.ANCHOR_CENTER) {
            x += look.getWidth()/2;
            y += look.getHeight()/2;
        }
        return new Coord2D(x,y);
    }

}
