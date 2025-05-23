import boardifier.control.Logger;
import boardifier.control.StageFactory;
import boardifier.view.View;
import control.PuissanceXController;
import control.ai.RandomAIDecider;
import model.PuissanceXModel;
import view.PuissanceXMenu;

public class PuissanceXConsole {
    public static void main(String[] args) {
        // Initialize boardifier logger settings
        Logger.setLevel(Logger.LOGGER_TRACE); 
        Logger.setVerbosity(Logger.VERBOSE_HIGH);

        Logger.info("Starting PuissanceX Console...");

        // Default values
        int winCondition = 4;
        int boardRows = 6;
        int boardCols = 7;
        int currentGameMode = 1;
        
        // If no arguments are provided, show the menu
        if (args.length == 0) {
            PuissanceXMenu menu = new PuissanceXMenu();
            boolean startGame = menu.showMenu();
            
            if (!startGame) {
                Logger.info("User chose to exit. Goodbye!");
                return;
            }
            
            // Get settings from menu
            winCondition = menu.getWinCondition();
            boardRows = menu.getBoardRows();
            boardCols = menu.getBoardCols();
            currentGameMode = menu.getGameMode();
            
            Logger.info("Starting game with menu settings:");
            Logger.info("  Win Condition: " + winCondition);
            Logger.info("  Board rows: " + boardRows);
            Logger.info("  Board columns: " + boardCols);
            Logger.info("  Game mode: " + currentGameMode);
        }
        else {
            try {
                if (args.length >= 1) {
                    winCondition = Integer.parseInt(args[0]);
                    Logger.debug("Parsed Win Condition argument: " + winCondition);
                }
                if (args.length >= 2) {
                    boardRows = Integer.parseInt(args[1]);
                    Logger.debug("Parsed rows argument: " + boardRows);
                }
                if (args.length >= 3) {
                    boardCols = Integer.parseInt(args[2]);
                    Logger.debug("Parsed columns argument: " + boardCols);
                }
                if (args.length >= 4) {
                    currentGameMode = Integer.parseInt(args[3]);
                    Logger.debug("Parsed mode argument: " + currentGameMode);
                }
            } catch (NumberFormatException e) {
                Logger.info("Error parsing arguments: " + e.getMessage() + ". Review arguments. Some or all parameters will use defaults.");
            }

            // Game Mode validation
            if (currentGameMode < 0 || currentGameMode > 2) {
                Logger.info("Parsed game mode (" + currentGameMode + ") is invalid. Setting to 0 (Human vs Human).");
                currentGameMode = 0;
            }

            Logger.info("Final effective game parameters set:");
            Logger.info("  Win Condition: " + winCondition);
            Logger.info("  Board rows: " + boardRows);
            Logger.info("  Board columns: " + boardCols);
            Logger.info("  Game mode: " + currentGameMode);
        }

        // Initialize model with parameters
        PuissanceXModel model = new PuissanceXModel();
        model.setWinCondition(winCondition);
        model.setBoardRows(boardRows);
        model.setBoardCols(boardCols);

        Logger.info("Setting up players for mode: " + currentGameMode);
        if (currentGameMode == 0) {
            model.addHumanPlayer("player1");
            model.addHumanPlayer("player2");
            Logger.debug("Added player1 (Human) and player2 (Human).");
        } else if (currentGameMode == 1) {
            model.addHumanPlayer("player");
            model.addComputerPlayer("computer");
            Logger.debug("Added player (Human) and computer (AI).");
        } else {
            model.addComputerPlayer("computer1");
            model.addComputerPlayer("computer2");
            Logger.debug("Added computer1 (AI) and computer2 (AI).");
        }

        Logger.info("Registering PuissanceX model and view with StageFactory.");
        StageFactory.registerModelAndView("puissanceX", "model.PuissanceXStageModel", "view.PuissanceXStageView");
        
        View gameView = new View(model); 
        Logger.debug("View object (boardifier.view.View) created.");

        PuissanceXController control = new PuissanceXController(model, gameView);
        Logger.debug("PuissanceXController object created.");
        
        // Set up AI decider if needed
        if (currentGameMode == 1 || currentGameMode == 2) {
            RandomAIDecider aiDecider = new RandomAIDecider(model, control);
            control.setAIDecider(aiDecider);
            Logger.debug("MinimaxAI created and set for AI players.");
        }
        
        control.setFirstStageName("puissanceX");
        Logger.debug("First stage name set to 'puissanceX'.");

        try {
            Logger.info("Attempting to start game...");
            control.startGame();
            Logger.info("Game started successfully. Entering stage loop.");
            control.stageLoop();
            Logger.info("Stage loop exited. Game ended.");
        } catch (Exception e) {
            Logger.info("An unexpected error occurred: " + e.getMessage() + ". Aborting.");
            Logger.info("ERROR_DETAILS: " + e);
        }
    }
} 