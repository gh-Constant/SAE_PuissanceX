package control;

import boardifier.control.ActionFactory;
import boardifier.control.ActionPlayer;
import boardifier.control.Controller;
import boardifier.control.Decider;
import boardifier.model.GameException;
import boardifier.model.Model;
import boardifier.model.Player;
import boardifier.model.action.ActionList;
import boardifier.view.View;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import model.PuissanceXBoard;
import model.PuissanceXDisk;
import model.PuissanceXStageModel;
import view.GameLauncher;

/**
 * JavaFX Controller for the PuissanceX game.
 * Handles game flow, JavaFX events, and interactions between model and view.
 */
public class PuissanceXJavaFXController extends Controller {

    public Decider aiDecider;
    public Decider secondAiDecider;
    private boolean gameEnded = false;
    private boolean inUpdate = false;
    private GameLauncher mainApp;

    /**
     * Constructor for the PuissanceX JavaFX controller.
     */
    public PuissanceXJavaFXController(Model model, View view, GameLauncher mainApp) {
        super(model, view);
        this.mainApp = mainApp;
        this.aiDecider = null;
        this.secondAiDecider = null;
    }
    
    public void setAIDecider(Decider decider) {
        this.aiDecider = decider;
    }

    public void setSecondAIDecider(Decider decider) {
        this.secondAiDecider = decider;
    }

    /**
     * Starts the game by initializing the model and view.
     */
    @Override
    public void startGame() {
        System.out.println("DEBUG: PuissanceXJavaFXController.startGame() called.");
        gameEnded = false;
        try {
            super.startGame();
            System.out.println("DEBUG: super.startGame() completed successfully.");
        } catch (GameException e) {
            System.err.println("ERROR in startGame: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Main game loop that handles player turns and game state.
     * For JavaFX, this is event-driven rather than loop-driven for human players.
     */
    public void stageLoop() {
        update();

        // Start the first turn
        if (!model.isEndStage() && !gameEnded) {
            playTurn();
        }
    }

    /**
     * Override update to track update state and prevent concurrent updates
     */
    @Override
    public void update() {
        if (inUpdate) {
            System.err.println("DEBUG: Skipping concurrent update");
            return;
        }

        inUpdate = true;
        try {
            super.update();
        } finally {
            inUpdate = false;
        }
    }

    /**
     * Continues the game after a move has been made.
     * This is called after human moves or AI moves complete.
     */
    public void continueGame() {
        if (!model.isEndStage() && !gameEnded) {
            endOfTurn();
            update();

            // Start the next turn if game is still ongoing
            if (!model.isEndStage() && !gameEnded) {
                playTurn();
            }
        }

        // Check if game has ended
        if (model.isEndStage() && !gameEnded) {
            update();
            displayGameResult();
        }
    }

    public void playTurn() {
        PuissanceXStageModel stageModel = (PuissanceXStageModel) model.getGameStage();
        PuissanceXBoard board = stageModel.getBoard();
        int currentPlayer = model.getIdPlayer();
        Player player = model.getPlayers().get(currentPlayer);
        
        System.out.println("DEBUG: Starting turn for Player " + (currentPlayer + 1) + " (" + model.getCurrentPlayerName() + ")");
        System.out.println("DEBUG: Player type: " + (player.getType() == Player.COMPUTER ? "Computer" : "Human"));
        
        // Check if current player is AI
        if (player.getType() == Player.COMPUTER) {
            System.out.println("DEBUG: AI turn - Player " + (currentPlayer + 1));
            
            // Use the appropriate AI decider based on the current player
            Decider currentDecider;
            if (currentPlayer == 0) {
                currentDecider = aiDecider;
                System.out.println("DEBUG: Using first AI decider: " + (currentDecider != null ? currentDecider.getClass().getSimpleName() : "null"));
            } else {
                currentDecider = secondAiDecider;
                System.out.println("DEBUG: Using second AI decider: " + (currentDecider != null ? currentDecider.getClass().getSimpleName() : "null"));
            }
            
            if (currentDecider != null) {
                // Add a small delay for AI moves to make them visible
                new Thread(() -> {
                    try {
                        System.out.println("DEBUG: AI " + (currentPlayer + 1) + " is thinking...");
                        Thread.sleep(1000); // 1 second delay
                        ActionList actions = currentDecider.decide();
                        if (actions != null) {
                            System.out.println("DEBUG: AI " + (currentPlayer + 1) + " made a decision");
                            actions.setDoEndOfTurn(false); // We'll handle end of turn manually
                            ActionPlayer actionPlayer = new ActionPlayer(model, this, actions);
                            actionPlayer.start();

                            // Wait for the action to complete before continuing
                            actionPlayer.join();
                            System.out.println("DEBUG: AI " + (currentPlayer + 1) + " action completed");

                            Platform.runLater(() -> {
                                checkGameEndConditions(board);
                                continueGame();
                            });
                        } else {
                            System.out.println("ERROR: AI " + (currentPlayer + 1) + " couldn't make a valid move!");
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        System.err.println("ERROR: AI " + (currentPlayer + 1) + " thread interrupted");
                    }
                }).start();
            } else {
                System.err.println("ERROR: AI decider not set for Player " + (currentPlayer + 1) + "!");
            }
        } else {
            // Human player's turn - wait for mouse click on the board
            System.out.println("DEBUG: Waiting for human player to make a move...");
        }
    }
    
    /**
     * Handles a human player's move when they click on a column
     */
    public void handleHumanMove(int col) {
        // Ensure this runs on the JavaFX Application Thread
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> handleHumanMove(col));
            return;
        }

        // Check if the game stage is initialized
        if (model.getGameStage() == null) {
            System.err.println("Game stage not initialized yet. Ignoring click.");
            return;
        }

        PuissanceXStageModel stageModel = (PuissanceXStageModel) model.getGameStage();
        if (stageModel == null) {
            System.err.println("Stage model is null. Ignoring click.");
            return;
        }

        PuissanceXBoard board = stageModel.getBoard();
        if (board == null) {
            System.err.println("Board is null. Ignoring click.");
            return;
        }
        int currentPlayer = model.getIdPlayer();

        // Check if it's actually a human player's turn
        Player player = model.getPlayers().get(currentPlayer);
        if (player.getType() != Player.HUMAN) {
            return; // Ignore clicks during AI turns
        }

        // Check if we're already in an update cycle to prevent concurrent updates
        if (inUpdate) {
            System.out.println("DEBUG: Ignoring click during update cycle");
            return;
        }

        try {
            // Set update flag to prevent concurrent updates
            inUpdate = true;

            // Check if the column is valid
            if (col < 0 || col >= board.getNbCols()) {
                showAlert("Invalid Move", "Invalid column number.");
                inUpdate = false;
                return;
            }

            // Check if the column is full
            if (board.isColumnFull(col)) {
                showAlert("Invalid Move", "Column " + (col + 1) + " is full. Try another one.");
                inUpdate = false;
                return;
            }

            // Find the first empty row in the column
            int row = board.getFirstEmptyRow(col);
            if (row == -1) {
                showAlert("Invalid Move", "Column " + (col + 1) + " is full. Try another one.");
                inUpdate = false;
                return;
            }

            // Create a new disk
            PuissanceXDisk disk = new PuissanceXDisk(currentPlayer, stageModel);

            // Create an action to place the disk
            ActionList actions = ActionFactory.generatePutInContainer(this, model, disk, "board", row, col);
            actions.setDoEndOfTurn(false); // We'll handle end of turn manually

            // Play the action synchronously
            ActionPlayer actionPlayer = new ActionPlayer(model, this, actions);
            actionPlayer.start();

            // Wait for the action to complete in a separate thread
            Thread actionThread = new Thread(() -> {
                try {
                    actionPlayer.join(); // Wait for the action to complete
                    
                    // Ensure UI updates happen on the JavaFX thread
                    Platform.runLater(() -> {
                        try {
                            // Check for win or draw after the move is complete
                            checkGameEndConditions(board);
                            // Continue the game
                            continueGame();
                        } finally {
                            // Always reset the update flag, even if an error occurs
                            inUpdate = false;
                        }
                    });
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("Interrupted while waiting for action to complete");
                    Platform.runLater(() -> inUpdate = false);
                } catch (Exception e) {
                    System.err.println("Error during action execution: " + e.getMessage());
                    e.printStackTrace();
                    Platform.runLater(() -> inUpdate = false);
                }
            });
            actionThread.start();

        } catch (Exception e) {
            System.err.println("Error handling human move: " + e.getMessage());
            e.printStackTrace();
            inUpdate = false;
        }
    }
    
    private void checkGameEndConditions(PuissanceXBoard board) {
        PuissanceXStageModel stageModel = (PuissanceXStageModel) model.getGameStage();
        int currentPlayer = model.getIdPlayer();
        
        // Check for win - this assumes the last move was at the last filled position
        for (int col = 0; col < board.getNbCols(); col++) {
            int row = board.getFirstEmptyRow(col);
            if (row < board.getNbRows() - 1) {  // If there's a piece below the empty spot
                if (stageModel.checkWin(row + 1, col, currentPlayer)) {
                    model.setIdWinner(currentPlayer);
                    model.stopStage();
                    return;
                }
            }
        }
        
        // Check for draw
        if (stageModel.isBoardFull()) {
            model.stopStage();
        }
    }
    
    @Override
    public void endOfTurn() {
        model.setNextPlayer();
        
        // Update the player text
        PuissanceXStageModel stageModel = (PuissanceXStageModel) model.getGameStage();
        stageModel.getPlayerText().setText("Current player: " + model.getCurrentPlayerName());
    }
    
    private void displayGameResult() {
        gameEnded = true;
        int winnerId = model.getIdWinner();
        
        Platform.runLater(() -> {
            String message;
            if (winnerId != -1) {
                message = "Player " + (winnerId + 1) + " (" + model.getPlayers().get(winnerId).getName() + ") wins!";
            } else {
                message = "Game ended in a draw!";
            }
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Game Over");
            alert.setHeaderText("Game Result");
            alert.setContentText(message);
            
            ButtonType playAgainButton = new ButtonType("Play Again");
            ButtonType mainMenuButton = new ButtonType("Main Menu");
            ButtonType exitButton = new ButtonType("Exit");
            
            alert.getButtonTypes().setAll(playAgainButton, mainMenuButton, exitButton);
            
            alert.showAndWait().ifPresent(response -> {
                if (response == playAgainButton) {
                    // Restart the game with same settings
                    restartGame();
                } else if (response == mainMenuButton) {
                    // Return to main menu
                    returnToMainMenu();
                } else if (response == exitButton) {
                    // Exit application
                    System.exit(0);
                }
            });
        });
    }
    
    private void restartGame() {
        try {
            // Reset the model and restart
            model.reset();
            startGame();
            stageLoop();
        } catch (Exception e) {
            System.err.println("Error restarting game: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void returnToMainMenu() {
        Platform.runLater(() -> {
            if (mainApp != null) {
                mainApp.showMainMenu();
            }
        });
    }
    
    /**
     * Shows an alert dialog with the specified title and message
     */
    private void showAlert(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
    
    /**
     * Stops the current game
     */
    public void stopCurrentGame() {
        gameEnded = true;
        model.stopStage();
    }
}
