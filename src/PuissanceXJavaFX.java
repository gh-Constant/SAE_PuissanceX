import boardifier.model.Model;
import boardifier.view.View;
import boardifier.control.StageFactory;
import control.PuissanceXController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PuissanceXJavaFX extends Application {
    
    // Game parameters
    private static int BOARD_ROWS = 6;
    private static int BOARD_COLS = 7;
    private static int WIN_CONDITION = 4;
    private static int GAME_MODE = 0;
    
    @Override
    public void start(Stage primaryStage) {
        // TODO: Créer le modèle
        
        // TODO: Ajouter les joueurs selon le mode de jeu
        
        // TODO: Enregistrer les classes de modèle et vue pour JavaFX
        // StageFactory.registerModelAndView("puissanceXStage", "model.PuissanceXLevel", "view.javafx.PuissanceXLevelView");
        
        // TODO: Créer la vue et le contrôleur
        
        // TODO: Configurer la scène JavaFX
        
        // TODO: Démarrer le jeu
    }
    
    public static void main(String[] args) {
        // TODO: Traiter les arguments de ligne de commande
        
        launch(args);
    }
}