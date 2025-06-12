package view;

/**
 * Interface for launching games from the menu
 */
public interface GameLauncher {
    void startGame(int winCondition, int boardRows, int boardCols, int gameMode, int aiType1, int aiType2);
    void exitApplication();
}
