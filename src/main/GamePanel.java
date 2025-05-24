package main;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable{
    // SCREEN SETTINGS
    private static final int ORIGINAL_TILE_SIZE = 16;                        // 16x16 default tile size
    private static final int SCALE = 3;

    private static final int TILE_SIZE = ORIGINAL_TILE_SIZE * SCALE;          // 48x48 actual tile size
    private static final int MAX_SCREEN_COL = 16;                            // 4x3 ratio
    private static final int MAX_SCREEN_ROW = 12;
    private static final int SCREEN_WIDTH = MAX_SCREEN_COL * TILE_SIZE;        // 768x576 pixels
    private static final int SCREEN_HEIGHT = MAX_SCREEN_ROW * TILE_SIZE;

    // FPS
    private static final int FPS = 60;

    private final KeyHandler gameKeyHandler = new KeyHandler();
    private Thread gameThread;

    // TODO create player class
    // Player state
    private int playerX = 100;
    private int playerY = 100;
    private int playerSpeed = 4;

    public GamePanel() {
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);               // component drawing will be done in an offscreen painting buffer (improves game's rendering performance)
        this.addKeyListener(gameKeyHandler);
        this.setFocusable(true);                    // GamePanel can be "focused" to receive key input
        requestFocusInWindow();                     // Request input focus for GamePanel
    }

    /**
     * Starts the game loop thread (if it has not yet been created
     * or if the existing one has finished)
     */
    public void startGameThread() {

        if (gameThread == null || !gameThread.isAlive()) {
            gameThread = new Thread(this, "Game Thread");
            // starting the thread causes the run method to be automatically called in that separately executing thread
            gameThread.start();
        }
    }

    @Override
    public void run() {

        final double drawInterval = 1_000_000_000.0 / FPS;                  // Interval between frames (nanos in one second / FPS) [0.1666s for 60 FPS]
        double nextDrawTime = System.nanoTime() + drawInterval;

        while(!Thread.currentThread().isInterrupted()) {

            update();
            repaint();

            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = Math.max(remainingTime, 0) / 1_000_000.0;       // convert to milliseconds & set to 0 if value is negative
                Thread.sleep((long) remainingTime);
                nextDrawTime += drawInterval;
            } catch (InterruptedException e) {
                // Exit the loop and clean up
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Updates game state based on current input
     */
    public void update() {

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

        playerX += dx;
        playerY += dy;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;            // extends Graphics providing more advanced features

        g2d.setColor(Color.white);
        g2d.fillOval(playerX, playerY, TILE_SIZE, TILE_SIZE);

        // disposal of Graphics object and release of system resources that it is using is handled by Swing
    }
}
