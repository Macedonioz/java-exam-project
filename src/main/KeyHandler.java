package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    public boolean upPressed, downPressed, leftPressed, rightPressed;

    // TODO cercarare teoria per keyhandler senza tutte le implementazioni

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        updateKeyState(e.getKeyCode(), true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        updateKeyState(e.getKeyCode(), false);
    }

    /**
     * Updates the key press state for directional keys (WASD)
     * @param keyCode the key code of the key event
     * @param isPressed true if the key is pressed, false if released
     */
    private void updateKeyState(int keyCode, boolean isPressed) {
        // Java enhanced switch statement      // TODO controllare docuentazione
        switch (keyCode) {
            case KeyEvent.VK_W -> upPressed = isPressed;
            case KeyEvent.VK_S -> downPressed = isPressed;
            case KeyEvent.VK_A -> leftPressed = isPressed;
            case KeyEvent.VK_D -> rightPressed = isPressed;
            default -> {
                // Optional: handle other keys if needed
            }
        }
    }
}
