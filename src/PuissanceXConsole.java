import boardifier.model.GameException;
import boardifier.view.View;
import control.PuissanceXController;
import boardifier.control.StageFactory;
import boardifier.model.Model;
import boardifier.control.Controller;
import boardifier.control.Logger;
import java.util.Arrays;

public class PuissanceXConsole {
    // Game parameters
    private static int BOARD_ROWS = 6;    // default value
    private static int BOARD_COLS = 7;    // default value
    private static int WIN_CONDITION = 4; // default value
    private static int GAME_MODE = 0;     // 0: Human vs Human, 1: Human vs AI, 2: AI vs AI

    public static void main(String[] args) throws GameException {
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

        // Create the model that will manage game state
        Model model = new Model();
        
        // Add players based on game mode
        switch (GAME_MODE) {
            case 0: // Human vs Human
                model.addHumanPlayer("Joueur 1");
                model.addHumanPlayer("Joueur 2");
                break;
            case 1: // Human vs AI
                model.addHumanPlayer("Joueur 1");
                model.addComputerPlayer("Ordinateur");
                break;
            case 2: // AI vs AI
                model.addComputerPlayer("Ordinateur 1");
                model.addComputerPlayer("Ordinateur 2");
                break;
        }

        // Register model and view classes for the game stage
        // This allows the framework to create instances dynamically
        StageFactory.registerModelAndView("puissanceXStage", "model.PuissanceXLevel", "view.console.PuissanceXLevelView");

        // Create the view that will display the game
        View view = new View(model);
        
        // Create the controller that will manage game logic
        control.PuissanceXController controller = new control.PuissanceXController(model, view, BOARD_ROWS, BOARD_COLS, WIN_CONDITION);

        // Set the first stage to be used when starting the game
        controller.setFirstStageName("puissanceXStage");

        // Start the game (creates the first stage)
        try {
            controller.startGame();
        } catch (GameException e) {
            Logger.info("Failed to start game: " + e.getMessage());
            Logger.debug("Stack trace: " + Arrays.toString(e.getStackTrace()));
            return;
        }

        // Start the main game loop
        controller.stageLoop();
    }

    // Getter methods for game parameters
    public static int getBoardRows() { return BOARD_ROWS; }
    public static int getBoardCols() { return BOARD_COLS; }
    public static int getWinCondition() { return WIN_CONDITION; }
    public static int getGameMode() { return GAME_MODE; }
}