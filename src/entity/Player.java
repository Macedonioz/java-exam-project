package entity;

import game_logic.GamePanel;
import game_logic.KeyHandler;
import game_logic.Sound;
import game_logic.UI;
import object.GameObject;
import utils.GameUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class Player extends RenderableEntity {

    private final GamePanel gamePanel;

    private static final int DEFAULT_SPEED = 4;
    private final int screenX;
    private final int screenY;

    // SPRITE
    private static final int NUM_ANIMATION_FRAMES = 4;          // number of animation frames for every sprite row
    private static final int SPRITE_ROWS = 4;                   // a row in the sprite sheet for every direction

    // ANIMATION
    private static final int ANIMATION_FRAME_DELAY = 10;                // animation speed
    private int frameDelayCounter = 0;                          // to update player animation frames
    private int currentAnimationFrame = 0;

    // COLLISION
    private static final int COLLISION_BOX_OFFSET_X = 8;
    private static final int COLLISION_BOX_OFFSET_Y = 16;
    private static final int COLLISION_BOX_WIDTH = 32;
    private static final int COLLISION_BOX_HEIGHT = 32;

    // GAME PARAMS
    private int numKeys = 0;
    private static final int REQUIRED_KEYS = 4;
    private static final float SPEED_BOOST_MULTIPLIER = 1.5f;

    public Player(GamePanel gamePanel) {
        this.gamePanel = gamePanel;

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
        this.setSolidAreaDefaultX(COLLISION_BOX_OFFSET_X);
        this.setSolidAreaDefaultY(COLLISION_BOX_OFFSET_Y);

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
            this.setIdleFrames(GameUtils.sliceSpriteSheet(GameUtils.loadImageSafe("/sprites/player/player_idle.png"),
                    GamePanel.ORIGINAL_TILE_SIZE, SPRITE_ROWS, NUM_ANIMATION_FRAMES));
            this.setRunFrames(GameUtils.sliceSpriteSheet(GameUtils.loadImageSafe("/sprites/player/player_run.png"),
                    GamePanel.ORIGINAL_TILE_SIZE, SPRITE_ROWS, NUM_ANIMATION_FRAMES));

        } catch (IOException e) {
            System.err.println("Error loading player sprites:" + e.getMessage());
            e.printStackTrace();
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

    /**
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

    /**
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

    /**
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

    /**
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

    /**
     * Updates player world position based on velocity.
     * @param dx player horizontal velocity
     * @param dy player vertical velocity
     */
    private void updateWorldPosition(int dx, int dy) {
        setWorldX(getWorldX() + dx);
        setWorldY(getWorldY() + dy);
    }

    /**
     * Handles player collisions
     */
    private void handleCollisions() {
        handleTileCollisions();
        handleObjectCollisions();
    }

    /**
     * Enables player collision if checked tiles are collidable
     */
    private void handleTileCollisions() {
        this.setCollisionOn(false);
        gamePanel.getCollisionChecker().checkTile(this);
    }

    /**
     * Handles player interaction with game objects on collision.
     * Enables player collision if checked object is collidable
     */
    private void handleObjectCollisions() {
        int gameObjIndex = gamePanel.getCollisionChecker().checkObject(this, true);

        if (gameObjIndex == -1) return;

        ArrayList<GameObject> gameObjects = gamePanel.getGameObjects();
        String gameObjName = gameObjects.get(gameObjIndex).getName();

        switch (gameObjName) {
            case "Key" -> {
                gamePanel.playSoundEffect(Sound.PICK_UP_KEY);

                gameObjects.remove(gameObjIndex);
                this.numKeys++;
                gamePanel.getUi().showMessage("You got a key!");
            }
            case "Chest" -> {
                if (this.numKeys >= REQUIRED_KEYS) {
                    gamePanel.getUi().endGame();
                    gamePanel.stopMusic();
                    gamePanel.playSoundEffect(Sound.VICTORY);

                } else {
                    gamePanel.getUi().showMessage("You need " + (REQUIRED_KEYS - this.numKeys)
                            + " more keys to open the chest!");
                }
            }
            case "Boots" -> {
                gamePanel.playSoundEffect(Sound.POWER_UP);

                gameObjects.remove(gameObjIndex);
                this.setSpeed((int) (this.getSpeed() * SPEED_BOOST_MULTIPLIER));
                gamePanel.getUi().showMessage("Speed up!");
            }
        }
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
     * Draws current player sprite to the screen
     * @param g Graphics class to actually draw the player
     */
    @Override
    public void draw(Graphics g) {
        g.drawImage(getCurrentSprite(), screenX, screenY,
                    GamePanel.TILE_SIZE, GamePanel.TILE_SIZE, null);
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


    // Getter methods
    public int getScreenX() { return screenX; }
    public int getScreenY() { return screenY; }
    public int getNumKeys() { return numKeys; }
}