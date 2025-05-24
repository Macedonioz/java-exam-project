package entity;

import main.GamePanel;
import main.KeyHandler;

import java.awt.*;

public class Player extends Entity {

    GamePanel gamePanel;
    KeyHandler gameKeyHandler;

    public Player(GamePanel gamePanel, KeyHandler gameKeyHandler) {
        this.gamePanel = gamePanel;
        this.gameKeyHandler = gameKeyHandler;

        setDefaultValues();
    }

    public void setDefaultValues() {
        setX(100);
        setY(100);
        setSpeed(4);
    }

    public void update() {
        int playerSpeed = this.getSpeed();
        int dx = 0;
        int dy = 0;

        // Checks all keys independently to allow diagonal movement
        if (gameKeyHandler.isUpPressed()) {
            dy -= 1;
        }
        if (gameKeyHandler.isDownPressed()) {
            dy += 1;
        }
        if (gameKeyHandler.isLeftPressed()) {
            dx -= 1;
        }
        if (gameKeyHandler.isRightPressed()) {
            dx += 1;
        }

        // Normalize diagonal movement to make it the same speed as horizontal/vertical one
        if (dx != 0 && dy != 0) {
            double length = Math.sqrt(dx * dx + dy * dy);
            dx = (int) Math.round((dx / length) * playerSpeed);
            dy = (int) Math.round((dy / length) * playerSpeed);
        } else {
            dx *= playerSpeed;
            dy *= playerSpeed;
        }

        setX(getX() + dx);
        setY(getY() + dy);
    }

    public void render(Graphics g) {
        g.setColor(Color.white);
        g.fillOval(getX(), getY(), GamePanel.TILE_SIZE, GamePanel.TILE_SIZE);
    }
}
