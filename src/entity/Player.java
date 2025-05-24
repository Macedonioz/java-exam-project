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
    private final KeyHandler gameKeyHandler;

    // Sprites
    private static final int NUM_ANIMATION_FRAMES = 4;
    private static final int SPRITE_ROWS = 4;
    private final int FRAME_SIZE = GamePanel.ORIGINAL_TILE_SIZE;

    // Animation
    private int frameDelayCounter = 0;
    private int currentAnimationFrame = 0;
    private static final int ANIMATION_FRAME_DELAY = 10;

    public Player(GamePanel gamePanel, KeyHandler gameKeyHandler) {
        this.gamePanel = gamePanel;
        this.gameKeyHandler = gameKeyHandler;

        setDefaultValues();
        loadSprites();
    }

    public void setDefaultValues() {
        setX(GamePanel.SCREEN_WIDTH / 2 - FRAME_SIZE);
        setY(GamePanel.SCREEN_HEIGHT / 2 - FRAME_SIZE);
        setSpeed(4);
    }

    @Override
    public void update() {
        // TODO implement placeholder/safety measures
        handleMovement();
        updateAnimation();
    }

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
        isMoving = (dx != 0 || dy != 0);

        // TODO Boundary check
    }

    private void updateAnimation() {
        if (++frameDelayCounter > ANIMATION_FRAME_DELAY) {
            currentAnimationFrame = (currentAnimationFrame + 1) % NUM_ANIMATION_FRAMES;
            frameDelayCounter = 0;
        }
    }

    private BufferedImage loadImageSafe(String path) throws IOException {
        try (InputStream is = getClass().getResourceAsStream(path)) {
            if (is == null) {
                throw new FileNotFoundException("File non trovato: " + path);
            }

            BufferedImage image = ImageIO.read(is);
            if (image == null) {
                throw new IOException("Formato immagine non valido");
            }
            return image;
        }
    }

    @Override
    public void loadSprites() {
        try {
            idleFrames = sliceSpriteSheet(loadImageSafe("/sprites/player/player_idle.png"));
            runFrames = sliceSpriteSheet(loadImageSafe("/sprites/player/player_run.png"));

        } catch (IOException e) {
            System.err.println("Error loading player sprites:" + e.getMessage());
        }
    }

    private BufferedImage[] sliceSpriteSheet(BufferedImage sheet) {
        BufferedImage[] frames = new BufferedImage[SPRITE_ROWS * NUM_ANIMATION_FRAMES];

        for (int i = 0; i < SPRITE_ROWS; i++) {
            for (int j = 0; j < NUM_ANIMATION_FRAMES; j++) {
                int frameIndex = (i * NUM_ANIMATION_FRAMES) + j;
                frames[frameIndex] = sheet.getSubimage(j * FRAME_SIZE, i * FRAME_SIZE, FRAME_SIZE, FRAME_SIZE);
            }
        }

        return frames;
    }

    @Override
    public BufferedImage getCurrentSprite() {
        int directionRow = switch (facing) {
            case DOWN -> directionRow = 0;
            case LEFT -> directionRow = 1;
            case RIGHT -> directionRow = 2;
            case UP -> directionRow = 3;
        };

        BufferedImage[] frames = isMoving ? runFrames : idleFrames;
        int frameIndex = (directionRow * NUM_ANIMATION_FRAMES) + currentAnimationFrame;

        return frames[frameIndex];
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(getCurrentSprite(), getX(), getY(), GamePanel.TILE_SIZE, GamePanel.TILE_SIZE, null);
        // Per debug: mostra hitbox
        // g.setColor(Color.RED);
        // g.drawRect(getX(), getY(), GamePanel.TILE_SIZE, GamePanel.TILE_SIZE);
    }
}
