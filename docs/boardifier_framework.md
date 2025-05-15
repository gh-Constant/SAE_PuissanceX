# Boardifier Framework

The `boardifier` directory (`src/boardifier/`) appears to contain a reusable framework or library for creating board games. It follows an MVC (Model-View-Controller) pattern, providing base classes and mechanisms for game structure, display, and control.

## Key Components

### `StageFactory.java`

**Location:** `src/boardifier/control/StageFactory.java`

This is a critical class within the Boardifier framework. Its primary responsibility is to manage and instantiate different "stages" of a game. A stage can be thought of as a distinct screen or phase of the game, such as the main menu, the game board, settings screen, or a game over screen.

**Functionality:**

1.  **Registration of Stages:**
    *   It uses two static `HashMap` collections:
        *   `stageModelNames`: Stores mappings between a `stageName` (String) and the fully qualified class name (String) of its corresponding `GameStageModel`.
        *   `stageViewNames`: Stores mappings between a `stageName` (String) and the fully qualified class name (String) of its corresponding `GameStageView`.
    *   The `registerModelAndView(String stageName, String modelClassName, String viewClassName)` method is used to populate these maps. This method should be called at the beginning of the game (e.g., in the main application setup) to declare all available game stages.

2.  **Creation of Stage Models:**
    *   The `createStageModel(String stageName, Model model)` method instantiates a `GameStageModel` for a given `stageName`.
    *   It looks up the model's class name from `stageModelNames`.
    *   It then uses Java Reflection (`Class.forName()`, `getDeclaredConstructor()`, `newInstance()`) to create an instance of the model class.
    *   The constructor of the stage model is expected to take two arguments: a `String` (the stage name) and a `Model` (the main game model).
    *   If the `stageName` is not found or if any reflection-related error occurs (e.g., `ClassNotFoundException`, `NoSuchMethodException`), it prints a stack trace and may throw a `GameException`.

3.  **Creation of Stage Views:**
    *   The `createStageView(String stageName, GameStageModel model)` method instantiates a `GameStageView` for a given `stageName`.
    *   It looks up the view's class name from `stageViewNames`.
    *   Similar to model creation, it uses Java Reflection to create an instance of the view class.
    *   The constructor of the stage view is expected to take two arguments: a `String` (the stage name) and a `GameStageModel` (the model for that stage).
    *   It handles potential exceptions similarly to `createStageModel`.

**How to Use (Example):**

```java
// In your main game setup or initialization class:

// 1. Register your game stages
StageFactory.registerModelAndView("mainMenu", "com.mygame.view.MainMenuModel", "com.mygame.view.MainMenuView");
StageFactory.registerModelAndView("gameplay", "com.mygame.model.MyGameStageModel", "com.mygame.view.MyGameStageView");
StageFactory.registerModelAndView("gameOver", "com.mygame.model.GameOverModel", "com.mygame.view.GameOverView");

// ... later, when you need to switch to a stage ...

// 2. Create the stage model
Model mainGameModel = new Model(); // Your main game model
GameStageModel gameplayModel = null;
try {
    gameplayModel = StageFactory.createStageModel("gameplay", mainGameModel);
} catch (GameException e) {
    e.printStackTrace();
    // Handle error: stage couldn't be created
}

// 3. Create the stage view (if model creation was successful)
GameStageView gameplayView = null;
if (gameplayModel != null) {
    try {
        gameplayView = StageFactory.createStageView("gameplay", gameplayModel);
    } catch (GameException e) {
        e.printStackTrace();
        // Handle error: view couldn't be created
    }
}

// 4. Set the current stage in your game controller
// controller.setCurrentStage(gameplayModel, gameplayView);
```

**Key Considerations:**

*   **Error Handling:** The current error handling primarily involves printing stack traces. For a robust application, more sophisticated error management might be needed (e.g., logging to a file, displaying user-friendly error messages).
*   **Reflection:** The use of reflection allows for a flexible way to define and instantiate stages without hardcoding class names directly in the factory. However, it can make debugging slightly more complex if class names are incorrect or constructors don't match the expected signature, as these errors occur at runtime.
*   **Dependencies:** Stage models depend on a main `Model` object, and stage views depend on their respective `GameStageModel`. This defines a clear flow of data and dependencies within the MVC structure facilitated by the factory.

This class is fundamental for games that have multiple screens or states. To add a new screen/stage to your game, you would typically:
1.  Create a new `YourStageNameModel` class that extends `GameStageModel` (or a similar base).
2.  Create a new `YourStageNameView` class that extends `GameStageView` (or a similar base).
3.  Register them using `StageFactory.registerModelAndView(...)` with a unique stage name and the fully qualified class names of your new model and view. 