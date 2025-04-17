package boardifier.view;

import boardifier.control.Logger;
import boardifier.model.ContainerElement;
import boardifier.model.Coord2D;

import java.util.ArrayList;
import java.util.List;

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
        // there is a +1 on the size to be able to put the rigth/bottom border
        // and a +2 if the coords of the cells is shown
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

    protected int getGridWidth() {
        int w = 0;
        for(int j=0;j<nbCols;j++) w += colsWidth[j];
        return w;
    }

    protected int getGridHeight() {
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
        // move the look group to its "new" position in the RootPane
        look.moveTo(pos.getX(), pos.getY());
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

        Logger.trace("called", this);
        if (rowColUpdate) {
            updateRowHeight();
            updateColWidth();

            // if size changes, then other ContainerLook may also be impacted
            ContainerLook up = (ContainerLook) parent;
            // back to the top to propagate changes
            while (up != null) {
                Logger.trace("change in the structure => call parent updateInners()", this);
                up.updateInners();
                up = (ContainerLook) up.parent;
            }
        }
        // now render the inners looks
        computeInnersLocation();
    }

    @Override
    public void onFaceChange() {
        Logger.trace("called", this);
        updateInners();
    }

    protected void render() {
        Logger.trace("called", this);
        // create & clear the viewport if needed
        setSize(getWidth(), getHeight());
        // clear the viewport => if there are more than inners looks to render (e.g. borders), must override this method
        clearShape();
        renderInners();
    }

    protected void renderInners() {
        for(int i=0;i<nbRows;i++) {
            for(int j=0;j<nbCols;j++) {
                for(ElementLook look : grid[i][j]) {
                    // render the look to its internal shape array
                    look.render();
                    // copy this array within the array of this container
                    double lookX = innersLeft + look.getElement().getX();
                    double lookY = innersTop + look.getElement().getY();
                    // render the look shape, taking care of not going out of the layout space.
                    for (int k = 0; k < look.getHeight(); k++) {
                        for (int l = 0; l < look.getWidth(); l++) {
                            if ((lookY + k < getHeight()) && (lookX + l < getWidth())) {
                                String s = look.getShapePoint(l, k);
                                if ((s != null) && (!" ".equals(s))) {
                                    shape[(int)lookY + k][(int)lookX + l] = s;
                                }
                            }
                        }
                    }
                }
            }
        }
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
                    // if anchor is at center, add size/2 so that the center of the look
                    // is at the right place.
                    if (look.getAnchorType() == ElementLook.ANCHOR_CENTER) {
                        x += look.getWidth()/2;
                        y += look.getHeight()/2;
                    }
                    // move the look shape to its correct location within the container cells
                    // knowing that x,y is the top-left corner in the local grid (i.e. not counting innerTop/Left)
                    if ((x != look.getElement().getX()) || (y != look.getElement().getY())) {
                        look.moveTo(x, y);
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

/* *****************************************************
    REMINDER COMMENT
    Methods that are defined in boardifier but useless for boardifierconsole

    public int[] getCellFromSceneLocation(Coord2D p) {}
    public int[] getCellFromSceneLocation(double x, double y) {}

    public Coord2D getContainerLocationForLookFromCell(ElementLook look, int row, int col) {}

    public Coord2D getRootPaneLocationForLookFromCell(ElementLook look, int row, int col) {}

    public int[] getCellFromInnersLocation(double x, double y) {}

    public Coord2D getLookLocationInCell(ElementLook look, int row, int col) {}

    ********************************************************** */
}
