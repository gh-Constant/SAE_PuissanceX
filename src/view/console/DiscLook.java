package view.console;

import boardifier.model.GameElement;
import boardifier.view.ElementLook;
import boardifier.view.ConsoleColor;
import model.Disc;

public class DiscLook extends ElementLook {
    
    public DiscLook(GameElement element) {
        super(element, 1, 1); // Taille 1x1 pour un jeton en mode console
    }
    
    @Override
    protected void render() {
        // TODO: Rendre le jeton avec le bon caractère et la bonne couleur
        // Utiliser ConsoleColor pour les couleurs
        // Exemple: shape[0][0] = ConsoleColor.RED + "O" + ConsoleColor.RESET;
    }
    
    @Override
    public void onSelectionChange() {
        // TODO: Changer l'apparence quand le jeton est sélectionné
    }

    public void onPutInContainer() {
        // TODO: Ajuster l'apparence quand le jeton est placé dans un conteneur
    }
}