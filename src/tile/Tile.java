package tile;

import java.awt.image.BufferedImage;

public class Tile {

    // TILE IMAGE
    private BufferedImage image;

    // COLLISION
    private boolean hasCollision;

    public Tile(BufferedImage image, boolean hasCollision) {
        this.image = image;
        this.hasCollision = hasCollision;
    }


    /* --------------- [GETTER METHODS] --------------- */

    public boolean isCollidable() { return hasCollision; }
    public BufferedImage getImage() { return image; }

    /* ------------------------------------------------ */


    /* --------------- [SETTER METHODS] --------------- */

    public void setCollision(boolean hasCollision) { this.hasCollision = hasCollision; }
    public void setImage(BufferedImage image) { this.image = image; }

    /* ------------------------------------------------ */
}