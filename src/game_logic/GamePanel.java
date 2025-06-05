package game_logic;

import entity.Player;
import object.GameObject;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Game panel where all game components are displayed.
 * Responsible for Game Loop update and rendering
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
    public static final int MAX_WORLD_COL = 50;
    public static final int MAX_WORLD_ROW = 50;
    public static final int WORLD_WIDTH = MAX_WORLD_COL * TILE_SIZE;
    public static final int WORLD_HEIGHT = MAX_WORLD_ROW * TILE_SIZE;

    // FPS
    public static final int FPS = 60;

    // GAME ENGINE COMPONENTS
    private Thread gameThread;
    private final KeyHandler gameKeyHandler = new KeyHandler();

    // GAME ELEMENTS
    private final Player player = new Player(this);
    private final TileManager tileManager = new TileManager(this);
    private final CollisionChecker collisionChecker = new CollisionChecker(this);
    private final AssetSetter assetSetter = new AssetSetter(this);
    private final ArrayList<GameObject> gameObjects = new ArrayList<>();
    private final Sound music = new Sound();
    private final Sound soundEffect = new Sound();
    private final UI ui = new UI(this);

    // DEBUG
    private final Font debugFont = new Font("Monospaced", Font.BOLD, 25);
    private final Color debugColor = Color.WHITE;

    public GamePanel() {
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);               // component drawing will be done in an offscreen painting buffer (improves game's rendering performance)
        this.addKeyListener(gameKeyHandler);
        this.setFocusable(true);                    // GamePanel can be "focused" to receive key input
        requestFocusInWindow();                     // Request input focus for GamePanel
    }

    public void setupGame() {
        assetSetter.setGameObjects();
        playMusic(Sound.GAME_THEME);
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
     * Stops the game loop thread
     */
    public void stopGameThread() {
        gameThread.interrupt();
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

            // to reduce CPU usage
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();         // Restore interrupt flag
                break;
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
     * This method is automatically called by Swing when the component needs to be redrawn.
     * Disposal of Graphics object and release of system resources that it is using is handled by Swing
     * @param g The Graphics context used for rendering. This is provided by Swing's painting system.
     *          It represents the drawing surface of the component.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);                    // to ensure proper rendering hierarchy and clear background
        Graphics2D g2 = (Graphics2D) g;            // extends Graphics providing more advanced features

        // DEBUG
        boolean debug = gameKeyHandler.isDebugModeOn();
        long drawStart = 0;
        if (debug) {
            drawStart = System.nanoTime();
        }

        // TILES
        tileManager.draw(g2);

        // OBJECTS
        for (GameObject gameObject : gameObjects) {
            gameObject.draw(g2);
        }

        // PLAYER
        player.draw(g2);

        // UI
        ui.draw(g2);

        // DEBUG
        if (debug) {
            drawDebugInfo(drawStart, g2);
            player.drawDebug(g2);
        }
    }

    private void drawDebugInfo(long drawStart, Graphics2D g2) {
        // Save original font properties
        Font originalFont = g2.getFont();
        Color originalColor = g2.getColor();

        // Set debug font properties
        g2.setFont(debugFont);
        g2.setColor(debugColor);

        long drawEnd = System.nanoTime();
        long timePassed = (drawEnd - drawStart) / 1_000;            // draw time in micro seconds (µs)
        g2.drawString("Draw Time: " + timePassed + " µs", 20, 550);
        System.out.println("Draw Time: " + timePassed + " µs");

        // Restore font properties
        g2.setFont(originalFont);
        g2.setColor(originalColor);
    }

    public void playMusic(int soundID) {
        music.setFile(soundID);
        music.play();
        music.loop();
    }

    public void stopMusic() {
        music.stop();
    }

    public void playSoundEffect(int soundID) {
        soundEffect.setFile(soundID);
        soundEffect.play();
    }

    // Getter methods
    public Player getPlayer() { return player; }
    public TileManager getTileManager() { return tileManager; }
    public CollisionChecker getCollisionChecker() { return collisionChecker; }
    public KeyHandler getGameKeyHandler() { return gameKeyHandler; }
    public AssetSetter getAssetSetter() { return assetSetter; }
    public ArrayList<GameObject> getGameObjects() { return gameObjects; }
    public UI getUi() { return ui; }
}
