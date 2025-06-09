package entity;

import game_logic.GamePanel;
import game_logic.KeyHandler;
import game_logic.Sound;
import object.Chest;
import object.GameObject;
import tile.Tile;
import utils.GameUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class Player extends RenderableEntity {

    /* --------------- [CONSTANTS] --------------- */

    // COORDINATES
    private static final int DEFAULT_X = GamePanel.WORLD_WIDTH / 2;
    private static final int DEFAULT_Y = GamePanel.WORLD_HEIGHT / 2;

    // SPRITE SHEET
    private static final int NUM_ANIMATION_FRAMES = 4;
    private static final int SPRITE_ROWS = 4;

    // ANIMATION
    private static final int DEFAULT_ANIMATION_SPEED = 10;

    // COLLISION
    private static final int COLLISION_BOX_WIDTH = 25;
    private static final int COLLISION_BOX_HEIGHT = 28;

    // GAME PARAMS
    private static final int DEFAULT_SPEED = 5;
    public static final int REQUIRED_KEYS = 0;
    public static final float SPEED_BOOST_MULTIPLIER = 1.2f;

    /* ------------------------------------------- */

    // ANIMATION
    private int frameDelayCounter = 0;                          // to update player animation frames
    private int currentAnimationFrame = 0;

    // COORDINATES
    private final int screenX;
    private final int screenY;

    // GAME PARAMS
    private int numKeys = 0;


    public Player(GamePanel gamePanel) {
        super(gamePanel);

        screenX = (GamePanel.SCREEN_WIDTH / 2) - (GamePanel.TILE_SIZE / 2);
        screenY = (GamePanel.SCREEN_HEIGHT / 2) - (GamePanel.TILE_SIZE / 2);

        setCollisionBox();
        setDefaultValues();
        loadSprites();
    }

    /**
     * Set player's collision box (~solidArea) default values
     */
    private void setCollisionBox() {
        int collisionBoxOffsetX = (GamePanel.TILE_SIZE - COLLISION_BOX_WIDTH) / 2;
        int collisionBoxOffsetY = (GamePanel.TILE_SIZE - COLLISION_BOX_HEIGHT) / 2;
        this.setSolidAreaDefaultX(collisionBoxOffsetX);
        this.setSolidAreaDefaultY(collisionBoxOffsetY);

        Rectangle collisionBox = new Rectangle(
                getSolidAreaDefaultX(),
                getSolidAreaDefaultY(),
                COLLISION_BOX_WIDTH,
                COLLISION_BOX_HEIGHT
        );
        this.setSolidArea(collisionBox);
    }

    /**
     * Set player default position (X, Y world coordinates) and speed as game starts
     */
    private void setDefaultValues() {
        setWorldX(DEFAULT_X);
        setWorldY(DEFAULT_Y);
        setSpeed(DEFAULT_SPEED);
        setFacing(Direction.DOWN);
    }

    /**
     * Loads player sprites from sprite sheets in res folder
     */
    @Override
    public void loadSprites() {

        this.setIdleFrames(loadPlayerSheet("/sprites/player/player_idle.png"));
        this.setRunFrames(loadPlayerSheet("/sprites/player/player_run.png"));
    }

    /*
     * Loads player sprite sheet from the given path, slices it into individual frames,
     * scales each frame to the game's tile size, and returns the resulting array.
     * @param path The path to the sprite sheet image file
     * @return An array containing the scaled frames if loading was successfully,
     *         a placeholder sprites otherwise.
     */
    private BufferedImage[] loadPlayerSheet(String path) {
        try {
            BufferedImage sheet = GameUtils.loadImageSafe(path);
            BufferedImage[] sprites = GameUtils
                    .sliceSpriteSheet(sheet, GamePanel.ORIGINAL_TILE_SIZE, SPRITE_ROWS, NUM_ANIMATION_FRAMES)
                    .toArray(new BufferedImage[0]);

            for (int i = 0; i < sprites.length; i++) {
                sprites[i] = GameUtils.scaleImage(sprites[i], GamePanel.TILE_SIZE, GamePanel.TILE_SIZE);
            }

            System.out.println("Loaded " + sprites.length + " player sprites from " + path);

            return sprites;

        } catch (IOException e) {
            System.err.println("Error loading player sprites:" + e.getMessage());

            // Create placeholders to fill the sprites array
            BufferedImage[] placeholders = new BufferedImage[SPRITE_ROWS * NUM_ANIMATION_FRAMES];
            for (int i = 0; i < placeholders.length; i++) {
                placeholders[i] = GameUtils.getPlaceholderImage(GamePanel.TILE_SIZE, GamePanel.TILE_SIZE, Color.MAGENTA);
            }

            return placeholders;
        }
    }

    /**
     * Updates player state (movement, animation)
     */
    @Override
    public void update() {
        handleMovement();
        updateAnimation();
    }

    /*
     * Handle player movement based on keys input and collisions
     */
    private void handleMovement() {
        int[] direction = calculateMovementDirection();
        int dx = direction[0];
        int dy = direction[1];

        if (dx == 0 && dy == 0) {
            setMoving(false);
            return;
        }

        updateFacingDirection(dx, dy);
        handleCollisions();

        int[] Velocity = calculateFinalVelocity(dx, dy);
        int vx = Velocity[0];
        int vy = Velocity[1];

        updateWorldPosition(vx, vy);

        setMoving(vx != 0 || vy != 0);
    }

    /*
     * Calculates player's movement direction based on keys input.
     * @return player movement direction {dx, dy}
     */
    private int[] calculateMovementDirection() {
        int dx = 0, dy = 0;
        KeyHandler input = gamePanel.getGameKeyHandler();

        if (input.isUpPressed()) dy--;
        if (input.isDownPressed()) dy++;
        if (input.isLeftPressed()) dx--;
        if (input.isRightPressed()) dx++;

        return new int[]{dx, dy};
    }

    /*
     * Updates player facing direction.
     * For diagonal movement prioritizes horizontal directions.
     * @param dx horizontal movement direction
     * @param dy vertical movement direction
     */
    private void updateFacingDirection(int dx, int dy) {
        if (Math.abs(dy) > Math.abs(dx)) {
            this.setFacing(dy > 0 ? Direction.DOWN : Direction.UP);
        } else {
            this.setFacing(dx > 0 ? Direction.RIGHT : Direction.LEFT);
        }
    }

    /*
     * Calculates the player final velocity based on speed and direction,
     * unless player collision is enabled.
     * @param dx horizontal movement direction
     * @param dy vertical movement direction
     * @return player movement velocity {vx, vy},
     * {0, 0} if player collision is enabled
     */
    private int[] calculateFinalVelocity(int dx, int dy) {
        if (isCollisionOn()) return new int[]{0, 0};

        int speed = this.getSpeed();
        return normalizeDiagonalMovement(dx, dy, speed);
    }

    /*
     * Normalizes diagonal movement and applies speed.
     * @param dx horizontal movement direction
     * @param dy vertical movement direction
     * @param speed player speed scalar
     * @return normalized player movement velocity {vx, vy},
     */
    private int[] normalizeDiagonalMovement(int dx, int dy, int speed) {
        if (dx != 0 && dy != 0) {
            double length = Math.sqrt(dx * dx + dy * dy);
            int vx = (int) Math.round((dx / length) * speed);
            int vy = (int) Math.round((dy / length) * speed);
            return new int[]{vx, vy};
        }
        return new int[]{dx * speed, dy * speed};
    }

    /*
     * Updates player world position based on velocity.
     * Clamp player position if not within world boundaries
     * @param dx player horizontal velocity
     * @param dy player vertical velocity
     */
    private void updateWorldPosition(int dx, int dy) {

        // Ensures player cannot move out of world boundaries
        int newX = getWorldX() + dx;
        int newY = getWorldY() + dy;

        int maxWorldWidth = GamePanel.MAX_WORLD_COL * GamePanel.TILE_SIZE;
        int maxWorldHeight = GamePanel.MAX_WORLD_ROW * GamePanel.TILE_SIZE;
        int playerWidth = this.getSolidArea().width;
        int playerHeight = this.getSolidArea().height;

        newX = Math.max(0, Math.min(newX, maxWorldWidth - GamePanel.TILE_SIZE));
        newY = Math.max(0, Math.min(newY, maxWorldHeight - GamePanel.TILE_SIZE));

        setWorldX(newX);
        setWorldY(newY);
    }

    /*
     * Handles player collisions
     */
    private void handleCollisions() {
        handleTileCollisions();
        handleObjectCollisions();
    }

    /*
     * Enables player collision if checked tiles are collidable
     */
    private void handleTileCollisions() {
        this.setCollisionOn(false);
        gamePanel.getCollisionChecker().checkTile(this);
    }

    /*
     * Handles player interaction with game objects on collision.
     * Enables player collision if checked object is collidable
     */
    private void handleObjectCollisions() {
        int gameObjIndex = gamePanel.getCollisionChecker().checkObject(this, true);

        if (gameObjIndex == -1) return;

        GameObject gameObj = gamePanel.getGameObjects().get(gameObjIndex);
        gameObj.onPlayerCollision(gamePanel);
    }

    /**
     * Increments the number of keys the player currently holds
     */
    public void addKey() {
        numKeys++;
    }

    /*
     * Updates current animation frame according to animation speed
     */
    private void updateAnimation() {
        int animationSpeed = (int) (DEFAULT_ANIMATION_SPEED * (DEFAULT_ANIMATION_SPEED / this.getSpeed()));
        if (++this.frameDelayCounter > animationSpeed) {
            this.currentAnimationFrame = (this.currentAnimationFrame + 1) % NUM_ANIMATION_FRAMES;
            this.frameDelayCounter = 0;
        }
    }

    /**
     * Draws current player sprite to the screen
     * @param g2 The Graphics2D context to draw on
     */
    @Override
    public void draw(Graphics2D g2) {
        g2.drawImage(getCurrentSprite(), screenX, screenY,null);
    }

    /**
     * Draws player hitbox when in debug mode
     * @param g2 The Graphics2D context to draw on
     */
    @Override
    public void drawDebug(Graphics2D g2) {
        // Get original color of Graphics context
        Color originalColor = g2.getColor();

        g2.setColor(Color.RED);
        Rectangle hitbox = getSolidArea();
        g2.drawRect(screenX + hitbox.x, screenY + hitbox.y,
                hitbox.width, hitbox.height);

        // Restore original Graphics color after drawing operation
        g2.setColor(originalColor);
    }

    /**
     * Returns current player sprite based on facing direction, current animation frame and player state (idle/run)
     * @return current player sprite as BufferedImage
     */
    public BufferedImage getCurrentSprite() {
        int directionRow = switch (this.getFacing()) {
            case DOWN -> 0;
            case LEFT -> 1;
            case RIGHT -> 2;
            case UP -> 3;
        };

        BufferedImage[] frames = this.isMoving() ? this.getRunFrames() : this.getIdleFrames();
        int frameIndex = (directionRow * NUM_ANIMATION_FRAMES) + currentAnimationFrame;

        return frames[frameIndex];
    }

    /**
     * Reset player values and game params to default value
     */
    public void reset() {
        setDefaultValues();
        numKeys = 0;
        setMoving(false);
    }

    /* --------------- [GETTER METHODS] --------------- */

    public int getScreenX() { return screenX; }
    public int getScreenY() { return screenY; }
    public int getNumKeys() { return numKeys; }

    /* ------------------------------------------------ */
}