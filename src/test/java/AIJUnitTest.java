import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import static org.junit.jupiter.api.Assertions.*;

import model.PuissanceXModel;
import boardifier.view.View;
import control.PuissanceXController;
import control.ai.RandomAIDecider;
import control.ai.Minimax;
import control.ai.ConditionAI;

/**
 * Tests JUnit pour les intelligences artificielles du jeu PuissanceX.
 * Tests d'initialisation et de création des IA.
 */
@DisplayName("Tests des IA PuissanceX")
public class AIJUnitTest {
    
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
    @DisplayName("Tests d'initialisation des IA")
    class AIInitializationTests {
        
        @Test
        @DisplayName("Initialisation de RandomAI")
        void testRandomAIInitialization() {
            assertDoesNotThrow(() -> {
                RandomAIDecider randomAI = new RandomAIDecider(model, controller);
                assertNotNull(randomAI, "RandomAI s'initialise correctement");
            }, "Initialisation RandomAI sans erreur");
        }
        
        @Test
        @DisplayName("Initialisation de Minimax")
        void testMinimaxInitialization() {
            assertDoesNotThrow(() -> {
                Minimax minimax = new Minimax(model, controller);
                assertNotNull(minimax, "Minimax s'initialise correctement");
            }, "Initialisation Minimax sans erreur");
        }
        
        @Test
        @DisplayName("Initialisation de ConditionAI")
        void testConditionAIInitialization() {
            assertDoesNotThrow(() -> {
                ConditionAI conditionAI = new ConditionAI(model, controller);
                assertNotNull(conditionAI, "ConditionAI s'initialise correctement");
            }, "Initialisation ConditionAI sans erreur");
        }
        
        @Test
        @DisplayName("Création simultanée de toutes les IA")
        void testAllAICreation() {
            assertDoesNotThrow(() -> {
                RandomAIDecider randomAI = new RandomAIDecider(model, controller);
                Minimax minimax = new Minimax(model, controller);
                ConditionAI conditionAI = new ConditionAI(model, controller);
                
                assertNotNull(randomAI, "RandomAI créée");
                assertNotNull(minimax, "Minimax créée");
                assertNotNull(conditionAI, "ConditionAI créée");
                
                // Vérifier qu'elles sont toutes différentes
                assertNotSame(randomAI, minimax, "RandomAI et Minimax sont différentes");
                assertNotSame(randomAI, conditionAI, "RandomAI et ConditionAI sont différentes");
                assertNotSame(minimax, conditionAI, "Minimax et ConditionAI sont différentes");
            }, "Création de toutes les IA sans erreur");
        }
    }
    
    @Nested
    @DisplayName("Tests de configuration des IA")
    class AIConfigurationTests {
        
        @Test
        @DisplayName("Configuration IA dans le contrôleur")
        void testAIControllerConfiguration() {
            assertDoesNotThrow(() -> {
                RandomAIDecider randomAI = new RandomAIDecider(model, controller);
                Minimax minimax = new Minimax(model, controller);
                
                controller.setAIDecider(randomAI);
                controller.setSecondAIDecider(minimax);
                
                // Vérifier via réflexion que les IA sont configurées
                java.lang.reflect.Field aiField1 = controller.getClass().getDeclaredField("aiDecider");
                aiField1.setAccessible(true);
                Object configuredAI1 = aiField1.get(controller);
                
                java.lang.reflect.Field aiField2 = controller.getClass().getDeclaredField("secondAiDecider");
                aiField2.setAccessible(true);
                Object configuredAI2 = aiField2.get(controller);
                
                assertNotNull(configuredAI1, "IA1 configurée dans le contrôleur");
                assertNotNull(configuredAI2, "IA2 configurée dans le contrôleur");
                assertTrue(configuredAI1 instanceof RandomAIDecider, "IA1 est RandomAI");
                assertTrue(configuredAI2 instanceof Minimax, "IA2 est Minimax");
            }, "Configuration IA-contrôleur réussie");
        }
        
        @Test
        @DisplayName("Changement de configuration des IA")
        void testAIConfigurationChange() {
            assertDoesNotThrow(() -> {
                RandomAIDecider randomAI = new RandomAIDecider(model, controller);
                Minimax minimax = new Minimax(model, controller);
                ConditionAI conditionAI = new ConditionAI(model, controller);
                
                // Configuration initiale
                controller.setAIDecider(randomAI);
                controller.setSecondAIDecider(minimax);
                
                // Changement de configuration
                controller.setAIDecider(conditionAI);
                controller.setSecondAIDecider(randomAI);
                
                // Vérifier le changement
                java.lang.reflect.Field aiField1 = controller.getClass().getDeclaredField("aiDecider");
                aiField1.setAccessible(true);
                Object configuredAI1 = aiField1.get(controller);
                
                java.lang.reflect.Field aiField2 = controller.getClass().getDeclaredField("secondAiDecider");
                aiField2.setAccessible(true);
                Object configuredAI2 = aiField2.get(controller);
                
                assertTrue(configuredAI1 instanceof ConditionAI, "IA1 changée vers ConditionAI");
                assertTrue(configuredAI2 instanceof RandomAIDecider, "IA2 changée vers RandomAI");
            }, "Changement de configuration réussi");
        }
    }
    
    @Nested
    @DisplayName("Tests de robustesse")
    class RobustnessTests {
        
        @Test
        @DisplayName("Création multiple d'IA")
        void testMultipleAICreation() {
            assertDoesNotThrow(() -> {
                for (int i = 0; i < 5; i++) {
                    RandomAIDecider randomAI = new RandomAIDecider(model, controller);
                    Minimax minimax = new Minimax(model, controller);
                    ConditionAI conditionAI = new ConditionAI(model, controller);
                    
                    assertNotNull(randomAI, "RandomAI " + i + " créée");
                    assertNotNull(minimax, "Minimax " + i + " créée");
                    assertNotNull(conditionAI, "ConditionAI " + i + " créée");
                }
            }, "Création multiple d'IA sans problème");
        }
        
        @Test
        @DisplayName("IA avec différents modèles")
        void testAIWithDifferentModels() {
            assertDoesNotThrow(() -> {
                // Créer différents modèles
                PuissanceXModel model1 = createTestModel();
                PuissanceXModel model2 = createTestModel();
                model2.setBoardRows(5);
                model2.setBoardCols(6);
                
                View view1 = new View(model1);
                View view2 = new View(model2);
                PuissanceXController controller1 = new PuissanceXController(model1, view1);
                PuissanceXController controller2 = new PuissanceXController(model2, view2);
                
                // Créer des IA avec différents modèles
                RandomAIDecider randomAI1 = new RandomAIDecider(model1, controller1);
                RandomAIDecider randomAI2 = new RandomAIDecider(model2, controller2);
                
                assertNotNull(randomAI1, "IA avec modèle 1 créée");
                assertNotNull(randomAI2, "IA avec modèle 2 créée");
                assertNotSame(randomAI1, randomAI2, "IA différentes pour modèles différents");
            }, "IA avec différents modèles sans erreur");
        }
        
        @Test
        @DisplayName("Gestion des cas limites")
        void testEdgeCases() {
            assertDoesNotThrow(() -> {
                // Test avec un plateau minimal
                PuissanceXModel smallModel = new PuissanceXModel();
                smallModel.setWinCondition(3);
                smallModel.setBoardRows(4);
                smallModel.setBoardCols(4);
                smallModel.addComputerPlayer("ai1");
                smallModel.addComputerPlayer("ai2");
                
                boardifier.control.StageFactory.registerModelAndView("puissanceX", "model.PuissanceXStageModel", "view.PuissanceXStageView");
                
                View smallView = new View(smallModel);
                PuissanceXController smallController = new PuissanceXController(smallModel, smallView);
                
                RandomAIDecider randomAI = new RandomAIDecider(smallModel, smallController);
                Minimax minimax = new Minimax(smallModel, smallController);
                ConditionAI conditionAI = new ConditionAI(smallModel, smallController);
                
                assertNotNull(randomAI, "IA gère les plateaux minimaux");
                assertNotNull(minimax, "Minimax gère les plateaux minimaux");
                assertNotNull(conditionAI, "ConditionAI gère les plateaux minimaux");
            }, "IA robuste avec plateaux de taille minimale");
        }
    }
    
    @Nested
    @DisplayName("Tests de types d'IA")
    class AITypeTests {
        
        @Test
        @DisplayName("Vérification des types d'IA")
        void testAITypes() {
            RandomAIDecider randomAI = new RandomAIDecider(model, controller);
            Minimax minimax = new Minimax(model, controller);
            ConditionAI conditionAI = new ConditionAI(model, controller);
            
            // Vérifier les types
            assertTrue(randomAI instanceof RandomAIDecider, "RandomAI est du bon type");
            assertTrue(minimax instanceof Minimax, "Minimax est du bon type");
            assertTrue(conditionAI instanceof ConditionAI, "ConditionAI est du bon type");
            
            // Vérifier l'héritage (toutes héritent de Decider)
            assertTrue(randomAI instanceof boardifier.control.Decider, "RandomAI hérite de Decider");
            assertTrue(minimax instanceof boardifier.control.Decider, "Minimax hérite de Decider");
            assertTrue(conditionAI instanceof boardifier.control.Decider, "ConditionAI hérite de Decider");
        }
        
        @Test
        @DisplayName("Polymorphisme des IA")
        void testAIPolymorphism() {
            assertDoesNotThrow(() -> {
                // Créer les IA
                RandomAIDecider randomAI = new RandomAIDecider(model, controller);
                Minimax minimax = new Minimax(model, controller);
                ConditionAI conditionAI = new ConditionAI(model, controller);
                
                // Les traiter comme des Decider
                boardifier.control.Decider[] ais = {randomAI, minimax, conditionAI};
                
                for (boardifier.control.Decider ai : ais) {
                    assertNotNull(ai, "IA polymorphe non null");
                    assertTrue(ai instanceof boardifier.control.Decider, "IA est un Decider");
                }
            }, "Polymorphisme des IA fonctionnel");
        }
    }
    
    @Nested
    @DisplayName("Tests d'intégration")
    class IntegrationTests {
        
        @Test
        @DisplayName("Intégration IA-Modèle-Contrôleur")
        void testAIModelControllerIntegration() {
            assertDoesNotThrow(() -> {
                // Créer une IA
                RandomAIDecider randomAI = new RandomAIDecider(model, controller);
                
                // Configurer dans le contrôleur
                controller.setAIDecider(randomAI);
                
                // Vérifier l'intégration
                assertNotNull(randomAI, "IA créée");
                assertNotNull(model, "Modèle disponible");
                assertNotNull(controller, "Contrôleur disponible");
                
                // Vérifier que l'IA est configurée dans le contrôleur
                java.lang.reflect.Field aiField = controller.getClass().getDeclaredField("aiDecider");
                aiField.setAccessible(true);
                Object configuredAI = aiField.get(controller);
                assertSame(randomAI, configuredAI, "Même instance d'IA dans le contrôleur");
            }, "Intégration IA-Modèle-Contrôleur réussie");
        }
        
        @Test
        @DisplayName("Workflow complet de configuration IA")
        void testCompleteAIConfigurationWorkflow() {
            assertDoesNotThrow(() -> {
                // 1. Créer les IA
                RandomAIDecider randomAI = new RandomAIDecider(model, controller);
                Minimax minimax = new Minimax(model, controller);
                
                // 2. Configurer dans le contrôleur
                controller.setAIDecider(randomAI);
                controller.setSecondAIDecider(minimax);
                
                // 3. Vérifier la configuration
                java.lang.reflect.Field aiField1 = controller.getClass().getDeclaredField("aiDecider");
                aiField1.setAccessible(true);
                Object configuredAI1 = aiField1.get(controller);
                
                java.lang.reflect.Field aiField2 = controller.getClass().getDeclaredField("secondAiDecider");
                aiField2.setAccessible(true);
                Object configuredAI2 = aiField2.get(controller);
                
                // 4. Vérifications finales
                assertSame(randomAI, configuredAI1, "IA1 correctement configurée");
                assertSame(minimax, configuredAI2, "IA2 correctement configurée");
                assertNotSame(configuredAI1, configuredAI2, "IA1 et IA2 sont différentes");
            }, "Workflow complet de configuration IA réussi");
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
        model.addComputerPlayer("ai1");
        model.addComputerPlayer("ai2");
        
        // Enregistrer le stage factory
        boardifier.control.StageFactory.registerModelAndView("puissanceX", "model.PuissanceXStageModel", "view.PuissanceXStageView");
        
        return model;
    }
}