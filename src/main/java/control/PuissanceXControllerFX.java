package control;

import model.PuissanceXDisk;
import model.PuissanceXStageModel;
import view.PuissanceXFXStageView;
import boardifier.model.Model;
import boardifier.control.Controller;
import boardifier.view.View;

public class PuissanceXControllerFX extends Controller {

    private PuissanceXFXStageView stageView;

    public PuissanceXControllerFX(Model model, PuissanceXFXStageView stageView) {
        super(model, new View(model)); // We need to pass a View to the parent Controller
        this.stageView = stageView;
    }

    @Override
    public void stageLoop() {
        // Implement game loop logic here
    }

    public void handleMove(int row, int col, PuissanceXDisk disk) {
        stageView.updateDisk(disk, row, col);
    }
}