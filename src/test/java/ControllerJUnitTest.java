import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import static org.junit.jupiter.api.Assertions.*;

import model.PuissanceXModel;
import boardifier.view.View;
import control.PuissanceXController;
import control.ai.Minimax;
import control.ai.RandomAIDecider;
import java.lang.reflect.Field;

/**
 * Tests JUnit pour le contrôleur du jeu PuissanceX.
 * Focus sur les fonctionnalités de fin de partie et rejeu.
 */
@DisplayName("Tests du Contrôleur PuissanceX")
public class ControllerJUnitTest {
    
    private PuissanceXModel model;
    private View view;
    private PuissanceXController controller;
    
    @BeforeEach
    void setUp() throws Exception {
        model = createTestModel();
        view = new View(model);
        controller = new PuissanceXController(model, view);
    }
    
    @Nested
    @DisplayName("Tests d'initialisation")
    class InitializationTests {
        
        @Test
        @DisplayName("Initialisation du contrôleur")
        void testControllerInitialization() throws Exception {
            // Vérifier l'initialisation via réflexion
            Field endGameChoiceField = controller.getClass().getDeclaredField("endGameChoice");
            endGameChoiceField.setAccessible(true);
            int endGameChoice = endGameChoiceField.getInt(controller);
            
            assertEquals(0, endGameChoice, "endGameChoice initialisé à 0");
        }
        
        @Test
        @DisplayName("Contrôleur créé avec succès")
        void testControllerCreation() {
            assertNotNull(controller, "Contrôleur créé avec succès");
            assertNotNull(model, "Modèle associé au contrôleur");
        }
    }
    
    @Nested
    @DisplayName("Tests de gestion des choix de fin de partie")
    class EndGameChoiceTests {
        
        @Test
        @DisplayName("Récupération du choix de fin de partie initial")
        void testInitialEndGameChoice() {
            int initialChoice = controller.getEndGameChoice();
            assertEquals(0, initialChoice, "Choix initial à 0");
        }
        
        @Test
        @DisplayName("Modification du choix via réflexion")
        void testEndGameChoiceModification() throws Exception {
            // Modifier le choix via réflexion (simulation de l'interaction utilisateur)
            Field endGameChoiceField = controller.getClass().getDeclaredField("endGameChoice");
            endGameChoiceField.setAccessible(true);
            endGameChoiceField.setInt(controller, 2);
            
            int retrievedChoice = controller.getEndGameChoice();
            assertEquals(2, retrievedChoice, "Choix modifié et récupéré correctement");
        }
        
        @Test
        @DisplayName("Reset du choix de fin de partie")
        void testResetEndGameChoice() throws Exception {
            // Modifier le choix via réflexion
            Field endGameChoiceField = controller.getClass().getDeclaredField("endGameChoice");
            endGameChoiceField.setAccessible(true);
            endGameChoiceField.setInt(controller, 3);
            
            // Vérifier que le choix a été modifié
            assertEquals(3, controller.getEndGameChoice(), "Choix modifié");
            
            // Reset
            controller.resetEndGameChoice();
            
            // Vérifier que c'est remis à 0
            assertEquals(0, controller.getEndGameChoice(), "Choix remis à zéro après reset");
        }
    }
    
    @Nested
    @DisplayName("Tests de configuration des IA")
    class AIConfigurationTests {
        
        @Test
        @DisplayName("Configuration de la première IA")
        void testFirstAIConfiguration() throws Exception {
            Minimax ai1 = new Minimax(model, controller);
            controller.setAIDecider(ai1);
            
            // Vérifier via réflexion
            Field aiField = controller.getClass().getDeclaredField("aiDecider");
            aiField.setAccessible(true);
            Object configuredAI = aiField.get(controller);
            
            assertNotNull(configuredAI, "Première IA configurée");
            assertTrue(configuredAI instanceof Minimax, "IA de type Minimax");
        }
        
        @Test
        @DisplayName("Configuration de la seconde IA")
        void testSecondAIConfiguration() throws Exception {
            RandomAIDecider ai2 = new RandomAIDecider(model, controller);
            controller.setSecondAIDecider(ai2);
            
            // Vérifier via réflexion
            Field aiField = controller.getClass().getDeclaredField("secondAiDecider");
            aiField.setAccessible(true);
            Object configuredAI = aiField.get(controller);
            
            assertNotNull(configuredAI, "Seconde IA configurée");
            assertTrue(configuredAI instanceof RandomAIDecider, "IA de type RandomAI");
        }
        
        @Test
        @DisplayName("Configuration des deux IA simultanément")
        void testBothAIConfiguration() throws Exception {
            Minimax ai1 = new Minimax(model, controller);
            RandomAIDecider ai2 = new RandomAIDecider(model, controller);
            
            controller.setAIDecider(ai1);
            controller.setSecondAIDecider(ai2);
            
            // Vérifier les deux IA
            Field aiField1 = controller.getClass().getDeclaredField("aiDecider");
            aiField1.setAccessible(true);
            Object configuredAI1 = aiField1.get(controller);
            
            Field aiField2 = controller.getClass().getDeclaredField("secondAiDecider");
            aiField2.setAccessible(true);
            Object configuredAI2 = aiField2.get(controller);
            
            assertNotNull(configuredAI1, "Première IA configurée");
            assertNotNull(configuredAI2, "Seconde IA configurée");
            assertNotSame(configuredAI1, configuredAI2, "Les deux IA sont différentes");
        }
    }
    
    @Nested
    @DisplayName("Tests de persistance entre parties")
    class PersistenceTests {
        
        @Test
        @DisplayName("Persistance du choix après reset")
        void testChoicePersistenceAfterReset() throws Exception {
            // Modifier le choix via réflexion
            Field endGameChoiceField = controller.getClass().getDeclaredField("endGameChoice");
            endGameChoiceField.setAccessible(true);
            endGameChoiceField.setInt(controller, 2);
            
            // Vérifier la modification
            assertEquals(2, controller.getEndGameChoice(), "Choix modifié");
            
            // Simuler une nouvelle partie
            controller.resetEndGameChoice();
            
            // Le choix devrait être remis à 0
            assertEquals(0, controller.getEndGameChoice(), "Choix remis à zéro pour nouvelle partie");
        }
        
        @Test
        @DisplayName("Indépendance des instances de contrôleur")
        void testControllerInstanceIndependence() throws Exception {
            PuissanceXModel model2 = createTestModel();
            View view2 = new View(model2);
            PuissanceXController controller2 = new PuissanceXController(model2, view2);
            
            // Modifier les deux contrôleurs différemment via réflexion
            Field endGameChoiceField1 = controller.getClass().getDeclaredField("endGameChoice");
            endGameChoiceField1.setAccessible(true);
            endGameChoiceField1.setInt(controller, 1);
            
            Field endGameChoiceField2 = controller2.getClass().getDeclaredField("endGameChoice");
            endGameChoiceField2.setAccessible(true);
            endGameChoiceField2.setInt(controller2, 3);
            
            // Vérifier l'indépendance
            assertEquals(1, controller.getEndGameChoice(), "Premier contrôleur non affecté");
            assertEquals(3, controller2.getEndGameChoice(), "Second contrôleur non affecté");
        }
    }
    
    @Nested
    @DisplayName("Tests de robustesse")
    class RobustnessTests {
        
        @Test
        @DisplayName("Stabilité après opérations multiples")
        void testStabilityAfterMultipleOperations() throws Exception {
            assertDoesNotThrow(() -> {
                // Effectuer plusieurs opérations
                Field endGameChoiceField = controller.getClass().getDeclaredField("endGameChoice");
                endGameChoiceField.setAccessible(true);
                
                for (int i = 0; i < 10; i++) {
                    endGameChoiceField.setInt(controller, 1);
                    controller.resetEndGameChoice();
                    endGameChoiceField.setInt(controller, 2);
                    controller.resetEndGameChoice();
                    endGameChoiceField.setInt(controller, 3);
                    controller.resetEndGameChoice();
                }
                
                // Vérifier la stabilité finale
                assertEquals(0, controller.getEndGameChoice(), "Contrôleur stable après opérations multiples");
            }, "Contrôleur stable après opérations répétées");
        }
        
        @Test
        @DisplayName("Création multiple de contrôleurs")
        void testMultipleControllerCreation() {
            assertDoesNotThrow(() -> {
                for (int i = 0; i < 5; i++) {
                    PuissanceXModel testModel = createTestModel();
                    View testView = new View(testModel);
                    PuissanceXController testController = new PuissanceXController(testModel, testView);
                    
                    assertNotNull(testController, "Contrôleur " + i + " créé avec succès");
                    assertEquals(0, testController.getEndGameChoice(), "Contrôleur " + i + " initialisé correctement");
                }
            }, "Création multiple de contrôleurs sans erreur");
        }
    }
    
    @Nested
    @DisplayName("Tests d'intégration")
    class IntegrationTests {
        
        @Test
        @DisplayName("Intégration contrôleur-modèle")
        void testControllerModelIntegration() {
            // Vérifier que le contrôleur peut interagir avec le modèle
            assertDoesNotThrow(() -> {
                assertEquals(4, model.getWinCondition(), "Contrôleur accède aux données du modèle");
                assertEquals(6, model.getBoardRows(), "Contrôleur accède aux paramètres du plateau");
                assertEquals(7, model.getBoardCols(), "Contrôleur accède aux dimensions du plateau");
            }, "Intégration contrôleur-modèle fonctionnelle");
        }
        
        @Test
        @DisplayName("Workflow complet de fin de partie")
        void testCompleteEndGameWorkflow() throws Exception {
            assertDoesNotThrow(() -> {
                Field endGameChoiceField = controller.getClass().getDeclaredField("endGameChoice");
                endGameChoiceField.setAccessible(true);
                
                // 1. Partie en cours (choix initial à 0)
                assertEquals(0, controller.getEndGameChoice(), "Partie en cours");
                
                // 2. Fin de partie - choix rejouer (simulation)
                endGameChoiceField.setInt(controller, 1);
                assertEquals(1, controller.getEndGameChoice(), "Choix rejouer sélectionné");
                
                // 3. Nouvelle partie - reset
                controller.resetEndGameChoice();
                assertEquals(0, controller.getEndGameChoice(), "Nouvelle partie initialisée");
                
                // 4. Fin de partie - choix menu (simulation)
                endGameChoiceField.setInt(controller, 2);
                assertEquals(2, controller.getEndGameChoice(), "Choix menu sélectionné");
                
                // 5. Retour au menu - reset
                controller.resetEndGameChoice();
                assertEquals(0, controller.getEndGameChoice(), "Retour au menu effectué");
                
            }, "Workflow complet de fin de partie fonctionnel");
        }
    }
    
    /**
     * Crée un modèle de test standard
     */
    private PuissanceXModel createTestModel() throws Exception {
        PuissanceXModel model = new PuissanceXModel();
        model.setWinCondition(4);
        model.setBoardRows(6);
        model.setBoardCols(7);
        model.addHumanPlayer("player1");
        model.addHumanPlayer("player2");
        
        // Enregistrer le stage factory
        boardifier.control.StageFactory.registerModelAndView("puissanceX", "model.PuissanceXStageModel", "view.PuissanceXStageView");
        
        return model;
    }
}