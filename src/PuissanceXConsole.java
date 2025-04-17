import boardifier.model.GameException;
import boardifier.view.View;

import boardifier.control.StageFactory;
import boardifier.model.Model;

public class PuissanceXConsole {
    // Game parameters
    private static int BOARD_ROWS = 6;    // default value
    private static int BOARD_COLS = 7;    // default value
    private static int WIN_CONDITION_X = 4; // default value
    private static int gameMode = 0;      // 0: Human vs Human, 1: Human vs AI, 2: AI vs AI

    public static void main(String[] args) {
        // Parse command line arguments
        if (args.length >= 4) {
            try {
                BOARD_ROWS = Integer.parseInt(args[0]);
                BOARD_COLS = Integer.parseInt(args[1]);
                WIN_CONDITION_X = Integer.parseInt(args[2]);
                gameMode = Integer.parseInt(args[3]);
                
                // Validate parameters
                if (BOARD_ROWS < 4 || BOARD_COLS < 4) {
                    System.out.println("Board dimensions must be at least 4x4");
                    return;
                }
                if (WIN_CONDITION_X < 3 || WIN_CONDITION_X > Math.max(BOARD_ROWS, BOARD_COLS)) {
                    System.out.println("Invalid win condition value");
                    return;
                }
                if (gameMode < 0 || gameMode > 2) {
                    gameMode = 0;
                }
            }
            catch(NumberFormatException e) {
                System.out.println("Invalid arguments. Using default values.");
                BOARD_ROWS = 6;
                BOARD_COLS = 7;
                WIN_CONDITION_X = 4;
                gameMode = 0;
            }
        }

        //Model model = new Model();

        System.out.println("PuissanceXConsole");

        /*
        TO FULFILL:
            - add both players to model taking mode value into account
            - register the model and view class names (i.e model.HoleStageModel & view.HoleStageView
            - create the controller
            - set the name of the first stage to use when starting the game
            - start the game
            - start the stage loop.
         */
    }

    // Getter methods for game parameters
    public static int getBoardRows() { return BOARD_ROWS; }
    public static int getBoardCols() { return BOARD_COLS; }
    public static int getWinConditionX() { return WIN_CONDITION_X; }
    public static int getGameMode() { return gameMode; }
}