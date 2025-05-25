package view;

import boardifier.model.GameStageModel;
import boardifier.model.TextElement;
import boardifier.view.GameStageView;
import boardifier.view.TextLook;
import model.PuissanceXBoard;
import model.PuissanceXDisk;
import model.PuissanceXStageModel;
import model.PuissanceXModel;

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
        PuissanceXModel gameModel = (PuissanceXModel) model.getModel();
        
        // Create look for the board
        BoardLook boardLook = new BoardLook(model.getBoard());
        addLook(boardLook);
        
        // Create look for the player text
        TextLook textLook = new TextLook(model.getPlayerText());
        addLook(textLook);
        
        // Create pot looks with text labels above them
        RedDiskPot redPot = new RedDiskPot(3, 5, gameModel.getPlayer1Pot());
        YellowDiskPot yellowPot = new YellowDiskPot(3, 5, gameModel.getPlayer2Pot());

        // Add text labels for the pots
        TextElement redLabel = new TextElement("RED", model);
        redLabel.setLocation(gameModel.getPlayer1Pot().getX(), gameModel.getPlayer1Pot().getY() - 1);
        model.addElement(redLabel);
        addLook(new TextLook(redLabel));

        TextElement yellowLabel = new TextElement("YELLOW", model);
        yellowLabel.setLocation(gameModel.getPlayer2Pot().getX(), gameModel.getPlayer2Pot().getY() - 1);
        model.addElement(yellowLabel);
        addLook(new TextLook(yellowLabel));
        
        // Add the pot looks
        addLook(redPot);
        addLook(yellowPot);
    }
}
