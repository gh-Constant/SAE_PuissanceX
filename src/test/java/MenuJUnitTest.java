import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import static org.junit.jupiter.api.Assertions.*;

import view.PuissanceXMenu;
import java.lang.reflect.Field;

/**
 * Tests JUnit pour le système de menu PuissanceX.
 * Basés sur l'arbre de logique défini dans docs/menu_logic_tree.md
 */
@DisplayName("Tests du Menu PuissanceX")
public class MenuJUnitTest {
    
    private PuissanceXMenu menu;
    
    @BeforeEach
    void setUp() {
        menu = new PuissanceXMenu();
    }
    
    @Nested
    @DisplayName("Tests des valeurs par défaut")
    class DefaultValuesTests {
        
        @Test
        @DisplayName("Valeurs par défaut du menu")
        void testDefaultValues() {
            assertEquals(4, menu.getWinCondition(), "Condition de victoire par défaut");
            assertEquals(6, menu.getBoardRows(), "Nombre de lignes par défaut");
            assertEquals(7, menu.getBoardCols(), "Nombre de colonnes par défaut");
            assertEquals(1, menu.getGameMode(), "Mode de jeu par défaut (Human vs Human)");
            assertEquals(0, menu.getAIType1(), "Type IA1 par défaut");
            assertEquals(0, menu.getAIType2(), "Type IA2 par défaut");
        }
    }
    
    @Nested
    @DisplayName("Tests de configuration des paramètres")
    class ParameterConfigurationTests {
        
        @Test
        @DisplayName("Configuration Human vs Computer")
        void testHumanVsComputerConfiguration() throws Exception {
            // Simuler la sélection du mode Human vs Computer
            setPrivateField("gameMode", 2);
            setPrivateField("aiType1", 1); // Minimax
            
            assertEquals(2, menu.getGameMode(), "Mode Human vs Computer configuré");
            assertEquals(1, menu.getAIType1(), "IA Minimax configurée");
        }
        
        @Test
        @DisplayName("Configuration Computer vs Computer")
        void testComputerVsComputerConfiguration() throws Exception {
            // Simuler la sélection du mode Computer vs Computer
            setPrivateField("gameMode", 3);
            setPrivateField("aiType1", 1); // Minimax
            setPrivateField("aiType2", 2); // Random
            
            assertEquals(3, menu.getGameMode(), "Mode Computer vs Computer configuré");
            assertEquals(1, menu.getAIType1(), "IA1 Minimax configurée");
            assertEquals(2, menu.getAIType2(), "IA2 Random configurée");
        }
        
        @Test
        @DisplayName("Valeurs limites minimales")
        void testMinimalLimitValues() throws Exception {
            // Configurer les valeurs minimales
            setPrivateField("winCondition", 3);
            setPrivateField("boardRows", 4);
            setPrivateField("boardCols", 4);
            
            assertEquals(3, menu.getWinCondition(), "Condition de victoire minimale");
            assertEquals(4, menu.getBoardRows(), "Lignes minimales");
            assertEquals(4, menu.getBoardCols(), "Colonnes minimales");
        }
        
        @Test
        @DisplayName("Valeurs limites maximales")
        void testMaximalLimitValues() throws Exception {
            // Configurer les valeurs maximales
            setPrivateField("winCondition", 10);
            setPrivateField("boardRows", 10);
            setPrivateField("boardCols", 10);
            
            assertEquals(10, menu.getWinCondition(), "Condition de victoire maximale");
            assertEquals(10, menu.getBoardRows(), "Lignes maximales");
            assertEquals(10, menu.getBoardCols(), "Colonnes maximales");
        }
    }
    
    @Nested
    @DisplayName("Tests des types d'IA")
    class AITypeTests {
        
        @Test
        @DisplayName("Configuration IA Minimax")
        void testMinimaxAIConfiguration() throws Exception {
            setPrivateField("aiType1", 1); // Minimax
            assertEquals(1, menu.getAIType1(), "IA Minimax configurée");
        }
        
        @Test
        @DisplayName("Configuration IA Random")
        void testRandomAIConfiguration() throws Exception {
            setPrivateField("aiType2", 2); // Random
            assertEquals(2, menu.getAIType2(), "IA Random configurée");
        }
        
        @Test
        @DisplayName("Configuration IA Condition")
        void testConditionAIConfiguration() throws Exception {
            setPrivateField("aiType1", 3); // Condition
            assertEquals(3, menu.getAIType1(), "IA Condition configurée");
        }
    }
    
    @Nested
    @DisplayName("Tests de cohérence")
    class ConsistencyTests {
        
        @Test
        @DisplayName("Cohérence des paramètres après modifications multiples")
        void testParameterConsistency() throws Exception {
            // Effectuer plusieurs modifications
            setPrivateField("gameMode", 2);
            setPrivateField("winCondition", 5);
            setPrivateField("boardRows", 8);
            setPrivateField("boardCols", 9);
            setPrivateField("aiType1", 1);
            
            // Vérifier que tous les paramètres sont cohérents
            assertEquals(2, menu.getGameMode(), "Mode de jeu cohérent");
            assertEquals(5, menu.getWinCondition(), "Condition de victoire cohérente");
            assertEquals(8, menu.getBoardRows(), "Lignes cohérentes");
            assertEquals(9, menu.getBoardCols(), "Colonnes cohérentes");
            assertEquals(1, menu.getAIType1(), "Type IA cohérent");
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
                    assertNotNull(testMenu, "Menu " + i + " créé avec succès");
                }
            }, "Création multiple de menus sans erreur");
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
    
    /**
     * Méthode utilitaire pour modifier les champs privés via réflexion
     */
    private void setPrivateField(String fieldName, int value) throws Exception {
        Field field = menu.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.setInt(menu, value);
    }
}