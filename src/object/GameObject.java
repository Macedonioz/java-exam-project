package object;

import entity.Player;
import game_logic.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class GameObject {
    private final GamePanel gamePanel;

    // PROPERTIES
    private final String name;
    private BufferedImage image;

    // COORDINATES
    private int worldX, worldY;

    // HITBOX
    private final Rectangle solidArea = new Rectangle(0, 0, GamePanel.TILE_SIZE, GamePanel.TILE_SIZE);
    private int solidAreaDefaultX, solidAreaDefaultY;
    private boolean hasCollision;


    public GameObject(String name, GamePanel gamePanel) {
        this.name = name;
        this.gamePanel = gamePanel;
    }


    /*
     * Checks if object is within the player's visible area.
     * @param worldX The object's world X coordinate
     * @param worldY The object's world Y coordinate
     * @param playerWorldX The player's world X coordinate
     * @param playerWorldY The player's world Y coordinate
     * @param playerScreenX The player's screen X coordinate
     * @param playerScreenY The player's screen Y coordinate
     * @return true if object is inside the boundaries,
     *         false otherwise
     */
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

    /*
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
            g2.drawImage(image, screenX, screenY, null);
        }
    }

    /**
     * Draws the object to the screen only if it is visible by the player
     * @param g2 Graphics context used for drawing
     */
    public void draw(Graphics2D g2) {
        renderObject(g2);
    }


    /* --------------- [GETTER METHODS] --------------- */

    public String getName() { return name; }
    public BufferedImage getImage() { return image; }
    public int getWorldX() { return worldX; }
    public int getWorldY() { return worldY; }
    public Rectangle getSolidArea() { return solidArea; }
    public int getSolidAreaDefaultX() { return solidAreaDefaultX; }
    public int getSolidAreaDefaultY() { return solidAreaDefaultY; }
    public boolean isCollidable() { return hasCollision; }

    /* ------------------------------------------------ */


    /* --------------- [SETTER METHODS] --------------- */

    public void setImage(BufferedImage image) { this.image = image; }
    public void setWorldX(int worldX) { this.worldX = worldX; }
    public void setWorldY(int worldY) { this.worldY = worldY; }
    public void setCollision(boolean hasCollision) { this.hasCollision = hasCollision; }

    /* ------------------------------------------------ */

    /* -------------- [ABSTRACT METHODS] -------------- */

    public abstract void onPlayerCollision(GamePanel gamePanel);

    /* ------------------------------------------------ */
}
