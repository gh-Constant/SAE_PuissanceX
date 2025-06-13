package boardifier.view;

import boardifier.control.Logger;
import boardifier.model.ContainerElement;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

public class GridLook extends ContainerLook {

    protected int borderWidth;
    protected Color borderColor;
    protected Rectangle[][] borders;

    public GridLook(int rowHeight, int colWidth, ContainerElement containerElement, int depth, int borderWidth, Color borderColor) {
        this(rowHeight, colWidth, containerElement,depth, 0 , 0, borderWidth, borderColor);
    }

    public GridLook(int rowHeight, int colWidth, ContainerElement containerElement, int depth, int innersTop, int innersLeft, int borderWidth, Color borderColor) {
        super(containerElement, rowHeight, colWidth, depth, innersTop, innersLeft);

        // force cell dimensions to at least >= 1
        if (rowHeight < 1) {
            setRowHeight(1);
        }
        if (colWidth < 1) {
            setColWidth(1);
        }
        // check if the container is also a grid, i.e. has no spanning cells
        if (containerElement.hasSpannings()) {
            Logger.trace("TRYING TO SET A GRID LOOK FOR A CONTAINER THAT HAS SPANNIG CELLS. RESULT UNKNOWN");
        }

        this.borderWidth = borderWidth;
        this.borderColor = borderColor;
        borders = null;
    }

    protected void render() {
        super.render();
        // create borders if needed
        if (borderWidth >= 1) {
            borders = new Rectangle[nbRows][nbCols];
            for(int i=0;i<nbRows;i++) {
                for (int j = 0; j < nbCols; j++) {
                    borders[i][j] = new Rectangle(colWidth, rowHeight, Color.TRANSPARENT);
                    borders[i][j].setSmooth(false);
                    borders[i][j].setStroke(borderColor);
                    borders[i][j].setStrokeWidth(borderWidth);
                    borders[i][j].setStrokeMiterLimit(10);
                    borders[i][j].setStrokeType(StrokeType.CENTERED);
                    borders[i][j].setX(innersLeft + j * colWidth);
                    borders[i][j].setY(innersTop + i * rowHeight);
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

    protected void updateBorders() {
        /* if borders have not been already created, do nothing
          This case occurs when inherting from this class and the constructor calls
          a method that leads to a call to updateBorders() while render() has not been yet called.
          For example, setting paddings is in this case.
         */
        if (borders == null) return;

        for(int i=0;i<nbRows;i++) {
            for(int j=0;j<nbCols;j++) {
                borders[i][j].setWidth(colWidth);
                borders[i][j].setHeight(rowHeight);
                borders[i][j].setX(innersLeft+j*colWidth);
                borders[i][j].setY(innersTop+i*rowHeight);
            }
        }
    }
}
