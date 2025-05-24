package entity;

import java.awt.image.BufferedImage;

public abstract class Entity {

    private int x, y;
    private int speed;

    public int getX() { return x; }
    public int getY() { return y; }
    public int getSpeed() { return speed; }

    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public void setSpeed(int speed) {
        if(speed >= 0) this.speed = speed;
    }

    // Abstract methods
    public abstract void update();
}
