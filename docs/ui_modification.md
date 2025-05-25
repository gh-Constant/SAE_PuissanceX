# Modifying the UI

This section explains how to make changes to the User Interface, focusing on the console display of the game board.

## Example: Swapping Row Numbers and Column Letters

Let's say you want to modify the board display so that letters (A, B, C, ...) are used for rows (down the side) and numbers (1, 2, 3, ...) are used for columns (across the top), the opposite of the current setup.

**File to Modify:** `src/boardifier/main.view/ClassicBoardLook.java`

The method responsible for drawing the row and column coordinates is `renderCoords()` in this file.

### Current Logic in `renderCoords()`:

*   **Row Numbers (1, 2, 3... on the side):**
    ```java
    // Loops through each row (i)
    // Calculates number 'n' (i + 1)
    // Places digits of 'n' at:
    // shape[ Y_POSITION_FOR_ROW_i ][ X_POSITION_LEFT_OF_BOARD ]
    for (int i = 0; i < nbRows; i++) {
        int n = i + 1;
        int k = innersLeft-1; // X position (to the left of the first cell)
        while(n>0) {
            // Y position: innersTop + (int) ((i + 0.5) * rowHeight)
            shape[innersTop + (int) ((i + 0.5) * rowHeight)][k] = String.valueOf(n%10);
            n = n/10;
            k--;
        }
    }
    ```

*   **Column Letters (A, B, C... on the top):**
    ```java
    // Loops through each column (j)
    // Calculates character 'c' (based on j)
    // Places character 'c' at:
    // shape[ Y_POSITION_ABOVE_BOARD ][ X_POSITION_FOR_COL_j ]
    for (int j = 0; j < nbCols; j++) {
        // ... logic to handle single or multiple characters (e.g., A, Z, AA) ...
        n = j;
        for(int k=0;k<nbChars;k++) { // nbChars is 1 for A-Z, 2 for AA-ZZ, etc.
            char c = (char) (n%26 + 'A');
            // X position: innersLeft + (int) ((j + 0.5) * colWidth)+ nbChars/2 - k
            // Y position: 0 (topmost row of the shape)
            shape[0][innersLeft + (int) ((j + 0.5) * colWidth)+ nbChars/2 - k] = String.valueOf(c);
            n = n/26;
            if (k == nbChars-2) n--;
        }
    }
    ```

### How to Swap Them:

You would need to adapt these two loops:

1.  **For Row Letters (A, B, C... on the side):**
    *   Iterate from `i = 0` to `nbRows - 1`.
    *   Convert `i` to a character (e.g., `(char) (i % 26 + 'A')`). Handle cases beyond 'Z' if necessary (AA, AB, etc.), similar to the original column logic.
    *   The `shape` coordinates for placing these letters would be:
        *   `Y_POSITION_FOR_ROW_i`: `innersTop + (int) ((i + 0.5) * rowHeight)` (similar to original row numbers)
        *   `X_POSITION_LEFT_OF_BOARD`: `innersLeft - 1` (or adjust based on the number of characters for the row label)

2.  **For Column Numbers (1, 2, 3... on the top):**
    *   Iterate from `j = 0` to `nbCols - 1`.
    *   The number to display will be `j + 1`.
    *   The `shape` coordinates for placing these numbers would be:
        *   `Y_POSITION_ABOVE_BOARD`: `0` (similar to original column letters)
        *   `X_POSITION_FOR_COL_j`: `innersLeft + (int) ((j + 0.5) * colWidth)` (adjust for multi-digit numbers if necessary, similar to original row number logic)

**Modified `renderCoords()` (Conceptual):**

```java
protected void renderCoords() {
    if (!showCoords) return;
    Logger.trace("update coords", this);

    // NEW: Render Column Numbers (1, 2, 3... on top)
    for (int j = 0; j < nbCols; j++) {
        int n = j + 1; // Number for the column
        // Determine number of digits for positioning (similar to old row logic)
        String numStr = String.valueOf(n);
        int numDigits = numStr.length();
        // Calculate starting X position to center the number in the column header space
        // This needs careful adjustment based on colWidth and numDigits.
        // For simplicity, let's adapt the original row logic's character-by-character placement.
        int tempN = n;
        int k = innersLeft + (int)((j+0.5)*colWidth) + numDigits/2; // Approximate center
        // Ensure k doesn't exceed bounds and is within the cell's top border area.
        // This positioning logic needs to be precise for good alignment.
        // The original logic for row numbers placed them right-to-left:
        // int xPosForDigit = innersLeft + (int) ((j + 0.5) * colWidth) + (numDigits / 2) - digitIndex;
        // For now, placing the string directly:
        // This is a simplified placement and might need refinement for perfect centering/alignment.
        String val = String.valueOf(tempN);
        for (int digitIdx = 0; digitIdx < val.length(); digitIdx++) {
             // Crude positioning, assumes shape[0] is wide enough for all column numbers
             // A more robust solution would calculate precise character positions.
            if ( (innersLeft + (int)( (j+0.5)*colWidth ) - (val.length()/2) + digitIdx) < shape[0].length) {
                 shape[0][innersLeft + (int)( (j+0.5)*colWidth ) - (val.length()/2) + digitIdx] = String.valueOf(val.charAt(digitIdx));
            }
        }
    }

    // NEW: Render Row Letters (A, B, C... on the side)
    for (int i = 0; i < nbRows; i++) {
        int n = i; // For A, B, C conversion
        int nbChars = 0;
        if (n < 26) {
            nbChars = 1;
        } else {
            // Handle multi-char labels like AA, AB if needed
            int temp = n;
            while (temp >= 0) { // Adjusted loop condition
                nbChars++;
                if (temp < 26) break;
                temp = temp / 26 -1; // Adjust for 0-indexed to 26-based
            }
        }

        // Place letters from right to left (e.g., if "AA", first 'A' then second 'A')
        int currentX = innersLeft - 1; // Start placing one char left of the board
        for (int k = 0; k < nbChars; k++) {
            char c = (char) (n % 26 + 'A');
            if (currentX - k >= 0) { // Ensure we don't go out of bounds
                 shape[innersTop + (int) ((i + 0.5) * rowHeight)][currentX - k] = String.valueOf(c);
            }
            n = n / 26 -1; // Prepare for the next character (e.g. for AA, after A, n becomes 0, then -1)
                           // The -1 adjustment is because 0=A, 1=B ... 25=Z, then 26 should effectively be the start of 'AA'
                           // which means the first char is A (0) and the "next part" is also A (0).
                           // So, (n/26)-1 is a way to shift to the next "digit" in base 26.
        }
    }
}
```

**Important Considerations for the Change:**

*   **`innersLeft` and `innersTop`:** These variables define the starting x and y pixel (character position) of the actual grid cells. The coordinate rendering logic uses them to position labels *outside* this inner grid.
*   **`gapXToCells` and `gapYToCells`:** These are calculated in the `ClassicBoardLook` constructor based on `showCoords` and the maximum number of digits/letters needed. If you change from numbers to letters for rows, `gapXToCells` (the space reserved to the left of the board) might need adjustment if your row labels (e.g., "AA") become wider than the space previously allocated for row numbers (e.g., "10"). Similarly for `gapYToCells` if column numbers become taller than single letters. You might need to adjust how `gapXToCells` and `gapYToCells` are calculated or manually set them if the automatic calculation is no longer sufficient.
*   **Positioning Logic:** The exact calculation for `shape[Y][X]` needs to be precise. The original code uses `(int) ((i + 0.5) * rowHeight)` to vertically center the label within a cell's height. Similar logic applies horizontally with `colWidth`. Pay attention to how multi-digit numbers or multi-character letters are handled to ensure they are aligned correctly (e.g., right-aligned, centered). The conceptual code above is a starting point and might need refinement for pixel-perfect alignment.
*   **Multi-Character Row Labels:** The original column logic handles labels like `AA`, `AB`. You'd need to replicate and adapt this logic for rows if you expect more than 26 rows. The conceptual code above includes a basic adaptation.
*   **Testing:** After making changes, thoroughly test with different board sizes to ensure the coordinates are always displayed correctly.

This example should give you a good starting point for understanding where and how to modify the board's appearance. The key is to trace how `shape` (the 2D char array representing the console output) is populated. 