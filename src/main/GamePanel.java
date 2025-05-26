package main;

import entity.Player;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;

/**
 * Game panel where all game components are displayed
 * @author LC
 */
public class GamePanel extends JPanel implements Runnable{
    // SCREEN SETTINGS
    public static final int ORIGINAL_TILE_SIZE = 16;                        // 16x16 default tile size
    public static final int SCALE = 3;
    public static final int TILE_SIZE = ORIGINAL_TILE_SIZE * SCALE;          // 48x48 actual tile size

    public static final int MAX_SCREEN_COL = 16;                            // 4x3 ratio
    public static final int MAX_SCREEN_ROW = 12;
    public static final int SCREEN_WIDTH = MAX_SCREEN_COL * TILE_SIZE;        // 768x576 pixels
    public static final int SCREEN_HEIGHT = MAX_SCREEN_ROW * TILE_SIZE;

    // WORLD SETTINGS
    public static final int MAX_WORLD_COL = 30;
    public static final int MAX_WORLD_ROW = 30;
    public static final int WORLD_WIDTH = TILE_SIZE * MAX_WORLD_COL;
    public static final int WORLD_HEIGHT = TILE_SIZE * MAX_WORLD_ROW;

    // FPS
    private static final int FPS = 60;

    private final KeyHandler gameKeyHandler = new KeyHandler();
    private Thread gameThread;

    private Player player = new Player(this, gameKeyHandler);
    private TileManager tileManager = new TileManager(this);

    public GamePanel() {
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);               // component drawing will be done in an offscreen painting buffer (improves game's rendering performance)
        this.addKeyListener(gameKeyHandler);
        this.setFocusable(true);                    // GamePanel can be "focused" to receive key input
        requestFocusInWindow();                     // Request input focus for GamePanel
    }

    /**
     * Starts the game loop thread
     */
    public void startGameThread() {
        gameThread = new Thread(this, "Game Thread");
        // starting the thread causes the run method to be automatically called in that separately executing thread
        gameThread.start();
    }

    /**
     * GameThread: fixed timestep game loop with a delta accumulator
     */
    @Override
    public void run() {

        final double drawInterval = 1_000_000_000.0 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();

        while (!Thread.currentThread().isInterrupted()) {

            long currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }

            // Small sleep to reduce CPU usage
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Updates game state based on current input
     */
    public void update() {
        // Prevent key events to get "stuck" if the window loses focus while a key is pressed
        if (this.isFocusOwner()) {
            player.update();
        } else {
            gameKeyHandler.resetAllKeys();
        }
    }

    /**
     * Handles custom rendering of game elements.
     * This method is automatically called by Swing when the component needs to be redrawn
     * @param g The Graphics context used for rendering. This is provided by Swing's painting system.
     *          It represents the drawing surface of the component.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);                    // to ensure proper rendering hierarchy and clear background

        Graphics2D g2d = (Graphics2D) g;            // extends Graphics providing more advanced features

        tileManager.render(g2d);
        player.render(g2d);

        // disposal of Graphics object and release of system resources that it is using is handled by Swing
    }

    public Player getPlayer() { return player; }
}
