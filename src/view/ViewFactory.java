package view;

import boardifier.model.GameElement;
import boardifier.model.GameStageModel;
import boardifier.view.ElementLook;
import boardifier.view.GameStageView;
import model.Disc;
import model.PuissanceXBoard;
import model.PuissanceXPot;

/**
 * Factory pour créer les vues appropriées selon le mode d'affichage (console ou JavaFX)
 */
public class ViewFactory {
    
    public static final int MODE_CONSOLE = 0;
    public static final int MODE_JAVAFX = 1;
    
    private static int currentMode = MODE_CONSOLE;
    
    public static void setMode(int mode) {
        if (mode == MODE_CONSOLE || mode == MODE_JAVAFX) {
            currentMode = mode;
        }
    }
    
    public static GameStageView createStageView(String name, GameStageModel model) {
        // TODO: Créer la vue d'étape appropriée selon le mode
        return null;
    }
    
    public static ElementLook createDiscLook(Disc disc) {
        // TODO: Créer le look de jeton approprié selon le mode
        return null;
    }
    
    public static ElementLook createBoardLook(PuissanceXBoard board) {
        // TODO: Créer le look de plateau approprié selon le mode
        return null;
    }
    
    public static ElementLook createPotLook(PuissanceXPot pot, int playerIndex) {
        // TODO: Créer le look de pot approprié selon le mode
        return null;
    }
}