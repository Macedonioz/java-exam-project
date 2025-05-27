package entity;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Entity {
    private int worldX, worldY;
    private int speed;
    private Entity.Direction facing;
    private boolean isMoving = false;

    private Rectangle solidArea;
    private boolean collisionOn = false;

    public enum Direction {
        DOWN, LEFT, RIGHT, UP;
    }

    // Getter methods
    public int getWorldX() { return worldX; }
    public int getWorldY() { return worldY; }
    public int getSpeed() { return speed; }
    public Direction getFacing() { return facing; }
    public boolean isMoving() { return isMoving; }

    public Rectangle getSolidArea() { return solidArea; }
    public boolean isCollisionOn() { return collisionOn; }


    // Setter methods
    public void setWorldX(int worldX) { this.worldX = worldX; }
    public void setWorldY(int worldY) { this.worldY = worldY; }
    public void setSpeed(int speed) {
        if(speed >= 0) this.speed = speed;
    }
    public void setFacing(Entity.Direction facing) {this.facing = facing;}
    public void setMoving(boolean isMoving) { this.isMoving = isMoving; }

    public void setSolidArea(Rectangle solidArea) { this.solidArea = solidArea; }
    public void setCollisionOn(boolean collisionOn) { this.collisionOn = collisionOn; }

    // Abstract methods
    public abstract void update();
}
