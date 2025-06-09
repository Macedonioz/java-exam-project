package game_logic;

import entity.Entity;
import object.GameObject;
import tile.Tile;

import java.awt.*;
import java.util.ArrayList;

public class CollisionChecker {
    private final GamePanel gamePanel;

    public CollisionChecker(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    /**
     * Based on entity direction and speed, checks if updated position would cause a tile collision.
     * In case one of the two tiles (at the ends of collisionBox) are collidable, set player collision on true
     * @param entity the entity for which to check collisions
     */
    public void checkTile(Entity entity) {

        // Calculate entity's collision box world coordinates
        Rectangle solidArea = entity.getSolidArea();
        int entityLeftWorldX = entity.getWorldX() + solidArea.x;
        int entityRightWorldX = entityLeftWorldX + solidArea.width;
        int entityTopWorldY = entity.getWorldY() + solidArea.y;
        int entityBottomWorldY = entityTopWorldY + solidArea.height;

        // Get tiles data
        int[][] tileMap = gamePanel.getTileManager().getMapTileNum();
        ArrayList<Tile> tiles = gamePanel.getTileManager().getTiles();

        int checkedTileIndex1, checkedTileIndex2;
        int maxCols = tileMap.length;
        int maxRows = tileMap[0].length;

        // Tile coordinates to check for collision
        int checkCol1 = entityLeftWorldX / GamePanel.TILE_SIZE;
        int checkRow1 = entityTopWorldY / GamePanel.TILE_SIZE;
        int checkCol2 = entityRightWorldX / GamePanel.TILE_SIZE;
        int checkRow2 = entityBottomWorldY / GamePanel.TILE_SIZE;

        // Calculates which tiles to check
        switch (entity.getFacing()) {
            case UP -> {
                int newTopRow = (entityTopWorldY - entity.getSpeed()) / GamePanel.TILE_SIZE;
                checkRow1 = checkRow2 = newTopRow;
            }
            case DOWN -> {
                int newBottomRow = (entityBottomWorldY + entity.getSpeed()) / GamePanel.TILE_SIZE;
                checkRow1 = checkRow2 = newBottomRow;
            }
            case LEFT -> {
                int newLeftCol = (entityLeftWorldX - entity.getSpeed()) / GamePanel.TILE_SIZE;
                checkCol1 = checkCol2 = newLeftCol;
            }
            case RIGHT -> {
                int newRightCol = (entityRightWorldX + entity.getSpeed()) / GamePanel.TILE_SIZE;
                checkCol1 = checkCol2 = newRightCol;
            }
        }

        // Common collision check.
        // Performed only if entity is within world boundaries
        if (isInsideMap(checkCol1, checkRow1, maxCols, maxRows) && isInsideMap(checkCol2, checkRow2, maxCols, maxRows)) {
            int tileIndex1 = tileMap[checkCol1][checkRow1];
            int tileIndex2 = tileMap[checkCol2][checkRow2];

            if (isValidTile(tileIndex1, tiles) || isValidTile(tileIndex2, tiles)) {
                entity.setCollisionOn(true);
            }
        }
    }

    /*
     * Checks if entity is within world boundaries
     * @param col The entity world column (in tiles)
     * @param row The entity world row (in tiles)
     * @param maxCols The max world column (in tiles)
     * @param maxRows The max world row (in tiles)
     * @return true if entity is within world boundaries,
     *         false otherwise
     */
    private boolean isInsideMap(int col, int row, int maxCols, int maxRows) {
        return col >= 0 && col < maxCols && row >= 0 && row < maxRows;
    }

    /*
     * Checks if given tile index is within game tiles list and is therefore valid
     * @param tileIndex The tile index to check
     * @param tiles The game tiles list
     * @return true if tile is valid,
     *         false otherwise
     */
    private boolean isValidTile(int tileIndex, ArrayList<Tile> tiles) {
        return tileIndex >= 0 && tileIndex < tiles.size() && tiles.get(tileIndex).isCollidable();
    }

    /**
     * Based on entity direction and speed, checks if updated position would cause an object collision.
     * In case entity solid area intersects object solid area, set entity collision on true if object is collidable
     * and return object index if entity is player
     * @param entity the entity for which to check collisions
     * @param isPlayer flag to check if entity is player
     * @return Object index if entity is player, -1 otherwise
     */
    public int checkObject(Entity entity, boolean isPlayer) {
        int index = -1;

        int entityX = entity.getWorldX();
        int entityY = entity.getWorldY();

        int mapWidth = GamePanel.MAX_WORLD_COL * GamePanel.TILE_SIZE;
        int mapHeight = GamePanel.MAX_WORLD_ROW * GamePanel.TILE_SIZE;

        // If entity is outside the world map, skip collision detection
        if (entityX < 0 || entityY < 0 || entityX >= mapWidth || entityY >= mapHeight) {
            return index;
        }

        ArrayList<GameObject> gameObjects = gamePanel.getGameObjects();

        for (int i = 0; i < gameObjects.size(); i++) {
            GameObject gameObj = gameObjects.get(i);

            // Get entity's solid area world position
            Rectangle entitySolidArea = entity.getSolidArea();
            entitySolidArea.x += entityX;
            entitySolidArea.y += entityY;

            // Get object's solid area world position
            Rectangle gameObjSolidArea =  gameObj.getSolidArea();
            gameObjSolidArea.x += gameObj.getWorldX();
            gameObjSolidArea.y += gameObj.getWorldY();

            switch (entity.getFacing()) {
                case UP -> {
                    entitySolidArea.y -= entity.getSpeed();
                    if (entitySolidArea.intersects(gameObjSolidArea)) {
                        if (gameObj.isCollidable()) {
                            entity.setCollisionOn(true);
                        }
                        if (isPlayer) {
                            index = i;
                        }
                    }
                }
                case DOWN -> {
                    entitySolidArea.y += entity.getSpeed();
                    if (entitySolidArea.intersects(gameObjSolidArea)) {
                        if (gameObj.isCollidable()) {
                            entity.setCollisionOn(true);
                        }
                        if (isPlayer) {
                            index = i;
                        }
                    }
                }
                case LEFT -> {
                    entitySolidArea.x -= entity.getSpeed();
                    if (entitySolidArea.intersects(gameObjSolidArea)) {
                        if (gameObj.isCollidable()) {
                            entity.setCollisionOn(true);
                        }
                        if (isPlayer) {
                            index = i;
                        }
                    }
                }
                case RIGHT -> {
                    entitySolidArea.x += entity.getSpeed();
                    if (entitySolidArea.intersects(gameObjSolidArea)) {
                        if (gameObj.isCollidable()) {
                            entity.setCollisionOn(true);
                        }
                        if (isPlayer) {
                            index = i;
                        }
                    }
                }
            }

            entitySolidArea.x = entity.getSolidAreaDefaultX();
            entitySolidArea.y = entity.getSolidAreaDefaultY();

            gameObjSolidArea.x = gameObj.getSolidAreaDefaultX();
            gameObjSolidArea.y = gameObj.getSolidAreaDefaultY();
        }

        return index;
    }
}
