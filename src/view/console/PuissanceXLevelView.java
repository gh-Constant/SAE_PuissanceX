package view.console;

import boardifier.model.GameStageModel;
import boardifier.view.GameStageView;
import model.PuissanceXLevel;
import model.PuissanceXBoard;
import model.Disc;
import model.PuissanceXPot;

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
        
        // TODO: Create the board look (PuissanceXBoardLook)
        // 1. Get the board from the level model
        // 2. Create a PuissanceXBoardLook for the board
        // 3. Add the look to the view
        
        // TODO: Create the disc pot looks (PuissanceXPotLook)
        // 1. Get the player pots from the level model
        // 2. Create a PuissanceXPotLook for each pot
        // 3. Add the looks to the view
        
        // TODO: Create the disc looks (DiscLook)
        // 1. Get the player discs from the level model
        // 2. Create a DiscLook for each disc
        // 3. Add the looks to the view
        
        // TODO: Create the text element looks
        // 1. Get the player name text element from the level model
        // 2. Create a TextLook for the text element
        // 3. Add the look to the view
    }
}