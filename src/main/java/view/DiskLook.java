package view;

import boardifier.view.ConsoleColor;
import boardifier.view.ElementLook;
import model.PuissanceXDisk;

public class DiskLook extends ElementLook {

    // Define some characters for a more "rounded" look
    private static final String TL_CORNER = "╭"; // Top-left
    private static final String TR_CORNER = "╮"; // Top-right
    private static final String BL_CORNER = "╰"; // Bottom-left
    private static final String BR_CORNER = "╯"; // Bottom-right
    private static final String H_LINE = "─";    // Horizontal line
    private static final String V_LINE = "│";    // Vertical line
    private static final String SHINE_CHAR = "·"; // A small dot for shine effect
    // private static final String SHINE_CHAR = "׳"; // Alternative shine (apostrophe like)
    // private static final String SHINE_CHAR = "*"; // Alternative shine (asterisk)

    public DiskLook(PuissanceXDisk puissanceXDisk) {
        // Size: Width = 5, Height = 3
        super(puissanceXDisk, 5, 3);
        setAnchorType(ANCHOR_TOPLEFT); // Or ANCHOR_CENTER if you prefer, adjust rendering logic slightly
        render();
    }

    @Override
    public void render() {
        PuissanceXDisk disk = (PuissanceXDisk) element;

        String fgColor;          // Foreground color for borders and the main '●' symbol character
        String mainSymbolStr;      // The '●' symbol with its foreground color
        String shineSymbolStr;     // The shine character, colored directly (no background)

        if (disk.getPlayerId() == 0) { // Player 1 (e.g., Red)
            fgColor = ConsoleColor.RED_BOLD;
            mainSymbolStr = ConsoleColor.RED_BOLD_BRIGHT + "●" + ConsoleColor.RESET; // Brighter symbol
            // Shine for red disk: white dot (no background)
            shineSymbolStr = ConsoleColor.WHITE_BOLD + SHINE_CHAR + ConsoleColor.RESET;
        } else { // Player 2 (e.g., Yellow)
            fgColor = ConsoleColor.YELLOW_BOLD;
            mainSymbolStr = ConsoleColor.YELLOW_BOLD_BRIGHT + "●" + ConsoleColor.RESET; // Brighter symbol
            // Shine for yellow disk: black or dark gray dot (no background) for contrast
            shineSymbolStr = ConsoleColor.BLACK_BOLD + SHINE_CHAR + ConsoleColor.RESET;
        }

        // Initialize shape with empty spaces (transparent)
        // This ensures that any part of the 5x3 grid not explicitly set
        // will be a space, effectively making it transparent.
        for (int i = 0; i < getHeight(); i++) {
            for (int j = 0; j < getWidth(); j++) {
                shape[i][j] = " "; // A single space for transparency
            }
        }

        // Create the disk shape (no background color for the "body"):
        // Example for Red Player (fgColor=RED_BOLD, mainSymbol=RED_BOLD_BRIGHT●, shine=WHITE_BOLD·):
        //   ╭·╮   (╭,╮ are red; · is white)
        //  │ ● │   (│ are red; ● is bright red; spaces are transparent)
        //   ╰─╯   (╰,╯,─ are red)

        // Row 0 (Top)
        // shape[0][0] remains " "
        shape[0][1] = fgColor + TL_CORNER + ConsoleColor.RESET;
        shape[0][2] = shineSymbolStr; // Shine in the middle of the top border
        shape[0][3] = fgColor + TR_CORNER + ConsoleColor.RESET;
        // shape[0][4] remains " "

        // Row 1 (Middle)
        shape[1][0] = fgColor + V_LINE + ConsoleColor.RESET;
        // shape[1][1] remains " " (transparent space inside the disk)
        shape[1][2] = mainSymbolStr; // Central '●' symbol
        // shape[1][3] remains " " (transparent space inside the disk)
        shape[1][4] = fgColor + V_LINE + ConsoleColor.RESET;

        // Row 2 (Bottom)
        // shape[2][0] remains " "
        shape[2][1] = fgColor + BL_CORNER + ConsoleColor.RESET;
        shape[2][2] = fgColor + H_LINE + ConsoleColor.RESET; // Bottom border
        shape[2][3] = fgColor + BR_CORNER + ConsoleColor.RESET;
        // shape[2][4] remains " "
    }
}