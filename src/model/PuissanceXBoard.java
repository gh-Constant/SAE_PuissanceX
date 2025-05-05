package model;

import boardifier.model.GameStageModel;
import boardifier.model.ContainerElement;
import java.util.List;
import java.awt.Point;

public class PuissanceXBoard extends ContainerElement {
    private int rows;
    private int cols;
    private int winCondition;
    
    public PuissanceXBoard(int x, int y, int rows, int cols, int winCondition, GameStageModel gameStageModel) {
        super("puissancexboard", x, y, rows, cols, gameStageModel);
        
        // TODO: Initialisation
        // 1. Stocker les dimensions et la condition de victoire
        // 2. Initialiser la grille
    }
    
    // TODO: Méthodes pour la gestion du jeu
    // 1. Vérifier si une colonne est valide pour placer un jeton
    // 2. Trouver la première ligne disponible dans une colonne
    // 3. Vérifier s'il y a un gagnant (alignements horizontaux, verticaux, diagonaux)
    // 4. Vérifier si la grille est pleine (match nul)
}
