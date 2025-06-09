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

    /* --------------- [CONSTANTS] --------------- */

    // SCREEN SETTINGS
    public static final int ORIGINAL_TILE_SIZE = 16;                        // 16x16 default tile size
    public static final int SCALE = 3;
    public static final int TILE_SIZE = ORIGINAL_TILE_SIZE * SCALE;          // 48x48 actual tile size
    public static final int MAX_SCREEN_COL = 16;                            // 4x3 ratio
    public static final int MAX_SCREEN_ROW = 12;
    public static final int SCREEN_WIDTH = MAX_SCREEN_COL * TILE_SIZE;        // 768x576 pixels
    public static final int SCREEN_HEIGHT = MAX_SCREEN_ROW * TILE_SIZE;

    // WORLD SETTINGS
    public static final int MAX_WORLD_COL = 80;
    public static final int MAX_WORLD_ROW = 80;
    public static final int WORLD_WIDTH = MAX_WORLD_COL * TILE_SIZE;
    public static final int WORLD_HEIGHT = MAX_WORLD_ROW * TILE_SIZE;

    // FPS
    public static final int FPS = 60;

    // DEBUG
    private static final int DEBUG_TEXT_INITIAL_X = 20;
    private static final int DEBUG_TEXT_INITIAL_Y = 450;
    private static final int DEBUG_TEXT_SPACING = 20;
    private final Font debugFont = new Font("Monospaced", Font.BOLD, 25);
    private final Color debugColor = Color.WHITE;

    /* ------------------------------------------- */

    // GAME ENGINE COMPONENTS
    private Thread gameThread;
    private final KeyHandler gameKeyHandler = new KeyHandler(this);
    private final TileManager tileManager = new TileManager(this);
    private final CollisionChecker collisionChecker = new CollisionChecker(this);
    private final AssetSetter assetSetter = new AssetSetter(this);

    // GAME ELEMENTS
    private final Player player = new Player(this);
    private final ArrayList<GameObject> gameObjects = new ArrayList<>();
    private final Sound music = new Sound();
    private final Sound SE = new Sound();
    private final UI ui = new UI(this);

    // GAME STATE
    public enum GameState {
        PLAYING,
        PAUSED,
        TITLE,
        OPTIONS,
        ENDING
    }
    private GameState gameState;


    public GamePanel() {
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);               // component drawing will be done in an offscreen painting buffer (improves game's rendering performance)
        this.addKeyListener(gameKeyHandler);
        this.setFocusable(true);                    // GamePanel can be "focused" to receive key input
        requestFocusInWindow();                     // Request input focus for GamePanel
    }

    /**
     * Setup initial game config
     */
    public void setupGame() {
        gameState = GameState.TITLE;
        assetSetter.setGameObjects();
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
            switch (gameState) {
                case PLAYING -> {
                    player.update();
                }
                case PAUSED, OPTIONS, ENDING -> {
                    // No updates
                }
            }
        } else {
            gameKeyHandler.resetAllKeys();
        }
    }

    /**
     * Handles custom rendering of game elements.
     * This method is automatically called by Swing when the component needs to be redrawn.
     * Disposal of Graphics object and release of system resources that it is using is handled by Swing
     * @param g The Graphics2D context to draw on
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);                    // to ensure proper rendering hierarchy and clear background

        Graphics2D g2 = (Graphics2D) g;            // extends Graphics providing more advanced features

        switch (gameState) {
            case TITLE -> drawTitleScreen(g2);
            case PLAYING, PAUSED, OPTIONS, ENDING -> drawGame(g2);
        }
    }

    /*
     * Draws title screen UI elements
     * @param g2 The Graphics2D context to draw on
     */
    private void drawTitleScreen(Graphics2D g2) {
        ui.draw(g2);
    }

    /*
     * Draws all game elements such as player sprites, tiles, object, UI, ...
     * @param g2 The Graphics2D context to draw on
     */
    private void drawGame(Graphics2D g2) {
        // DEBUG
        boolean debug = gameKeyHandler.isDebugModeOn();
        long drawStart = debug ? System.nanoTime() : 0;

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
        if (debug && gameState == GameState.PLAYING) {
            drawDebugInfo(drawStart, g2);
            player.drawDebug(g2);
        }
    }

    /*
     * Draws debug info on the screen, such as player hitbox, coordinates
     * and draw time for each call to paintComponent method (60 times per second)
     * @param drawStart Start time of paint component method
     * @param g2 The Graphics2D context to draw on
     */
    private void drawDebugInfo(long drawStart, Graphics2D g2) {
        // Save original font properties
        Font originalFont = g2.getFont();
        Color originalColor = g2.getColor();

        // Set debug font properties
        g2.setFont(debugFont);
        g2.setColor(debugColor);

        long drawEnd = System.nanoTime();
        long timePassed = (drawEnd - drawStart) / 1_000;            // draw time in micro seconds (µs)

        int x = DEBUG_TEXT_INITIAL_X;
        int y = DEBUG_TEXT_INITIAL_Y;
        int spacing = DEBUG_TEXT_SPACING;

        g2.drawString("WorldX: " + player.getWorldX(), x, y); y += spacing;
        g2.drawString("WorldY: " + player.getWorldY(), x, y); y += spacing;
        g2.drawString("Col: " + ((player.getWorldX() + player.getSolidArea().x) / TILE_SIZE), x, y); y += spacing;
        g2.drawString("Row: " + ((player.getWorldY() + player.getSolidArea().y) / TILE_SIZE), x, y); y += spacing * 2;
        g2.drawString("Draw Time: " + timePassed + " µs", x, y);

        // Restore font properties
        g2.setFont(originalFont);
        g2.setColor(originalColor);
    }

    /**
     * Plays music with given soundID
     * @param soundID ID of music to play
     */
    public void playMusic(int soundID) {
        music.loadAudio(soundID);
        music.play();
        music.loop();
    }

    /**
     * Stops currently playing music
     */
    public void stopMusic() {
        music.stop();
    }

    /**
     * Resume currently stopped music
     */
    public void resumeMusic() {
        music.play();
        music.loop();
    }

    /**
     * Plays sound effect with given soundID
     * @param soundID ID of sound to play
     */
    public void playSE(int soundID) {
        SE.loadAudio(soundID);
        SE.play();
    }

    /**
     * Sets game state to the given one.
     * Stops music if gameState = PAUSED, resume it if gameState = PLAYING
     * @param gameState Game state to set
     */
    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    /**
     * Reset game state
     */
    public void resetGame() {
        // Reset player state
        player.reset();

        // Clear all existing objects
        gameObjects.clear();

        // Reset UI elements
        ui.reset();

        // Reset buffered keys
        gameKeyHandler.resetAllKeys();;

        setupGame();
        stopMusic();
    }

    /* --------------- [GETTER METHODS] --------------- */

    public Player getPlayer() { return player; }
    public TileManager getTileManager() { return tileManager; }
    public CollisionChecker getCollisionChecker() { return collisionChecker; }
    public KeyHandler getGameKeyHandler() { return gameKeyHandler; }
    public AssetSetter getAssetSetter() { return assetSetter; }
    public ArrayList<GameObject> getGameObjects() { return gameObjects; }
    public UI getUi() { return ui; }
    public GameState getGameState() { return gameState; }
    public Sound getMusic() { return music; }
    public Sound getSE() { return SE; }

    /* ------------------------------------------------ */
}
