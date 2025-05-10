package view;

import boardifier.model.GameElement;
import boardifier.model.GameStageModel;
import boardifier.view.GameStageView;
import boardifier.view.TextLook;
import model.Board;
import model.Disk;
import model.PuissanceXStageModel;

/**
 * View class for the PuissanceX game.
 * Handles the console-based display of the game board and state.
 */
public class PuissanceXStageView extends GameStageView {

    /**
     * Constructor required by the boardifier framework.
     */
    public PuissanceXStageView(String name, GameStageModel gameStageModel) {
        super(name, gameStageModel);
    }

    /**
     * Required by boardifier framework, but not needed for console display.
     */
    @Override
    public void createLooks() {
        PuissanceXStageModel model = (PuissanceXStageModel) gameStageModel;
        
        // Create look for the board
        BoardLook boardLook = new BoardLook(model.getBoard());
        addLook(boardLook);
        
        // Create look for the player text
        TextLook textLook = new TextLook(model.getPlayerText());
        addLook(textLook);
        
        // Create looks for any disks that might already be on the board
        for (int row = 0; row < model.getBoard().getNbRows(); row++) {
            for (int col = 0; col < model.getBoard().getNbCols(); col++) {
                for (GameElement element : model.getBoard().getElements(row, col)) {
                    if (element instanceof Disk) {
                        DiskLook diskLook = new DiskLook((Disk) element);
                        addLook(diskLook);
                    }
                }
            }
        }
    }
}
