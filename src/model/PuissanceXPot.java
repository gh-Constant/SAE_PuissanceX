package model;

import boardifier.model.GameStageModel;
import boardifier.model.ContainerElement;

public class PuissanceXPot extends ContainerElement {
    
    // TODO: Attributs
    // 1. Identifiant du joueur propriétaire du pot
    
    public PuissanceXPot(int x, int y, int nbDiscs, GameStageModel gameStageModel) {
        super("discpot", x, y, 1, nbDiscs, gameStageModel);
        
        // TODO: Initialisation
        // 1. Stocker l'identifiant du joueur
    }
    
    // TODO: Méthodes utilitaires
    // 1. Obtenir le prochain jeton disponible
    // 2. Vérifier s'il reste des jetons
}
