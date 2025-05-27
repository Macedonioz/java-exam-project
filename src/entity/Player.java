package entity;

import main.GamePanel;
import main.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


public class Player extends RenderableEntity {

    private final GamePanel gamePanel;

    private static final int DEFAULT_SPEED = 4;
    private final int screenX;
    private final int screenY;

    // Sprites
    private static final int NUM_ANIMATION_FRAMES = 4;          // number of animation frames for every sprite row
    private static final int SPRITE_ROWS = 4;                   // a row in the sprite sheet for every direction

    // Animation
    private int frameDelayCounter = 0;                          // to update player animation frames
    private int currentAnimationFrame = 0;
    private static final int ANIMATION_FRAME_DELAY = 10;                // animation speed

    // Collision
    private static final int COLLISION_BOX_OFFSET_X = 8;
    private static final int COLLISION_BOX_OFFSET_Y = 16;
    private static final int COLLISION_BOX_WIDTH = 32;
    private static final int COLLISION_BOX_HEIGHT = 32;


    public Player(GamePanel gamePanel) {
        this.gamePanel = gamePanel;

        screenX = (GamePanel.SCREEN_WIDTH / 2) - (GamePanel.TILE_SIZE / 2);
        screenY = (GamePanel.SCREEN_HEIGHT / 2) - (GamePanel.TILE_SIZE / 2);

        Rectangle collisionBox = new Rectangle(
                COLLISION_BOX_OFFSET_X,
                COLLISION_BOX_OFFSET_Y,
                COLLISION_BOX_WIDTH,
                COLLISION_BOX_HEIGHT
        );
        this.setSolidArea(collisionBox);

        setDefaultValues();
        loadSprites();
    }

    /**
     * Set player default position (X, Y world coordinates) and speed as game starts
     */
    public void setDefaultValues() {
        setWorldX(GamePanel.WORLD_WIDTH / 2);
        setWorldY(GamePanel.WORLD_HEIGHT / 2);
        setSpeed(DEFAULT_SPEED);
        setFacing(Direction.DOWN);
    }

    /**
     * Loads player sprites from sprite sheets in res folder
     */
    @Override
    public void loadSprites() {
        try {
            this.setIdleFrames(sliceSpriteSheet(loadImageSafe("/sprites/player/player_idle.png")));
            this.setRunFrames(sliceSpriteSheet(loadImageSafe("/sprites/player/player_run.png")));

        } catch (IOException e) {
            System.err.println("Error loading player sprites:" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Slice given sprite sheet into its single sprites.
     * Supports sprite sheets of the following format:
     * - single frame --> ORIGINAL_TILE_SIZE x ORIGINAL_TILE_SIZE (es. 16x16)
     * - number of rows --> SPRITE_ROWS
     * - number of animation frames per row --> NUM_ANIMATION_FRAMES
     * - no empty pixels between frames (frames must be "attached" to each other)
     * @param sheet the sprite sheet to slice
     * @return an array containing the individual sprites (as BufferedImages)
     */
    private BufferedImage[] sliceSpriteSheet(BufferedImage sheet) {
        BufferedImage[] frames = new BufferedImage[SPRITE_ROWS * NUM_ANIMATION_FRAMES];

        int frameSize = GamePanel.ORIGINAL_TILE_SIZE;

        for (int i = 0; i < SPRITE_ROWS; i++) {
            for (int j = 0; j < NUM_ANIMATION_FRAMES; j++) {
                int frameIndex = (i * NUM_ANIMATION_FRAMES) + j;
                frames[frameIndex] = sheet.getSubimage(j * frameSize, i * frameSize,
                        frameSize, frameSize);
            }
        }

        return frames;
    }

    /**
     * Loads image safely from given path
     * @param path file path from which to open image
     * @return the image if loading was succesfull
     * @throws FileNotFoundException if file was not found
     * @throws IOException if image format is invalid
     */
    private BufferedImage loadImageSafe(String path) throws IOException {
        try (InputStream is = getClass().getResourceAsStream(path)) {
            if (is == null) {
                throw new FileNotFoundException("File not found: " + path);
            }

            BufferedImage image = ImageIO.read(is);
            if (image == null) {
                throw new IOException("Invalid image format!");
            }
            return image;
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

    /**
     * Handle player movement based on keys input and collisions
     */
    private void handleMovement() {
        Point movementDirection = calculateMovementDirection();

        if (movementDirection.x == 0 && movementDirection.y == 0) {
            setMoving(false);
            return;
        }

        updateFacingDirection(movementDirection);
        handleCollisionCheck();

        Point finalVelocity = calculateFinalVelocity(movementDirection);
        updateWorldPosition(finalVelocity);

        setMoving(finalVelocity.x != 0 || finalVelocity.y != 0);
    }

    /**
     * Calculates player's movement direction based on keys input
     * @return a point(x, y) representing movement direction
     *         dx = 1 --> RIGHT;    dx = -1 --> LEFT
     *         dy = 1 --> DOWN;     dy = -1 --> UP
     */
    private Point calculateMovementDirection() {
        int dx = 0, dy = 0;
        KeyHandler input = gamePanel.getGameKeyHandler();

        if (input.isUpPressed()) dy--;
        if (input.isDownPressed()) dy++;
        if (input.isLeftPressed()) dx--;
        if (input.isRightPressed()) dx++;

        return new Point(dx, dy);
    }

    /**
     * Updates player facing direction.
     * For diagonal movement prioritizes horizontal directions
     * @param direction player movement directions (x, y)
     */
    private void updateFacingDirection(Point direction) {
        if (Math.abs(direction.y) > Math.abs(direction.x)) {
            this.setFacing(direction.y > 0 ? Direction.DOWN : Direction.UP);
        } else {
            this.setFacing(direction.x > 0 ? Direction.RIGHT : Direction.LEFT);
        }
    }

    /**
     * Enables player collision if checked tiles are collidable
     */
    private void handleCollisionCheck() {
        this.setCollisionOn(false);
        gamePanel.getCollisionCheker().checkTileCollision(this);
    }

    /**
     * Calculates the player final velocity based on speed and given direction,
     * unless player collision is enabled
     * @param direction player movement directions (x, y)
     * @return player movement velocity (x, y),
     * (0, 0) if player collision is enabled
     */
    private Point calculateFinalVelocity(Point direction) {
        if (isCollisionOn())
            return new Point(0, 0);

        int speed = this.getSpeed();
        return normalizeDiagonalMovement(direction.x, direction.y, speed);
    }

    /**
     * Normalizes diagonal movement by converting it into a unary vector
     * (same direction but a length of exactly 1) and then multiplying it by player speed
     * @param dx player horizontal movement
     * @param dy player vertical movement
     * @param speed player speed to apply as scalar after normalization
     * @return normalized player movement velocity (x, y)
     */
    private Point normalizeDiagonalMovement(int dx, int dy, int speed) {
        if (dx != 0 && dy != 0) {
            double length = Math.hypot(dx, dy);             // sqrt(dx^2 + dy^2)
            return new Point(
                    (int) Math.round((dx / length) * speed),
                    (int) Math.round((dy / length) * speed)
            );
        }
        return new Point(dx * speed, dy * speed);
    }

    /**
     * Updates player world position based on his current velocity
     * @param velocity player movement velocity (x, y)
     */
    private void updateWorldPosition(Point velocity) {
        setWorldX(getWorldX() + velocity.x);
        setWorldY(getWorldY() + velocity.y);
    }

    /**
     * Updates current animation frame according to animation speed
     */
    private void updateAnimation() {
        if (++this.frameDelayCounter > ANIMATION_FRAME_DELAY) {
            this.currentAnimationFrame = (this.currentAnimationFrame + 1) % NUM_ANIMATION_FRAMES;
            this.frameDelayCounter = 0;
        }
    }

    /**
     * Renders current player sprite to the screen
     * @param g Graphics class to actually draw the player
     */
    @Override
    public void render(Graphics g) {
        g.drawImage(getCurrentSprite(), screenX, screenY,
                    GamePanel.TILE_SIZE, GamePanel.TILE_SIZE, null);
    }

    /**
     * Returns current player sprite based on facing direction, current animation frame and player state (idle/run)
     * @return current player sprite as BufferedImage
     */
    @Override
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


    // Getter methods
    public int getScreenX() { return screenX; }
    public int getScreenY() { return screenY; }
}