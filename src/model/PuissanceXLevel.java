package model;

import boardifier.model.GameStageModel;
import boardifier.model.Model;
import boardifier.model.StageElementsFactory;
import boardifier.model.TextElement;

public class PuissanceXLevel extends GameStageModel {
    // Éléments du jeu
    private PuissanceXBoard board;
    private PuissanceXPot[] playerPots;
    private Disc[][] playerDiscs;
    private TextElement playerName;
    
    // Variables d'état
    private int rows;
    private int cols;
    private int winCondition;
    
    public PuissanceXLevel(String name, Model model) {
        super(name, model);
        
        // TODO: Initialisation
        // 1. Récupérer les paramètres du jeu (rows, cols, winCondition)
        // 2. Initialiser les tableaux pour les pots et les jetons
        // 3. Créer l'élément texte pour afficher le joueur actif
        
        // TODO: Callbacks
        // 1. Ajouter un écouteur pour mettre à jour le texte quand le joueur change
    }
    
    // TODO: Getters et setters
    // 1. Créer les getters/setters pour board, playerPots, playerDiscs, playerName
    // 2. Créer les getters pour rows, cols, winCondition
    
    @Override
    public StageElementsFactory getDefaultElementFactory() {
        return new PuissanceXStageFactory(this);
    }
}