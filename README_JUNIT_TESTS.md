# Tests JUnit pour PuissanceX

## 🧪 Vue d'ensemble

Ce projet utilise **JUnit 5 (Jupiter)** pour une suite complète de tests unitaires couvrant tous les composants du jeu PuissanceX.

## 📋 Suites de tests disponibles

### 1. **MenuJUnitTest** - Tests du système de menu
- ✅ Valeurs par défaut et configuration
- ✅ Modes de jeu (HvH, HvC, CvC)
- ✅ Types d'IA (Random, Minimax, Condition)
- ✅ Valeurs limites et robustesse

### 2. **ControllerJUnitTest** - Tests du contrôleur
- ✅ Gestion des choix de fin de partie
- ✅ Configuration des IA
- ✅ Persistance entre parties
- ✅ Intégration contrôleur-modèle

### 3. **AIJUnitTest** - Tests des intelligences artificielles
- ✅ RandomAI - Fonctionnement et performance
- ✅ Minimax - Algorithme et stabilité
- ✅ ConditionAI - Logique stratégique
- ✅ Tests comparatifs et d'intégration

### 4. **ModelJUnitTest** - Tests des modèles de données
- ✅ PuissanceXModel - Configuration et paramètres
- ✅ PuissanceXBoard - Création et tailles
- ✅ PuissanceXDisk - Gestion des pièces
- ✅ PuissanceXStageModel - Intégration

### 5. **ViewJUnitTest** - Tests des interfaces utilisateur
- ✅ PuissanceXMenu - Interface de configuration
- ✅ PuissanceXStageView - Vue de jeu
- ✅ Cohérence des paramètres
- ✅ Robustesse de l'affichage

### 6. **PuissanceXTestSuite** - Suite d'intégration globale
- ✅ Tests d'intégration entre composants
- ✅ Tests de performance globale
- ✅ Cohérence du système complet

## 🚀 Exécution des tests

### Prérequis
- Java 11 ou supérieur
- Maven 3.6 ou supérieur

### Commandes Maven

```bash
# Compilation du projet et des tests
mvn compile test-compile

# Exécution de tous les tests
mvn test

# Exécution d'une suite spécifique
mvn test -Dtest=MenuJUnitTest
mvn test -Dtest=AIJUnitTest
mvn test -Dtest=ControllerJUnitTest
mvn test -Dtest=ModelJUnitTest
mvn test -Dtest=ViewJUnitTest
mvn test -Dtest=PuissanceXTestSuite

# Exécution avec rapport détaillé
mvn test -Dtest=PuissanceXTestSuite -X

# Génération de rapports de test
mvn surefire-report:report
```

### Exécution manuelle (sans Maven)

```bash
# Compilation
cd src/test/java
javac -cp ../../main/java:junit-platform-console-standalone-1.9.2.jar *.java

# Exécution d'un test spécifique
java -cp ../../main/java:.:junit-platform-console-standalone-1.9.2.jar org.junit.platform.console.ConsoleLauncher --select-class MenuJUnitTest
```

## 📊 Métriques de couverture

### Fonctionnalités testées
- **Menu système** : 100% des fonctionnalités critiques
- **Contrôleur** : Gestion complète du flux de jeu
- **IA** : Tous les algorithmes (Random, Minimax, Condition)
- **Modèles** : Toutes les classes de données
- **Vues** : Interfaces utilisateur complètes

### Types de tests
- **Tests unitaires** : Chaque composant isolément
- **Tests d'intégration** : Communication entre composants
- **Tests de performance** : Temps d'exécution et stabilité
- **Tests de robustesse** : Cas limites et erreurs
- **Tests de régression** : Fonctionnalités existantes

## 🎯 Fonctionnalités clés testées

### ✅ Menu de fin de partie
- Options rejouer/menu/quitter
- Persistance des choix
- Navigation intuitive

### ✅ Configuration des IA
- 3 types d'IA disponibles
- Configuration dynamique
- Tests de performance

### ✅ Gestion du plateau
- Tailles variables (4x4 à 10x10)
- Conditions de victoire (3 à 10)
- Validation des paramètres

### ✅ Modes de jeu
- Human vs Human (HvH)
- Human vs Computer (HvC)
- Computer vs Computer (CvC)

### ✅ Affichage amélioré
- Numéros de colonnes au-dessus du plateau
- Espacement optimisé
- Interface adaptée au Puissance 4

## 🏗️ Architecture des tests

### Structure JUnit 5
```java
@DisplayName("Description du test")
class TestClass {
    
    @BeforeEach
    void setUp() { /* Initialisation */ }
    
    @Nested
    @DisplayName("Groupe de tests")
    class NestedTests {
        
        @Test
        @DisplayName("Test spécifique")
        void testMethod() { /* Test */ }
        
        @Test
        @Timeout(value = 5, unit = TimeUnit.SECONDS)
        void performanceTest() { /* Test de performance */ }
    }
}
```

### Annotations utilisées
- `@Test` : Méthode de test
- `@BeforeEach` : Initialisation avant chaque test
- `@DisplayName` : Description lisible du test
- `@Nested` : Groupement de tests
- `@Timeout` : Tests de performance

### Assertions modernes
```java
assertEquals(expected, actual, "Message descriptif");
assertNotNull(object, "Objet non null");
assertDoesNotThrow(() -> code, "Pas d'exception");
assertTrue(condition, "Condition vraie");
```

## 📈 Stratégie de test

### 1. **Tests unitaires purs**
- Aucune dépendance externe
- Tests rapides et fiables
- Isolation complète des composants

### 2. **Tests organisés**
- Classes imbriquées pour le groupement
- Noms descriptifs et clairs
- Structure logique et intuitive

### 3. **Couverture complète**
- Tous les chemins d'exécution
- Cas nominaux et cas d'erreur
- Valeurs limites et extrêmes

### 4. **Performance et robustesse**
- Tests de charge et stress
- Gestion des ressources
- Stabilité à long terme

## 🔧 Configuration Maven

Le fichier [`pom.xml`](pom.xml) inclut :

```xml
<dependencies>
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-engine</artifactId>
        <version>5.9.2</version>
        <scope>test</scope>
    </dependency>
</dependencies>

<plugins>
    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-surefire-plugin</artifactId>
        <version>3.0.0</version>
    </plugin>
</plugins>
```

## 📝 Rapports de test

### Sortie console
Les tests affichent des rapports détaillés avec :
- Métriques d'exécution
- Composants testés
- Fonctionnalités validées
- Commandes d'exécution

### Rapports Maven
```bash
# Génération de rapports HTML
mvn surefire-report:report

# Rapports disponibles dans target/site/surefire-report.html
```

## 🎉 Avantages de JUnit 5

### ✅ **Modernité**
- Annotations expressives
- API fluide et intuitive
- Support Java 8+

### ✅ **Organisation**
- Tests imbriquées (@Nested)
- Noms descriptifs (@DisplayName)
- Groupement logique

### ✅ **Performance**
- Exécution parallèle
- Tests conditionnels
- Timeouts configurables

### ✅ **Extensibilité**
- Extensions personnalisées
- Paramètres dynamiques
- Intégration IDE

## 🚀 Prochaines étapes

1. **Intégration continue** : Configuration CI/CD
2. **Couverture de code** : Intégration JaCoCo
3. **Tests d'acceptation** : Scénarios utilisateur
4. **Performance** : Benchmarks détaillés

---

**Suite de tests JUnit 5 complète pour PuissanceX** 🎯