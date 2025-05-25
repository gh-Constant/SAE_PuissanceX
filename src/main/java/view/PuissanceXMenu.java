package view;

import boardifier.control.Logger;
import java.util.Scanner;

/**
 * Menu system for PuissanceX game.
 * Allows users to configure game settings interactively.
 */
public class PuissanceXMenu {
    private Scanner scanner;
    private int winCondition = 4;
    private int boardRows = 6;
    private int boardCols = 7;
    private int gameMode = 1;
    private boolean exitRequested = false;
    private int aiType1 = 0;
    private int aiType2 = 0;

    public PuissanceXMenu() {
        scanner = new Scanner(System.in);
    }

    /**
     * Display the main menu and handle user input.
     * @return true if the game should start, false if user wants to exit
     */
    public boolean showMenu() {
        while (!exitRequested) {
            displayMainMenu();
            int choice = getIntInput("Enter your choice: ", 1, 5);
            
            switch (choice) {
                case 1: // Start game
                    return true;
                case 2: // Change game mode
                    changeGameMode();
                    break;
                case 3: // Change board size
                    changeBoardSize();
                    break;
                case 4: // Change win condition
                    changeWinCondition();
                    break;
                case 5: // Exit
                    exitRequested = true;
                    break;
            }
        }
        return false;
    }

    private void displayMainMenu() {
        clearScreen();
        System.out.println("╔════════════════════════════════════╗");
        System.out.println("║          PUISSANCE X GAME          ║");
        System.out.println("╠════════════════════════════════════╣");
        System.out.println("║ Current Settings:                  ║");
        System.out.println(String.format("║ • Game Mode: %-22s║", getGameModeString()));
        String aiType1Str = (aiType1 == 1 ? "Minimax" : aiType1 == 2 ? "Random" : aiType1 == 3 ? "Condition" : "None");
        System.out.println(String.format("║ • AI Type 1: %-22s║", aiType1Str));
        String aiType2Str = (aiType2 == 1 ? "Minimax" : aiType2 == 2 ? "Random" : aiType2 == 3 ? "Condition" : "None");
        System.out.println(String.format("║ • AI Type 2: %-22s║", aiType2Str));
        System.out.println(String.format("║ • Board Size: %-21s║", boardRows + "x" + boardCols));
        System.out.println(String.format("║ • Win Condition: %-18s║", winCondition));
        System.out.println(String.format("║ • Column Input: 1-%-17s║", boardCols));
        System.out.println("╠════════════════════════════════════╣");
        System.out.println("║ 1. Start Game                      ║");
        System.out.println("║ 2. Change Game Mode                ║");
        System.out.println("║ 3. Change Board Size               ║");
        System.out.println("║ 4. Change Win Condition            ║");
        System.out.println("║ 5. Exit                            ║");
        System.out.println("╚════════════════════════════════════╝");
    }

    private void changeGameMode() {
        clearScreen();
        System.out.println("╔════════════════════════════════════╗");
        System.out.println("║          SELECT GAME MODE          ║");
        System.out.println("╠════════════════════════════════════╣");
        System.out.println("║ 1. Human vs Human                  ║");
        System.out.println("║ 2. Human vs Computer               ║");
        System.out.println("║ 3. Computer vs Computer            ║");
        System.out.println("╚════════════════════════════════════╝");

        gameMode = getIntInput("Enter game mode (1-3): ", 1, 3);

        if (gameMode == 2) {
            selectAI(true);
        } else if (gameMode == 3) {
            System.out.println("\nSelect first AI:");
            selectAI(true);
            System.out.println("\nSelect second AI:");
            selectAI(false);
        }
        
        Logger.debug("Game mode set to: " + gameMode);
    }

    private void changeBoardSize() {
        clearScreen();
        System.out.println("╔════════════════════════════════════╗");
        System.out.println("║          SET BOARD SIZE            ║");
        System.out.println("╠════════════════════════════════════╣");
        System.out.println(String.format("║ Current size: %-21s ║", boardRows + "x" + boardCols));
        System.out.println("╚════════════════════════════════════╝");

        boardRows = getIntInput("Enter number of rows (between 4-10): ", 4, 10);
        boardCols = getIntInput("Enter number of columns (between 4-10): ", 4, 10);
        Logger.debug("Board size set to: " + boardRows + "x" + boardCols);
    }

    private void changeWinCondition() {
        clearScreen();
        System.out.println("╔════════════════════════════════════╗");
        System.out.println("║         SET WIN CONDITION          ║");
        System.out.println("╠════════════════════════════════════╣");
        System.out.println(String.format("║ Current win condition: %-12s ║", winCondition));
        System.out.println("╚════════════════════════════════════╝");
        
        int maxWin = Math.min(boardRows, boardCols);
        winCondition = getIntInput("Enter win condition (3-" + maxWin + "): ", 3, maxWin);
        Logger.debug("Win condition set to: " + winCondition);
    }

    private String getGameModeString() {
        switch (gameMode) {
            case 1: return "Human vs Human";
            case 2: return "Human vs Computer";
            case 3: return "Computer vs Computer";
            default: return "Unknown";
        }
    }

    private int getIntInput(String prompt, int min, int max) {
        int input = -1;
        do {
            System.out.print(prompt);
            try {
                input = Integer.parseInt(scanner.nextLine().trim());
                if (input < min || input > max) {
                    System.out.println("Please enter a number between " + min + " and " + max);
                    input = -1;
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number");
            }
        } while (input < min || input > max);
        return input;
    }

    private void clearScreen() {
        // This is a simple way to "clear" the console
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

    public int getWinCondition() {
        return winCondition;
    }

    public int getBoardRows() {
        return boardRows;
    }

    public int getBoardCols() {
        return boardCols;
    }

    public int getGameMode() {
        return gameMode;
    }

    public int getAIType1() {
        return aiType1;
    }

    public int getAIType2() {
        return aiType2;
    }

    private void selectAI(boolean isFirstAI) {
        clearScreen();
        System.out.println("╔════════════════════════════════════╗");
        System.out.println("║          SELECT AI TYPE            ║");
        System.out.println("╠════════════════════════════════════╣");
        System.out.println("║ 1. Minimax AI                      ║");
        System.out.println("║ 2. Random AI                       ║");
        System.out.println("║ 3. Condition AI                    ║");
        System.out.println("╚════════════════════════════════════╝");

        int selection = getIntInput("Enter AI type (1-3): ", 1, 3);
        if (isFirstAI) {
            aiType1 = selection;
        } else {
            aiType2 = selection;
        }
    }

    /**
     * Affiche le menu de fin de partie avec les options rejouer ou quitter.
     * @return true si le joueur veut rejouer, false s'il veut quitter
     */
    public boolean showEndGameMenu() {
        clearScreen();
        System.out.println("╔════════════════════════════════════╗");
        System.out.println("║            PARTIE TERMINÉE         ║");
        System.out.println("╠════════════════════════════════════╣");
        System.out.println("║ 1. Rejouer                         ║");
        System.out.println("║ 2. Retour au menu principal        ║");
        System.out.println("║ 3. Quitter                         ║");
        System.out.println("╚════════════════════════════════════╝");

        int choice = getIntInput("Entrez votre choix: ", 1, 3);
        
        switch (choice) {
            case 1: // Rejouer
                return true;
            case 2: // Retour au menu principal
                return false;
            case 3: // Quitter
                exitRequested = true;
                return false;
            default:
                return false;
        }
    }

    /**
     * Réinitialise l'état du menu pour permettre de revenir au menu principal.
     */
    public void resetExitRequest() {
        exitRequested = false;
    }

    /**
     * Vérifie si l'utilisateur a demandé à quitter.
     * @return true si l'utilisateur veut quitter
     */
    public boolean isExitRequested() {
        return exitRequested;
    }
}