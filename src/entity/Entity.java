package entity;

public class Entity {

    private int x, y;
    private int speed;

    protected int getX() {
        return x;
    }

    protected int getY() {
        return y;
    }

    protected int getSpeed() {
        return speed;
    }

    protected void setX(int x) {
        this.x = x;
    }

    protected void setY(int y) {
        this.y = y;
    }

    protected void setSpeed(int speed) {
        this.speed = speed;
    }
}
