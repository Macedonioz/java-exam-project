package object;

import entity.Player;
import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class GameObject {
    private final GamePanel gamePanel;

    private final String name;
    private BufferedImage image;
    private boolean hasCollision;

    private int worldX, worldY;
    // TODO CHANGE THIS SHIT
    private Rectangle solidArea = new Rectangle(0, 0, GamePanel.TILE_SIZE, GamePanel.TILE_SIZE);
    private int solidAreaDefaultX, solidAreaDefaultY;

    public GameObject(String name, GamePanel gamePanel) {
        this.name = name;
        this.gamePanel = gamePanel;
    }

    // Checks if object is within the player's visible area.
    // Returns true if object is inside the boundaries, false otherwise
    private boolean isObjectVisible(int worldX, int worldY,
                                  int playerWorldX, int playerWorldY,
                                  int playerScreenX, int playerScreenY) {
        int leftBound = playerWorldX - playerScreenX;
        int rightBound = playerWorldX + playerScreenX;
        int upperBound = playerWorldY - playerScreenY;
        int lowerBound = playerWorldY + playerScreenY;

        return  (worldX + GamePanel.TILE_SIZE) > leftBound &&
                (worldX - GamePanel.TILE_SIZE) < rightBound &&
                (worldY + GamePanel.TILE_SIZE) > upperBound &&
                (worldY - GamePanel.TILE_SIZE) < lowerBound;
    }

    /**
     * Renders the object to the screen.
     * Only objects within the visible screen area are drawn.
     * @param g2 Graphics context used for drawing
     */
    private void renderObject(Graphics2D g2) {

        Player player = gamePanel.getPlayer();
        int playerWorldX = player.getWorldX();
        int playerWorldY = player.getWorldY();
        int playerScreenX = player.getScreenX();
        int playerScreenY = player.getScreenY();

        int screenX = this.worldX - playerWorldX + playerScreenX;
        int screenY = this.worldY - playerWorldY + playerScreenY;

        if (isObjectVisible(worldX, worldY, playerWorldX, playerWorldY, playerScreenX, playerScreenY)) {
            g2.drawImage(image, screenX, screenY,
                    GamePanel.TILE_SIZE, GamePanel.TILE_SIZE, null);
        }
    }

    /**
     * Draws the object to the screen only if it is visible by the player
     * @param g2 Graphics context used for drawing
     */
    public void draw(Graphics2D g2) {
        renderObject(g2);
    }

    // Getter methods
    public String getName() { return name; }
    public BufferedImage getImage() { return image; }
    public boolean isCollidable() { return hasCollision; }
    public int getWorldX() { return worldX; }
    public int getWorldY() { return worldY; }
    public Rectangle getSolidArea() { return solidArea; }
    public int getSolidAreaDefaultX() { return solidAreaDefaultX; }
    public int getSolidAreaDefaultY() { return solidAreaDefaultY; }

    // Setter methods
    public void setImage(BufferedImage image) { this.image = image; }
    public void setCollision(boolean hasCollision) { this.hasCollision = hasCollision; }
    public void setWorldX(int worldX) { this.worldX = worldX; }
    public void setWorldY(int worldY) { this.worldY = worldY; }
}
