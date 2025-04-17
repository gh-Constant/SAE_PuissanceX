import boardifier.model.GameException;
import boardifier.view.View;
import boardifier.control.StageFactory;
import boardifier.model.Model;
import boardifier.control.Logger;

public class PuissanceXConsole {
    // Game parameters
    private static int BOARD_ROWS = 6;    // default value
    private static int BOARD_COLS = 7;    // default value
    private static int WIN_CONDITION = 4; // default value
    private static int GAME_MODE = 0;      // 0: Human vs Human, 1: Human vs AI, 2: AI vs AI

    public static void main(String[] args) {
        // Set logger level to INFO
        Logger.setLevel(Logger.LOGGER_INFO);
        
        // Parse command line arguments
        if (args.length >= 4) {
            try {
                BOARD_ROWS = Integer.parseInt(args[0]);
                BOARD_COLS = Integer.parseInt(args[1]);
                WIN_CONDITION = Integer.parseInt(args[2]);
                GAME_MODE = Integer.parseInt(args[3]);
                
                // Validate parameters
                if (BOARD_ROWS < 4 || BOARD_COLS < 4) {
                    Logger.info("Board dimensions must be at least 4x4");
                    return;
                }
                if (WIN_CONDITION < 3 || WIN_CONDITION > Math.max(BOARD_ROWS, BOARD_COLS)) {
                    Logger.info("Invalid win condition value");
                    return;
                }
                if (GAME_MODE < 0 || GAME_MODE > 2) {
                    Logger.info("Invalid game mode. Using default mode (Human vs Human)");
                    GAME_MODE = 0;
                }
                
                Logger.info("Game parameters: " + BOARD_ROWS + "x" + BOARD_COLS + 
                          ", Win condition: " + WIN_CONDITION + 
                          ", Mode: " + GAME_MODE);
            }
            catch(NumberFormatException e) {
                Logger.info("Invalid arguments. Using default values.");
                BOARD_ROWS = 6;
                BOARD_COLS = 7;
                WIN_CONDITION = 4;
                GAME_MODE = 0;
            }
        }
        else {
            Logger.info("Using default values: " + BOARD_ROWS + "x" + BOARD_COLS + 
                      ", Win condition: " + WIN_CONDITION + 
                      ", Mode: " + GAME_MODE);
        }

        Model model = new Model();
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
    public static int getWinCondition() { return WIN_CONDITION; }
    public static int getGameMode() { return GAME_MODE; }
}