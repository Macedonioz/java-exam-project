package entity;

import java.awt.image.BufferedImage;

public abstract class Entity {
    private int worldX, worldY;
    private int speed;

    public int getWorldX() { return worldX; }
    public int getWorldY() { return worldY; }
    public int getSpeed() { return speed; }

    public void setWorldX(int worldX) { this.worldX = worldX; }
    public void setWorldY(int worldY) { this.worldY = worldY; }
    public void setSpeed(int speed) {
        if(speed >= 0) this.speed = speed;
    }

    // Abstract methods
    public abstract void update();
}
