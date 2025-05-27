package tile;

import java.awt.image.BufferedImage;

public class Tile {

    private BufferedImage image;
    private boolean hasCollision;

    public Tile(BufferedImage image, boolean hasCollision) {
        this.image = image;
        this.hasCollision = hasCollision;
    }

    // Getter methods
    public boolean isCollidable() { return hasCollision; }
    public BufferedImage getImage() { return image; }

    // Setter methods
    public void setCollision(boolean hasCollision) { this.hasCollision = hasCollision; }
    public void setImage(BufferedImage image) { this.image = image; }
}