import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import static org.junit.jupiter.api.Assertions.*;

import view.PuissanceXMenu;
import model.PuissanceXModel;
import boardifier.view.View;
import java.lang.reflect.Field;

/**
 * Tests JUnit pour les vues du jeu PuissanceX.
 * Teste les classes du package view : PuissanceXMenu, PuissanceXStageView, etc.
 */
@DisplayName("Tests des Vues PuissanceX")
public class ViewJUnitTest {
    
    private PuissanceXMenu menu;
    
    @BeforeEach
    void setUp() {
        menu = new PuissanceXMenu();
    }
    
    @Nested
    @DisplayName("Tests PuissanceXMenu")
    class PuissanceXMenuTests {
        
        @Test
        @DisplayName("Création et initialisation du menu")
        void testMenuCreationAndInitialization() {
            assertNotNull(menu, "Menu créé avec succès");
        }
        
        @Test
        @DisplayName("Valeurs par défaut du menu")
        void testMenuDefaultValues() {
            assertEquals(4, menu.getWinCondition(), "Condition de victoire par défaut");
            assertEquals(6, menu.getBoardRows(), "Lignes par défaut");
            assertEquals(7, menu.getBoardCols(), "Colonnes par défaut");
            assertEquals(1, menu.getGameMode(), "Mode de jeu par défaut (HvH)");
            assertEquals(0, menu.getAIType1(), "Type IA1 par défaut");
            assertEquals(0, menu.getAIType2(), "Type IA2 par défaut");
        }
        
        @Test
        @DisplayName("Modification des paramètres du menu")
        void testMenuParameterModification() throws Exception {
            // Utiliser la réflexion pour modifier les paramètres
            setPrivateField("winCondition", 5);
            setPrivateField("boardRows", 8);
            setPrivateField("boardCols", 9);
            setPrivateField("gameMode", 2);
            
            assertEquals(5, menu.getWinCondition(), "Condition de victoire modifiée");
            assertEquals(8, menu.getBoardRows(), "Lignes modifiées");
            assertEquals(9, menu.getBoardCols(), "Colonnes modifiées");
            assertEquals(2, menu.getGameMode(), "Mode de jeu modifié");
        }
        
        @Test
        @DisplayName("Configuration des types d'IA")
        void testAITypeConfiguration() throws Exception {
            // Configurer les types d'IA
            setPrivateField("aiType1", 1); // Minimax
            setPrivateField("aiType2", 2); // Random
            
            assertEquals(1, menu.getAIType1(), "IA1 configurée (Minimax)");
            assertEquals(2, menu.getAIType2(), "IA2 configurée (Random)");
        }
        
        @Test
        @DisplayName("Valeurs limites minimales")
        void testMinimalBoundaryValues() throws Exception {
            setPrivateField("winCondition", 3);
            setPrivateField("boardRows", 4);
            setPrivateField("boardCols", 4);
            
            assertEquals(3, menu.getWinCondition(), "Condition minimale");
            assertEquals(4, menu.getBoardRows(), "Lignes minimales");
            assertEquals(4, menu.getBoardCols(), "Colonnes minimales");
        }
        
        @Test
        @DisplayName("Valeurs limites maximales")
        void testMaximalBoundaryValues() throws Exception {
            setPrivateField("winCondition", 10);
            setPrivateField("boardRows", 10);
            setPrivateField("boardCols", 10);
            
            assertEquals(10, menu.getWinCondition(), "Condition maximale");
            assertEquals(10, menu.getBoardRows(), "Lignes maximales");
            assertEquals(10, menu.getBoardCols(), "Colonnes maximales");
        }
    }
    
    @Nested
    @DisplayName("Tests des modes de jeu")
    class GameModeTests {
        
        @Test
        @DisplayName("Mode Human vs Human")
        void testHumanVsHumanMode() throws Exception {
            setPrivateField("gameMode", 1);
            assertEquals(1, menu.getGameMode(), "Mode Human vs Human");
        }
        
        @Test
        @DisplayName("Mode Human vs Computer")
        void testHumanVsComputerMode() throws Exception {
            setPrivateField("gameMode", 2);
            setPrivateField("aiType1", 1); // Minimax
            
            assertEquals(2, menu.getGameMode(), "Mode Human vs Computer");
            assertEquals(1, menu.getAIType1(), "IA configurée pour HvC");
        }
        
        @Test
        @DisplayName("Mode Computer vs Computer")
        void testComputerVsComputerMode() throws Exception {
            setPrivateField("gameMode", 3);
            setPrivateField("aiType1", 1); // Minimax
            setPrivateField("aiType2", 2); // Random
            
            assertEquals(3, menu.getGameMode(), "Mode Computer vs Computer");
            assertEquals(1, menu.getAIType1(), "IA1 configurée pour CvC");
            assertEquals(2, menu.getAIType2(), "IA2 configurée pour CvC");
        }
        
        @Test
        @DisplayName("Changement de modes multiples")
        void testMultipleModeChanges() throws Exception {
            // Tester les 3 modes successivement
            for (int mode = 1; mode <= 3; mode++) {
                setPrivateField("gameMode", mode);
                assertEquals(mode, menu.getGameMode(), "Mode " + mode + " configuré");
            }
        }
    }
    
    @Nested
    @DisplayName("Tests des types d'IA")
    class AITypeTests {
        
        @Test
        @DisplayName("Configuration IA Minimax")
        void testMinimaxAIConfiguration() throws Exception {
            setPrivateField("aiType1", 1);
            assertEquals(1, menu.getAIType1(), "IA Minimax configurée");
        }
        
        @Test
        @DisplayName("Configuration IA Random")
        void testRandomAIConfiguration() throws Exception {
            setPrivateField("aiType2", 2);
            assertEquals(2, menu.getAIType2(), "IA Random configurée");
        }
        
        @Test
        @DisplayName("Configuration IA Condition")
        void testConditionAIConfiguration() throws Exception {
            setPrivateField("aiType1", 3);
            assertEquals(3, menu.getAIType1(), "IA Condition configurée");
        }
        
        @Test
        @DisplayName("Configuration simultanée des deux IA")
        void testBothAIConfiguration() throws Exception {
            setPrivateField("aiType1", 1); // Minimax
            setPrivateField("aiType2", 2); // Random
            
            assertEquals(1, menu.getAIType1(), "IA1 Minimax");
            assertEquals(2, menu.getAIType2(), "IA2 Random");
            assertNotEquals(menu.getAIType1(), menu.getAIType2(), "IA différentes");
        }
    }
    
    @Nested
    @DisplayName("Tests PuissanceXStageView")
    class PuissanceXStageViewTests {
        
        @Test
        @DisplayName("Initialisation de la vue de jeu")
        void testStageViewInitialization() {
            assertDoesNotThrow(() -> {
                PuissanceXModel model = new PuissanceXModel();
                model.addHumanPlayer("player1");
                model.addHumanPlayer("player2");
                
                // Enregistrer le stage factory
                boardifier.control.StageFactory.registerModelAndView("puissanceX", "model.PuissanceXStageModel", "view.PuissanceXStageView");
                
                View mainView = new View(model);
                assertNotNull(mainView, "Vue principale créée");
                
            }, "Initialisation de la vue de jeu sans erreur");
        }
        
        @Test
        @DisplayName("Intégration vue-modèle")
        void testViewModelIntegration() {
            assertDoesNotThrow(() -> {
                PuissanceXModel model = new PuissanceXModel();
                model.addHumanPlayer("player1");
                model.addHumanPlayer("player2");
                
                PuissanceXMenu testMenu = new PuissanceXMenu();
                View mainView = new View(model);
                
                // Vérifier que les composants s'intègrent
                assertNotNull(model, "Modèle créé");
                assertNotNull(testMenu, "Menu créé");
                assertNotNull(mainView, "Vue créée");
                
            }, "Intégration vue-modèle réussie");
        }
    }
    
    @Nested
    @DisplayName("Tests de cohérence")
    class ConsistencyTests {
        
        @Test
        @DisplayName("Cohérence des paramètres après modifications multiples")
        void testParameterConsistencyAfterMultipleChanges() throws Exception {
            // Effectuer plusieurs modifications
            setPrivateField("gameMode", 2);
            setPrivateField("winCondition", 5);
            setPrivateField("boardRows", 8);
            setPrivateField("boardCols", 9);
            setPrivateField("aiType1", 1);
            
            // Vérifier que tous les paramètres sont cohérents
            assertEquals(2, menu.getGameMode(), "Mode cohérent");
            assertEquals(5, menu.getWinCondition(), "Condition cohérente");
            assertEquals(8, menu.getBoardRows(), "Lignes cohérentes");
            assertEquals(9, menu.getBoardCols(), "Colonnes cohérentes");
            assertEquals(1, menu.getAIType1(), "IA cohérente");
        }
        
        @Test
        @DisplayName("Indépendance des instances de menu")
        void testMenuInstanceIndependence() throws Exception {
            PuissanceXMenu menu2 = new PuissanceXMenu();
            
            // Modifier le premier menu
            setPrivateField("winCondition", 5);
            
            // Vérifier que le second menu n'est pas affecté
            assertEquals(5, menu.getWinCondition(), "Premier menu modifié");
            assertEquals(4, menu2.getWinCondition(), "Second menu non affecté");
        }
        
        @Test
        @DisplayName("Stabilité des getters")
        void testGetterStability() {
            // Appeler les getters plusieurs fois
            for (int i = 0; i < 5; i++) {
                assertEquals(4, menu.getWinCondition(), "WinCondition stable");
                assertEquals(6, menu.getBoardRows(), "BoardRows stable");
                assertEquals(7, menu.getBoardCols(), "BoardCols stable");
                assertEquals(1, menu.getGameMode(), "GameMode stable");
            }
        }
    }
    
    @Nested
    @DisplayName("Tests de robustesse")
    class RobustnessTests {
        
        @Test
        @DisplayName("Création multiple de menus")
        void testMultipleMenuCreation() {
            assertDoesNotThrow(() -> {
                for (int i = 0; i < 10; i++) {
                    PuissanceXMenu testMenu = new PuissanceXMenu();
                    assertNotNull(testMenu, "Menu " + i + " créé");
                    assertEquals(4, testMenu.getWinCondition(), "Menu " + i + " initialisé correctement");
                }
            }, "Création multiple de menus sans erreur");
        }
        
        @Test
        @DisplayName("Robustesse après modifications intensives")
        void testRobustnessAfterIntensiveModifications() throws Exception {
            assertDoesNotThrow(() -> {
                // Effectuer de nombreuses modifications
                for (int i = 0; i < 20; i++) {
                    setPrivateField("gameMode", (i % 3) + 1);
                    setPrivateField("winCondition", (i % 5) + 3);
                    setPrivateField("boardRows", (i % 4) + 4);
                    setPrivateField("boardCols", (i % 5) + 4);
                    setPrivateField("aiType1", i % 4);
                    setPrivateField("aiType2", (i + 1) % 4);
                }
                
                // Vérifier que le menu est toujours fonctionnel
                assertTrue(menu.getGameMode() >= 1 && menu.getGameMode() <= 3, "Mode dans les limites");
                assertTrue(menu.getWinCondition() >= 3, "Condition dans les limites");
                assertTrue(menu.getBoardRows() >= 4, "Lignes dans les limites");
                assertTrue(menu.getBoardCols() >= 4, "Colonnes dans les limites");
                
            }, "Menu robuste après modifications intensives");
        }
        
        @Test
        @DisplayName("Gestion des configurations extrêmes")
        void testExtremeConfigurations() throws Exception {
            assertDoesNotThrow(() -> {
                // Configuration minimale
                setPrivateField("winCondition", 3);
                setPrivateField("boardRows", 4);
                setPrivateField("boardCols", 4);
                setPrivateField("gameMode", 1);
                
                assertEquals(3, menu.getWinCondition(), "Configuration minimale - condition");
                assertEquals(4, menu.getBoardRows(), "Configuration minimale - lignes");
                assertEquals(4, menu.getBoardCols(), "Configuration minimale - colonnes");
                
                // Configuration maximale
                setPrivateField("winCondition", 10);
                setPrivateField("boardRows", 10);
                setPrivateField("boardCols", 10);
                setPrivateField("gameMode", 3);
                
                assertEquals(10, menu.getWinCondition(), "Configuration maximale - condition");
                assertEquals(10, menu.getBoardRows(), "Configuration maximale - lignes");
                assertEquals(10, menu.getBoardCols(), "Configuration maximale - colonnes");
                
            }, "Gestion des configurations extrêmes sans erreur");
        }
    }
    
    @Nested
    @DisplayName("Tests d'intégration complète")
    class CompleteIntegrationTests {
        
        @Test
        @DisplayName("Workflow complet de configuration")
        void testCompleteConfigurationWorkflow() throws Exception {
            assertDoesNotThrow(() -> {
                // 1. Configuration initiale
                assertEquals(1, menu.getGameMode(), "Mode initial");
                assertEquals(4, menu.getWinCondition(), "Condition initiale");
                
                // 2. Changement vers Human vs Computer
                setPrivateField("gameMode", 2);
                setPrivateField("aiType1", 1); // Minimax
                assertEquals(2, menu.getGameMode(), "Mode HvC configuré");
                assertEquals(1, menu.getAIType1(), "IA configurée");
                
                // 3. Modification du plateau
                setPrivateField("boardRows", 8);
                setPrivateField("boardCols", 9);
                setPrivateField("winCondition", 5);
                assertEquals(8, menu.getBoardRows(), "Plateau configuré - lignes");
                assertEquals(9, menu.getBoardCols(), "Plateau configuré - colonnes");
                assertEquals(5, menu.getWinCondition(), "Condition configurée");
                
                // 4. Changement vers Computer vs Computer
                setPrivateField("gameMode", 3);
                setPrivateField("aiType2", 2); // Random
                assertEquals(3, menu.getGameMode(), "Mode CvC configuré");
                assertEquals(2, menu.getAIType2(), "Seconde IA configurée");
                
                // 5. Vérification finale de cohérence
                assertTrue(menu.getGameMode() >= 1 && menu.getGameMode() <= 3, "Mode final valide");
                assertTrue(menu.getWinCondition() >= 3, "Condition finale valide");
                assertTrue(menu.getBoardRows() >= 4, "Lignes finales valides");
                assertTrue(menu.getBoardCols() >= 4, "Colonnes finales valides");
                
            }, "Workflow complet de configuration réussi");
        }
    }
    
    /**
     * Méthode utilitaire pour modifier les champs privés via réflexion
     */
    private void setPrivateField(String fieldName, int value) throws Exception {
        Field field = menu.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.setInt(menu, value);
    }
}