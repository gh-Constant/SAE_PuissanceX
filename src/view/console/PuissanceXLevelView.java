package view.console;

import boardifier.model.GameStageModel;
import boardifier.view.GameStageView;
import boardifier.view.TextLook;
import model.PuissanceXLevel;
import model.PuissanceXBoard;
import model.Disc;
import model.PuissanceXPot;
import boardifier.model.TextElement;

/**
 * PuissanceXLevelView defines the view for the Puissance X game stage.
 * It creates all the visual elements needed to represent the game state:
 * - The game board
 * - The disc pots for each player
 * - The discs
 * - Text elements for player information
 */
public class PuissanceXLevelView extends GameStageView {
    
    public PuissanceXLevelView(String name, GameStageModel gameStageModel) {
        super(name, gameStageModel);
    }
    
    @Override
    public void createLooks() {
        PuissanceXLevel level = (PuissanceXLevel)gameStageModel;
        
        // Create the board look
        PuissanceXBoard board = level.getBoard();
        PuissanceXBoardLook boardLook = new PuissanceXBoardLook(board);
        addLook(boardLook);
        
        // Create the disc pot looks
        PuissanceXPot[] playerPots = level.getPlayerPots();
        for (int i = 0; i < playerPots.length; i++) {
            PuissanceXPotLook potLook = new PuissanceXPotLook(playerPots[i], i);
            addLook(potLook);
        }
        
        // Create the disc looks
        Disc[][] playerDiscs = level.getPlayerDiscs();
        for (int i = 0; i < playerDiscs.length; i++) {
            for (int j = 0; j < playerDiscs[i].length; j++) {
                if (playerDiscs[i][j] != null) {
                    DiscLook discLook = new DiscLook(playerDiscs[i][j]);
                    addLook(discLook);
                }
            }
        }
        
        // Create the text element look for player name
        TextElement playerName = level.getPlayerName();
        if (playerName != null) {
            TextLook textLook = new TextLook(playerName);
            // Position the text above the board
            playerName.setLocation(board.getX() + board.getNbCols()/2 - 5, board.getY() - 2);
            addLook(textLook);
        }
    }
}