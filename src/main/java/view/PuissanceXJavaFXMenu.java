package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import boardifier.control.Logger;

/**
 * JavaFX Menu system for PuissanceX game.
 * Provides a graphical interface for configuring game settings.
 */
public class PuissanceXJavaFXMenu {
    
    private Stage primaryStage;
    private GameLauncher mainApp;
    
    // Game settings
    private int winCondition = 4;
    private int boardRows = 6;
    private int boardCols = 7;
    private int gameMode = 1;
    private int aiType1 = 1; // Default to Minimax
    private int aiType2 = 1; // Default to Minimax
    
    // UI Components
    private ComboBox<String> gameModeCombo;
    private ComboBox<String> aiType1Combo;
    private ComboBox<String> aiType2Combo;
    private Spinner<Integer> rowsSpinner;
    private Spinner<Integer> colsSpinner;
    private Spinner<Integer> winConditionSpinner;
    private Label aiType1Label;
    private Label aiType2Label;
    
    public PuissanceXJavaFXMenu(Stage primaryStage, GameLauncher mainApp) {
        this.primaryStage = primaryStage;
        this.mainApp = mainApp;
    }
    
    /**
     * Shows the main menu interface
     */
    public void showMainMenu() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #f0f0f0;");
        
        // Title
        Label titleLabel = new Label("PUISSANCE X");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        titleLabel.setStyle("-fx-text-fill: #2c3e50;");
        
        // Settings panel
        VBox settingsPanel = createSettingsPanel();
        
        // Conservation des valeurs personnalisées lors du retour au menu
        if (rowsSpinner != null && colsSpinner != null && winConditionSpinner != null) {
            rowsSpinner.getValueFactory().setValue(boardRows);
            colsSpinner.getValueFactory().setValue(boardCols);
            winConditionSpinner.getValueFactory().setValue(winCondition);
        }
        
        // Buttons
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        
        Button startButton = new Button("Start Game");
        startButton.setPrefSize(120, 40);
        startButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
        startButton.setOnAction(e -> startGame());
        
        Button exitButton = new Button("Exit");
        exitButton.setPrefSize(120, 40);
        exitButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
        exitButton.setOnAction(e -> mainApp.exitApplication());
        
        buttonBox.getChildren().addAll(startButton, exitButton);
        
        root.getChildren().addAll(titleLabel, settingsPanel, buttonBox);
        
        Scene scene = new Scene(root, 500, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("PuissanceX - Main Menu");
    }
    
    /**
     * Creates the settings panel with all game configuration options
     */
    private VBox createSettingsPanel() {
        VBox panel = new VBox(15);
        panel.setAlignment(Pos.CENTER_LEFT);
        panel.setStyle("-fx-background-color: white; -fx-border-color: #bdc3c7; -fx-border-width: 1; -fx-border-radius: 5; -fx-background-radius: 5;");
        panel.setPadding(new Insets(20));
        
        Label settingsTitle = new Label("Game Settings");
        settingsTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        settingsTitle.setStyle("-fx-text-fill: #2c3e50;");
        
        // Game Mode
        HBox gameModeBox = new HBox(10);
        gameModeBox.setAlignment(Pos.CENTER_LEFT);
        Label gameModeLabel = new Label("Game Mode:");
        gameModeLabel.setPrefWidth(120);
        gameModeCombo = new ComboBox<>();
        gameModeCombo.getItems().addAll("Human vs Human", "Human vs Computer", "Computer vs Computer");
        gameModeCombo.setValue("Human vs Human");
        gameModeCombo.setOnAction(e -> updateGameMode());
        gameModeBox.getChildren().addAll(gameModeLabel, gameModeCombo);
        
        // AI Type 1
        HBox aiType1Box = new HBox(10);
        aiType1Box.setAlignment(Pos.CENTER_LEFT);
        aiType1Label = new Label("AI Type 1:");
        aiType1Label.setPrefWidth(120);
        aiType1Combo = new ComboBox<>();
        aiType1Combo.getItems().addAll("Minimax", "Random", "Condition");
        aiType1Combo.setValue("Minimax");
        aiType1Box.getChildren().addAll(aiType1Label, aiType1Combo);
        
        // AI Type 2
        HBox aiType2Box = new HBox(10);
        aiType2Box.setAlignment(Pos.CENTER_LEFT);
        aiType2Label = new Label("AI Type 2:");
        aiType2Label.setPrefWidth(120);
        aiType2Combo = new ComboBox<>();
        aiType2Combo.getItems().addAll("Minimax", "Random", "Condition");
        aiType2Combo.setValue("Minimax");
        aiType2Box.getChildren().addAll(aiType2Label, aiType2Combo);
        
        // Board Rows
        HBox rowsBox = new HBox(10);
        rowsBox.setAlignment(Pos.CENTER_LEFT);
        Label rowsLabel = new Label("Board Rows:");
        rowsLabel.setPrefWidth(120);
        rowsSpinner = new Spinner<>(4, 10, 6);
        rowsSpinner.setEditable(true);
        rowsSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
            boardRows = newVal;
            updateWinConditionMax();
        });
        rowsBox.getChildren().addAll(rowsLabel, rowsSpinner);
        
        // Board Columns
        HBox colsBox = new HBox(10);
        colsBox.setAlignment(Pos.CENTER_LEFT);
        Label colsLabel = new Label("Board Columns:");
        colsLabel.setPrefWidth(120);
        colsSpinner = new Spinner<>(4, 10, 7);
        colsSpinner.setEditable(true);
        colsSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
            boardCols = newVal;
            updateWinConditionMax();
        });
        colsBox.getChildren().addAll(colsLabel, colsSpinner);
        
        // Win Condition
        HBox winBox = new HBox(10);
        winBox.setAlignment(Pos.CENTER_LEFT);
        Label winLabel = new Label("Win Condition:");
        winLabel.setPrefWidth(120);
        winConditionSpinner = new Spinner<>(3, Math.min(boardRows, boardCols), 4);
        winConditionSpinner.setEditable(true);
        winConditionSpinner.valueProperty().addListener((obs, oldVal, newVal) -> winCondition = newVal);
        winBox.getChildren().addAll(winLabel, winConditionSpinner);
        
        panel.getChildren().addAll(settingsTitle, gameModeBox, aiType1Box, aiType2Box, rowsBox, colsBox, winBox);
        
        // Initially hide AI options for Human vs Human mode
        updateGameMode();
        
        return panel;
    }
    
    /**
     * Updates the game mode and shows/hides AI options accordingly
     */
    private void updateGameMode() {
        String selectedMode = gameModeCombo.getValue();
        
        switch (selectedMode) {
            case "Human vs Human":
                gameMode = 1;
                aiType1Label.setVisible(false);
                aiType1Combo.setVisible(false);
                aiType2Label.setVisible(false);
                aiType2Combo.setVisible(false);
                break;
            case "Human vs Computer":
                gameMode = 2;
                aiType1Label.setText("Computer AI:");
                aiType1Label.setVisible(true);
                aiType1Combo.setVisible(true);
                aiType2Label.setVisible(false);
                aiType2Combo.setVisible(false);
                break;
            case "Computer vs Computer":
                gameMode = 3;
                aiType1Label.setText("AI Type 1:");
                aiType1Label.setVisible(true);
                aiType1Combo.setVisible(true);
                aiType2Label.setVisible(true);
                aiType2Combo.setVisible(true);
                break;
        }
    }
    
    /**
     * Updates the maximum value for win condition based on board size
     */
    private void updateWinConditionMax() {
        int maxWin = Math.min(boardRows, boardCols);
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(3, maxWin, Math.min(winCondition, maxWin));
        winConditionSpinner.setValueFactory(valueFactory);
        winCondition = winConditionSpinner.getValue();
    }
    
    /**
     * Starts the game with current settings
     */
    private void startGame() {
        // Get AI types
        aiType1 = getAITypeIndex(aiType1Combo.getValue());
        aiType2 = getAITypeIndex(aiType2Combo.getValue());
        
        Logger.info("Starting game with settings from JavaFX menu:");
        Logger.info("  Game Mode: " + gameMode);
        Logger.info("  Board Size: " + boardRows + "x" + boardCols);
        Logger.info("  Win Condition: " + winCondition);
        Logger.info("  AI Type 1: " + aiType1);
        Logger.info("  AI Type 2: " + aiType2);
        
        mainApp.startGame(winCondition, boardRows, boardCols, gameMode, aiType1, aiType2);
    }
    
    /**
     * Converts AI type string to index
     */
    private int getAITypeIndex(String aiType) {
        switch (aiType) {
            case "Minimax": return 1;
            case "Random": return 2;
            case "Condition": return 3;
            default: return 1;
        }
    }
    
    /**
     * Shows the end game menu with options to replay or return to main menu
     */
    public void showEndGameMenu() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText("What would you like to do?");
        
        ButtonType replayButton = new ButtonType("Play Again");
        ButtonType menuButton = new ButtonType("Main Menu");
        ButtonType exitButton = new ButtonType("Exit");
        
        alert.getButtonTypes().setAll(replayButton, menuButton, exitButton);
        
        alert.showAndWait().ifPresent(response -> {
            if (response == replayButton) {
                startGame(); // Replay with same settings
            } else if (response == menuButton) {
                showMainMenu(); // Return to main menu
            } else if (response == exitButton) {
                mainApp.exitApplication(); // Exit application
            }
        });
    }
}
