package game_logic;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

// KeyAdapter implements the KeyListener interface by providing empty method definitions
// (we override only the ones we're interested in)
public class KeyHandler extends KeyAdapter {

    private boolean upPressed, downPressed, leftPressed, rightPressed;
    private boolean debugMode = false;
    private final GamePanel gamePanel;

    public KeyHandler(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        GamePanel.GameState currentState = gamePanel.getGameState();
        UI ui = gamePanel.getUi();

        switch (currentState) {
            case TITLE -> { handleTitleInput(code, ui); }
            case PLAYING, PAUSED -> { handleGameInput(code, currentState); }
        }
    }

    /*
     * Handles input in the game title screen
     * @param code The code of the pressed key
     * @param ui The game UI
     */
    private void handleTitleInput(int code, UI ui) {
        UI.TitleScreenState screenState = ui.getTitleScreenState();

        switch (screenState) {
            case MAIN_MENU -> {
                switch (code) {
                    // Navigate between menu commands with WS or up/down arrows
                    case KeyEvent.VK_W, KeyEvent.VK_UP -> { ui.decreaseSelectedCommand(); }
                    case KeyEvent.VK_S, KeyEvent.VK_DOWN -> { ui.increaseSelectedCommand(); }

                    // Select command when Enter Key is pressed
                    case KeyEvent.VK_ENTER -> { handleMainMenuCommands(ui); }
                }
            }
            case COMMANDS_SCREEN -> {
                // Start game when Enter Key is pressed
                if (code == KeyEvent.VK_ENTER) {
                    gamePanel.setGameState(GamePanel.GameState.PLAYING);
                    gamePanel.playMusic(Sound.GAME_THEME);
                }
            }
        }
    }

    /*
     * Handles actions performed when a menu command is selected
     * @param ui The game UI
     */
    private void handleMainMenuCommands(UI ui) {
        switch (ui.getSelectedCommand()) {
            case 0 -> { ui.setTitleScreenState(UI.TitleScreenState.COMMANDS_SCREEN); }
            case 1 -> {
                // TODO add function
            }
            case 2 -> System.exit(0);
        }
    }

    /*
     * Handles game inputs (PLAYING/PAUSED game state)
     * @param code The code of the pressed key
     * @param currentState Current game state
     */
    private void handleGameInput(int code, GamePanel.GameState currentState) {
        updateDirKeyState(code, true);

        switch (code) {
            // Enable/disable debug mode when quote key(') is pressed
            case KeyEvent.VK_QUOTE -> debugMode = !debugMode;

            // Pause/resume game when P key is pressed
            case KeyEvent.VK_P -> {
                switch (currentState) {
                    case PLAYING -> gamePanel.setGameState(GamePanel.GameState.PAUSED);
                    case PAUSED -> gamePanel.setGameState(GamePanel.GameState.PLAYING);
                }
            }

            // Refresh game world map when K key is pressed
            case KeyEvent.VK_K -> {
                System.out.println("Reloading map...");
                gamePanel.getTileManager().loadTileMap("res/maps/world01.txt");
            }

            // Stop/resume music when M key is pressed
            case KeyEvent.VK_M -> {
                // TODO implement
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        updateDirKeyState(e.getKeyCode(), false);
    }

    /**
     * Updates the key press state for directional keys (WASD / directional arrows)
     * @param keyCode the key code of the key event
     * @param isPressed true if the key is pressed, false if released
     */
    private void updateDirKeyState(int keyCode, boolean isPressed) {
        switch (keyCode) {
            case KeyEvent.VK_W, KeyEvent.VK_UP -> upPressed = isPressed;
            case KeyEvent.VK_S, KeyEvent.VK_DOWN  -> downPressed = isPressed;
            case KeyEvent.VK_A, KeyEvent.VK_LEFT -> leftPressed = isPressed;
            case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> rightPressed = isPressed;
        }
    }

    /**
     * Set all keys pressed state to false
     */
    public void resetAllKeys() {
        upPressed = false;
        downPressed = false;
        leftPressed = false;
        rightPressed = false;
    }

    /* --------------- [GETTER METHODS] --------------- */

    public boolean isUpPressed() { return upPressed; }
    public boolean isDownPressed() { return downPressed; }
    public boolean isLeftPressed() { return leftPressed; }
    public boolean isRightPressed() { return rightPressed; }
    public boolean isDebugModeOn() {return debugMode;}

    /* ------------------------------------------------ */
}
