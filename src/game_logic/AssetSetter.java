package game_logic;

import object.Boots;
import object.Chest;
import object.GameObject;
import object.Key;

public class AssetSetter {
    private final GamePanel gamePanel;

    public AssetSetter(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    /*
     * Sets game object world coordinates based on tile location
     * and adds it to game objects list
     * @param gameObj The game object to be added
     * @param tileCol The world column (in tiles) where the object should be placed
     * @param tileRow The world row (in tiles) where the object should be placed.
     */
    private void addGameObject(GameObject gameObj, int tileCol, int tileRow) {
        gameObj.setWorldX(tileCol * GamePanel.TILE_SIZE);
        gameObj.setWorldY(tileRow * GamePanel.TILE_SIZE);
        gamePanel.getGameObjects().add(gameObj);
    }

    /**
     * Sets all game objects position at the start of the game
     */
    public void setGameObjects() {

        addGameObject(new Key(gamePanel), 58, 32);
        addGameObject(new Key(gamePanel), 68, 60);
        addGameObject(new Key(gamePanel), 54, 52);
        addGameObject(new Key(gamePanel), 18, 34);
        addGameObject(new Key(gamePanel), 9, 44);
        addGameObject(new Key(gamePanel), 18, 34);
        addGameObject(new Key(gamePanel), 10, 69);
        addGameObject(new Key(gamePanel), 15, 10);
        addGameObject(new Key(gamePanel), 64, 13);
        addGameObject(new Key(gamePanel), 40, 31);

        addGameObject(new Boots(gamePanel), 64, 49);
        addGameObject(new Boots(gamePanel), 40, 62);
        addGameObject(new Boots(gamePanel), 39, 31);

        addGameObject(new Chest(gamePanel), 41, 15);
    }

}
