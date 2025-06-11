package view;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import model.PuissanceXBoard;
import model.PuissanceXDisk;
import model.PuissanceXStageModel;
import boardifier.view.GameStageView; // Import GameStageView
import boardifier.model.GameStageModel;

public class PuissanceXFXStageView extends GameStageView { // Extend GameStageView

    private GridPane gridPane;
    private PuissanceXFXRootPane rootPane;
    private static final int CELL_SIZE = 60;

    public PuissanceXFXStageView(String name, GameStageModel gameStageModel) { // Add this constructor
        super(name, gameStageModel);
        this.rootPane = rootPane;
        gridPane = new GridPane();
        gridPane.setHgap(5);
        gridPane.setVgap(5);
        createBoard(((PuissanceXStageModel)gameStageModel).getBoard()); // Cast to PuissanceXStageModel
        //rootPane.getChildren().add(gridPane); // Add grid to root pane
    }

    private void createBoard(PuissanceXBoard board) {
        for (int row = 0; row < board.getNbRows(); row++) {
            for (int col = 0; col < board.getNbCols(); col++) {
                Circle cell = new Circle(CELL_SIZE / 2);
                cell.setFill(Color.WHITE);
                cell.setStroke(Color.BLACK);
                gridPane.add(cell, col, row);
                // Add click handler (example)
                final int column = col;
                cell.setOnMouseClicked(e -> handleCellClick(column));
            }
        }
    }

    public void updateDisk(PuissanceXDisk disk, int row, int col) {
        DiskLookFX diskLook = new DiskLookFX(disk);
        Circle diskCircle = diskLook.getDiskCircle();
        gridPane.add(diskCircle, col, row);
    }

    private void handleCellClick(int col) {
        System.out.println("Clicked on column: " + col);
        // Implement game logic here
    }

    @Override
    public void createLooks() {
        // Implement createLooks method
    }
}