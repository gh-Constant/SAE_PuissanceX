package control;

import boardifier.control.Controller;
import boardifier.model.Model;
import boardifier.model.Player;
import boardifier.view.View;
import model.PuissanceXLevel;

public class PuissanceXController extends Controller {
    private int boardRows;
    private int boardCols;
    private int winCondition;
    
    public PuissanceXController(Model model, View view, int boardRows, int boardCols, int winCondition) {
        super(model, view);
        // TODO Initialisation
        this.boardRows = boardRows;
        this.boardCols = boardCols;
        this.winCondition = winCondition;
    }
    
    @Override
    public void stageLoop() {
        //TODO Logique principale du jeu
    }
    
    //TODO Méthodes pour gérer les tours, les mouvements et la fin de partie
}