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
        System.out.println("║ • Game Mode: " + getGameModeString() + "              ║");
        System.out.println("║ • Board Size: " + boardRows + "x" + boardCols + "                  ║");
        System.out.println("║ • Win Condition: " + winCondition + "                  ║");
        System.out.println("║ • Column Input: 1-" + boardCols + "                ║");
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
        System.out.println("║ 0. Human vs Human                  ║");
        System.out.println("║ 1. Human vs Computer               ║");
        System.out.println("║ 2. Computer vs Computer            ║");
        System.out.println("╚════════════════════════════════════╝");
        
        gameMode = getIntInput("Enter game mode (0-2): ", 0, 2);
        Logger.debug("Game mode set to: " + gameMode);
    }

    private void changeBoardSize() {
        clearScreen();
        System.out.println("╔════════════════════════════════════╗");
        System.out.println("║          SET BOARD SIZE            ║");
        System.out.println("╠════════════════════════════════════╣");
        System.out.println("║ Current size: " + boardRows + "x" + boardCols + "                  ║");
        System.out.println("╚════════════════════════════════════╝");
        
        boardRows = getIntInput("Enter number of rows (4-10): ", 4, 10);
        boardCols = getIntInput("Enter number of columns (4-10): ", 4, 10);
        Logger.debug("Board size set to: " + boardRows + "x" + boardCols);
    }

    private void changeWinCondition() {
        clearScreen();
        System.out.println("╔════════════════════════════════════╗");
        System.out.println("║         SET WIN CONDITION          ║");
        System.out.println("╠════════════════════════════════════╣");
        System.out.println("║ Current win condition: " + winCondition + "           ║");
        System.out.println("╚════════════════════════════════════╝");
        
        int maxWin = Math.min(boardRows, boardCols);
        winCondition = getIntInput("Enter win condition (3-" + maxWin + "): ", 3, maxWin);
        Logger.debug("Win condition set to: " + winCondition);
    }

    private String getGameModeString() {
        switch (gameMode) {
            case 0: return "Human vs Human";
            case 1: return "Human vs Computer";
            case 2: return "Computer vs Computer";
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
}