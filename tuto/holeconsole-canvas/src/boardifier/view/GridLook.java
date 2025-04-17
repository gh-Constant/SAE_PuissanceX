package boardifier.view;

import boardifier.control.Logger;
import boardifier.model.ContainerElement;


public class GridLook extends ContainerLook {

    protected int borderWidth;

    public GridLook(int rowHeight, int colWidth, ContainerElement containerElement, int depth, int borderWidth) {
        this(rowHeight, colWidth, containerElement,depth, 0 , 0, borderWidth);
    }

    public GridLook(int rowHeight, int colWidth, ContainerElement containerElement, int depth, int innersTop, int innersLeft, int borderWidth) {
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
        if (borderWidth > 0) {
            setPaddingLeft(1);
            setPaddingTop(1);
        }
    }

    @Override
    public int getHeight() {
        int h = super.getHeight();
        // if there are borders, it needs +1 in height to be drawn
        if (borderWidth > 0) h++;
        return h;
    }

    @Override
    public int getWidth() {
        int w = super.getWidth();
        // if there are borders, it needs +1 in width to be drawn
        if (borderWidth > 0) w++;
        return w;
    }

    /**
     * overrides default method that does nothing, because as soon as the GridLook is created,
     * the space dedicated to the look is already fixed.
     */
    protected void render() {
        Logger.trace("called", this);
        // create & clear the viewport if needed
        setSize(getWidth(), getHeight());
        // clear the viewport => if there are more than inners looks to render (e.g. borders), must override this method
        clearShape();
        renderBorders();
        renderInners();
    }

    protected void renderBorders() {
        if (borderWidth < 1) return;

        Logger.trace("update borders", this);

        // start by drawing the border of each cell, which will be change after
        for (int i = 0; i < nbRows; i++) {
            for (int j = 0; j < nbCols; j++) {
                int x = getCellLeft(i,j);
                int xx = getCellRight(i,j)+1; // +1 because border is at the right of the real position
                int y = getCellTop(i,j);
                int yy = getCellBottom(i,j)+1; // +1 because border is below of the real position
                //top-left corner
                if (y==0) {
                    if (x==0) shape[innersTop+y][innersLeft+x] = "\u2554"; // draw ╔
                    else shape[innersTop+y][innersLeft+x] = "\u2566"; // draw ╦
                }
                else {
                    if (x==0) shape[innersTop+y][innersLeft+x] = "\u2560"; // draw ╠
                    else shape[innersTop+y][innersLeft+x] = "\u256c"; // draw ╬
                }

                // top-right corner
                if (y==0) {
                    if (xx==getGridWidth()) shape[innersTop+y][innersLeft+xx] = "\u2557"; // draw ╗
                    else shape[ innersTop+y][innersLeft+xx] = "\u2566"; // draw ╦
                }
                else {
                    if (xx==getGridWidth()) shape[ innersTop+y][innersLeft+xx] = "\u2563"; // draw ╣
                    else shape[ innersTop+y][innersLeft+xx] = "\u256c"; // draw ╬
                }

                //bottom-left corner
                if (yy==getGridHeight()) {
                    if (x==0) shape[ innersTop+yy][innersLeft+x] = "\u255A"; // draw ╚
                    else shape[ innersTop+yy][innersLeft+x] = "\u2569"; // draw ╩
                }
                else {
                    if (x==0) shape[ innersTop+yy][innersLeft+x] = "\u2560"; // draw ╠
                    else shape[ innersTop+yy][innersLeft+x] = "\u256c"; // draw ╬
                }

                // bottom-right corner
                if (yy==getGridHeight()) {
                    if (xx==getGridWidth()) shape[ innersTop+yy][innersLeft+xx] = "\u255d"; // draw ╝
                    else shape[ innersTop+yy][innersLeft+xx] = "\u2569"; // draw ╩
                }
                else {
                    if (xx==getGridWidth()) shape[ innersTop+yy][innersLeft+xx] = "\u2563"; // draw ╣
                    else shape[ innersTop+yy][innersLeft+xx] = "\u256c"; // draw ╬
                }

                // draw top/bottom horizontal lines
                for (int k = x+1; k < xx; k++) {
                    shape[ innersTop+y][innersLeft+k] = "\u2550";
                    shape[ innersTop+yy][innersLeft+k] = "\u2550";
                }
                // draw left/right vertical lines
                for (int k = y+1; k < yy; k++) {
                    shape[innersTop+k][innersLeft+x] = "\u2551";
                    shape[innersTop+k][innersLeft+xx] = "\u2551";
                }
            }
        }
    }
}
