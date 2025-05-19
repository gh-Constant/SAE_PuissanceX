package view;

import boardifier.model.GameStageModel;
import boardifier.view.GameStageView;
import boardifier.view.TextLook;
import model.PuissanceXBoard;
import model.PuissanceXDisk;
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

        // Ajoute le callback pour chaque ajout de disque dans le board
        gameStageModel.onPutInContainer((element, container, row, col) -> {
            if (element instanceof PuissanceXDisk && container instanceof PuissanceXBoard) {
                // Cherche le BoardLook déjà créé
                BoardLook boardLook = (BoardLook) getElementLook(container);
                if (boardLook != null) {
                    DiskLook diskLook = new DiskLook((PuissanceXDisk) element);
                    boardLook.addInnerLook(diskLook, row, col);
                }
            }
        });
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
    }
}
