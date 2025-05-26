package main;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

// KeyAdapter implements the KeyListener interface by providing empty method definitions
// (we override only the ones we're interested in)
public class KeyHandler extends KeyAdapter {

    private boolean upPressed, downPressed, leftPressed, rightPressed;

    @Override
    public void keyPressed(KeyEvent e) {
        updateKeyState(e.getKeyCode(), true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        updateKeyState(e.getKeyCode(), false);
    }

    /**
     * Updates the key press state for directional keys (WASD / directional arrows)
     * @param keyCode the key code of the key event
     * @param isPressed true if the key is pressed, false if released
     */
    private void updateKeyState(int keyCode, boolean isPressed) {
        // Java enhanced switch statement
        switch (keyCode) {
            case KeyEvent.VK_W, KeyEvent.VK_UP -> upPressed = isPressed;
            case KeyEvent.VK_S, KeyEvent.VK_DOWN  -> downPressed = isPressed;
            case KeyEvent.VK_A, KeyEvent.VK_LEFT -> leftPressed = isPressed;
            case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> rightPressed = isPressed;
            default -> {
                // Optional: handle other keys if needed
            }
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

    public boolean isUpPressed() {
        return upPressed;
    }

    public boolean isDownPressed() {
        return downPressed;
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }
}
