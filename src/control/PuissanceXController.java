package control;

import boardifier.control.ActionFactory;
import boardifier.control.ActionPlayer;
import boardifier.control.Controller;
import boardifier.model.GameElement;
import boardifier.model.Model;
import boardifier.model.Player;
import boardifier.model.action.ActionList;
import boardifier.view.View;
import model.PuissanceXLevel;
import model.PuissanceXPot;
import model.Disc;
import model.PuissanceXBoard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PuissanceXController extends Controller {
    private int boardRows;
    private int boardCols;
    private int winCondition;
    private BufferedReader consoleIn;
    
    public PuissanceXController(Model model, View view, int boardRows, int boardCols, int winCondition) {
        super(model, view);
        this.boardRows = boardRows;
        this.boardCols = boardCols;
        this.winCondition = winCondition;
    }
    
    @Override
    public void stageLoop() {
        // 1. Initialiser le lecteur de console
        consoleIn = new BufferedReader(new InputStreamReader(System.in));
        
        // 2. Mettre à jour l'affichage initial
        update();
        
        // 3. Boucle principale
        while (!model.isEndGame()) {
            // a. Jouer un tour
            playTurn();
            
            if (!model.isEndGame()) {
                // b. Fin du tour
                endOfTurn();
                
                // c. Mettre à jour l'affichage
                update();
            }
        }
        
        // 4. Fin de partie
        endGame();
    }
    
    /**
     * Handles a player's turn
     */
    private void playTurn() {
        Player currentPlayer = model.getCurrentPlayer();
        boolean validMove = false;

        // Afficher le joueur actuel
        System.out.println("\nTour de " + currentPlayer.getName());
        
        if (currentPlayer.getType() == Player.COMPUTER) {
            // Utiliser l'IA
            PuissanceXDecider decider = new PuissanceXDecider(model, this);
            ActionPlayer actionPlayer = new ActionPlayer(model, this, decider, null);
            actionPlayer.start();
            validMove = true;
        } 
        else {
            // Tour du joueur humain
            while (!validMove && !model.isEndGame()) {
                try {
                    System.out.print("Choisissez une colonne (0-" + (boardCols-1) + "): ");
                    String input = consoleIn.readLine();
                    validMove = analyseAndPlay(input);
                    if (!validMove) {
                        System.out.println("Coup invalide, réessayez");
                    }
                } 
                catch (IOException e) {
                    System.out.println("Erreur de lecture. Réessayez.");
                }
            }
        }
    }
    
    @Override
    public void endOfTurn() {
        // 1. Passer au joueur suivant
        model.setNextPlayer();
        
        // 2. Mettre à jour l'affichage du nom du joueur
        PuissanceXLevel level = (PuissanceXLevel) model.getGameStage();
        level.getPlayerName().setText(model.getCurrentPlayerName());
    }
    
    /**
     * Analyzes player input and executes the move if valid
     * @param input The player's input
     * @return true if the move was valid and executed, false otherwise
     */
    private boolean analyseAndPlay(String input) {
        try {
            // 1. Extraire la colonne choisie
            int col = Integer.parseInt(input);
            
            // 2. Vérifier si la colonne est valide
            PuissanceXLevel level = (PuissanceXLevel) model.getGameStage();
            PuissanceXBoard board = level.getBoard();
            
            if (!board.isColumnPlayable(col)) {
                return false;
            }
            
            // 3. Trouver la première ligne disponible
            int row = board.getFirstAvailableRow(col);
            if (row == -1) return false;
            
            // 4. Récupérer un jeton du pot du joueur
            PuissanceXPot pot = level.getPlayerPot(model.getIdPlayer());
            Disc disc = pot.getFirstAvailableDisc();
            if (disc == null) return false;
            
            // 5. Créer et exécuter les actions pour placer le jeton
            ActionList actions = new ActionList(true);
            
            // Retirer le jeton du pot
            ActionList removeAction = ActionFactory.generateRemoveFromContainer(model, disc);
            actions.addAll(removeAction);
            
            // Placer le jeton dans le plateau
            ActionList putAction = ActionFactory.generatePutInContainer(model, disc, "puissancexboard", row, col);
            actions.addAll(putAction);
            
            // Exécuter les actions
            ActionPlayer actionPlayer = new ActionPlayer(model, this, actions);
            actionPlayer.start();
            
            // Vérifier s'il y a un gagnant
            if (board.checkWin(row, col, model.getIdPlayer())) {
                model.setIdWinner(model.getIdPlayer());
                model.stopGame();
            }
            // Vérifier s'il y a match nul
            else if (board.isFull()) {
                model.stopGame();
            }
            
            return true;
        } 
        catch (NumberFormatException e) {
            return false;
        }
    }
    
    @Override
    public void endGame() {
        if (model.getIdWinner() != -1) {
            System.out.println("\n" + model.getPlayers().get(model.getIdWinner()).getName() + " a gagné !");
        } 
        else {
            System.out.println("\nMatch nul !");
        }
        
        // Demander une nouvelle partie
        try {
            System.out.print("\nNouvelle partie ? (o/n): ");
            String input = consoleIn.readLine();
            if (input.toLowerCase().startsWith("o")) {
                model.reset();
                startStage("puissanceXStage");
            }
        } 
        catch (Exception e) {
            System.out.println("Erreur lors de la lecture. Fin du jeu.");
        }
    }
    
    /**
     * Gets the number of rows in the board
     * @return The number of rows
     */
    public int getBoardRows() {
        return boardRows;
    }
    
    /**
     * Gets the number of columns in the board
     * @return The number of columns
     */
    public int getBoardCols() {
        return boardCols;
    }
    
    /**
     * Gets the win condition
     * @return The win condition
     */
    public int getWinCondition() {
        return winCondition;
    }
}