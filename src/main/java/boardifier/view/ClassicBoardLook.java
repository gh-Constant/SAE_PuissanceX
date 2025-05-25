package boardifier.view;

import boardifier.control.Logger;
import boardifier.model.ContainerElement;

/**
 * ClassicBoardLook is a chess like board, with optional coordinate system to show, using A, B, C for columns
 * and 1, 2, ... for rows.
 * Because of the text mode, the cells are not mandatory square in order to obtain a good render of the board
 * Note that
 */
public class ClassicBoardLook extends GridLook{

    protected boolean showCoords;

    public ClassicBoardLook(int rowHeight, int colWidth, ContainerElement element,  int depth, int borderWidth, boolean showCoords) {
        super(rowHeight, colWidth, element, -1, borderWidth);

        this.showCoords = showCoords;
        int gapXToCells = 0;
        int gapYToCells = 0;
        if (showCoords) {
            gapYToCells ++;
            int n = nbRows;
            while (n>0) {
                gapXToCells++;
                n = n/10;
            }
        }
        setInnersTopLeft(gapYToCells, gapXToCells);
        setVerticalAlignment(ALIGN_MIDDLE);
        setHorizontalAlignment(ALIGN_CENTER);
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
        renderCoords();
        renderBorders();
        renderInners();
    }

    protected void renderCoords() {

        if (!showCoords) return;
        Logger.trace("update coords", this);

        for (int i = 0; i < nbRows; i++) {
            int n = i + 1;
            int k = innersLeft-1;
            while(n>0) {
                shape[innersTop + (int) ((i + 0.5) * rowHeight)][k] = String.valueOf(n%10);
                n = n/10;
                k--;
            }
        }
        for (int j = 0; j < nbCols; j++) {
            int nbChars = 0;
            int n = j;
            if (n<26) {
                nbChars = 1;
            }
            else {
                while (n > 0) {
                    nbChars++;
                    n = n / 26;
                }
            }
            n = j;
            for(int k=0;k<nbChars;k++) {
                char c = (char) (n%26 + 'A');
                shape[0][innersLeft + (int) ((j + 0.5) * colWidth)+ nbChars/2 - k] = String.valueOf(c);
                n = n/26;
                // adjust the last digit to print, so that if n=1, it prints A and not B.
                if (k == nbChars-2) n--;
            }
        }
    }
}
