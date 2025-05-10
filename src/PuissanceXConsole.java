import boardifier.control.Logger;
import boardifier.control.StageFactory;
import boardifier.model.GameException;
import boardifier.view.View;
import control.PuissanceXController;
import model.PuissanceXModel;

public class PuissanceXConsole {
    public static void main(String[] args) {
        // Initialize boardifier logger settings
        Logger.setLevel(Logger.LOGGER_TRACE); 
        Logger.setVerbosity(Logger.VERBOSE_HIGH);

        System.out.println("INFO: Starting PuissanceX Console...");

        // Default values
        int winCondition = 4;
        int boardRows = 6;
        int boardCols = 7;
        int currentGameMode = 0;

        try {
            if (args.length >= 1) {
                winCondition = Integer.parseInt(args[0]);
                System.out.println("DEBUG: Parsed Win Condition argument: " + winCondition);
            }
            if (args.length >= 2) {
                boardRows = Integer.parseInt(args[1]);
                System.out.println("DEBUG: Parsed rows argument: " + boardRows);
            }
            if (args.length >= 3) {
                boardCols = Integer.parseInt(args[2]);
                System.out.println("DEBUG: Parsed columns argument: " + boardCols);
            }
            if (args.length >= 4) {
                currentGameMode = Integer.parseInt(args[3]);
                System.out.println("DEBUG: Parsed mode argument: " + currentGameMode);
            }
        } catch (NumberFormatException e) {
            System.err.println("WARNING: Error parsing arguments: " + e.getMessage() + ". Review arguments. Some or all parameters will use defaults.");
        }

        // Game Mode validation
        if (currentGameMode < 0 || currentGameMode > 2) {
            System.err.println("WARNING: Parsed game mode (" + currentGameMode + ") is invalid. Setting to 0 (Human vs Human).");
            currentGameMode = 0;
        }

        System.out.println("INFO: Final effective game parameters set:");
        System.out.println("  Win Condition: " + winCondition);
        System.out.println("  Board rows: " + boardRows);
        System.out.println("  Board columns: " + boardCols);
        System.out.println("  Game mode: " + currentGameMode);

        // Initialize model with parameters
        PuissanceXModel model = new PuissanceXModel();
        model.setWinCondition(winCondition);
        model.setBoardRows(boardRows);
        model.setBoardCols(boardCols);

        System.out.println("INFO: Setting up players for mode: " + currentGameMode);
        if (currentGameMode == 0) {
            model.addHumanPlayer("player1");
            model.addHumanPlayer("player2");
            System.out.println("DEBUG: Added player1 (Human) and player2 (Human).");
        } else if (currentGameMode == 1) {
            model.addHumanPlayer("player");
            model.addComputerPlayer("computer");
            System.out.println("DEBUG: Added player (Human) and computer (AI).");
        } else if (currentGameMode == 2) {
            model.addComputerPlayer("computer1");
            model.addComputerPlayer("computer2");
            System.out.println("DEBUG: Added computer1 (AI) and computer2 (AI).");
        }

        System.out.println("INFO: Registering PuissanceX model and view with StageFactory.");
        StageFactory.registerModelAndView("puissanceX", "model.PuissanceXStageModel", "view.PuissanceXStageView");
        
        View gameView = new View(model); 
        System.out.println("DEBUG: View object (boardifier.view.View) created.");

        PuissanceXController control = new PuissanceXController(model, gameView);
        System.out.println("DEBUG: PuissanceXController object created.");
        
        control.setFirstStageName("puissanceX");
        System.out.println("DEBUG: First stage name set to 'puissanceX'.");

        try {
            System.out.println("INFO: Attempting to start game...");
            control.startGame();
            System.out.println("INFO: Game started successfully. Entering stage loop.");
            control.stageLoop();
            System.out.println("INFO: Stage loop exited. Game ended.");
        } catch (Exception e) {
            System.err.println("ERROR: An unexpected error occurred: " + e.getMessage() + ". Aborting.");
            System.err.println("ERROR_DETAILS: " + e.toString());
        }
    }
} 