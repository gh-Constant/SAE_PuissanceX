package boardifier.view;

import boardifier.control.Logger;
import boardifier.model.ContainerElement;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

public class TableLook extends ContainerLook {

    protected int borderWidth;
    protected Color borderColor;
    protected Rectangle[][] borders;

    /**
     * constructor to get a flexible cell size, with inners in 0,0
     * @param containerElement
     * @param depth
     * @param borderWidth
     * @param borderColor
     */
    public TableLook(ContainerElement containerElement, int depth, int borderWidth, Color borderColor) {
        this(containerElement, depth, 0,0, borderWidth, borderColor);
    }

    /**
     * constructor to get a flexible cell size, with inners given as parameters
     * @param containerElement
     * @param depth
     * @param innersTop
     * @param innersLeft
     * @param borderWidth
     * @param borderColor
     */
    public TableLook(ContainerElement containerElement, int depth, int innersTop, int innersLeft, int borderWidth, Color borderColor) {

        this(containerElement, -1,  -1, depth, innersTop, innersLeft, borderWidth, borderColor);
    }

    /**
     * constructor to get either flexible or fixed cell size
     * @param containerElement
     * @param rowHeight
     * @param colWidth
     * @param depth
     * @param innersTop
     * @param innersLeft
     * @param borderWidth
     * @param borderColor
     */
    public TableLook(ContainerElement containerElement, int rowHeight, int colWidth, int depth, int innersTop, int innersLeft, int borderWidth, Color borderColor) {

        super(containerElement, rowHeight,  colWidth, depth, innersTop, innersLeft);
        this.borderWidth = borderWidth;
        this.borderColor = borderColor;
    }

    protected void render() {
        super.render();
        // create the borders, if needed
        if (borderWidth >= 1) {
            borders = new Rectangle[nbRows][nbCols];
            for (int i = 0; i < nbRows; i++) {
                for (int j = 0; j < nbCols; j++) {
                    borders[i][j] = new Rectangle(10,10, Color.TRANSPARENT);
                    borders[i][j].setSmooth(false);
                    borders[i][j].setStroke(borderColor);
                    borders[i][j].setStrokeWidth(borderWidth);
                    borders[i][j].setStrokeMiterLimit(10);
                    borders[i][j].setStrokeType(StrokeType.CENTERED);
                    borders[i][j].setVisible(false);
                    addShape(borders[i][j]);
                }
            }
        }
        else {
            borders = null;
        }
    }

    /* override all methods that lead to change the inners location
       because it also implies an update of the borders
     */

    @Override
    public void setInnersTop(int innersTop) {
        super.setInnersTop(innersTop);
        updateBorders();
    }

    @Override
    public void setInnersLeft(int innersLeft) {
        super.setInnersLeft(innersLeft);
        updateBorders();
    }

    @Override
    public void setInnersTopLeft(int top, int left) {
        super.setInnersTopLeft(top, left);
        updateBorders();
    }

    @Override
    public void setRowHeight(int rowHeight) {
        super.setRowHeight(rowHeight);
        updateBorders();
    }

    @Override
    public void setColWidth(int colWidth) {
        super.setColWidth(colWidth);
        updateBorders();
    }

    @Override
    public void setPadding(int padding) {
        super.setPadding(padding);
        updateBorders();
    }

    @Override
    public void setPaddingTop(int padding) {
        super.setPaddingTop(padding);
        updateBorders();
    }

    @Override
    public void setPaddingBottom(int padding) {
        super.setPaddingBottom(padding);
        updateBorders();
    }

    @Override
    public void setPaddingLeft(int padding) {
        super.setPaddingLeft(padding);
        updateBorders();
    }

    @Override
    public void setPaddingRight(int padding) {
        super.setPaddingRight(padding);
        updateBorders();
    }

    @Override
    public void setCellPadding(int row, int col, int padding) {
        super.setCellPadding(row, col, padding);
        updateBorders();
    }

    @Override
    public void setCellPaddingTop(int row, int col, int padding) {
        super.setCellPaddingTop(row, col, padding);
        updateBorders();
    }

    @Override
    public void setCellPaddingBottom(int row, int col, int padding) {
        super.setCellPaddingBottom(row, col, padding);
        updateBorders();
    }

    @Override
    public void setCellPaddingLeft(int row, int col, int padding) {
        super.setCellPaddingLeft(row, col, padding);
        updateBorders();
    }

    @Override
    public void setCellPaddingRight(int row, int col, int padding) {
        super.setCellPaddingRight(row, col, padding);
        updateBorders();
    }

    @Override
    public void addInnerLook(ElementLook look, int row, int col) {
       super.addInnerLook(look, row, col);
       updateBorders();
    }

    @Override
    public void removeInnerLook(ElementLook look, int row, int col) {
        super.removeInnerLook(look, row, col);
        updateBorders();
    }

    @Override
    public void moveInnerLook(ElementLook look, int rowSrc, int colSrc, int rowDest, int colDest) {
        super.moveInnerLook(look, rowSrc, colSrc, rowDest, colDest);
        updateBorders();
    }

    public void updateBorders() {
        /* if borders have not been already create, do nothing
          This case occurs when inherting from this class and the constructor calls
          a method that leads to a call to updateBorders() while render() has not been yet called.
          For example, setting paddings is in this case.
         */
        if (borders == null) return;
        Logger.trace("updating borders of ["+this+"]");
        int[][] rowSpans = ((ContainerElement)element).getRowSpans();
        int[][] colSpans = ((ContainerElement)element).getColSpans();
        int rowStart = 0;
        for(int i=0;i<nbRows;i++) {
            int colStart = 0;
            for(int j=0;j<nbCols;j++) {
                if ((rowSpans[i][j] <1) || (colSpans[i][j] <1) || (grid[i][j].isEmpty())) {
                    borders[i][j].setVisible(false);
                    continue; // no need of a border for cells covered by a spanning
                }
                // x,y are the top-left corner of the grid cell
                int x = colStart;
                int y = rowStart;
                // xx,yy are the bottom-right corner, in case of there is no spanning
                int w = 0;
                for(int k=0;k<colSpans[i][j];k++) {
                    w += colsWidth[j+k];
                }
                int xx = colStart + w - 1;
                int h = 0;
                for(int k=0;k<rowSpans[i][j];k++) {
                    h += rowsHeight[i+k];
                }
                int yy = rowStart + h - 1;

                borders[i][j].setWidth(xx-x+1);
                borders[i][j].setHeight(yy-y+1);
                borders[i][j].setX(x+ innersLeft);
                borders[i][j].setY(y+ innersTop);
                borders[i][j].setVisible(true);

                Logger.trace("showing border in "+(x+innersLeft)+","+(y+innersTop)+" of size "+(xx-x)+"x"+(yy-y));
                colStart += colsWidth[j];
            }
            rowStart += rowsHeight[i];
        }
    }
}
