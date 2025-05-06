package model;

import boardifier.model.GameStageModel;
import boardifier.model.StageElementsFactory;
import boardifier.model.TextElement;

public class PuissanceXStageFactory extends StageElementsFactory {
    private PuissanceXLevel stageModel;
    
    public PuissanceXStageFactory(GameStageModel gameStageModel) {
        super(gameStageModel);
        stageModel = (PuissanceXLevel)gameStageModel;
    }
    
    @Override
    public void setup() {
        // Création du plateau
        // 1. Créer le plateau avec les dimensions spécifiées
        int rows = stageModel.getRows();
        int cols = stageModel.getCols();
        int winCondition = stageModel.getWinCondition();

        PuissanceXBoard board = new PuissanceXBoard(0, 0, rows, cols, winCondition, stageModel);
        
        // 2. Ajouter le plateau au modèle
        stageModel.setBoard(board);
        stageModel.addElement(board);
        
        // Création des pots de jetons
        // 1. Créer un pot pour chaque joueur
        int nbPlayers = stageModel.getModel().getPlayers().size();
        PuissanceXPot[] playerPots = new PuissanceXPot[nbPlayers];

        // Calculer le nombre de jetons nécessaires par joueur (moitié du plateau + 1 pour arrondir)
        int nbDiscsPerPlayer = (rows * cols + 1) / 2;

        // 2. Positionner les pots sur les côtés
        // Pot du joueur 1 (à gauche)
        playerPots[0] = new PuissanceXPot(-5, 2, nbDiscsPerPlayer, stageModel, 0);
        stageModel.addElement(playerPots[0]);

        // Pot du joueur 2 (à droite)
        playerPots[1] = new PuissanceXPot(cols + 5, 2, nbDiscsPerPlayer, stageModel, 1);
        stageModel.addElement(playerPots[1]);

        // Stocker les pots dans le modèle pour un accès facile
        stageModel.setPlayerPots(playerPots);
        
        // Création des jetons
        // 1. Créer les jetons pour chaque joueur
        Disc[][] playerDiscs = new Disc[nbPlayers][nbDiscsPerPlayer];

        // Jetons pour le joueur 1
        for (int j = 0; j < nbDiscsPerPlayer; j++) {
            // Créer un jeton pour le joueur 0
            playerDiscs[0][j] = new Disc(0, 0, 0, stageModel);
            
            // Placer le jeton dans le pot du joueur 1
            playerPots[0].addElement(playerDiscs[0][j], 0, 0);
            
            // Ajouter le jeton au modèle
            stageModel.addElement(playerDiscs[0][j]);
        }

        // Jetons pour le joueur 2
        for (int j = 0; j < nbDiscsPerPlayer; j++) {
            // Créer un jeton pour le joueur 1
            playerDiscs[1][j] = new Disc(0, 0, 1, stageModel);
            
            // Placer le jeton dans le pot du joueur 2
            playerPots[1].addElement(playerDiscs[1][j], 0, 0);
            
            // Ajouter le jeton au modèle
            stageModel.addElement(playerDiscs[1][j]);
        }

        // Stocker les jetons dans le modèle pour un accès facile
        stageModel.setPlayerDiscs(playerDiscs);
        
        // Création des éléments textuels
        // 1. Positionner l'élément texte pour le nom du joueur
        TextElement playerName = stageModel.getPlayerName();
        playerName.setLocation(cols/2, rows + 3); // Position en dessous du plateau

        // 2. Ajouter l'élément texte au modèle
        stageModel.addElement(playerName);
    }
}