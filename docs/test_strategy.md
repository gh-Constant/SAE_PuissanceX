# Stratégie de Test du Menu PuissanceX

## Vue d'ensemble

Cette stratégie de test est basée sur l'arbre de logique défini dans [`menu_logic_tree.md`](menu_logic_tree.md) et vise à tester les chemins critiques avec le minimum de tests mais la couverture maximale.

## Principe de Sélection des Tests

### Critères de Priorisation
1. **Longueur du chemin** : Privilégier les chemins qui traversent le plus d'états
2. **Complexité logique** : Tester les branches avec le plus de conditions
3. **Cas limites** : Valider les valeurs minimales et maximales
4. **Boucles critiques** : Tester les cycles de rejeu et retour au menu

### Tests Sélectionnés

#### Test 1: Parcours Complet (Chemin le plus long)
```
Menu → Config Mode (CvC) → Config Board → Config Win → Start → Rejouer → Menu → Exit
```
- **Objectif** : Tester le chemin le plus long possible
- **États traversés** : 8 états différents
- **Validations** : Configuration complète + cycle de rejeu + retour menu

#### Test 2: Boucle de Rejeu Multiple
```
Menu → Start → Rejouer → Rejouer → Quitter
```
- **Objectif** : Valider la stabilité de la boucle de rejeu
- **Focus** : Gestion mémoire et état entre parties
- **Cas critique** : Rejeu multiple sans retour au menu

#### Test 3: Human vs Computer
```
Menu → Config Mode (HvC) → AI Selection → Start → Menu → Exit
```
- **Objectif** : Tester la configuration IA pour mode hybride
- **Validation** : Sélection IA + transition vers jeu + retour menu

#### Test 4: Valeurs Limites Maximales
```
Menu → Board 10x10 → Win 10 → Start → Rejouer → Quitter
```
- **Objectif** : Tester les limites hautes du système
- **Validation** : Plateau maximum + condition victoire maximum

#### Test 5: Valeurs Limites Minimales
```
Menu → Board 4x4 → Win 3 → Start → Menu → Exit
```
- **Objectif** : Tester les limites basses du système
- **Validation** : Configuration minimale viable

#### Test 6: Computer vs Computer
```
Menu → CvC → Different AIs → Start → Rejouer → Quitter
```
- **Objectif** : Tester la configuration IA complète
- **Validation** : Sélection de 2 IA différentes

#### Test 7: Sortie Directe
```
Menu → Exit
```
- **Objectif** : Tester le chemin le plus court
- **Validation** : Sortie propre sans configuration

## Couverture de Test

### États Couverts
- ✅ MENU_PRINCIPAL (tous les tests)
- ✅ SELECTION_MODE (tests 1, 3, 6)
- ✅ SELECTION_AI1 (tests 1, 3, 6)
- ✅ SELECTION_AI2 (tests 1, 6)
- ✅ CONFIG_PLATEAU (tests 1, 4, 5)
- ✅ CONFIG_VICTOIRE (tests 1, 4, 5)
- ✅ JEU (tous sauf test 7)
- ✅ FIN_PARTIE (tous sauf test 7)
- ✅ SORTIE (tous les tests)

### Transitions Critiques Testées
1. **Menu → Configuration** : Tests 1, 3, 4, 5, 6
2. **Configuration → Menu** : Tests 1, 3, 5
3. **Menu → Jeu** : Tests 2, 4, 6
4. **Jeu → Fin de partie** : Tous sauf test 7
5. **Fin de partie → Rejeu** : Tests 1, 2, 4, 6
6. **Fin de partie → Menu** : Tests 1, 3, 5
7. **Fin de partie → Sortie** : Tests 2, 4, 6
8. **Menu → Sortie** : Tests 3, 5, 7

### Variables d'État Testées
- `gameMode` : 1 (tests 2,4,5,7), 2 (test 3), 3 (tests 1,6)
- `aiType1` : 1 (tests 1,3), 3 (test 6)
- `aiType2` : 1 (test 6), 2 (test 1)
- `boardRows/Cols` : 4x4 (test 5), 10x10 (test 4), défaut 6x7 (autres)
- `winCondition` : 3 (tests 1,5), 10 (test 4), défaut 4 (autres)

## Exécution des Tests

### Prérequis
```bash
# Compiler le projet
cd src/main/java
javac -cp . PuissanceXConsole.java
```

### Lancement des Tests
```bash
cd tests
chmod +x test_scenarios.sh
./test_scenarios.sh
```

### Analyse des Résultats
Les résultats sont sauvegardés dans `test_outputs/` avec un fichier par test :
- `test1_parcours_complet_output.txt`
- `test2_boucle_rejeu_output.txt`
- etc.

### Critères de Succès
1. **Aucun crash** : Tous les tests doivent se terminer proprement
2. **Navigation correcte** : Les menus doivent s'afficher dans l'ordre attendu
3. **Validation des entrées** : Les valeurs limites doivent être acceptées
4. **Gestion des états** : Les transitions doivent préserver l'état correct
5. **Mémoire stable** : Les boucles de rejeu ne doivent pas causer de fuites

## Maintenance des Tests

### Ajout de Nouveaux Tests
1. Identifier le nouveau chemin dans l'arbre de logique
2. Évaluer sa criticité par rapport aux tests existants
3. Créer le scénario d'entrée correspondant
4. Ajouter le test au script principal

### Modification des Tests Existants
- Mettre à jour l'arbre de logique en premier
- Adapter les scénarios d'entrée
- Vérifier que la couverture reste optimale

Cette stratégie garantit une couverture maximale avec un nombre minimal de tests, en se concentrant sur les chemins les plus critiques et les plus longs du système de menu.