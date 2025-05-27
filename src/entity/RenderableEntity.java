package entity;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class RenderableEntity extends Entity {

    private BufferedImage[] idleFrames, runFrames;

    // Getter methods
    public BufferedImage[] getRunFrames() { return runFrames; }
    public BufferedImage[] getIdleFrames() { return idleFrames; }

    // Setter methods
    public void setRunFrames(BufferedImage[] runFrames) { this.runFrames = runFrames; }
    public void setIdleFrames(BufferedImage[] idleFrames) { this.idleFrames = idleFrames; }

    // Abstract methods
    protected abstract void loadSprites();
    public abstract void draw(Graphics g);
}
