package view;

import boardifier.control.Logger;
import boardifier.model.ContainerElement;
import boardifier.view.GridLook;

/**
 * Red pot inherits from GridLook but overrides the renderBorders() method
 * so that a special look is given to borders copared to the default look defined
 * in GridLook. Moreover, cells have a fixed size, meaning that if an element is
 * too big to fit within a cell, it will overlap neighbors cells.
 * The default alignment is also changed and set to the middle of the cells.
 */
public class RedPawnPotLook extends GridLook {

    public RedPawnPotLook(int rowHeight, int colWidth, ContainerElement containerElement) {
        super(rowHeight, colWidth, containerElement, -1, 1);
        setVerticalAlignment(ALIGN_MIDDLE);
        setHorizontalAlignment(ALIGN_CENTER);
    }

    protected void renderBorders() {
        Logger.debug("called", this);
        // start by drawing the border of each cell, which will be change after
        for (int i = 0; i < nbRows; i++) {
            //top-left corner
            shape[i * rowHeight][0] = "\u250F";
            // top-right corner
            shape[i * rowHeight][colWidth] = "\u2513";
            //bottom-left corner
            shape[(i + 1) * rowHeight][0] = "\u2517";
            // bottom-right corner
            shape[(i + 1) * rowHeight][colWidth] = "\u251B";

            for (int k = 1; k < colWidth; k++) {
                shape[i * rowHeight][k] = "\u2501";
                shape[(i + 1) * rowHeight][k] = "\u2501";
            }
            // draw left & righ vertical lines
            for (int k = 1; k < rowHeight; k++) {
                shape[i * rowHeight + k][0] = "\u2503";
                shape[i * rowHeight + k][colWidth] = "\u2503";
            }
        }
        // change intersections on first & last vert. border
        for (int i = 1; i < nbRows; i++) {
            shape[i * rowHeight][0] = "\u2523";
            shape[i * rowHeight][colWidth] = "\u252B";
        }
    }
}