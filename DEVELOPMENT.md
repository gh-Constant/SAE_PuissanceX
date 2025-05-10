# PuissanceX Game Development Guide

## Overview

PuissanceX is a Connect-4 style game implemented using the Boardifier framework. The current implementation provides a console-based interface, with plans to extend it to a JavaFX GUI in the future.

## Project Structure

The project follows the MVC (Model-View-Controller) architecture:

- **Model**: Contains game logic and state
  - `PuissanceXStageModel`: Manages the game board, player turns, and win conditions
- **View**: Handles display of game elements
  - `PuissanceXStageView`: Renders the game board in the console
- **Controller**: Coordinates interactions between model and view
  - `PuissanceXController`: Manages game flow and processes user input

## Current Implementation

The current implementation provides a functional console-based game with the following features:

- Configurable board size (rows and columns)
- Configurable win condition (number of pieces to connect)
- Two-player gameplay
- Win detection in horizontal, vertical, and diagonal directions
- Draw detection when the board is full

## Future Improvements

### Separation of Game Elements for JavaFX Implementation

To facilitate the transition to a JavaFX GUI, we need to refactor the code to better separate game elements:

#### 1. Create Dedicated Disk Class

```java
public class Disk extends GameElement {
    private int playerId;
    
    public Disk(int playerId, GameStageModel gameStageModel) {
        super(gameStageModel);
        this.playerId = playerId;
    }
    
    public int getPlayerId() {
        return playerId;
    }
}
```

#### 2. Create Board Class

```java
public class Board extends ContainerElement {
    public Board(int rows, int cols, GameStageModel gameStageModel) {
        super(rows, cols, gameStageModel);
    }
    
    // Methods for board-specific operations
}
```

#### 3. Create Disk Look Classes

```java
public class DiskLook extends ElementLook {
    public DiskLook(Disk disk) {
        super(disk);
        // Configure appearance based on player
    }
    
    @Override
    public void onRender() {
        // Render disk with appropriate color
    }
}
```

#### 4. Create Board Look Class

```java
public class BoardLook extends ContainerLook {
    public BoardLook(Board board) {
        super(board);
        // Configure board appearance
    }
    
    @Override
    public void onRender() {
        // Render board grid
    }
}
```

### Benefits of Element Separation

1. **Improved Modularity**: Each game element has a clear responsibility
2. **Easier GUI Implementation**: Visual components can be mapped directly to game elements
3. **Better Animation Support**: Individual elements can be animated independently
4. **Enhanced Extensibility**: New game elements can be added without modifying existing code

## Implementation Plan

1. Refactor the model to use dedicated classes for game elements
2. Update the controller to work with the new element structure
3. Create JavaFX-specific look classes that extend the base look classes
4. Implement a JavaFX view that renders the game elements using JavaFX nodes

## Best Practices

- Keep game logic in the model, separate from presentation logic
- Use the observer pattern to notify the view of model changes
- Implement proper encapsulation to hide implementation details
- Write comprehensive unit tests for game logic
- Document public APIs with clear JavaDoc comments

## Technical Debt

- The current implementation uses a 2D array to represent the board state, which should be replaced with a more object-oriented approach
- Win detection algorithms could be optimized for better performance
- Error handling could be improved with more specific exception types