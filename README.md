# PuissanceX Game Development Guide

## Overview

PuissanceX is a Connect-4 style game implemented using the Boardifier framework. The current implementation provides a console-based interface, with plans to extend it to a JavaFX GUI in the future.

## Project Structure

The project follows the MVC (Model-View-Controller) architecture:

- **Model**: Contains game logic and state
- **View**: Handles display of game elements
- **Controller**: Coordinates interactions between main.model and main.view

## Development Roadmap

### 1. Console Visualization Implementation
- [ ] **Implement BoardLook class**
  - [ ] Create proper border rendering with Unicode box-drawing characters
  - [ ] Add column numbers at the top of the board
  - [ ] Set appropriate cell size for readability
  - [ ] Implement color support using ANSI escape codes

- [ ] **Implement DiskLook class**
  - [ ] Create distinct symbols for each player (e.g., '●' for Player 1, '○' for Player 2)
  - [ ] Add color differentiation (e.g., red for Player 1, yellow for Player 2)
  - [ ] Ensure proper centering within cells

- [ ] **Implement game information display**
  - [ ] Create a dedicated status area for game messages
  - [ ] Add turn indicator with player name highlighting
  - [ ] Display win/draw messages prominently
  - [ ] Add game statistics (moves played, time elapsed)

### 2. MinMax AI Implementation
- [ ] **Create MinMaxPlayer class**
  - [ ] Implement basic MinMax algorithm
    ```java
    public int findBestMove(Board board) {
        int bestScore = Integer.MIN_VALUE;
        int bestCol = -1;
        
        for (int col = 0; col < board.getNbCols(); col++) {
            // Try this column if it's valid
            if (!board.isColumnFull(col)) {
                // Make the move
                int row = board.getFirstEmptyRow(col);
                board.addElement(new Disk(playerId, board.getGameStageModel()), row, col);
                
                // Evaluate with minimax
                int score = minimax(board, maxDepth, false, Integer.MIN_VALUE, Integer.MAX_VALUE);
                
                // Undo the move
                board.removeElement(/* parameters */);
                
                // Update best move
                if (score > bestScore) {
                    bestScore = score;
                    bestCol = col;
                }
            }
        }
        return bestCol;
    }
    ```

  - [ ] Add alpha-beta pruning optimization
    ```java
    private int minimax(Board board, int depth, boolean isMaximizing, int alpha, int beta) {
        // Terminal conditions
        if (depth == 0 || isTerminalNode(board)) {
            return evaluateBoard(board);
        }
        
        if (isMaximizing) {
            int maxScore = Integer.MIN_VALUE;
            // For each possible move
            for (int col = 0; col < board.getNbCols(); col++) {
                if (!board.isColumnFull(col)) {
                    // Make move, evaluate, undo move
                    // Update alpha
                    // Prune if needed (beta <= alpha)
                }
            }
            return maxScore;
        } else {
            int minScore = Integer.MAX_VALUE;
            // For each possible move
            for (int col = 0; col < board.getNbCols(); col++) {
                if (!board.isColumnFull(col)) {
                    // Make move, evaluate, undo move
                    // Update beta
                    // Prune if needed (beta <= alpha)
                }
            }
            return minScore;
        }
    }
    ```

  - [ ] Develop board evaluation heuristics
    ```java
    private int evaluateBoard(Board board) {
        int score = 0;
        
        // Evaluate center main.control (center columns are more valuable)
        // Evaluate connected pieces (2-in-a-row, 3-in-a-row)
        // Evaluate blocking opponent's potential wins
        // Evaluate winning positions
        
        return score;
    }
    ```

  - [ ] Implement difficulty levels by varying search depth
  - [ ] Add move randomization for equal-value moves

- [ ] **Integrate AI with game controller**
  - [ ] Create AI player selection in game setup
  - [ ] Implement AI move selection in controller
  - [ ] Add delay for AI moves to improve user experience

### 3. Deep Learning AI Guided by MinMax
- [ ] **Design neural network architecture**
  - [ ] Define input representation of board state
    ```java
    private float[] boardToInput(Board board) {
        // For a 6x7 board, create a 6x7x3 input tensor
        // Channel 1: Player's pieces (1 where present, 0 elsewhere)
        // Channel 2: Opponent's pieces (1 where present, 0 elsewhere)
        // Channel 3: Valid moves (1 for empty columns, 0 for full columns)
    }
    ```

  - [ ] Design network structure
    ```
    Input: Board state (rows x cols x channels)
    Conv2D: 64 filters, 3x3 kernel, ReLU activation
    Conv2D: 64 filters, 3x3 kernel, ReLU activation
    Flatten
    Dense: 128 neurons, ReLU activation
    Dense: 64 neurons, ReLU activation
    Output: Move scores (cols neurons, tanh activation)
    ```

- [ ] **Generate training data**
  - [ ] Use MinMax to create labeled position datasets
  - [ ] Implement self-play for additional training data
  - [ ] Create data augmentation through board rotations/reflections

- [ ] **Train neural network main.model**
  - [ ] Set up training pipeline
  - [ ] Implement supervised learning from MinMax data
  - [ ] Add reinforcement learning through self-play
  - [ ] Tune hyperparameters for optimal performance

- [ ] **Create DeepLearningPlayer class**
  - [ ] Implement main.model loading and inference
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
        float[] predictions = main.model.predict(input);
        
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
  - [ ] Implement confidence thresholds for main.model predictions

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

- [ ] **Design responsive UI**
  - [ ] Create main menu with game options
  - [ ] Implement settings panel for customization
  - [ ] Add player profile management
  - [ ] Create responsive layout for different screen sizes

## Implementation Timeline

| Task | Estimated Duration | Dependencies |
|------|-------------------|--------------|
| Console Visualization Implementation | 1-2 days | None |
| MinMax AI Implementation | 3-4 days | None |
| Deep Learning AI - Design & Data | 3-5 days | MinMax AI |
| Deep Learning AI - Training | 4-7 days | Design & Data |
| Deep Learning AI - Integration | 2-3 days | Training |
| JavaFX GUI Implementation | 5-7 days | Console Visualization |

## Technical Debt

- Current game logic needs better error handling
- Need comprehensive test suite for game logic and AI components
- Documentation requires updates to reflect new features
- Performance optimization for AI algorithms
