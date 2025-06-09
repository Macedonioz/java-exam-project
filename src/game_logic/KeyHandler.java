package game_logic;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyHandler extends KeyAdapter {

    private final GamePanel gamePanel;

    // KEY FLAGS
    private boolean upPressed, downPressed, leftPressed, rightPressed;
    private boolean enterPressed = false;
    private boolean debugMode = false;

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
            case OPTIONS -> { handleOptionsInput(code, ui); }
            case ENDING -> { handleEndingInput(code); }
        }
    }

    /*
     * Handles input in the ending screen
     * @param Code The code of the pressed key
     */
    private void handleEndingInput(int Code) {
        if (Code == KeyEvent.VK_ENTER) {
            enterPressed = true;
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
                    case KeyEvent.VK_W, KeyEvent.VK_UP -> {
                        ui.decreaseSelectedCommand(UI.MENU_COMMANDS_NUM);
                        gamePanel.playSE(Sound.MENU_SELECT); }

                    case KeyEvent.VK_S, KeyEvent.VK_DOWN -> {
                        ui.increaseSelectedCommand(UI.MENU_COMMANDS_NUM);
                        gamePanel.playSE(Sound.MENU_SELECT);
                    }

                    // Select command when Enter Key is pressed
                    case KeyEvent.VK_ENTER -> { enterPressed = true; }
                }
            }
            case COMMANDS_SCREEN -> {
                if (code == KeyEvent.VK_ENTER) { enterPressed = true; }
            }
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
                    case PLAYING -> {
                        gamePanel.setGameState(GamePanel.GameState.PAUSED);
                        gamePanel.stopMusic();
                    }
                    case PAUSED -> {
                        gamePanel.setGameState(GamePanel.GameState.PLAYING);
                        gamePanel.resumeMusic();
                    }
                }
            }

            // Refresh game world map when K key is pressed
            case KeyEvent.VK_K -> {
                System.out.println("Reloading map...");
                gamePanel.getTileManager().loadTileMap("res/maps/world01.txt");
            }

            // Open/Close option menu when Esc key is pressed
            case KeyEvent.VK_ESCAPE -> {
                gamePanel.setGameState(GamePanel.GameState.OPTIONS);
            }
        }
    }

    /*
     * Handles input in the options state
     * @param code The code of the pressed key
     * @param ui The game UI
     */
    private void handleOptionsInput(int code, UI ui) {

        // Select max number of commands based on option substate
        int maxCommandNum = switch (ui.getSubState()) {
            case UI.SUB_OPTION_MAIN -> UI.MAIN_OPTION_COMMANDS_NUM;
            case UI.SUB_OPTION_END_CONFIRM -> UI.END_CONFIRM_OPTION_COMMANDS_NUM;
            default -> 0;
        };

        switch (code) {
            case KeyEvent.VK_ESCAPE -> { gamePanel.setGameState(GamePanel.GameState.PLAYING); }
            case KeyEvent.VK_ENTER -> { enterPressed = true; }

            // Navigate between options
            case KeyEvent.VK_W, KeyEvent.VK_UP -> {
                ui.decreaseSelectedCommand(maxCommandNum);
                gamePanel.playSE(Sound.MENU_SELECT);
            }
            case KeyEvent.VK_S, KeyEvent.VK_DOWN -> {
                ui.increaseSelectedCommand(maxCommandNum);
                gamePanel.playSE(Sound.MENU_SELECT);
            }

            // Adjust volume while in main option substate
            case KeyEvent.VK_A, KeyEvent.VK_LEFT -> {  handleVolumeAdjustment(ui, false); }
            case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> {  handleVolumeAdjustment(ui, true); }
        }

    }

    /**
     * Handle music and se volume adjustment based on current substate and input detected
     * @param ui The game UI
     * @param increase Increase volume if true, decrease if false
     */
    private void handleVolumeAdjustment(UI ui, boolean increase) {
        if (ui.getSubState() != UI.SUB_OPTION_MAIN) return;

        int selected = ui.getSelectedCommand();

        if (selected == UI.OPTIONS_MUSIC_COMMAND) {
            if (increase) {
                gamePanel.getMusic().increaseVolumeScale();
            } else {
                gamePanel.getMusic().decreaseVolumeScale();
            }
            gamePanel.getMusic().adjustVolume();
            gamePanel.playSE(Sound.MENU_SELECT);
        }

        if (selected == UI.OPTIONS_SE_COMMAND) {
            if (increase) {
                gamePanel.getSE().increaseVolumeScale();
            } else {
                gamePanel.getSE().decreaseVolumeScale();
            }
            gamePanel.playSE(Sound.MENU_SELECT);
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
     * Set enter key pressed state to false
     */
    public void resetEnterKeyState() {
        enterPressed = false;
    }

    /**
     * Set all keys pressed state to false
     */
    public void resetAllKeys() {
        upPressed = false;
        downPressed = false;
        leftPressed = false;
        rightPressed = false;
        enterPressed = false;
    }

    /* --------------- [GETTER METHODS] --------------- */

    public boolean isUpPressed() { return upPressed; }
    public boolean isDownPressed() { return downPressed; }
    public boolean isLeftPressed() { return leftPressed; }
    public boolean isRightPressed() { return rightPressed; }
    public boolean isEnterPressed() { return enterPressed; }
    public boolean isDebugModeOn() {return debugMode;}

    /* ------------------------------------------------ */
}
