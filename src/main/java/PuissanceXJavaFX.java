import javafx.application.Application;
import javafx.stage.Stage;
import boardifier.control.Logger;
import boardifier.control.StageFactory;
import boardifier.view.RootPane;
import boardifier.view.View;
import control.PuissanceXJavaFXController;
import model.PuissanceXModel;
import view.PuissanceXJavaFXMenu;
import view.PuissanceXRootPane;
import view.GameLauncher;

/**
 * JavaFX Application for PuissanceX game.
 * This class serves as the entry point for the JavaFX version of the game.
 */
public class PuissanceXJavaFX extends Application implements GameLauncher {
    
    private PuissanceXModel model;
    private View gameView;
    private PuissanceXJavaFXController controller;
    private PuissanceXJavaFXMenu menuManager;
    
    @Override
    public void start(Stage primaryStage) {
        // Initialize boardifier logger settings
        Logger.setLevel(Logger.LOGGER_TRACE);
        Logger.setVerbosity(Logger.VERBOSE_HIGH);
        
        Logger.info("Starting PuissanceX JavaFX...");
        
        // Set up the primary stage
        primaryStage.setTitle("PuissanceX - JavaFX Edition");
        primaryStage.setResizable(false);
        primaryStage.setWidth(1600);
        primaryStage.setHeight(1000);

        // Initialize the model
        model = new PuissanceXModel();

        // Create the custom root pane first (we'll set the controller later)
        PuissanceXRootPane rootPane = new PuissanceXRootPane(null);

        // Create the view with JavaFX components
        gameView = new View(model, primaryStage, rootPane);

        // Create the controller with the proper view
        controller = new PuissanceXJavaFXController(model, gameView, this);

        // Now set the controller in the root pane
        rootPane.setController(controller);
        
        // Create the menu manager
        menuManager = new PuissanceXJavaFXMenu(primaryStage, this);
        
        // Show the main menu
        showMainMenu();
        
        // Show the stage
        primaryStage.show();
    }
    
    /**
     * Shows the main menu interface
     */
    public void showMainMenu() {
        Logger.info("Showing main menu");
        menuManager.showMainMenu();
    }
    
    /**
     * Starts a new game with the specified settings
     */
    public void startGame(int winCondition, int boardRows, int boardCols, int gameMode, int aiType1, int aiType2) {
        Logger.info("Starting game with settings:");
        Logger.info("  Win Condition: " + winCondition);
        Logger.info("  Board rows: " + boardRows);
        Logger.info("  Board columns: " + boardCols);
        Logger.info("  Game mode: " + gameMode);
        
        try {
            // Reset the model for a new game
            model.reset();
            
            // Set game parameters
            model.setWinCondition(winCondition);
            model.setBoardRows(boardRows);
            model.setBoardCols(boardCols);
            
            // Set up players based on game mode
            setupPlayers(gameMode);
            
            // Set up AI deciders
            setupAI(gameMode, aiType1, aiType2);
            
            // Register the stage model and view
            Logger.info("Registering PuissanceX model and view with StageFactory.");
            StageFactory.registerModelAndView("puissanceX", "model.PuissanceXStageModel", "view.PuissanceXStageView");
            
            // Set the first stage name
            controller.setFirstStageName("puissanceX");
            
            // Start the game
            controller.startGame();
            controller.stageLoop();
            
        } catch (Exception e) {
            Logger.info("An error occurred while starting the game: " + e.getMessage());
            e.printStackTrace();
            showMainMenu(); // Return to main menu on error
        }
    }
    
    /**
     * Sets up players based on the game mode
     */
    private void setupPlayers(int gameMode) {
        Logger.info("Setting up players for mode: " + gameMode);
        
        // Correction : vider la liste des joueurs avant d'en ajouter de nouveaux
        model.getPlayers().clear();
        
        switch (gameMode) {
            case 1: // Human vs Human
                model.addHumanPlayer("Player 1");
                model.addHumanPlayer("Player 2");
                Logger.debug("Added Player 1 (Human) and Player 2 (Human).");
                break;
            case 2: // Human vs Computer
                model.addHumanPlayer("Player");
                model.addComputerPlayer("Computer");
                Logger.debug("Added Player (Human) and Computer (AI).");
                break;
            case 3: // Computer vs Computer
                model.addComputerPlayer("Computer 1");
                model.addComputerPlayer("Computer 2");
                Logger.debug("Added Computer 1 (AI) and Computer 2 (AI).");
                break;
            default:
                throw new IllegalArgumentException("Invalid game mode: " + gameMode);
        }
    }
    
    /**
     * Sets up AI deciders based on the game mode and AI types
     */
    private void setupAI(int gameMode, int aiType1, int aiType2) {
        if (gameMode == 2 || gameMode == 3) {
            // Import AI classes
            try {
                if (gameMode == 2) {
                    // For Human vs Computer, the AI is the second player
                    controller.setSecondAIDecider(createAIDecider(aiType1));
                    Logger.debug("AI decider set for computer player (player 1)");
                } else if (gameMode == 3) {
                    // For AI vs AI, configure both AIs
                    controller.setAIDecider(createAIDecider(aiType1));
                    controller.setSecondAIDecider(createAIDecider(aiType2));
                    Logger.debug("AI deciders set for both AI players");
                }
            } catch (Exception e) {
                Logger.info("Error setting up AI: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Creates an AI decider based on the AI type
     */
    private boardifier.control.Decider createAIDecider(int aiType) {
        switch (aiType) {
            case 1:
                return new control.ai.Minimax(model, controller);
            case 2:
                return new control.ai.RandomAIDecider(model, controller);
            case 3:
                return new control.ai.ConditionAI(model, controller);
            default:
                Logger.info("Unknown AI type: " + aiType + ". Using Minimax as default.");
                return new control.ai.Minimax(model, controller);
        }
    }
    
    /**
     * Called when the game ends to show end game options
     */
    public void onGameEnd() {
        Logger.info("Game ended, showing end game menu");
        menuManager.showEndGameMenu();
    }
    
    /**
     * Exits the application
     */
    public void exitApplication() {
        Logger.info("Exiting PuissanceX JavaFX application");
        System.exit(0);
    }
    
    /**
     * Main method to launch the JavaFX application
     */
    public static void main(String[] args) {
        launch(args);
    }
}
