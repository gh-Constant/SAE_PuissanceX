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
        // TODO: Logique principale du jeu
        // 1. Initialiser le lecteur de console
        // 2. Mettre à jour l'affichage
        // 3. Boucle principale: tant que le jeu n'est pas terminé
        //    a. Jouer un tour
        //    b. Fin du tour
        //    c. Mettre à jour l'affichage
        // 4. Fin de partie
    }
    
    /**
     * Handles a player's turn
     */
    private void playTurn() {
        // TODO: Gérer le tour d'un joueur
        // 1. Récupérer le joueur actuel
        // 2. Si c'est un ordinateur, utiliser l'IA
        // 3. Si c'est un humain, demander une entrée
        // 4. Valider et exécuter le coup
    }
    
    @Override
    public void endOfTurn() {
        // TODO: Gérer la fin d'un tour
        // 1. Passer au joueur suivant
        // 2. Mettre à jour l'affichage du nom du joueur
    }
    
    /**
     * Analyzes player input and executes the move if valid
     * @param input The player's input
     * @return true if the move was valid and executed, false otherwise
     */
    private boolean analyseAndPlay(String input) {
        // TODO: Analyser et exécuter un coup
        // 1. Extraire la colonne choisie
        // 2. Vérifier si la colonne est valide
        // 3. Trouver la première ligne disponible
        // 4. Récupérer un jeton du pot du joueur
        // 5. Créer et exécuter les actions pour placer le jeton
        return false;
    }
    
    @Override
    public void endGame() {
        // TODO: Gérer la fin de partie
        // 1. Afficher le gagnant ou match nul
        // 2. Proposer une nouvelle partie
        super.endGame();
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