package view;

import boardifier.control.Logger;
import boardifier.model.ContainerElement;
import boardifier.view.ConsoleColor;
import boardifier.view.GridLook;

public class RedDiskPot extends GridLook {

    public RedDiskPot(int rowHeight, int colWidth, ContainerElement containerElement) {
        super(rowHeight, colWidth, containerElement, -1, 1);
        setVerticalAlignment(ALIGN_MIDDLE);
        setHorizontalAlignment(ALIGN_CENTER);
    }

    @Override
    protected void render() {
        super.render();
        
        // Create a TextElement for the label above the pot
        String redText = "RED";
        int textRow = -1; // Position one row above the pot
        
        // Place the text above the pot
        for (int i = 0; i < redText.length() && i < colWidth; i++) {
            if (textRow >= 0 && textRow < shape.length) {
                shape[textRow][i] = ConsoleColor.RED_BOLD + redText.charAt(i) + ConsoleColor.RESET;
            }
        }
    }

    protected void renderBorders() {
        Logger.debug("called", this);
        for (int i = 0; i < nbRows; i++) {
            shape[i * rowHeight][0] = "\u250F";
            shape[i * rowHeight][colWidth] = "\u2513";
            shape[(i + 1) * rowHeight][0] = "\u2517";
            shape[(i + 1) * rowHeight][colWidth] = "\u251B";

            for (int k = 1; k < colWidth; k++) {
                shape[i * rowHeight][k] = "\u2501";
                shape[(i + 1) * rowHeight][k] = "\u2501";
            }
            for (int k = 1; k < rowHeight; k++) {
                shape[i * rowHeight + k][0] = "\u2503";
                shape[i * rowHeight + k][colWidth] = "\u2503";
            }
        }
        for (int i = 1; i < nbRows; i++) {
            shape[i * rowHeight][0] = "\u2523";
            shape[i * rowHeight][colWidth] = "\u252B";
        }
    }
}