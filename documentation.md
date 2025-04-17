```markdown
# Documentation du Framework Boardifier-Console

## Table des Matières

1.  [Introduction](#introduction)
2.  [Architecture MVC](#architecture-mvc)
3.  [Concepts Fondamentaux](#concepts-fondamentaux)
    *   [GameElement](#gameelement)
    *   [ContainerElement](#containerelement)
    *   [Stages](#stages)
    *   [Actions & Animations (Mode Console)](#actions--animations-mode-console)
4.  [Couche Modèle (Model)](#couche-modèle-model)
    *   [Sous-classer `GameElement`](#sous-classer-gameelement)
    *   [Sous-classer `ContainerElement`](#sous-classer-containerelement)
    *   [`Model`](#model-classe)
    *   [`GameStageModel`](#gamestagemodel)
    *   [`StageElementsFactory`](#stageelementsfactory)
    *   [Types d'Éléments (`ElementTypes`)](#types-déléments-elementtypes)
    *   [Notifier les Changements Visuels (`addChangeFaceEvent`)](#notifier-les-changements-visuels-addchangefaceevent)
5.  [Couche Vue (View)](#couche-vue-view)
    *   [`ElementLook`](#elementlook)
    *   [`ContainerLook` et ses sous-classes](#containerlook-et-ses-sous-classes)
    *   [`GameStageView`](#gamestageview)
    *   [`RootPane` et `View`](#rootpane-et-view)
6.  [Couche Contrôleur (Control)](#couche-contrôleur-control)
    *   [`Controller`](#controller-classe)
    *   [`Decider`](#decider)
7.  [Gestion des Actions](#gestion-des-actions)
    *   [`GameAction`](#gameaction)
    *   [`ActionList`](#actionlist)
    *   [`ActionFactory`](#actionfactory)
    *   [`ActionPlayer`](#actionplayer)
8.  [Mise en Place Initiale (Classe Principale)](#mise-en-place-initiale-classe-principale)
9.  [Exemple de Structure de Projet](#exemple-de-structure-de-projet)

---

## 1. Introduction

`boardifier-console` est un framework Java conçu pour faciliter le développement de jeux de plateau en mode texte dans la console. Il fournit une structure MVC (Modèle-Vue-Contrôleur) robuste et des composants réutilisables pour gérer les éléments de jeu, l'affichage en mode texte, et la logique de jeu.

L'objectif principal est de séparer clairement les préoccupations :
*   **Modèle :** Représente l'état du jeu (plateau, pièces, joueurs, règles).
*   **Vue :** Gère l'affichage de l'état du jeu dans la console.
*   **Contrôleur :** Orchestre le déroulement du jeu, gère les entrées utilisateur et les décisions de l'IA.

Ce framework partage de nombreuses classes et concepts avec son "grand frère" `boardifier`, facilitant une éventuelle migration vers une version graphique.

---

## 2. Architecture MVC

Le framework impose une structure de projet basée sur le pattern MVC :

*   `boardifier/` : Contient les classes du framework. **Ne pas modifier.**
*   `model/` : Vos classes spécifiques au jeu représentant l'état (héritant des classes du modèle de boardifier).
*   `view/` : Vos classes spécifiques au jeu pour l'affichage (héritant des classes de vue de boardifier).
*   `control/` : Vos classes spécifiques pour la logique de jeu et l'IA (héritant des classes de contrôle de boardifier).
*   `[NomDeVotreJeu]Console.java` (ou similaire) : La classe principale contenant le `main()`.

---

## 3. Concepts Fondamentaux

### GameElement

*   **Description :** La classe de base abstraite pour *tous* les objets présents dans le jeu (pions, cartes, dés, plateaux, zones de texte, boutons...).
*   **Attributs Clés :**
    *   `x`, `y` : Position (coordonnées en "caractères" dans la console).
    *   `visible` : Booléen indiquant si l'élément doit être affiché.
    *   `selectable` : Booléen indiquant si l'élément peut être sélectionné par le joueur.
    *   `type` : Entier identifiant le type d'élément (voir `ElementTypes`).
    *   `model` : Référence au `Model` global du jeu.
    *   `gameStage` : Référence au `GameStageModel` actuel.
*   **Utilisation :** Vous devez créer des sous-classes de `GameElement` pour représenter les entités spécifiques de votre jeu qui n'existent pas déjà dans le framework (par exemple, un pion, une carte spécifique).

### ContainerElement

*   **Description :** Une sous-classe de `GameElement` spécialisée pour représenter une zone (généralement une grille 2D) capable de contenir d'autres `GameElement`s. Pensez à un plateau d'échecs, une main de cartes, une défausse.
*   **Fonctionnalités :**
    *   Modélise une grille de cases (`cells`).
    *   Permet d'ajouter (`addElement`), supprimer (`removeElement`), déplacer (`moveElement`) des éléments dans ses cases.
    *   Peut gérer des cases jointes (comme dans un tableur).
    *   Possède un tableau `reachableCells` pour marquer les cases valides pour une action donnée (par exemple, où un pion peut se déplacer).
*   **Utilisation :** Très utile pour représenter les plateaux de jeu, les zones de stockage de pièces (pots), etc. Vous créerez généralement des sous-classes pour ajouter des logiques spécifiques à votre jeu (par exemple, une méthode pour calculer les cases valides `computeValidCells()`).

### Stages

*   **Description :** Un jeu peut être divisé en plusieurs phases ou niveaux, appelés "stages". La plupart des jeux de plateau simples n'ont qu'un seul stage principal.
*   **Gestion :**
    *   `GameStageModel` : Représente l'état spécifique d'un stage (quels éléments sont présents, qui joue, etc.).
    *   `GameStageView` : Représente l'affichage spécifique d'un stage.
    *   `Controller` : Gère la transition entre les stages (si nécessaire).
*   **Utilisation :** Pour chaque phase distincte de votre jeu (menu principal, jeu principal, écran de fin), vous créerez un ensemble Modèle-Vue-Contrôleur de stage, bien que souvent un seul stage suffise.

### Actions & Animations (Mode Console)

*   **Description :** Les modifications de l'état du jeu (déplacer un pion, piocher une carte) sont représentées par des objets `GameAction`.
*   **Mode Console :** Bien que le framework supporte les animations (pour la version graphique), en mode console, les actions modifient directement le modèle sans effet visuel de transition. Il est crucial d'utiliser les constructeurs de `GameAction` qui *ne prennent pas* de nom d'animation en paramètre.
*   **Gestion :** Les actions sont regroupées dans une `ActionList` et exécutées par un `ActionPlayer`. La classe `ActionFactory` est fortement recommandée pour générer ces `ActionList`s correctement.

---

## 4. Couche Modèle (Model)

Le modèle représente l'état de votre jeu. Vous devrez créer plusieurs classes spécifiques à votre jeu héritant des classes de base de `boardifier-console`.

### Sous-classer `GameElement`

Créez une classe héritant de `GameElement` pour chaque type d'objet unique dans votre jeu.

*   **Constructeur :**
    *   Doit appeler le constructeur `super(model)`.
    *   Doit enregistrer un type unique via `ElementTypes.register()` (voir section `ElementTypes`).
    *   Doit initialiser le type de l'instance avec `this.type = ElementTypes.getType("VotreNomDeType")`.
    *   Initialisez les attributs spécifiques à votre élément (par exemple, couleur, valeur, numéro).
*   **Attributs et Méthodes :** Ajoutez les attributs nécessaires pour représenter l'état de votre élément (par exemple, `int number`, `int color`, `boolean isKinged`). Ajoutez les getters et setters correspondants.
*   **Notification de Changement :** Si la modification d'un attribut doit entraîner une mise à jour visuelle, appelez `addChangeFaceEvent()` dans le setter correspondant (voir section `addChangeFaceEvent`).

```java
import boardifier.control.Logger;
import boardifier.model.ElementTypes;
import boardifier.model.GameElement;
import boardifier.model.GameStageModel;

public class Piece extends GameElement {

    // Définir des constantes pour les états possibles, si nécessaire
    public static final int COLOR_BLACK = 0;
    public static final int COLOR_WHITE = 1;

    private int color;
    private int value; // Exemple d'attribut spécifique
    private boolean promoted; // Exemple d'état pouvant changer visuellement

    // Enregistrer un type unique pour cette classe d'élément
    static {
        ElementTypes.register("piece", 50); // Utiliser une valeur >= 50 pour les types personnalisés
    }

    /**
     * Constructeur d'une pièce.
     * @param value La valeur de la pièce.
     * @param color La couleur de la pièce (ex: Piece.COLOR_BLACK).
     * @param gameStageModel Le modèle du stage auquel cette pièce appartient.
     */
    public Piece(int value, int color, GameStageModel gameStageModel) {
        super(gameStageModel); // Appel obligatoire au constructeur parent
        this.type = ElementTypes.getType("piece"); // Assigner le type enregistré
        this.color = color;
        this.value = value;
        this.promoted = false; // État initial
        Logger.debug("Creating Piece: value=" + value + ", color=" + color, Logger.LOGGER_DEBUG);
    }

    // Getters
    public int getColor() { return color; }
    public int getValue() { return value; }
    public boolean isPromoted() { return promoted; }

    /**
     * Promeut la pièce.
     * Notifie la vue qu'un changement visuel est nécessaire.
     */
    public void promote() {
        if (!this.promoted) {
            this.promoted = true;
            // IMPORTANT: Signaler que l'apparence doit être mise à jour
            addChangeFaceEvent();
            Logger.debug("Piece promoted.", Logger.LOGGER_INFO);
        }
    }

    // Autres méthodes spécifiques au jeu...
}
```

### Sous-classer `ContainerElement`

Créez une classe héritant de `ContainerElement` pour représenter vos plateaux, zones de stockage, etc.

*   **Constructeur :**
    *   Appelez `super(name, x, y, gridWidth, gridHeight, model)`.
    *   Enregistrez et assignez un type si nécessaire (souvent hérité implicitement, mais peut être spécialisé).
*   **Méthodes Spécifiques :** Ajoutez des méthodes liées aux règles de votre jeu. Une méthode courante est `computeValidCells()` pour déterminer où un élément donné peut être placé ou déplacé. Cette méthode met généralement à jour le tableau `reachableCells` hérité.

```java
import boardifier.control.Logger;
import boardifier.model.ContainerElement;
import boardifier.model.GameStageModel;
import boardifier.model.Model;
import boardifier.model.Coord2D;
import java.util.List;
import java.util.ArrayList;

public class GameBoard extends ContainerElement {

    // Enregistrer un type si un comportement très spécifique est nécessaire
    // static { ElementTypes.register("gameboard", 51); }

    /**
     * Constructeur du plateau de jeu.
     * @param x Coordonnée X (colonne) du coin supérieur gauche dans la console.
     * @param y Coordonnée Y (ligne) du coin supérieur gauche dans la console.
     * @param gameStageModel Le modèle du stage.
     */
    public GameBoard(int x, int y, GameStageModel gameStageModel) {
        // Crée un plateau de 8x8 cases
        super("gameboard", x, y, 8, 8, gameStageModel);
        // this.type = ElementTypes.getType("gameboard"); // Si un type a été enregistré
        Logger.debug("Creating GameBoard at (" + x + "," + y + ")", Logger.LOGGER_DEBUG);
    }

    /**
     * Calcule et met à jour les cases atteignables pour une pièce donnée.
     * Met à jour le tableau `reachableCells` hérité.
     * @param piece La pièce pour laquelle calculer les mouvements.
     */
    public void computeValidMoves(Piece piece) {
        // 1. Réinitialiser toutes les cases à 'non atteignable'
        resetReachableCells(false);

        // 2. Trouver la position actuelle de la pièce
        Coord2D pieceCoords = getElementCell(piece);
        if (pieceCoords == null) {
            Logger.warn("Cannot compute moves for a piece not on the board.", Logger.LOGGER_WARNING);
            return; // La pièce n'est pas sur ce plateau
        }
        int r = pieceCoords.getRow();
        int c = pieceCoords.getCol();

        // 3. Implémenter la logique de mouvement spécifique au jeu
        // Exemple simplifié: mouvement d'une case en diagonale avant (pour pièce noire)
        List<Coord2D> possibleMoves = new ArrayList<>();
        if (piece.getColor() == Piece.COLOR_BLACK) {
            if (r + 1 < gridHeight) {
                if (c - 1 >= 0) possibleMoves.add(new Coord2D(r + 1, c - 1));
                if (c + 1 < gridWidth) possibleMoves.add(new Coord2D(r + 1, c + 1));
            }
            // Ajouter la logique pour les pièces promues, les prises, etc.
        } else {
            // Logique pour les pièces blanches...
        }

        // 4. Marquer les cases valides et vides comme atteignables
        for (Coord2D move : possibleMoves) {
            // Vérifier si la case de destination est vide
            if (isEmptyAt(move.getRow(), move.getCol())) {
                reachableCells[move.getRow()][move.getCol()] = true;
                Logger.debug("Cell (" + move.getRow() + "," + move.getCol() + ") marked as reachable.", Logger.LOGGER_TRACE);
            }
            // Ajouter la logique pour les prises ici (si la case contient une pièce adverse)
        }
        // IMPORTANT: Notifier qu'il faut potentiellement redessiner les indicateurs de validité
        addChangeFaceEvent(); // Ou un événement plus spécifique si disponible/nécessaire
    }

    // Peut nécessiter d'autres méthodes: isWinConditionMet(), getScore(), etc.
}
```

### `Model` (Classe)

*   **Description :** Représente l'état global du jeu, indépendant des stages.
*   **Contenu Typique :**
    *   Liste des joueurs (`players`).
    *   ID du joueur courant (`idPlayer`).
    *   ID du gagnant (`idWinner`).
    *   Référence au stage courant (`gameStage`).
*   **Utilisation :** Généralement utilisé tel quel, mais vous interagissez avec lui pour ajouter des joueurs (`addHumanPlayer`, `addComputerPlayer`), obtenir le joueur courant, définir le gagnant (`setIdWinner`), etc.

### `GameStageModel`

*   **Description :** Représente l'état d'un stage spécifique. C'est là que se trouvent les éléments de jeu actifs.
*   **Contenu Clé Hérité :**
    *   `elements`: Liste de tous les `GameElement`s du stage.
    *   `containers`: Liste de tous les `ContainerElement`s du stage.
    *   `selection`: Liste des éléments actuellement sélectionnés.
*   **Nécessite une Sous-classe :** Vous *devez* créer une sous-classe de `GameStageModel` pour chaque stage de votre jeu.
*   **Contenu de la Sous-classe :**
    *   **Attributs :** Déclarez des attributs pour contenir les instances spécifiques de vos `GameElement`s et `ContainerElement`s (par exemple, `private GameBoard board;`, `private Piece[] blackPieces;`).
    *   **Setters Spéciaux :** Créez des setters pour ces attributs. **Crucial :** Dans chaque setter, après avoir assigné l'attribut, vous *devez* ajouter l'élément aux listes gérées par la classe parente en utilisant `addElement()` pour les `GameElement`s simples et `addContainer()` pour les `ContainerElement`s (ceci inclut les sous-classes de `ContainerElement`).
    *   **Callbacks :** Vous pouvez définir des logiques à exécuter automatiquement lorsque des événements se produisent (sélection, ajout/retrait/déplacement dans un conteneur) en utilisant les méthodes `onSelectionChange()`, `onPutInContainer()`, `onRemoveFromContainer()`, `onMoveInContainer()`. Ces méthodes acceptent des fonctions lambda.
    *   **Méthode `getDefaultFactory()` :** Doit être redéfinie pour retourner une instance de votre `StageElementsFactory` correspondante.
    *   **Autres Méthodes :** Ajoutez toute méthode nécessaire pour gérer la logique de ce stage (par exemple, `computeScore()`, `checkEndCondition()`).

```java
import boardifier.model.*;
import boardifier.control.Controller; // Nécessaire pour Controller.FUNCTION_EMPTY
import boardifier.control.Logger;

public class MyGameStageModel extends GameStageModel {

    private GameBoard board;
    private Piece[] blackPieces;
    private Piece[] whitePieces;
    private PiecePot blackPot; // Supposons une classe PiecePot héritant de ContainerElement
    private PiecePot whitePot;
    private TextElement currentPlayerText; // Pour afficher le nom du joueur

    private int blackPiecesLeft; // Compteur spécifique au jeu
    private int whitePiecesLeft;

    /**
     * Constructeur du modèle de stage.
     * @param name Nom du stage.
     * @param model Le modèle global du jeu.
     */
    public MyGameStageModel(String name, Model model) {
        super(name, model);
        // Initialiser les callbacks par défaut (peut être omis si on les définit dans setupCallbacks)
        resetCallbacks();
        // Mettre en place les callbacks spécifiques au jeu
        setupCallbacks();
        Logger.debug("Creating MyGameStageModel named: " + name, Logger.LOGGER_DEBUG);
    }

    // Getters pour les éléments spécifiques
    public GameBoard getBoard() { return board; }
    public Piece[] getBlackPieces() { return blackPieces; }
    public Piece[] getWhitePieces() { return whitePieces; }
    public PiecePot getBlackPot() { return blackPot; }
    public PiecePot getWhitePot() { return whitePot; }
    public TextElement getCurrentPlayerText() { return currentPlayerText; }

    // Setters spéciaux (ajoutent aux listes gérées par GameStageModel)
    public void setBoard(GameBoard board) {
        this.board = board;
        addContainer(board); // IMPORTANT
        Logger.debug("GameBoard set and added to containers list.", Logger.LOGGER_TRACE);
    }

    public void setBlackPieces(Piece[] blackPieces) {
        this.blackPieces = blackPieces;
        for (Piece p : blackPieces) {
            addElement(p); // IMPORTANT
        }
        this.blackPiecesLeft = blackPieces.length; // Initialiser le compteur
        Logger.debug("Black pieces set and added to elements list.", Logger.LOGGER_TRACE);
    }

    public void setWhitePieces(Piece[] whitePieces) {
        this.whitePieces = whitePieces;
        for (Piece p : whitePieces) {
            addElement(p); // IMPORTANT
        }
        this.whitePiecesLeft = whitePieces.length; // Initialiser le compteur
        Logger.debug("White pieces set and added to elements list.", Logger.LOGGER_TRACE);
    }

    public void setBlackPot(PiecePot blackPot) {
        this.blackPot = blackPot;
        addContainer(blackPot); // IMPORTANT
        Logger.debug("BlackPot set and added to containers list.", Logger.LOGGER_TRACE);
    }

     public void setWhitePot(PiecePot whitePot) {
        this.whitePot = whitePot;
        addContainer(whitePot); // IMPORTANT
        Logger.debug("WhitePot set and added to containers list.", Logger.LOGGER_TRACE);
    }

     public void setCurrentPlayerText(TextElement currentPlayerText) {
        this.currentPlayerText = currentPlayerText;
        addElement(currentPlayerText); // IMPORTANT
        Logger.debug("CurrentPlayerText set and added to elements list.", Logger.LOGGER_TRACE);
    }

    /**
     * Retourne l'usine par défaut pour créer les éléments de ce stage.
     * @return Une instance de MyGameStageFactory.
     */
    @Override
    public StageElementsFactory getDefaultFactory() {
        return new MyGameStageFactory(this);
    }

    /**
     * Met en place les callbacks spécifiques au jeu.
     */
    private void setupCallbacks() {
        // Callback appelé lorsqu'un élément est retiré d'un conteneur
        onRemoveFromContainer( (element, container) -> {
            // Si une pièce est retirée du plateau (capturée)
            if (container == board && element.getType() == ElementTypes.getType("piece")) {
                Piece p = (Piece) element;
                if (p.getColor() == Piece.COLOR_BLACK) {
                    whitePiecesLeft--; // Le joueur blanc a capturé une pièce noire
                } else {
                    blackPiecesLeft--; // Le joueur noir a capturé une pièce blanche
                }
                Logger.info("Piece removed from board. Black left: " + blackPiecesLeft + ", White left: " + whitePiecesLeft, Logger.LOGGER_INFO);
                // Vérifier immédiatement si la partie est terminée par capture
                checkEndCondition();
            }
        });

        // Callback appelé lorsqu'un élément est placé dans un conteneur
        onPutInContainer( (element, container, row, col) -> {
             // Si une pièce est placée sur le plateau
            if (container == board && element.getType() == ElementTypes.getType("piece")) {
                 // Vérifier si une pièce doit être promue (ex: arrivée sur la dernière rangée)
                 Piece p = (Piece) element;
                 if ((p.getColor() == Piece.COLOR_BLACK && row == board.getNbRows() - 1) ||
                     (p.getColor() == Piece.COLOR_WHITE && row == 0)) {
                     if (!p.isPromoted()) {
                         p.promote(); // La méthode promote() appelle addChangeFaceEvent()
                         Logger.info("Piece promoted at (" + row + "," + col + ").", Logger.LOGGER_INFO);
                     }
                 }
                 // Vérifier si la partie est terminée (ex: plus de mouvements possibles)
                 // checkEndCondition(); // Peut être appelé ici ou à la fin du tour
             }
        });

        // Mettre d'autres callbacks à Controller.FUNCTION_EMPTY si non utilisés
        onSelectionChange(Controller.FUNCTION_EMPTY);
        onMoveInContainer(Controller.FUNCTION_EMPTY);
    }

    /**
     * Vérifie si une condition de fin de partie est remplie.
     * Si oui, définit le gagnant et arrête le stage.
     */
    public void checkEndCondition() {
        int winner = -1; // -1 = pas de gagnant, 0 = joueur 0, 1 = joueur 1

        if (whitePiecesLeft == 0) {
            winner = model.getIdPlayerByColor(Piece.COLOR_BLACK); // Le joueur noir gagne
        } else if (blackPiecesLeft == 0) {
            winner = model.getIdPlayerByColor(Piece.COLOR_WHITE); // Le joueur blanc gagne
        }
        // Ajouter d'autres conditions (pat, plus de mouvements possibles...)

        if (winner != -1) {
            model.setIdWinner(winner); // Définir le gagnant dans le modèle global
            Logger.info("Game Over! Winner is Player " + winner, Logger.LOGGER_INFO);
            // Demander l'arrêt du stage (sera effectif à la fin de la boucle du contrôleur)
            model.stopStage();
        }
    }
}
```

### `StageElementsFactory`

*   **Description :** Classe responsable de l'instanciation *effective* de tous les `GameElement`s et `ContainerElement`s d'un stage. Cette séparation est **cruciale pour la testabilité**, car elle permet de créer le modèle sans dépendre de la vue ou du contrôleur.
*   **Nécessite une Sous-classe :** Vous *devez* créer une sous-classe de `StageElementsFactory` pour chaque `GameStageModel`.
*   **Méthode `setup()` :** C'est la méthode principale à implémenter.
    *   Elle reçoit le `GameStageModel` en paramètre.
    *   Elle doit instancier tous les éléments de jeu (plateau, pièces, textes...).
    *   Elle doit utiliser les **setters** définis dans votre sous-classe de `GameStageModel` pour assigner ces éléments au modèle du stage.
    *   Elle peut (et doit souvent) placer les éléments initiaux dans leurs conteneurs (par exemple, mettre les pièces sur le plateau ou dans leurs pots de départ) en utilisant les méthodes `addElement()` des `ContainerElement`s.

```java
import boardifier.model.*;
import boardifier.view.*;
import boardifier.control.Logger;

public class MyGameStageFactory extends StageElementsFactory {

    private MyGameStageModel stageModel; // Référence castée pour accès facile

    /**
     * Constructeur de l'usine d'éléments.
     * @param stageModel Le modèle de stage (sera casté en MyGameStageModel).
     */
    public MyGameStageFactory(GameStageModel stageModel) {
        this.stageModel = (MyGameStageModel) stageModel; // Cast pour éviter des casts répétés
    }

    /**
     * Crée et configure tous les éléments du stage.
     */
    @Override
    public void setup() {
        Logger.info("Setting up elements for MyGameStage...", Logger.LOGGER_INFO);

        // 1. Créer le texte du joueur courant
        TextElement playerText = new TextElement(stageModel.getCurrentPlayerName(), stageModel);
        playerText.setLocation(0, 0); // Coin supérieur gauche
        stageModel.setCurrentPlayerText(playerText); // Utiliser le setter

        // 2. Créer le plateau de jeu
        GameBoard board = new GameBoard(2, 2, stageModel); // Ex: plateau à (2,2)
        stageModel.setBoard(board); // Utiliser le setter

        // 3. Créer les pots de pièces (si nécessaire)
        PiecePot blackPot = new PiecePot(20, 2, stageModel); // Ex: pot noir à droite
        stageModel.setBlackPot(blackPot);
        PiecePot whitePot = new PiecePot(20, 10, stageModel); // Ex: pot blanc en dessous
        stageModel.setWhitePot(whitePot);

        // 4. Créer les pièces noires
        Piece[] blackPieces = new Piece[12]; // Supposons 12 pièces
        for (int i = 0; i < blackPieces.length; i++) {
            blackPieces[i] = new Piece(i + 1, Piece.COLOR_BLACK, stageModel);
        }
        stageModel.setBlackPieces(blackPieces); // Utiliser le setter

        // 5. Créer les pièces blanches
        Piece[] whitePieces = new Piece[12];
        for (int i = 0; i < whitePieces.length; i++) {
            whitePieces[i] = new Piece(i + 1, Piece.COLOR_WHITE, stageModel);
        }
        stageModel.setWhitePieces(whitePieces); // Utiliser le setter

        // 6. Placer les pièces initialement (Exemple: Dames)
        // Place les pièces noires
        int pieceIndex = 0;
        for (int r = 0; r < 3; r++) { // Sur les 3 premières rangées
            for (int c = (r + 1) % 2; c < 8; c += 2) { // Cases noires
                if (pieceIndex < blackPieces.length) {
                    try {
                        board.addElement(blackPieces[pieceIndex], r, c);
                        Logger.debug("Placed black piece " + blackPieces[pieceIndex].getValue() + " at (" + r + "," + c + ")", Logger.LOGGER_TRACE);
                        pieceIndex++;
                    } catch (GameElementException e) {
                        Logger.error("Error placing black piece: " + e.getMessage(), Logger.LOGGER_ERR);
                        // Gérer l'erreur (ne devrait pas arriver ici si la logique est correcte)
                    }
                }
            }
        }

        // Place les pièces blanches (sur les 3 dernières rangées)
        pieceIndex = 0;
        for (int r = 5; r < 8; r++) { // Sur les 3 dernières rangées (indices 5, 6, 7)
             for (int c = (r + 1) % 2; c < 8; c += 2) { // Cases noires
                if (pieceIndex < whitePieces.length) {
                    try {
                        board.addElement(whitePieces[pieceIndex], r, c);
                        Logger.debug("Placed white piece " + whitePieces[pieceIndex].getValue() + " at (" + r + "," + c + ")", Logger.LOGGER_TRACE);
                        pieceIndex++;
                    } catch (GameElementException e) {
                         Logger.error("Error placing white piece: " + e.getMessage(), Logger.LOGGER_ERR);
                    }
                }
            }
        }

        // Les pièces non placées restent dans les listes du modèle mais ne sont pas
        // dans un conteneur initialement (elles pourraient être dans les pots).

        Logger.info("Element setup complete.", Logger.LOGGER_INFO);
    }
}
```

### Types d'Éléments (`ElementTypes`)

*   **Objectif :** Permet de catégoriser et d'identifier facilement les `GameElement`s, notamment lorsqu'ils sont récupérés dans des listes génériques.
*   **Fonctionnement :**
    *   `ElementTypes.register(String name, int typeId)` : Enregistre un nouveau type. **Utilisez des `typeId >= 50` pour vos types personnalisés** afin d'éviter les conflits avec les types prédéfinis par boardifier. À appeler une seule fois, typiquement dans un bloc `static {}` de la classe de votre élément.
    *   `ElementTypes.getType(String name)` : Récupère l'`int` associé à un nom de type enregistré. À utiliser dans le constructeur de votre élément pour assigner la valeur à l'attribut `this.type`.

### Notifier les Changements Visuels (`addChangeFaceEvent`)

*   **Objectif :** Signaler à la couche Vue qu'un élément a changé d'état d'une manière qui nécessite une mise à jour de son apparence (son "Look").
*   **Quand l'appeler :** Dans les méthodes (typiquement les setters) de vos sous-classes de `GameElement` qui modifient un attribut dont dépend le rendu visuel. Par exemple, si un pion devient une dame et doit être affiché différemment.
*   **Conséquence :** La méthode `render()` du `ElementLook` associé sera appelée lors de la prochaine mise à jour de la vue. **Oublier cet appel signifie que le changement d'état ne sera pas reflété visuellement.**
*   **Cas Gérés Automatiquement :** Le changement de visibilité (`setVisible()`) est géré automatiquement par le framework, pas besoin d'appel explicite à `addChangeFaceEvent()`. La mise à jour de la position lors de l'ajout/suppression/déplacement dans un `ContainerElement` est également gérée.

---

## 5. Couche Vue (View)

La vue est responsable de l'affichage de l'état du modèle dans la console.

### `ElementLook`

*   **Description :** Classe de base pour la représentation visuelle d'un `GameElement`. En mode console, cette représentation est un tableau 2D de `String` (`shape`).
*   **Nécessite une Sous-classe :** Vous devez créer une sous-classe de `ElementLook` pour chaque sous-classe de `GameElement` que vous avez créée.
*   **Constructeur :** Appelez `super(gameElement, height, width)` où `height` et `width` sont les dimensions en caractères du look.
*   **Méthode `render()` :**
    *   **Obligatoire :** Vous *devez* implémenter cette méthode.
    *   **Rôle :** Remplir le tableau `shape[line][column]` avec les `String` représentant l'élément. Utilisez les constantes de `ConsoleColor` pour la couleur et le fond.
    *   **Logique :** Le code de `render()` doit typiquement vérifier l'état de l'`element` associé (casté dans le type spécifique, par exemple `(Piece)element`) et définir le `shape` en conséquence.
    *   **Appel Automatique :** Cette méthode est appelée par le framework lorsque l'élément est créé ou lorsque `addChangeFaceEvent()` a été appelé sur l'élément modèle associé.

```java
import boardifier.view.ElementLook;
import boardifier.model.GameElement;
import boardifier.view.ConsoleColor;

public class PieceLook extends ElementLook {

    /**
     * Constructeur du look d'une pièce.
     * @param piece L'élément modèle (Piece) associé.
     */
    public PieceLook(Piece piece) {
        // Une pièce simple occupe 1x1 caractère
        super(piece, 1, 1);
        // Le rendu initial est fait immédiatement
        render();
    }

    /**
     * Définit l'apparence de la pièce dans le tableau `shape`.
     * Appelé automatiquement quand nécessaire.
     */
    @Override
    public void render() {
        // Récupérer l'élément modèle associé et le caster
        Piece piece = (Piece) element;

        // Déterminer l'apparence en fonction de l'état de la pièce
        String symbol = String.valueOf(piece.getValue()); // Afficher sa valeur/numéro par défaut
        String foreground = ConsoleColor.BLACK;
        String background = ConsoleColor.WHITE_BACKGROUND;

        if (piece.getColor() == Piece.COLOR_BLACK) {
            foreground = ConsoleColor.WHITE;
            background = ConsoleColor.BLACK_BACKGROUND;
        } else { // COLOR_WHITE
            foreground = ConsoleColor.BLACK;
            background = ConsoleColor.RED_BACKGROUND; // Exemple: fond rouge pour les blancs
        }

        // Si la pièce est promue, changer le symbole (Exemple: mettre en majuscule ou ajouter '*')
        if (piece.isPromoted()) {
            symbol = symbol.toUpperCase() + "*"; // Exemple: "1*"
             // Ajuster la taille si le symbole devient plus grand
             // Si symbol devient "1*", il faut potentiellement width=2
             // NB: Il est préférable de définir la taille max dans le constructeur
             // et de padder avec des espaces si besoin. Ici, on suppose 1x1.
             if (symbol.length() > 1) symbol = symbol.substring(0,1); // Tronquer si dépasse
        }

        // Appliquer les couleurs et le symbole au tableau shape
        shape[0][0] = background + foreground + symbol + ConsoleColor.RESET;
    }
}
```

### `ContainerLook` et ses sous-classes

*   **Description :** Classes spécialisées pour afficher les `ContainerElement`s. Elles gèrent automatiquement le positionnement et le rendu des `ElementLook`s des éléments contenus.
*   **Sous-classes Courantes :**
    *   `GridLook`: Pour les grilles simples sans cases jointes, taille de case fixe, avec/sans bordures.
    *   `ClassicBoardLook`: Hérite de `GridLook`, ajoute la numérotation des lignes/colonnes. **Souvent le meilleur choix pour les plateaux de jeu standard.**
    *   `TableLook`: Plus flexible, gère les cases jointes, taille de case fixe ou variable. Moins performant si `GridLook` ou `ClassicBoardLook` suffisent.
*   **Utilisation :** Vous instanciez généralement l'une de ces classes (ou une sous-classe que vous créez) en lui passant le `ContainerElement` modèle correspondant.
*   **Personnalisation :** Si vous voulez modifier l'apparence par défaut (par exemple, le style des bordures), vous pouvez créer une sous-classe (par exemple de `GridLook`) et redéfinir les méthodes `renderBorders()`, `renderCoords()`, ou même `render()` (moins courant). La méthode `renderInners()` qui dessine les looks contenus est rarement redéfinie.

```java
import boardifier.view.ClassicBoardLook;
import boardifier.model.ContainerElement; // Ou GameBoard si on type spécifiquement

// Utilisation directe de ClassicBoardLook (pas de sous-classe nécessaire ici)
// Dans la méthode createLooks() de votre GameStageView:
// GameBoard boardModel = model.getBoard(); // Récupérer le modèle du plateau
// ClassicBoardLook boardLook = new ClassicBoardLook(
//     boardModel.getNbRows(), // Hauteur de case (en caractères)
//     boardModel.getNbCols(), // Largeur de case (en caractères)
//     boardModel,             // Le modèle ContainerElement associé
//     1,                      // Largeur de la bordure des lignes
//     1,                      // Largeur de la bordure des colonnes
//     true                    // Afficher les coordonnées ?
// );
// addLook(boardLook); // Ajouter le look à la GameStageView

// --- OU ---

// Exemple de personnalisation (si nécessaire)
import boardifier.view.GridLook;
import boardifier.model.ContainerElement;
import boardifier.view.ConsoleColor;

public class CustomPotLook extends GridLook {

    /**
     * Constructeur pour un look de pot personnalisé.
     * @param container L'élément modèle (ex: PiecePot).
     */
    public CustomPotLook(ContainerElement container) {
        // Pot de 4x1 cases, chaque case de 1x1 caractère, pas de bordures épaisses
        super(1, 1, container, 0, 0, false);
    }

    /**
     * Redéfinition pour dessiner des bordures personnalisées (ex: juste des |).
     */
    @Override
    protected void renderBorders() {
        // Logique très simplifiée: dessine des | entre les colonnes
        for(int i = 0; i < gridHeight; ++i) {
            for(int j = 0; j < gridWidth - 1; ++j) {
                // Position dans le shape global
                int x = (j + 1) * cellWidth; // Position de la bordure verticale
                int y = i * cellHeight + cellHeight / 2; // Milieu de la cellule en hauteur
                if (y < height) { // Vérifier les limites du shape
                   shape[y][x] = ConsoleColor.YELLOW + "|" + ConsoleColor.RESET;
                }
            }
        }
        // NB: La logique réelle de renderBorders est plus complexe pour gérer
        // les coins, les bordures externes, etc. Ceci est un exemple très simple.
    }
}
```

### `GameStageView`

*   **Description :** Conteneur pour tous les `ElementLook`s d'un stage spécifique. Ne fait pas l'affichage direct, mais organise les looks.
*   **Nécessite une Sous-classe :** Vous *devez* créer une sous-classe de `GameStageView` pour chaque `GameStageModel`.
*   **Méthode `createLooks()` :**
    *   **Obligatoire :** Vous *devez* implémenter cette méthode.
    *   **Rôle :** Instancier tous les `ElementLook`s nécessaires pour les `GameElement`s définis dans le `GameStageModel` correspondant.
    *   **Fonctionnement :** Récupérez les éléments du modèle (passé au constructeur et accessible via `gameStageModel`), créez leurs looks correspondants (par exemple, `new PieceLook(model.getBlackPieces()[i])`), et ajoutez-les à la vue du stage en utilisant `addLook(elementLook)`.

```java
import boardifier.view.*;
import boardifier.model.*;
// Importer vos classes de modèle et de look spécifiques
// import model.MyGameStageModel;
// import model.GameBoard;
// import model.Piece;
// import view.PieceLook;
// import view.CustomPotLook;

public class MyGameStageView extends GameStageView {

    /**
     * Constructeur de la vue du stage.
     * @param name Nom de la vue (correspond souvent au nom du stage).
     * @param gameStageModel Le modèle de stage associé.
     */
    public MyGameStageView(String name, GameStageModel gameStageModel) {
        super(name, gameStageModel);
        // La création des looks est différée à l'appel de createLooks() par le framework
    }

    /**
     * Crée les looks pour tous les éléments du modèle de stage.
     * Appelé automatiquement par le framework après l'instanciation de la vue.
     */
    @Override
    public void createLooks() {
        // Caster le modèle pour accéder aux éléments spécifiques
        MyGameStageModel model = (MyGameStageModel) gameStageModel;

        // 1. Créer le look pour le texte du joueur
        // TextLook est fourni par boardifier
        addLook(new TextLook(model.getCurrentPlayerText()));

        // 2. Créer le look pour le plateau
        // Utilisation de ClassicBoardLook (fourni par boardifier)
        GameBoard boardModel = model.getBoard();
        // Supposons des cases de 2 lignes x 4 colonnes de caractères
        ClassicBoardLook boardLook = new ClassicBoardLook(2, 4, boardModel, 1, 1, true);
        addLook(boardLook);

        // 3. Créer les looks pour les pots (en utilisant notre look personnalisé)
        // addLook(new CustomPotLook(model.getBlackPot()));
        // addLook(new CustomPotLook(model.getWhitePot()));
        // OU utiliser un look standard si CustomPotLook n'existe pas:
         addLook(new GridLook(1, 1, model.getBlackPot(), 1, 1, false)); // Cases 1x1, bordures fines
         addLook(new GridLook(1, 1, model.getWhitePot(), 1, 1, false));

        // 4. Créer les looks pour toutes les pièces
        for (Piece p : model.getBlackPieces()) {
            addLook(new PieceLook(p));
        }
        for (Piece p : model.getWhitePieces()) {
            addLook(new PieceLook(p));
        }

        Logger.info("Looks created for MyGameStageView.", Logger.LOGGER_INFO);
    }
}
```

### `RootPane` et `View`

*   `RootPane` : Le "panneau" interne où tous les looks sont effectivement dessinés à leurs coordonnées texte. Sa méthode `update()` parcourt les looks et construit l'image texte finale.
*   `View` : L'objet de plus haut niveau pour la vue, qui contient le `RootPane` et gère l'affichage global dans la fenêtre/console. En mode console, son rôle est assez limité, mais il maintient la cohérence architecturale avec la version graphique.

Vous interagissez peu directement avec ces classes, elles sont principalement gérées par le `Controller`.

---

## 6. Couche Contrôleur (Control)

Le contrôleur fait le lien entre le modèle et la vue, gère le flux du jeu et les interactions.

### `Controller` (Classe)

*   **Description :** Gère le déroulement global du jeu : lancement, boucle de jeu par stage, tours des joueurs, fin de partie.
*   **Nécessite une Sous-classe :** Vous *devez* créer une sous-classe de `Controller`.
*   **Méthodes Clés à Implémenter/Comprendre :**
    *   `stageLoop()` : La boucle principale pour un stage donné. Typiquement, elle boucle tant que `model.isStageStopped()` est faux. À chaque itération, elle appelle `playTurn()`, `endOfTurn()`, et `update()` (pour rafraîchir le modèle et la vue). Vous devez souvent redéfinir cette méthode pour implémenter la logique spécifique de votre boucle de stage.
    *   `playTurn()` : Gère le tour du joueur courant. Doit déterminer si le joueur est humain ou ordinateur. Si humain, attend une entrée ; si ordinateur, appelle un `Decider`. À la fin, elle doit exécuter les actions choisies (via `ActionPlayer`). Vous devez implémenter cette méthode selon les règles de votre jeu.
    *   `endOfTurn()` : Effectue les actions de fin de tour : passer au joueur suivant (`model.setNextPlayer()`), mettre à jour l'affichage du nom du joueur, potentiellement vérifier des conditions de fin de jeu. La logique de base de changement de joueur est souvent suffisante, mais peut être redéfinie.
    *   `analyseAndPlay()` (Méthode d'aide fréquente) : Une méthode que vous écrivez souvent dans votre sous-classe pour prendre l'entrée d'un joueur humain, la valider, et si elle est valide, créer et jouer l'`ActionList` correspondante en utilisant `ActionFactory` et `ActionPlayer`.
*   **Initialisation :** Dans le `main()`, vous créez une instance de votre contrôleur en lui passant le `Model` et la `View`. Vous définissez le premier stage avec `setFirstStageName()` puis lancez le jeu avec `startGame()` et entrez dans la boucle avec `stageLoop()`.

```java
import boardifier.control.*;
import boardifier.model.*;
import boardifier.view.*;
import java.util.Scanner;
// Importer vos classes spécifiques
// import model.MyGameStageModel;
// import model.GameBoard;
// import model.Piece;
// import control.MyGameDecider; // Votre classe Decider

public class MyGameController extends Controller {

    private Scanner scanner; // Pour lire l'entrée humaine
    private boolean firstPlayer; // Pour gérer le premier tour différemment si besoin

    /**
     * Constructeur du contrôleur.
     * @param model Le modèle global du jeu.
     * @param view La vue globale du jeu.
     */
    public MyGameController(Model model, View view) {
        super(model, view);
        this.scanner = new Scanner(System.in);
        this.firstPlayer = true; // Initialisation
        Logger.info("MyGameController created.", Logger.LOGGER_INFO);
    }

    /**
     * Boucle principale du jeu pour le stage courant.
     * Redéfinie pour la logique spécifique de ce jeu.
     */
    @Override
    public void stageLoop() {
        Logger.info("Entering stage loop for stage: " + model.getStageName(), Logger.LOGGER_INFO);
        update(); // Affichage initial

        while (!model.isStageStopped()) {
            Logger.debug("Start of turn for player " + model.getIdPlayer(), Logger.LOGGER_DEBUG);
            playTurn();
            if (!model.isStageStopped()) { // Vérifier si playTurn a arrêté le stage
                endOfTurn();
                update(); // Mettre à jour l'affichage après le changement de joueur
            }
        }

        // Fin du stage
        Logger.info("Stage " + model.getStageName() + " finished.", Logger.LOGGER_INFO);
        endGame(); // Afficher le message de fin de partie
    }

    /**
     * Gère le tour du joueur courant.
     */
    private void playTurn() {
        Player currentPlayer = model.getCurrentPlayer();
        MyGameStageModel currentStageModel = (MyGameStageModel) model.getGameStage();

        if (currentPlayer.getType() == Player.HUMAN) {
            // Logique pour joueur humain
            boolean validInput = false;
            while(!validInput) {
                System.out.print(currentPlayer.getName() + ", entrez votre coup (ex: '1 A2 B3' pour pièce 1 de A2 à B3): ");
                String input = scanner.nextLine();
                // Analyser l'entrée (méthode à créer)
                validInput = analyseAndPlayHuman(input);
                if (!validInput) {
                    System.out.println("Entrée invalide. Réessayez.");
                }
            }
        } else {
            // Logique pour joueur ordinateur
            System.out.println(currentPlayer.getName() + " (ordinateur) réfléchit...");
            MyGameDecider decider = new MyGameDecider(model, this); // Créer le décideur
            ActionList actions = decider.decide(); // Obtenir la décision

            if (actions != null && !actions.isEmpty()) {
                 // Exécuter les actions décidées par l'IA
                 ActionPlayer actionPlayer = new ActionPlayer(model, this);
                 actionPlayer.playActions(actions);
                 Logger.info("Computer played its turn.", Logger.LOGGER_INFO);
            } else {
                 Logger.warn("Computer could not decide on a move.", Logger.LOGGER_WARNING);
                 // Gérer le cas où l'IA ne peut pas jouer (pat? erreur?)
                 // Peut-être forcer la fin du tour ou arrêter le jeu
                 model.stopStage(); // Exemple: arrêter si l'IA est bloquée
            }
        }
         // Une fois le coup joué (humain ou IA), vérifier la fin de partie
         // La vérification peut aussi être dans un callback onPut/onRemove
         currentStageModel.checkEndCondition();
    }


    /**
     * Analyse l'entrée du joueur humain et joue le coup si valide.
     * @param input La chaîne entrée par l'utilisateur.
     * @return true si l'entrée était valide et le coup a été joué, false sinon.
     */
    private boolean analyseAndPlayHuman(String input) {
        MyGameStageModel stageModel = (MyGameStageModel) model.getGameStage();
        GameBoard board = stageModel.getBoard();
        // 1. Parser l'input: "1 A2 B3" -> pieceNumber=1, start=(1,0), end=(2,1) (adapter à votre format)
        //    Ceci demande une logique de parsing robuste.
        // Exemple très simplifié (NE PAS UTILISER TEL QUEL EN PRODUCTION):
        try {
            String[] parts = input.trim().split("\\s+");
            if (parts.length != 3) return false;

            int pieceId = Integer.parseInt(parts[0]); // Peut lever NumberFormatException
            Coord2D startCoords = parseCoords(parts[1]); // Méthode à créer, ex: "A2" -> (1, 0)
            Coord2D endCoords = parseCoords(parts[2]);   // Méthode à créer, ex: "B3" -> (2, 1)

            if (startCoords == null || endCoords == null) return false;

            // 2. Trouver la pièce correspondante
            Piece selectedPiece = findPieceByIdAndLocation(pieceId, startCoords.getRow(), startCoords.getCol());
            if (selectedPiece == null || selectedPiece.getColor() != model.getCurrentPlayer().getColor()) {
                 System.out.println("Pièce invalide ou n'appartient pas au joueur courant.");
                 return false; // Pièce non trouvée ou pas la bonne couleur
            }

            // 3. Vérifier si le mouvement est valide
            // a) Calculer les mouvements valides pour cette pièce
            board.computeValidMoves(selectedPiece);
            // b) Vérifier si la destination est dans reachableCells
            if (!board.isReachable(endCoords.getRow(), endCoords.getCol())) {
                 System.out.println("Mouvement invalide pour cette pièce.");
                 board.resetReachableCells(false); // Nettoyer l'affichage des cases valides
                 return false;
            }
            board.resetReachableCells(false); // Nettoyer l'affichage des cases valides

            // 4. Si tout est valide, créer et jouer l'ActionList
            // Utiliser ActionFactory est fortement recommandé
            ActionList actions = ActionFactory.generateMoveWithinContainer(model, selectedPiece,
                                                                           "gameboard", // Nom du ContainerElement (plateau)
                                                                           endCoords.getRow(), endCoords.getCol());

            // Gérer les prises: Si le mouvement est une prise, il faut ajouter une action
            // pour retirer la pièce adverse. La logique de détection de prise est complexe
            // et dépend du jeu (ex: si on saute par-dessus une pièce).
            // if (isCaptureMove(selectedPiece, startCoords, endCoords)) {
            //     Piece capturedPiece = getPieceAt(intermediateCoord); // Pièce sur la case sautée
            //     ActionList removeAction = ActionFactory.generateRemoveFromStage(model, capturedPiece);
            //     actions.addAll(removeAction); // Ajouter l'action de suppression
            // }

            // Indiquer que ce coup termine le tour du joueur
            actions.setDoEndOfTurn(true);

            // Jouer les actions
            ActionPlayer actionPlayer = new ActionPlayer(model, this);
            actionPlayer.playActions(actions);

            return true; // Coup valide et joué

        } catch (NumberFormatException e) {
            System.out.println("Format du numéro de pièce invalide.");
            return false;
        } catch (Exception e) { // Capturer d'autres erreurs potentielles (parsing, etc.)
            System.out.println("Erreur lors de l'analyse de l'entrée: " + e.getMessage());
            Logger.error("Error parsing human input: " + input + " - " + e.getMessage(), Logger.LOGGER_ERR);
            return false;
        }
    }

    /**
     * Méthode d'aide (à implémenter) pour parser les coordonnées type "A2".
     */
    private Coord2D parseCoords(String s) {
        s = s.toUpperCase();
        if (s.length() != 2) return null;
        int col = s.charAt(0) - 'A';
        int row = s.charAt(1) - '1'; // '1' correspond à la ligne 0 dans l'indexation tableau
         // Ajouter des vérifications de limites (col < 8, row < 8, etc.)
        if (col < 0 || col >= 8 || row < 0 || row >= 8) return null; // Ex: pour un plateau 8x8
        return new Coord2D(row, col);
    }

     /**
      * Méthode d'aide (à implémenter) pour trouver une pièce par son ID et sa position.
      */
     private Piece findPieceByIdAndLocation(int id, int row, int col) {
         MyGameStageModel stageModel = (MyGameStageModel) model.getGameStage();
         GameBoard board = stageModel.getBoard();
         GameElement element = board.getElement(row, col);
         if (element instanceof Piece) {
             Piece p = (Piece) element;
             // Vérifier si l'ID correspond (si vos pièces ont un ID unique affiché)
             // Ou si c'est la seule pièce à cet endroit
             // if (p.getValue() == id) { // Si getValue() retourne l'ID affiché
                 return p;
             // }
         }
         return null;
     }


    /**
     * Actions à effectuer à la fin du tour d'un joueur.
     */
    @Override
    protected void endOfTurn() {
        // Passer au joueur suivant
        model.setNextPlayer();
        // Mettre à jour le texte affichant le nom du joueur courant
        MyGameStageModel stageModel = (MyGameStageModel) model.getGameStage();
        TextElement playerText = stageModel.getCurrentPlayerText();
        if (playerText != null) {
            playerText.setText(model.getCurrentPlayerName());
            // Pas besoin d'addChangeFaceEvent ici, TextElement le gère en interne via setText
        }
        this.firstPlayer = false; // Le premier tour est passé
        Logger.debug("End of turn. Next player is " + model.getIdPlayer(), Logger.LOGGER_DEBUG);
    }

    /**
     * Afficher le résultat final de la partie.
     */
    private void endGame() {
        int winnerId = model.getIdWinner();
        if (winnerId != -1) {
            Player winner = model.getPlayer(winnerId);
            System.out.println("\n=============================");
            System.out.println(" Partie terminée!");
            System.out.println(" Le gagnant est: " + winner.getName());
            System.out.println("=============================");
        } else {
            // Cas d'égalité ou partie interrompue
            System.out.println("\n=============================");
            System.out.println(" Partie terminée! (Égalité ou interrompue)");
            System.out.println("=============================");
        }
        // Potentiellement attendre une entrée avant de fermer ou proposer une nouvelle partie
    }
}
```

### `Decider`

*   **Description :** Classe abstraite pour implémenter l'intelligence artificielle (IA) d'un joueur ordinateur.
*   **Nécessite une Sous-classe :** Vous *devez* créer une sous-classe de `Decider` pour votre IA.
*   **Méthode `decide()` :**
    *   **Obligatoire :** Vous *devez* implémenter cette méthode.
    *   **Rôle :** Analyser l'état actuel du jeu (via le `model` accessible) et choisir le "meilleur" coup à jouer selon la stratégie de l'IA.
    *   **Retour :** Doit retourner une `ActionList` représentant le coup choisi. Utilisez `ActionFactory` pour générer cette liste. N'oubliez pas d'appeler `actions.setDoEndOfTurn(true)` si le coup termine le tour de l'IA. Si l'IA ne peut pas jouer (aucun coup valide), retournez `null` ou une `ActionList` vide.

```java
import boardifier.control.ActionFactory;
import boardifier.control.ActionPlayer;
import boardifier.control.Controller;
import boardifier.control.Decider;
import boardifier.model.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
// Importer vos classes spécifiques
// import model.MyGameStageModel;
// import model.GameBoard;
// import model.Piece;

public class MyGameDecider extends Decider {

    private static Random random = new Random(System.currentTimeMillis());

    /**
     * Constructeur du décideur.
     * @param model Le modèle global du jeu.
     * @param controller Le contrôleur du jeu.
     */
    public MyGameDecider(Model model, Controller controller) {
        super(model, controller);
    }

    /**
     * Décide du prochain coup pour l'ordinateur.
     * Stratégie: Choisit une pièce au hasard parmi celles qui peuvent bouger,
     * puis choisit une destination valide au hasard pour cette pièce.
     * @return Une ActionList représentant le coup choisi, ou null si aucun coup n'est possible.
     */
    @Override
    public ActionList decide() {
        MyGameStageModel stageModel = (MyGameStageModel) model.getGameStage();
        GameBoard board = stageModel.getBoard();
        int currentPlayerId = model.getIdPlayer();
        int currentPlayerColor = model.getPlayer(currentPlayerId).getColor(); // Suppose que Player a une couleur

        // 1. Lister toutes les pièces du joueur courant qui sont sur le plateau
        List<Piece> myPiecesOnBoard = new ArrayList<>();
        for (int r = 0; r < board.getNbRows(); r++) {
            for (int c = 0; c < board.getNbCols(); c++) {
                GameElement element = board.getElement(r, c);
                if (element instanceof Piece) {
                    Piece p = (Piece) element;
                    if (p.getColor() == currentPlayerColor) {
                        myPiecesOnBoard.add(p);
                    }
                }
            }
        }

        if (myPiecesOnBoard.isEmpty()) {
            Logger.warn("Computer has no pieces left on board.", Logger.LOGGER_WARNING);
            return null; // Aucune pièce à jouer
        }

        // 2. Mélanger la liste pour choisir une pièce au hasard
        java.util.Collections.shuffle(myPiecesOnBoard, random);

        // 3. Essayer chaque pièce jusqu'à en trouver une qui peut bouger
        for (Piece pieceToMove : myPiecesOnBoard) {
            // a) Calculer les mouvements valides pour cette pièce
            board.computeValidMoves(pieceToMove);

            // b) Lister les destinations valides
            List<Coord2D> validDestinations = new ArrayList<>();
            for (int r = 0; r < board.getNbRows(); r++) {
                for (int c = 0; c < board.getNbCols(); c++) {
                    if (board.isReachable(r, c)) {
                        validDestinations.add(new Coord2D(r, c));
                    }
                }
            }

            // c) Si des mouvements sont possibles pour cette pièce
            if (!validDestinations.isEmpty()) {
                // Choisir une destination au hasard
                Coord2D chosenDestination = validDestinations.get(random.nextInt(validDestinations.size()));

                // Nettoyer l'affichage des cases valides avant de générer l'action
                board.resetReachableCells(false);

                // Générer l'ActionList pour ce mouvement
                ActionList actions = ActionFactory.generateMoveWithinContainer(
                    model,
                    pieceToMove,
                    board.getName(), // Utiliser le nom du conteneur ("gameboard")
                    chosenDestination.getRow(),
                    chosenDestination.getCol()
                );

                // Ajouter la gestion des prises ici si nécessaire (comme dans analyseAndPlayHuman)

                // Marquer comme fin de tour
                actions.setDoEndOfTurn(true);
                Logger.info("Computer decided to move piece from " + board.getElementCell(pieceToMove) + " to " + chosenDestination, Logger.LOGGER_INFO);
                return actions; // Retourner la première action valide trouvée
            }
             // Si aucune destination valide pour cette pièce, continuer avec la suivante
             board.resetReachableCells(false); // Nettoyer même si on n'a pas bougé
        }

        // Si on arrive ici, aucune pièce ne peut bouger
        Logger.warn("Computer cannot find any valid move.", Logger.LOGGER_WARNING);
        return null; // Aucun coup possible
    }
}
```

---

## 7. Gestion des Actions

Le framework utilise un système d'actions pour modifier l'état du jeu de manière contrôlée et cohérente, surtout lors de l'utilisation conjointe avec la version graphique (animations). Même en console, ce système est utilisé.

### `GameAction`

*   **Description :** Classe de base abstraite pour toutes les actions de jeu.
*   **Sous-classes Fournies :**
    *   `PutInContainerAction`: Placer un élément (qui n'est dans aucun conteneur) dans un conteneur.
    *   `RemoveFromContainerAction`: Retirer un élément d'un conteneur (il reste dans le stage).
    *   `MoveWithinContainerAction`: Déplacer un élément d'une case à une autre dans le *même* conteneur.
    *   `RemoveFromStageAction`: Retirer complètement un élément du jeu (le rend invisible et le place hors champ).
    *   `DrawDiceAction`: Gérer un lancer de dés (pour les `GameElement` de type dé).
*   **Méthode `start()`:** Contient le code qui modifie *effectivement* le modèle. Appelée par l'`ActionPlayer`.
*   **Création Manuelle :** Il est **fortement déconseillé** de créer et gérer manuellement des instances de `GameAction` à cause des contraintes internes du framework (par exemple, on ne peut pas déplacer directement entre conteneurs, il faut remove puis put). **Utilisez `ActionFactory` à la place.**

### `ActionList`

*   **Description :** Une liste ordonnée d'objets `GameAction`.
*   **Fonctionnement :** Les actions sont ajoutées à la liste (généralement via `ActionFactory`) et seront exécutées séquentiellement par l'`ActionPlayer`.
*   **Méthodes Utiles :**
    *   `addSingleAction(GameAction action)`: Ajoute une action.
    *   `addAll(ActionList otherList)`: Concatène une autre `ActionList` à la fin de celle-ci (utile pour combiner un mouvement et une capture, par exemple).
    *   `setDoEndOfTurn(boolean doEnd)`: Si `true`, l'`ActionPlayer` signalera au `Controller` de passer au joueur suivant *après* l'exécution de toutes les actions de cette liste. **Très important à mettre à `true` pour l'action principale d'un tour.**
    *   `isEmpty()`: Vérifie si la liste est vide.

### `ActionFactory`

*   **Description :** Classe utilitaire **essentielle** pour créer des `ActionList`s pour les opérations courantes. Elle gère automatiquement les contraintes du framework (comme le remove/put pour un déplacement inter-conteneurs).
*   **Méthodes Statiques Clés :**
    *   `generatePutInContainer(Model model, GameElement element, String destContainerName, int rowDest, int colDest)`: Crée l'ActionList pour placer `element` dans le conteneur nommé `destContainerName` aux coordonnées `rowDest`, `colDest`. Gère le cas où `element` était déjà dans un autre conteneur (fait le remove + put).
    *   `generateMoveWithinContainer(Model model, GameElement element, String containerName, int rowDest, int colDest)`: Crée l'ActionList pour déplacer `element` à l'intérieur du conteneur `containerName`.
    *   `generateRemoveFromContainer(Model model, GameElement element, String srcContainerName)`: Crée l'ActionList pour retirer `element` du conteneur `srcContainerName`.
    *   `generateRemoveFromStage(Model model, GameElement element)`: Crée l'ActionList pour retirer `element` complètement du jeu.
*   **Utilisation :** C'est la **méthode recommandée** pour créer les séquences d'actions dans votre `Controller` (pour les coups humains) et votre `Decider` (pour l'IA).

### `ActionPlayer`

*   **Description :** Classe responsable de l'exécution des `GameAction`s contenues dans une `ActionList`.
*   **Méthode `playActions(ActionList actions)`:**
    *   Parcourt la liste `actions`.
    *   Appelle la méthode `start()` de chaque `GameAction`.
    *   (En mode graphique, gérerait les animations).
    *   Si `actions.getDoEndOfTurn()` est `true`, appelle `controller.EndOfTurn()` (indirectement via un mécanisme interne) après avoir joué toutes les actions.
*   **Utilisation :** Vous créez une instance d'`ActionPlayer(model, controller)` et appelez `playActions()` en lui passant l'`ActionList` générée (typiquement par `ActionFactory`).

---

## 8. Mise en Place Initiale (Classe Principale)

La classe contenant votre méthode `main()` est responsable de l'initialisation et du lancement du jeu.

*   **Étapes Typiques :**
    1.  Créer une instance du `Model` global : `Model model = new Model();`
    2.  Définir les joueurs : Utilisez `model.addHumanPlayer("Nom")` et `model.addComputerPlayer("Nom")`. Assignez les couleurs ou autres identifiants si nécessaire (`model.getPlayer(0).setColor(...)`).
    3.  **Enregistrer les Stages :** Pour chaque stage de votre jeu, vous devez lier son nom à ses classes Modèle et Vue spécifiques en utilisant `StageFactory.registerModelAndView("nomDuStage", "package.model.VotreStageModel", "package.view.VotreStageView");`.
    4.  Créer la `View` globale : `View view = new View(model);`
    5.  Créer votre `Controller` : `VotreController control = new VotreController(model, view);`
    6.  Définir le premier stage à lancer : `control.setFirstStageName("nomDuStageInitial");`
    7.  Lancer le jeu : `control.startGame();` (initialise le premier stage).
    8.  Entrer dans la boucle de jeu du stage : `control.stageLoop();`.
    9.  Gérer les exceptions (`GameException`) autour de `startGame()`.

```java
import boardifier.model.*;
import boardifier.view.*;
import boardifier.control.*;
// Importer vos classes Controller, StageModel, StageView
// import control.MyGameController;

public class MyGameConsole {

    public static void main(String[] args) {

        // Configuration initiale (peut être via args ou Scanner)
        int mode = 0; // 0=HvsH, 1=HvsC, 2=CvsC
        // Exemple: lire le mode depuis la console
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choisissez le mode de jeu:");
        System.out.println("0: Humain vs Humain");
        System.out.println("1: Humain vs Ordinateur");
        System.out.println("2: Ordinateur vs Ordinateur");
        System.out.print("Votre choix: ");
        try {
            mode = Integer.parseInt(scanner.nextLine());
            if (mode < 0 || mode > 2) mode = 0; // Défaut si invalide
        } catch (NumberFormatException e) {
            mode = 0; // Défaut si erreur
        }

        // 1. Créer le Modèle global
        Model model = new Model();
        model.setCaptureEnabled(true); // Activer la capture (si gérée par votre jeu)

        // 2. Ajouter les joueurs en fonction du mode
        if (mode == 0) {
            model.addHumanPlayer("Joueur Noir");
            model.addHumanPlayer("Joueur Blanc");
            model.getPlayer(0).setColor(Piece.COLOR_BLACK); // Assigner couleur/ID spécifique
            model.getPlayer(1).setColor(Piece.COLOR_WHITE);
        } else if (mode == 1) {
            model.addHumanPlayer("Vous");
            model.addComputerPlayer("Ordinateur");
            model.getPlayer(0).setColor(Piece.COLOR_BLACK);
            model.getPlayer(1).setColor(Piece.COLOR_WHITE);
        } else { // mode == 2
            model.addComputerPlayer("Ordi Noir");
            model.addComputerPlayer("Ordi Blanc");
             model.getPlayer(0).setColor(Piece.COLOR_BLACK);
            model.getPlayer(1).setColor(Piece.COLOR_WHITE);
        }

        // 3. Enregistrer le(s) stage(s)
        // Assurez-vous que les noms de packages et de classes sont corrects
        StageFactory.registerModelAndView(
            "mygame",                      // Nom du stage
            "model.MyGameStageModel",      // Chemin complet de la classe modèle du stage
            "view.MyGameStageView"         // Chemin complet de la classe vue du stage
        );

        // 4. Créer la Vue globale
        View view = new View(model);

        // 5. Créer le Contrôleur spécifique au jeu
        MyGameController control = new MyGameController(model, view);

        // 6. Définir le premier stage
        control.setFirstStageName("mygame"); // Doit correspondre au nom enregistré

        // 7. Lancer le jeu et la boucle du stage
        try {
            Logger.setLevel(Logger.LOGGER_INFO); // Configurer le niveau de log (DEBUG, INFO, WARN, ERR)
            Logger.info("Starting game setup...", Logger.LOGGER_INFO);
            control.startGame(); // Initialise le premier stage (appelle la factory)
            Logger.info("Game started. Entering stage loop...", Logger.LOGGER_INFO);
            control.stageLoop(); // Entre dans la boucle de jeu du stage
            Logger.info("Game loop finished.", Logger.LOGGER_INFO);
        } catch (GameException e) {
            System.err.println("Impossible de lancer le jeu : " + e.getMessage());
            Logger.error("Cannot start the game: " + e.getMessage(), Logger.LOGGER_ERR);
            e.printStackTrace();
        } finally {
             scanner.close(); // Fermer le scanner
        }
         System.out.println("Programme terminé.");
    }
}
```

---

## 9. Exemple de Structure de Projet

```
MonJeu/
├── src/
│   ├── boardifier/          # Contenu du framework (ne pas modifier)
│   │   ├── control/
│   │   ├── model/
│   │   └── view/
│   ├── model/               # Vos classes Modèle
│   │   ├── Piece.java
│   │   ├── GameBoard.java
│   │   ├── PiecePot.java
│   │   └── MyGameStageModel.java
│   ├── view/                # Vos classes Vue
│   │   ├── PieceLook.java
│   │   ├── CustomPotLook.java # Si nécessaire
│   │   └── MyGameStageView.java
│   ├── control/             # Vos classes Contrôleur
│   │   ├── MyGameController.java
│   │   └── MyGameDecider.java
│   ├── factory/             # (Optionnel mais recommandé) Vos Factories
│   │   └── MyGameStageFactory.java
│   └── MyGameConsole.java   # Votre classe principale (main)
├── lib/                     # Dépendances (boardifier.jar)
└── ...                      # Fichiers de build (pom.xml, build.gradle, ...)
```

*(Note: L'emplacement de la factory peut varier, parfois placée dans `model` ou `control`, mais un package dédié `factory` peut améliorer la clarté).*

---
```