package entity;

import game_logic.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class RenderableEntity extends Entity {

    private BufferedImage[] idleFrames, runFrames;

    RenderableEntity(GamePanel gamePanel) {
        super(gamePanel);
    }

    // Getter methods
    public BufferedImage[] getRunFrames() { return runFrames; }
    public BufferedImage[] getIdleFrames() { return idleFrames; }

    // Setter methods
    public void setRunFrames(BufferedImage[] runFrames) { this.runFrames = runFrames; }
    public void setIdleFrames(BufferedImage[] idleFrames) { this.idleFrames = idleFrames; }

    // Abstract methods
    protected abstract void loadSprites();
    public abstract void draw(Graphics2D g2);
    public abstract void drawDebug(Graphics2D g2);
}
