package view;

import boardifier.view.RootPane;
import control.PuissanceXJavaFXController;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import model.PuissanceXBoard;
import model.PuissanceXStageModel;

/**
 * Custom root pane for PuissanceX JavaFX game.
 * Handles mouse clicks to allow players to select columns.
 */
public class PuissanceXRootPane extends RootPane {
    
    private PuissanceXJavaFXController controller;
    private static final double CELL_SIZE = 132.0;  // Agrandi chaque case de 10% vers la droite
    private static final double BOARD_MARGIN = 80.0;
    private static final double WINDOW_WIDTH = 1600.0;  // Much larger window
    private static final double WINDOW_HEIGHT = 1000.0;  // Much larger window
    private static final double POT_WIDTH = 200.0;
    private static final double POT_HEIGHT = 300.0;
    
    public PuissanceXRootPane(PuissanceXJavaFXController controller) {
        super();
        this.controller = controller;
        setupMouseHandling();
    }

    /**
     * Sets the controller for this root pane
     */
    public void setController(PuissanceXJavaFXController controller) {
        this.controller = controller;
    }
    
    /**
     * Sets up mouse event handling for column selection
     */
    private void setupMouseHandling() {
        setOnMouseClicked(this::handleMouseClick);
    }
    
    /**
     * Handles mouse clicks to determine which column was clicked
     */
    private void handleMouseClick(MouseEvent event) {
        if (controller == null || gameStageView == null) {
            return;
        }
        
        try {
            int defaultCols = 7;
            int defaultRows = 6;

            // Calculate which column was clicked
            double mouseX = event.getX();
            double mouseY = event.getY();

            // Calculate the board's position and dimensions with new layout
            double boardStartX = BOARD_MARGIN - (CELL_SIZE / 3.0);
            double boardStartY = BOARD_MARGIN + 80;
            double boardWidth = defaultCols * CELL_SIZE;
            double boardHeight = defaultRows * CELL_SIZE;

            // Debug information about click position
            System.out.println("\n=== DEBUG CLICK POSITION ===");
            System.out.println("Mouse click at (" + mouseX + ", " + mouseY + ")");
            System.out.println("Board starts at X: " + boardStartX);
            System.out.println("Board width: " + boardWidth);
            System.out.println("Cell size: " + CELL_SIZE);
            
            // Calculate and display column boundaries
            for (int i = 0; i <= defaultCols; i++) {
                double colBoundary = boardStartX + (i * CELL_SIZE);
                System.out.println("Column " + i + " boundary at X: " + colBoundary);
            }

            // Check if click is within the board area
            if (mouseX >= boardStartX && mouseX <= boardStartX + boardWidth &&
                mouseY >= boardStartY - 40 && mouseY <= boardStartY + boardHeight) {

                // Calculate the column index with improved precision
                double relativeX = mouseX - boardStartX;
                System.out.println("Relative X position: " + relativeX);
                System.out.println("Relative X in cells: " + (relativeX / CELL_SIZE));
                
                // Calculate column with different methods for comparison
                int colMethod1 = (int) Math.floor(relativeX / CELL_SIZE);
                int colMethod2 = (int) Math.round(relativeX / CELL_SIZE);
                int colMethod3 = (int) ((relativeX + CELL_SIZE/2) / CELL_SIZE);
                
                System.out.println("Column calculation methods:");
                System.out.println("Method 1 (floor): " + colMethod1);
                System.out.println("Method 2 (round): " + colMethod2);
                System.out.println("Method 3 (center): " + colMethod3);
                
                // Use the most appropriate method (we'll adjust this based on your feedback)
                int col = colMethod3; // Currently using center-based calculation
                
                // Ensure column is within valid range
                if (col >= 0 && col < defaultCols) {
                    System.out.println("Selected column: " + (col + 1));
                    controller.handleHumanMove(col);
                } else {
                    System.out.println("Invalid column calculated: " + col);
                }
            } else {
                System.out.println("Click outside board area");
            }
            System.out.println("=== END DEBUG ===\n");
            
        } catch (Exception e) {
            System.err.println("Error handling mouse click: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Creates a visual representation of the game board for mouse interaction
     */
    @Override
    protected void createDefaultGroup() {
        super.createDefaultGroup();

        // Clear the default content
        group.getChildren().clear();

        // Add a background with improved layout
        Rectangle background = new Rectangle(WINDOW_WIDTH, WINDOW_HEIGHT, Color.LIGHTSTEELBLUE);
        group.getChildren().add(background);

        // Add visual indicators for columns (this will be enhanced when the game starts)
        createColumnIndicators();

        // Create placeholder pots
        createPlaceholderPots();
    }
    
    /**
     * Creates visual indicators for the columns
     */
    private void createColumnIndicators() {
        // This will be called when we have access to the board dimensions
        // For now, create a default 7-column layout
        int defaultCols = 7;
        int defaultRows = 6;
        
        double boardStartX = BOARD_MARGIN;
        double boardStartY = BOARD_MARGIN + 80; // Updated to match new layout

        // Add title
        javafx.scene.text.Text title = new javafx.scene.text.Text("PuissanceX Game");
        title.setX(WINDOW_WIDTH / 2 - 120);
        title.setY(50);
        title.setFont(javafx.scene.text.Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 36));
        title.setFill(Color.DARKBLUE);
        group.getChildren().add(title);

        // Create board background
        Rectangle boardBackground = new Rectangle(
            defaultCols * CELL_SIZE + 30,
            defaultRows * CELL_SIZE + 80,
            Color.DARKBLUE
        );
        boardBackground.setX(boardStartX - 15);
        boardBackground.setY(boardStartY - 50);
        boardBackground.setArcWidth(15);
        boardBackground.setArcHeight(15);
        group.getChildren().add(boardBackground);

        // Create column headers with numbers
        for (int col = 0; col < defaultCols; col++) {
            Rectangle columnHeader = new Rectangle(CELL_SIZE, 40, Color.LIGHTGRAY);
            columnHeader.setX(boardStartX + col * CELL_SIZE);
            columnHeader.setY(boardStartY - 40);
            columnHeader.setStroke(Color.BLACK);
            columnHeader.setStrokeWidth(2);
            columnHeader.setArcWidth(8);
            columnHeader.setArcHeight(8);
            group.getChildren().add(columnHeader);

            // Add column number text
            javafx.scene.text.Text colText = new javafx.scene.text.Text(String.valueOf(col + 1));
            colText.setX(boardStartX + col * CELL_SIZE + CELL_SIZE/2 - 10);
            colText.setY(boardStartY - 15);
            colText.setFont(javafx.scene.text.Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 24));
            colText.setFill(Color.BLACK);
            group.getChildren().add(colText);
        }
        
        // Create board grid
        for (int row = 0; row < defaultRows; row++) {
            for (int col = 0; col < defaultCols; col++) {
                Rectangle cell = new Rectangle(CELL_SIZE, CELL_SIZE, Color.WHITE);
                cell.setX(boardStartX + col * CELL_SIZE);
                cell.setY(boardStartY + row * CELL_SIZE);
                cell.setStroke(Color.BLACK);
                cell.setStrokeWidth(3);
                cell.setArcWidth(10);
                cell.setArcHeight(10);
                group.getChildren().add(cell);
            }
        }

        // Create placeholder pots
        createPlaceholderPots();
    }
    
    /**
     * Updates the visual board based on the current game state
     */
    public void updateBoard(PuissanceXBoard board) {
        if (board == null) return;

        // Clear existing content
        group.getChildren().clear();

        // Add main background
        Rectangle background = new Rectangle(WINDOW_WIDTH, WINDOW_HEIGHT, Color.LIGHTSTEELBLUE);
        group.getChildren().add(background);

        // Calculate board position (left side)
        double boardStartX = BOARD_MARGIN;
        double boardStartY = BOARD_MARGIN + 80; // Leave space for title

        // Add title
        javafx.scene.text.Text title = new javafx.scene.text.Text("PuissanceX Game");
        title.setX(WINDOW_WIDTH / 2 - 120);
        title.setY(50);
        title.setFont(javafx.scene.text.Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 36));
        title.setFill(Color.DARKBLUE);
        group.getChildren().add(title);

        // Create board background
        Rectangle boardBackground = new Rectangle(
            board.getNbCols() * CELL_SIZE + 30,
            board.getNbRows() * CELL_SIZE + 80,
            Color.DARKBLUE
        );
        boardBackground.setX(boardStartX - 15);
        boardBackground.setY(boardStartY - 50);
        boardBackground.setArcWidth(15);
        boardBackground.setArcHeight(15);
        group.getChildren().add(boardBackground);

        // Create column headers with numbers
        for (int col = 0; col < board.getNbCols(); col++) {
            Rectangle columnHeader = new Rectangle(CELL_SIZE, 40, Color.LIGHTGRAY);
            columnHeader.setX(boardStartX + col * CELL_SIZE);
            columnHeader.setY(boardStartY - 40);
            columnHeader.setStroke(Color.BLACK);
            columnHeader.setStrokeWidth(2);
            columnHeader.setArcWidth(8);
            columnHeader.setArcHeight(8);
            group.getChildren().add(columnHeader);

            // Add column number text
            javafx.scene.text.Text colText = new javafx.scene.text.Text(String.valueOf(col + 1));
            colText.setX(boardStartX + col * CELL_SIZE + CELL_SIZE/2 - 10);
            colText.setY(boardStartY - 15);
            colText.setFont(javafx.scene.text.Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 24));
            colText.setFill(Color.BLACK);
            group.getChildren().add(colText);
        }

        // Create board cells
        for (int row = 0; row < board.getNbRows(); row++) {
            for (int col = 0; col < board.getNbCols(); col++) {
                Rectangle cell = new Rectangle(CELL_SIZE, CELL_SIZE, Color.WHITE);
                cell.setX(boardStartX + col * CELL_SIZE);
                cell.setY(boardStartY + row * CELL_SIZE);
                cell.setStroke(Color.BLACK);
                cell.setStrokeWidth(3);
                cell.setArcWidth(10);
                cell.setArcHeight(10);
                group.getChildren().add(cell);
            }
        }

        // Create pots on the right side
        createGamePots(board);
    }
    
    /**
     * Gets the cell size for positioning calculations
     */
    public static double getCellSize() {
        return CELL_SIZE;
    }
    
    /**
     * Gets the board margin for positioning calculations
     */
    public static double getBoardMargin() {
        return BOARD_MARGIN;
    }

    /**
     * Creates placeholder pots for the initial display
     */
    private void createPlaceholderPots() {
        double potStartX = WINDOW_WIDTH - POT_WIDTH - 80;
        double pot1Y = 200;
        double pot2Y = 550;

        // Red pot (Player 1)
        Rectangle redPot = new Rectangle(POT_WIDTH, POT_HEIGHT, Color.LIGHTCORAL);
        redPot.setX(potStartX);
        redPot.setY(pot1Y);
        redPot.setStroke(Color.DARKRED);
        redPot.setStrokeWidth(3);
        redPot.setArcWidth(15);
        redPot.setArcHeight(15);
        group.getChildren().add(redPot);

        javafx.scene.text.Text redLabel = new javafx.scene.text.Text("Player 1\n(Red)");
        redLabel.setX(potStartX + 60);
        redLabel.setY(pot1Y + POT_HEIGHT/2);
        redLabel.setFont(javafx.scene.text.Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 20));
        redLabel.setFill(Color.DARKRED);
        redLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        group.getChildren().add(redLabel);

        // Yellow pot (Player 2)
        Rectangle yellowPot = new Rectangle(POT_WIDTH, POT_HEIGHT, Color.LIGHTYELLOW);
        yellowPot.setX(potStartX);
        yellowPot.setY(pot2Y);
        yellowPot.setStroke(Color.ORANGE);
        yellowPot.setStrokeWidth(3);
        yellowPot.setArcWidth(15);
        yellowPot.setArcHeight(15);
        group.getChildren().add(yellowPot);

        javafx.scene.text.Text yellowLabel = new javafx.scene.text.Text("Player 2\n(Yellow)");
        yellowLabel.setX(potStartX + 45);
        yellowLabel.setY(pot2Y + POT_HEIGHT/2);
        yellowLabel.setFont(javafx.scene.text.Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 20));
        yellowLabel.setFill(Color.DARKORANGE);
        yellowLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        group.getChildren().add(yellowLabel);
    }

    /**
     * Creates the game pots during actual gameplay
     */
    private void createGamePots(PuissanceXBoard board) {
        double potStartX = WINDOW_WIDTH - POT_WIDTH - 80;
        double pot1Y = 200;
        double pot2Y = 550;

        // Red pot (Player 1)
        Rectangle redPot = new Rectangle(POT_WIDTH, POT_HEIGHT, Color.LIGHTCORAL);
        redPot.setX(potStartX);
        redPot.setY(pot1Y);
        redPot.setStroke(Color.DARKRED);
        redPot.setStrokeWidth(3);
        redPot.setArcWidth(15);
        redPot.setArcHeight(15);
        group.getChildren().add(redPot);

        javafx.scene.text.Text redLabel = new javafx.scene.text.Text("Player 1\n(Red)");
        redLabel.setX(potStartX + 40);
        redLabel.setY(pot1Y + 30);
        redLabel.setFont(javafx.scene.text.Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 14));
        redLabel.setFill(Color.DARKRED);
        redLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        group.getChildren().add(redLabel);

        // Yellow pot (Player 2)
        Rectangle yellowPot = new Rectangle(POT_WIDTH, POT_HEIGHT, Color.LIGHTYELLOW);
        yellowPot.setX(potStartX);
        yellowPot.setY(pot2Y);
        yellowPot.setStroke(Color.ORANGE);
        yellowPot.setStrokeWidth(3);
        yellowPot.setArcWidth(15);
        yellowPot.setArcHeight(15);
        group.getChildren().add(yellowPot);

        javafx.scene.text.Text yellowLabel = new javafx.scene.text.Text("Player 2\n(Yellow)");
        yellowLabel.setX(potStartX + 30);
        yellowLabel.setY(pot2Y + 30);
        yellowLabel.setFont(javafx.scene.text.Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 14));
        yellowLabel.setFill(Color.DARKORANGE);
        yellowLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        group.getChildren().add(yellowLabel);

        // Add some sample disks in the pots to show they're available
        for (int i = 0; i < 3; i++) {
            // Red disks
            javafx.scene.shape.Circle redDisk = new javafx.scene.shape.Circle(15, Color.RED);
            redDisk.setCenterX(potStartX + 30 + (i % 3) * 35);
            redDisk.setCenterY(pot1Y + 70 + (i / 3) * 35);
            redDisk.setStroke(Color.DARKRED);
            redDisk.setStrokeWidth(2);
            group.getChildren().add(redDisk);

            // Yellow disks
            javafx.scene.shape.Circle yellowDisk = new javafx.scene.shape.Circle(15, Color.YELLOW);
            yellowDisk.setCenterX(potStartX + 30 + (i % 3) * 35);
            yellowDisk.setCenterY(pot2Y + 70 + (i / 3) * 35);
            yellowDisk.setStroke(Color.ORANGE);
            yellowDisk.setStrokeWidth(2);
            group.getChildren().add(yellowDisk);
        }
    }
}
