package entity;

import game_logic.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Game entity that must be rendered on the screen
 */
public abstract class RenderableEntity extends Entity {
    // SPRITES
    private BufferedImage[] idleFrames, runFrames;


    RenderableEntity(GamePanel gamePanel) {
        super(gamePanel);
    }

    /* --------------- [GETTER METHODS] --------------- */

    public BufferedImage[] getRunFrames() { return runFrames; }
    public BufferedImage[] getIdleFrames() { return idleFrames; }

    /* ------------------------------------------------ */


    /* --------------- [SETTER METHODS] --------------- */

    public void setRunFrames(BufferedImage[] runFrames) { this.runFrames = runFrames; }
    public void setIdleFrames(BufferedImage[] idleFrames) { this.idleFrames = idleFrames; }

    /* ------------------------------------------------ */


    /* -------------- [ABSTRACT METHODS] -------------- */

    protected abstract void loadSprites();
    public abstract void draw(Graphics2D g2);
    public abstract void drawDebug(Graphics2D g2);

    /* ------------------------------------------------ */
}
