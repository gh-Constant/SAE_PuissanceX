import java.util.concurrent.locks.Condition;

import boardifier.control.Logger;
import boardifier.control.StageFactory;
import boardifier.view.View;
import control.PuissanceXController;
import control.ai.Minimax;
import control.ai.RandomAIDecider;
import control.ai.ConditionAI;
import model.PuissanceXModel;
import view.PuissanceXMenu;

public class PuissanceXConsole {
    public static void main(String[] args) {
        // Initialize boardifier logger settings
        Logger.setLevel(Logger.LOGGER_TRACE); 
        Logger.setVerbosity(Logger.VERBOSE_HIGH);

        Logger.info("Starting PuissanceX Console...");

        boolean continuePlaying = true;
        boolean showMainMenu = true;
        
        // Default values
        int winCondition = 4;
        int boardRows = 6;
        int boardCols = 7;
        int currentGameMode = 1;
        PuissanceXMenu menu = null;
        
        while (continuePlaying) {
            // If no arguments are provided, show the menu
            if (args.length == 0 && showMainMenu) {
                menu = new PuissanceXMenu();
                boolean startGame = menu.showMenu();
                
                if (!startGame) {
                    Logger.info("User chose to exit. Goodbye!");
                    return;
                }
                
                // Get settings from menu
                winCondition = menu.getWinCondition();
                boardRows = menu.getBoardRows();
                boardCols = menu.getBoardCols();
                currentGameMode = menu.getGameMode();
                
                Logger.info("Starting game with menu settings:");
                Logger.info("  Win Condition: " + winCondition);
                Logger.info("  Board rows: " + boardRows);
                Logger.info("  Board columns: " + boardCols);
                Logger.info("  Game mode: " + currentGameMode);
                
                showMainMenu = false; // Ne plus afficher le menu principal sauf si demandé
            }
            else {
                try {
                    if (args.length >= 1) {
                        winCondition = Integer.parseInt(args[0]);
                        Logger.debug("Parsed Win Condition argument: " + winCondition);
                    }
                    if (args.length >= 2) {
                        boardRows = Integer.parseInt(args[1]);
                        Logger.debug("Parsed rows argument: " + boardRows);
                    }
                    if (args.length >= 3) {
                        boardCols = Integer.parseInt(args[2]);
                        Logger.debug("Parsed columns argument: " + boardCols);
                    }
                    if (args.length >= 4) {
                        currentGameMode = Integer.parseInt(args[3]);
                        Logger.debug("Parsed mode argument: " + currentGameMode);
                    }
                } catch (NumberFormatException e) {
                    Logger.info("Error parsing arguments: " + e.getMessage() + ". Review arguments. Some or all parameters will use defaults.");
                }

                // Game Mode validation
                if (currentGameMode < 0 || currentGameMode > 2) {
                    Logger.info("Parsed game mode (" + currentGameMode + ") is invalid. Setting to 0 (Human vs Human).");
                    currentGameMode = 0;
                }

                Logger.info("Final effective game parameters set:");
                Logger.info("  Win Condition: " + winCondition);
                Logger.info("  Board rows: " + boardRows);
                Logger.info("  Board columns: " + boardCols);
                Logger.info("  Game mode: " + currentGameMode);
            }

            // Initialize model with parameters
            PuissanceXModel model = new PuissanceXModel();
            model.setWinCondition(winCondition);
            model.setBoardRows(boardRows);
            model.setBoardCols(boardCols);

            Logger.info("Setting up players for mode: " + currentGameMode);
            if (currentGameMode == 1) {
                model.addHumanPlayer("player1");
                model.addHumanPlayer("player2");
                Logger.debug("Added player1 (Human) and player2 (Human).");
            } else if (currentGameMode == 2) {
                model.addHumanPlayer("player");
                model.addComputerPlayer("computer");
                Logger.debug("Added player (Human) and computer (AI).");
            } else if (currentGameMode == 3) {
                model.addComputerPlayer("computer1");
                model.addComputerPlayer("computer2");
                Logger.debug("Added computer1 (AI) and computer2 (AI).");
            }

            Logger.info("Registering PuissanceX model and view with StageFactory.");
            StageFactory.registerModelAndView("puissanceX", "model.PuissanceXStageModel", "view.PuissanceXStageView");
            
            // TODO: Fix console version - for now, comment out to focus on JavaFX
            // View gameView = new View(model);
            Logger.debug("View object (boardifier.view.View) created.");

            // TODO: Fix console version
            // PuissanceXController control = new PuissanceXController(model, gameView);
            Logger.debug("PuissanceXController object created.");
            
            // TODO: Fix console version - commenting out for now
            /*
            // Set up AI decider if needed
            if (currentGameMode == 2 || currentGameMode == 3) {
                if (args.length == 0) {  // Si on utilise le menu
                    if (currentGameMode == 2) {
                        // Pour Human vs Computer, l'IA est le joueur 1 (secondAiDecider)
                        switch (menu.getAIType1()) {
                            case 1:
                                control.setSecondAIDecider(new Minimax(model, control));
                                Logger.debug("Minimax AI created and set for computer player (player 1)");
                                break;
                            case 2:
                                control.setSecondAIDecider(new RandomAIDecider(model, control));
                                Logger.debug("Random AI created and set for computer player (player 1)");
                                break;
                            case 3:
                                control.setSecondAIDecider(new ConditionAI(model, control));
                                Logger.debug("Condition AI created and set for computer player (player 1)");
                                break;
                        }
                    } else if (currentGameMode == 3) {
                        // Pour AI vs AI, configurer les deux IA
                        switch (menu.getAIType1()) {
                            case 1:
                                control.setAIDecider(new Minimax(model, control));
                                Logger.debug("Minimax AI created and set for first AI player (player 0)");
                                break;
                            case 2:
                                control.setAIDecider(new RandomAIDecider(model, control));
                                Logger.debug("Random AI created and set for first AI player (player 0)");
                                break;
                            case 3:
                                control.setAIDecider(new ConditionAI(model, control));
                                Logger.debug("Condition AI created and set for first AI player (player 0)");
                                break;
                        }
                        
                        switch (menu.getAIType2()) {
                            case 1:
                                control.setSecondAIDecider(new Minimax(model, control));
                                Logger.debug("Minimax AI created and set for second AI player (player 1)");
                                break;
                            case 2:
                                control.setSecondAIDecider(new RandomAIDecider(model, control));
                                Logger.debug("Random AI created and set for second AI player (player 1)");
                                break;
                            case 3:
                                control.setSecondAIDecider(new ConditionAI(model, control));
                                Logger.debug("Condition AI created and set for second AI player (player 1)");
                                break;
                        }
                    }
                } else {  // Si on utilise les arguments en ligne de commande
                    // Par défaut, utiliser Minimax
                    if (currentGameMode == 2) {
                        // Pour Human vs Computer, l'IA est le joueur 1 (secondAiDecider)
                        control.setSecondAIDecider(new Minimax(model, control));
                        Logger.debug("Default Minimax AI created for computer player (player 1)");
                    } else if (currentGameMode == 3) {
                        // Pour AI vs AI, configurer les deux IA
                        control.setAIDecider(new Minimax(model, control));
                        control.setSecondAIDecider(new Minimax(model, control));
                        Logger.debug("Default Minimax AIs created for both AI players");
                    }
                }
            }

            control.setFirstStageName("puissanceX");
            Logger.debug("First stage name set to 'puissanceX'.");


            try {
                Logger.info("Attempting to start game...");
                control.startGame();
                Logger.info("Game started successfully. Entering stage loop.");
                control.stageLoop();
                Logger.info("Stage loop exited. Game ended.");
                Thread.sleep(2000);

                // Gérer le choix de fin de partie
                if (args.length == 0) { // Seulement si on utilise le menu interactif
                    int endGameChoice = control.getEndGameChoice();

                    switch (endGameChoice) {
                        case 1: // Rejouer
                            Logger.info("Player chose to replay. Starting new game with same settings.");
                            control.resetEndGameChoice();
                            // Continue la boucle avec les mêmes paramètres
                            break;
                        case 2: // Retour au menu principal
                            Logger.info("Player chose to return to main menu.");
                            showMainMenu = true;
                            control.resetEndGameChoice();
                            break;
                        case 3: // Quitter
                            Logger.info("Player chose to exit. Goodbye!");
                            continuePlaying = false;
                            break;
                        default:
                            Logger.info("No valid choice made. Returning to main menu.");
                            showMainMenu = true;
                            break;
                    }
                } else {
                    // Si des arguments ont été fournis en ligne de commande, on sort de la boucle
                    continuePlaying = false;
                }

            } catch (Exception e) {
                Logger.info("An unexpected error occurred: " + e.getMessage() + ". Aborting.");
                Logger.info("ERROR_DETAILS: " + e);

                // En cas d'erreur, retourner au menu principal si on est en mode interactif
                if (args.length == 0) {
                    showMainMenu = true;
                } else {
                    continuePlaying = false;
                }
            }
            */

            // Console version temporarily disabled - use JavaFX version instead
            Logger.info("Console version is temporarily disabled. Please use the JavaFX version: PuissanceXJavaFX");
            return;
        }
    }
}
