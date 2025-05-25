# 🎯 PuissanceX

**PuissanceX** est une version moderne et évolutive du célèbre jeu **Puissance 4**, développée en Java avec une architecture **MVC** propre et propulsée par le framework **Boardifier**.
Le jeu est jouable en **console** et est conçu pour accueillir une future interface **graphique JavaFX**.

---

## 🚀 Fonctionnalités

* 🎮 **Modes de jeu variés** :

  * Humain vs Humain
  * Humain vs IA (Minimax ou Deep Learning)
  * IA vs IA

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

---

## 📦 Installation

```bash
git clone https://github.com/votre-utilisateur/puissancex.git
cd puissancex
mvn clean install
```

---

## ▶️ Lancement du jeu

### 🔹 Depuis la console

```bash
mvn exec:java
```

Avec des **paramètres personnalisés** :

```bash
mvn exec:java -Dexec.args="4 6 7 1"
```

* `4` → Condition de victoire (ex: aligner 4)
* `6` → Nombre de lignes
* `7` → Nombre de colonnes
* `1` → Mode de jeu :

  * `0` : Humain vs Humain
  * `1` : Humain vs IA
  * `2` : IA vs IA

### 🔹 Depuis un IDE

1. Ouvrez le projet dans **IntelliJ IDEA**
2. Lancez la classe **`PuissanceXConsole`** comme classe principale

---

## 🧱 Structure du projet

```
src/
├── main/
│   ├── java/
│   │   ├── model/         ← Logique du jeu, état des parties
│   │   ├── view/          ← Vue console (et future JavaFX)
│   │   └── control/       ← Contrôleurs MVC, interactions
│   │       └── ai/        ← IA Minimax & IA Deep Learning (WIP)
└── test/
    └── java/              ← Tests unitaires (JUnit 5)
```

---

## 🔧 Technologies utilisées

* ☕ Java 17
* 🧰 Maven
* 🧌 Boardifier (framework MVC)
* 🎨 JavaFX (à venir)
* 🧠 Deeplearning4j (IA avancée, à venir)
* 🧪 JUnit 5

---
