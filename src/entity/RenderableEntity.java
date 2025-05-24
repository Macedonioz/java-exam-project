package entity;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class RenderableEntity extends Entity {

    protected BufferedImage[] idleFrames, runFrames;
    protected Direction facing = Direction.DOWN;
    protected boolean isMoving = false;

    protected enum Direction {
        DOWN, LEFT, RIGHT, UP;
    }

    protected Direction getFacing() { return facing; }
    public boolean isMoving() { return isMoving; }

    protected abstract void loadSprites();
    protected abstract BufferedImage getCurrentSprite();
    public abstract void render(Graphics g);
}
