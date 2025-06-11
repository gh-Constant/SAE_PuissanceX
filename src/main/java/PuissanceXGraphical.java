import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.PuissanceXModel;
import model.PuissanceXBoard;
import view.PuissanceXFXRootPane;
import control.PuissanceXControllerFX;
import model.PuissanceXStageModel;
import view.PuissanceXFXStageView;
import boardifier.control.StageFactory;
import boardifier.model.GameException; // Import GameException

public class PuissanceXGraphical extends Application {

    @Override
    public void start(Stage primaryStage) throws GameException {
        // Model
        PuissanceXModel model = new PuissanceXModel();
        model.setWinCondition(4);
        model.setBoardRows(6);
        model.setBoardCols(7);
        model.addHumanPlayer("Player 1");
        model.addComputerPlayer("Computer");

        // Root Pane (JavaFX)
        PuissanceXFXRootPane rootPane = new PuissanceXFXRootPane();

        // Stage Model
        PuissanceXStageModel stageModel = new PuissanceXStageModel("puissanceX", model);

        // Create and set the board
        PuissanceXBoard board = new PuissanceXBoard(model.getBoardRows(),
                model.getBoardCols(),
                stageModel);
        stageModel.setBoard(board);

        // Set stage model in the main model
        model.setGameStage(stageModel);

        // Register with StageFactory
        StageFactory.registerModelAndView("puissanceX", "model.PuissanceXStageModel", "view.PuissanceXFXStageView"); // Corrected line

        // Stage View (JavaFX)
        PuissanceXFXStageView stageView = new PuissanceXFXStageView("puissanceX", stageModel);

        // Controller
        PuissanceXControllerFX controller = new PuissanceXControllerFX(model, stageView);

        // **SET THE FIRST STAGE NAME BEFORE STARTING THE GAME**
        controller.setFirstStageName("puissanceX");

        // Scene
        Scene scene = new Scene(rootPane, 800, 600);

        // Stage
        primaryStage.setTitle("PuissanceX");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Start game
        controller.startGame();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
