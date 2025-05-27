package main;

import object.Chest;
import object.GameObject;
import object.Key;

import java.util.ArrayList;

public class AssetSetter {
    private final GamePanel gamePanel;

    public AssetSetter(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    // Sets game object coordinates and adds it to game objects list
    private void addGameObject(GameObject gameObj, int tileCol, int tileRow) {
        gameObj.setWorldX(tileCol * GamePanel.TILE_SIZE);
        gameObj.setWorldY(tileRow * GamePanel.TILE_SIZE);
        gamePanel.getGameObjects().add(gameObj);
    }

    /**
     * Sets all game objects position at the start of the game
     */
    public void setGameObjects() {
        addGameObject(new Key(gamePanel), 24, 36);
        addGameObject(new Key(gamePanel), 30, 14);
        addGameObject(new Key(gamePanel), 41, 26);
        addGameObject(new Key(gamePanel), 15, 22);
        addGameObject(new Chest(gamePanel), 24, 24);
    }

}
