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
        // Handle disk placement in the board
        gameStageModel.onPutInContainer((element, container, row, col) -> {
            if (element instanceof PuissanceXDisk && container instanceof PuissanceXBoard) {
                // Ensure this runs on the JavaFX Application Thread
                javafx.application.Platform.runLater(() -> {
                    try {
                        // Find the BoardLook that was created
                        SimpleBoardLook boardLook = (SimpleBoardLook) getElementLook(container);
                        if (boardLook != null) {
                            SimpleDiskLook diskLook = new SimpleDiskLook((PuissanceXDisk) element);
                            boardLook.addInnerLook(diskLook, row, col);
                        }
                    } catch (Exception e) {
                        System.err.println("Error adding disk look: " + e.getMessage());
                        e.printStackTrace();
                    }
                });
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
        
        // Create JavaFX-compatible looks for all elements

        // Create look for the board
        SimpleBoardLook boardLook = new SimpleBoardLook(model.getBoard());
        addLook(boardLook);

        // Create look for the player text
        TextLook playerTextLook = new TextLook(24, "BLACK", model.getPlayerText());
        addLook(playerTextLook);

        // Create pot looks
        SimpleDiskPotLook redPotLook = new SimpleDiskPotLook(gameModel.getPlayer1Pot(), javafx.scene.paint.Color.RED);
        addLook(redPotLook);

        SimpleDiskPotLook yellowPotLook = new SimpleDiskPotLook(gameModel.getPlayer2Pot(), javafx.scene.paint.Color.YELLOW);
        addLook(yellowPotLook);
    }
}
