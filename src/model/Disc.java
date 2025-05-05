package model;

import boardifier.model.GameElement;
import boardifier.model.GameStageModel;

public class Disc extends GameElement {
    
    // TODO: Attributs
    // 1. Identifiant du joueur propriétaire du jeton
    // 2. Couleur du jeton
    
    public Disc(int x, int y, int playerId, GameStageModel gameStageModel) {
        super(x, y, gameStageModel);
        
        // TODO: Initialisation
        // 1. Stocker l'identifiant du joueur
        // 2. Définir la couleur en fonction du joueur
    }
    
    // TODO: Getters et setters
    // 1. Obtenir l'identifiant du joueur
    // 2. Obtenir la couleur du jeton
    
    @Override
    public void update() {
        // TODO: Mise à jour de l'état du jeton
        // 1. Gérer les animations si nécessaire
    }
}