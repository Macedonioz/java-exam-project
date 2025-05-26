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

    private final KeyHandler gameKeyHandler;

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

    public Player(GamePanel gamePanel, KeyHandler gameKeyHandler) {
        this.gameKeyHandler = gameKeyHandler;

        screenX = (GamePanel.SCREEN_WIDTH / 2) - (GamePanel.TILE_SIZE / 2);
        screenY = (GamePanel.SCREEN_HEIGHT / 2) - (GamePanel.TILE_SIZE / 2);

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
    }


    /**
     * Updates player state (movement, animation)
     */
    @Override
    public void update() {
        // TODO implement placeholder/safety measures
        handleMovement();
        updateAnimation();
    }

    /**
     * Change player world position based on currently pressed keys.
     * Diagonal movement is normalized to make it the same speed as horizontal/vertical one
     */
    private void handleMovement() {
        int playerSpeed = this.getSpeed();
        int dx = 0, dy = 0;

        // Checks all keys independently to allow diagonal movement
        if (gameKeyHandler.isUpPressed()) {
            dy -= 1;
            facing = Direction.UP;
        }
        if (gameKeyHandler.isDownPressed()) {
            dy += 1;
            facing = Direction.DOWN;
        }
        if (gameKeyHandler.isLeftPressed()) {
            dx -= 1;
            facing = Direction.LEFT;
        }
        if (gameKeyHandler.isRightPressed()) {
            dx += 1;
            facing = Direction.RIGHT;
        }

        // Normalize diagonal movement
        if (dx != 0 && dy != 0) {
            double length = Math.sqrt(dx * dx + dy * dy);
            dx = (int) Math.round((dx / length) * playerSpeed);
            dy = (int) Math.round((dy / length) * playerSpeed);
        } else {
            dx *= playerSpeed;
            dy *= playerSpeed;
        }

        setWorldX(getWorldX() + dx);
        setWorldY(getWorldY() + dy);
        isMoving = (dx != 0 || dy != 0);
    }

    /**
     * Update current animation frame according to animation speed
     */
    private void updateAnimation() {
        if (++frameDelayCounter > ANIMATION_FRAME_DELAY) {
            currentAnimationFrame = (currentAnimationFrame + 1) % NUM_ANIMATION_FRAMES;
            frameDelayCounter = 0;
        }
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
     * Loads player sprites from sprite sheets in res folder
     */
    @Override
    public void loadSprites() {
        try {
            idleFrames = sliceSpriteSheet(loadImageSafe("/sprites/player/player_idle.png"));
            runFrames = sliceSpriteSheet(loadImageSafe("/sprites/player/player_run.png"));

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
     * Returns current player sprite based on facing direction, current animation frame and player state (idle/run)
     * @return current player sprite as BufferedImage
     */
    @Override
    public BufferedImage getCurrentSprite() {
        int directionRow = switch (facing) {
            case DOWN -> 0;
            case LEFT -> 1;
            case RIGHT -> 2;
            case UP -> 3;
        };

        BufferedImage[] frames = isMoving ? runFrames : idleFrames;
        int frameIndex = (directionRow * NUM_ANIMATION_FRAMES) + currentAnimationFrame;

        return frames[frameIndex];
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

    public int getScreenX() { return screenX; }

    public int getScreenY() { return screenY; }
}