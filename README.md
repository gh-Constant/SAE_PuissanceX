# 🎯 PuissanceX

**PuissanceX** est une version moderne et évolutive du célèbre jeu **Puissance 4**, développée en Java avec une architecture **MVC** propre et propulsée par le framework **Boardifier**.
Le jeu est jouable en **console** et est conçu pour accueillir une future interface **graphique JavaFX**.

---

## 🚀 Fonctionnalités

* 🎮 **Modes de jeu variés** :

- **Model**: Contains game logic and state
- **View**: Handles display of game elements
- **Controller**: Coordinates interactions between model and view

* 🧹 **Paramétrage avancé** :

    * Nombre de **lignes**, **colonnes**
    * Condition de **victoire**
    * Mode de jeu
    * **Niveau de difficulté** de l'IA

* 🤖 **Intelligence Artificielle intégrée** :

    * Algorithme **Minimax** avec heuristiques personnalisées
    * Intégration à venir de **Deep Learning** via **Deeplearning4j**

* 🧱 **Architecture modulaire (MVC)** :

    * Séparation claire entre **modèle**, **vue** et **contrôleur**
    * Extensible pour de futures variantes ou interfaces

* ✅ **Tests unitaires** :

    * Couverture des composants critiques avec **JUnit 5**

---

## 💠 Prérequis

* **Java 17** ou supérieur
* **Maven 3.6+**
* (Optionnel) Un IDE Java (🔧 **IntelliJ IDEA recommandé**)

  - [ ] Develop board evaluation heuristics
    ```java
    private int evaluateBoard(Board board) {
        int score = 0;
        
        // Evaluate center control (center columns are more valuable)
        // Evaluate connected pieces (2-in-a-row, 3-in-a-row)
        // Evaluate blocking opponent's potential wins
        // Evaluate winning positions
        
        return score;
    }
    ```

---

## ▶️ Lancement du jeu

### 🔹 Depuis la console

```bash
mvn exec:java
```

Avec des **paramètres personnalisés** :

- [ ] **Train neural network model**
  - [ ] Set up training pipeline
  - [ ] Implement supervised learning from MinMax data
  - [ ] Add reinforcement learning through self-play
  - [ ] Tune hyperparameters for optimal performance

- [ ] **Create DeepLearningPlayer class**
  - [ ] Implement model loading and inference
    ```java
    public DeepLearningPlayer(int playerId) {
        this.playerId = playerId;
        this.model = loadModel("model/puissancex_nn.model");
        this.guide = new MinMaxPlayer(playerId, 3); // Fallback
    }
    
    public int selectMove(Board board) {
        // Convert board to input format
        float[] input = boardToInput(board);
        
        // Run inference
        float[] predictions = model.predict(input);
        
        // Select best valid move
        int bestCol = -1;
        float bestScore = Float.NEGATIVE_INFINITY;
        
        for (int col = 0; col < board.getNbCols(); col++) {
            if (!board.isColumnFull(col) && predictions[col] > bestScore) {
                bestScore = predictions[col];
                bestCol = col;
            }
        }
        
        // Fallback to MinMax if needed
        if (bestCol == -1) {
            bestCol = guide.findBestMove(board);
        }
        
        return bestCol;
    }
    ```

  - [ ] Add hybrid decision making (combining NN and MinMax)
  - [ ] Implement confidence thresholds for model predictions

### 4. JavaFX GUI Implementation
- [ ] **Create JavaFX-specific look classes**
  - [ ] Implement JavaFX BoardLook with proper styling
  - [ ] Design animated DiskLook with JavaFX shapes
  - [ ] Create game information panel with JavaFX controls

- [ ] **Add animations and effects**
  - [ ] Implement puissanceXDisk dropping animation
  - [ ] Add highlighting for winning combinations
  - [ ] Create transition effects between turns
  - [ ] Add sound effects for moves and game events

---

## 🔧 Technologies utilisées

* ☕ Java 17
* 🧰 Maven
* 🧌 Boardifier (framework MVC)
* 🎨 JavaFX (à venir)
* 🧠 Deeplearning4j (IA avancée, à venir)
* 🧪 JUnit 5

---

