# Arbre de Logique du Menu PuissanceX

## Structure de Navigation

```
DÉMARRAGE
├── Arguments ligne de commande fournis
│   └── Jeu direct → FIN_PARTIE → Sortie
└── Mode interactif (pas d'arguments)
    └── MENU_PRINCIPAL
        ├── 1. Start Game → JEU → FIN_PARTIE
        │   ├── 1. Rejouer → JEU → FIN_PARTIE (boucle)
        │   ├── 2. Menu principal → MENU_PRINCIPAL
        │   └── 3. Quitter → SORTIE
        ├── 2. Change Game Mode → SELECTION_MODE → MENU_PRINCIPAL
        │   ├── 1. Human vs Human → MENU_PRINCIPAL
        │   ├── 2. Human vs Computer → SELECTION_AI1 → MENU_PRINCIPAL
        │   └── 3. Computer vs Computer → SELECTION_AI1 → SELECTION_AI2 → MENU_PRINCIPAL
        ├── 3. Change Board Size → CONFIG_PLATEAU → MENU_PRINCIPAL
        ├── 4. Change Win Condition → CONFIG_VICTOIRE → MENU_PRINCIPAL
        └── 5. Exit → SORTIE
```

## Chemins Critiques à Tester

### Chemin 1: Parcours complet avec toutes les configurations
```
MENU_PRINCIPAL → Change Game Mode → Computer vs Computer → AI1 → AI2 → 
MENU_PRINCIPAL → Change Board Size → MENU_PRINCIPAL → 
Change Win Condition → MENU_PRINCIPAL → Start Game → JEU → 
FIN_PARTIE → Rejouer → JEU → FIN_PARTIE → Menu principal → MENU_PRINCIPAL → Exit
```

### Chemin 2: Boucle de rejeu multiple
```
MENU_PRINCIPAL → Start Game → JEU → FIN_PARTIE → 
Rejouer → JEU → FIN_PARTIE → 
Rejouer → JEU → FIN_PARTIE → 
Quitter
```

### Chemin 3: Configuration Human vs Computer
```
MENU_PRINCIPAL → Change Game Mode → Human vs Computer → AI Selection → 
MENU_PRINCIPAL → Start Game → JEU → FIN_PARTIE → Menu principal → 
MENU_PRINCIPAL → Exit
```

### Chemin 4: Test des limites et validations
```
MENU_PRINCIPAL → Change Board Size (valeurs limites) → 
Change Win Condition (valeurs limites) → 
Start Game → JEU → FIN_PARTIE → Rejouer
```

## États du Système

1. **MENU_PRINCIPAL**: Menu de configuration principal
2. **SELECTION_MODE**: Choix du mode de jeu
3. **SELECTION_AI1**: Choix de la première IA
4. **SELECTION_AI2**: Choix de la seconde IA
5. **CONFIG_PLATEAU**: Configuration taille du plateau
6. **CONFIG_VICTOIRE**: Configuration condition de victoire
7. **JEU**: Partie en cours
8. **FIN_PARTIE**: Menu de fin de partie
9. **SORTIE**: Fermeture de l'application

## Variables d'État

- `gameMode`: 1=HvH, 2=HvC, 3=CvC
- `aiType1`: 1=Minimax, 2=Random, 3=Condition
- `aiType2`: 1=Minimax, 2=Random, 3=Condition
- `boardRows`: 4-10
- `boardCols`: 4-10
- `winCondition`: 3-min(rows,cols)
- `showMainMenu`: boolean
- `continuePlaying`: boolean
- `endGameChoice`: 1=rejouer, 2=menu, 3=quitter