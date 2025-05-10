package control;

import boardifier.control.ActionFactory;
import boardifier.control.Controller;
import boardifier.model.GameException;
import boardifier.model.Model;
import boardifier.view.View;
import model.Board;
import model.Disk;
import model.PuissanceXStageModel;

import java.util.Scanner;

/**
 * Controller for the PuissanceX game.
 * Handles game flow, user input, and interactions between model and view.
 */
public class PuissanceXController extends Controller {

    private Scanner scanner;

    /**
     * Constructor for the PuissanceX controller.
     */
    public PuissanceXController(Model model, View view) {
        super(model, view);
        this.scanner = new Scanner(System.in);
    }

    /**
     * Starts the game by initializing the model and view.
     */
    @Override
    public void startGame() {
        System.out.println("DEBUG: PuissanceXController.startGame() called.");
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
     */
    @Override
    public void stageLoop() {
        update();
        
        while (!model.isEndStage()) {
            playTurn();
            if (!model.isEndStage()) {
                endOfTurn();
                update();
            }
        }
        
        // Game over
        displayGameResult();
    }
    
    private void playTurn() {
        PuissanceXStageModel stageModel = (PuissanceXStageModel) model.getGameStage();
        Board board = stageModel.getBoard();
        int currentPlayer = model.getIdPlayer();
        
        System.out.println("Player " + (currentPlayer + 1) + " (" + model.getCurrentPlayerName() + "), your turn.");
        System.out.println("Enter column number (0-" + (board.getNbCols() - 1) + "): ");
        
        boolean validMove = false;
        while (!validMove) {
            try {
                int col = Integer.parseInt(scanner.nextLine());
                
                if (col < 0 || col >= board.getNbCols()) {
                    System.out.println("Invalid column. Try again.");
                    continue;
                }
                
                if (board.isColumnFull(col)) {
                    System.out.println("Column is full. Try again.");
                    continue;
                }
                
                // Find the first empty row in the column
                int row = board.getFirstEmptyRow(col);
                
                // Create a new disk
                Disk disk = new Disk(currentPlayer, stageModel);
                
                // Create an action to place the disk
                boardifier.model.action.ActionList actions = ActionFactory.generatePutInContainer(model, disk, "board", row, col);
                actions.setDoEndOfTurn(true);
                
                // Play the action
                boardifier.control.ActionPlayer actionPlayer = new boardifier.control.ActionPlayer(model, this, actions);
                actionPlayer.start();
                
                // Check for win
                if (stageModel.checkWin(row, col, currentPlayer)) {
                    model.setIdWinner(currentPlayer);
                    model.stopStage();
                }
                // Check for draw
                else if (stageModel.isBoardFull()) {
                    model.stopStage();
                }
                
                validMove = true;
                
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
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
        int winnerId = model.getIdWinner();
        
        if (winnerId != -1) {
            System.out.println("Player " + (winnerId + 1) + " (" + model.getPlayers().get(winnerId).getName() + ") wins!");
        } else {
            System.out.println("Game ended in a draw!");
        }
    }
}