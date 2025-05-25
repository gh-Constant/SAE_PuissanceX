# Project Structure

The project is organized as follows:

```
.
├── .git/              # Git version main.control files
├── .idea/             # IntelliJ IDEA project files
├── docs/              # Documentation files (this folder)
│   ├── README.md      # Main documentation page
│   └── ...            # Other documentation files
├── src/               # Source code
│   ├── boardifier/    # Core game framework/library
│   │   ├── main.control/   # Controller components for the Boardifier framework
│   │   ├── main.model/     # Model components for the Boardifier framework
│   │   └── main.view/      # View components for the Boardifier framework
│   ├── main.control/       # Game-specific controller components
│   ├── main.model/         # Game-specific main.model components
│   ├── main.view/          # Game-specific main.view components
│   └── PuissanceXConsole.java # Main class for the console version of the PuissanceX game
├── HoleConsole.iml    # IntelliJ IDEA module file (likely for a game named "Hole")
├── PuissanceX.iml     # IntelliJ IDEA module file for the PuissanceX game
├── README.md          # Root project README (might contain setup instructions, etc.)
└── .gitignore         # Specifies intentionally untracked files that Git should ignore
```

## Key Directories

*   **`src/`**: Contains all the Java source code.
    *   **`src/boardifier/`**: This directory houses the core "Boardifier" framework. It has its own MVC structure (`main.control/`, `main.model/`, `main.view/`). This suggests it provides general functionalities for board games.
        *   `StageFactory.java` (located in `src/boardifier/main.control/`) is a crucial class responsible for creating and managing different game stages (like menus, gameplay screens, game over screens) and their associated models and views.
    *   **`src/main.control/`**, **`src/main.model/`**, **`src/main.view/`**: These directories at the `src/` level contain the game-specific logic for "PuissanceX". They extend or use the components from the `boardifier` framework.
    *   **`PuissanceXConsole.java`**: This is likely the entry point for running the "PuissanceX" game in a console environment.
*   **`docs/`**: This directory contains all the project documentation, including this file.

## IntelliJ IDEA Files

*   `.iml` files (`HoleConsole.iml`, `PuissanceX.iml`) and the `.idea/` directory are specific to the IntelliJ IDEA IDE. They store project configuration, module settings, and other IDE-specific information. 