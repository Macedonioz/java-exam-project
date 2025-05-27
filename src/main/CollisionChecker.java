package main;

import entity.Entity;
import object.GameObject;
import tile.Tile;

import java.awt.*;
import java.util.ArrayList;

public class CollisionChecker {
    private GamePanel gamePanel;

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

        // Convert to tile cordinates
        int entityLeftCol = entityLeftWorldX / GamePanel.TILE_SIZE;
        int entityRightCol = entityRightWorldX / GamePanel.TILE_SIZE;
        int entityTopRow = entityTopWorldY / GamePanel.TILE_SIZE;
        int entityBottomRow = entityBottomWorldY / GamePanel.TILE_SIZE;

        // Get tiles data
        int[][] tileMap = gamePanel.getTileManager().getMapTileNum();
        ArrayList<Tile> tiles = gamePanel.getTileManager().getTiles();
        int checkedTileIndex1, checkedTileIndex2;

        switch (entity.getFacing()) {
            case UP -> {
                entityTopRow = (entityTopWorldY - entity.getSpeed()) / GamePanel.TILE_SIZE;
                checkedTileIndex1 = tileMap[entityLeftCol][entityTopRow];
                checkedTileIndex2 = tileMap[entityRightCol][entityTopRow];

                if (tiles.get(checkedTileIndex1).isCollidable() || tiles.get(checkedTileIndex2).isCollidable()) {
                    entity.setCollisionOn(true);
                }
            }
            case DOWN -> {
                entityBottomRow = (entityBottomWorldY + entity.getSpeed()) / GamePanel.TILE_SIZE;
                checkedTileIndex1 = tileMap[entityLeftCol][entityBottomRow];
                checkedTileIndex2 = tileMap[entityRightCol][entityBottomRow];

                if (tiles.get(checkedTileIndex1).isCollidable() || tiles.get(checkedTileIndex2).isCollidable()) {
                    entity.setCollisionOn(true);
                }
            }
            case LEFT -> {
                entityLeftCol = (entityLeftWorldX - entity.getSpeed()) / GamePanel.TILE_SIZE;
                checkedTileIndex1 = tileMap[entityLeftCol][entityTopRow];
                checkedTileIndex2 = tileMap[entityLeftCol][entityBottomRow];

                if (tiles.get(checkedTileIndex1).isCollidable() || tiles.get(checkedTileIndex2).isCollidable()) {
                    entity.setCollisionOn(true);
                }
            }
            case RIGHT -> {
                entityRightCol = (entityRightWorldX + entity.getSpeed()) / GamePanel.TILE_SIZE;
                checkedTileIndex1 = tileMap[entityRightCol][entityTopRow];
                checkedTileIndex2 = tileMap[entityRightCol][entityBottomRow];

                if (tiles.get(checkedTileIndex1).isCollidable() || tiles.get(checkedTileIndex2).isCollidable()) {
                    entity.setCollisionOn(true);
                }
            }
        }
    }

    public int checkObject(Entity entity, boolean isPlayer) {
        int index = -1;

        ArrayList<GameObject> gameObjects = gamePanel.getGameObjects();

        for (int i = 0; i < gameObjects.size(); i++) {
            GameObject gameObj = gameObjects.get(i);

            // Get entity's solid area world position
            Rectangle entitySolidArea = entity.getSolidArea();
            entitySolidArea.x += entity.getWorldX();
            entitySolidArea.y += entity.getWorldY();

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
