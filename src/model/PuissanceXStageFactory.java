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
        // TODO: Création du plateau
        // 1. Créer le plateau avec les dimensions spécifiées
        // 2. Ajouter le plateau au modèle
        
        // TODO: Création des pots de jetons
        // 1. Créer un pot pour chaque joueur
        // 2. Positionner les pots sur les côtés
        // 3. Ajouter les pots au modèle
        
        // TODO: Création des jetons
        // 1. Créer les jetons pour chaque joueur
        // 2. Placer les jetons dans les pots correspondants
        // 3. Ajouter les jetons au modèle
        
        // TODO: Création des éléments textuels
        // 1. Positionner l'élément texte pour le nom du joueur
        // 2. Ajouter l'élément texte au modèle
    }
}
