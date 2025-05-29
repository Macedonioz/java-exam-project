package tile;

import entity.Player;
import game_logic.GamePanel;
import utils.GameUtils;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class TileManager {

    private final GamePanel gamePanel;
    private final ArrayList<Tile> tiles;
    private final int[][] mapTileNum;

    public TileManager(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.tiles = new ArrayList<>();
        this.mapTileNum = new int[GamePanel.MAX_WORLD_COL][GamePanel.MAX_WORLD_ROW];

        loadTiles();
        loadTileMap("/maps/world01.txt");
    }

    /**
     * Loads tiles from tiles dir in res folder
     */
    public void loadTiles() {
        try {
            tiles.add(new Tile(GameUtils.loadImageSafe("/tiles/grass_01.png"), false));
            tiles.add(new Tile(GameUtils.loadImageSafe("/tiles/sand_01.png"), false));
            tiles.add(new Tile(GameUtils.loadImageSafe("/tiles/water_01.png"), true));
            tiles.add(new Tile(GameUtils.loadImageSafe("/tiles/tree_01.png"), true));

        } catch (IOException e) {
            e.printStackTrace();
            // TODO add placeholder tiles
        }
    }

    /**
     * Loads tiles ID map into a 2D array
     * @param path file path of the world map (.txt) to load
     */
    public void loadTileMap(String path) {
        try {
            InputStream is = getClass().getResourceAsStream(path);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            for (int worldRow = 0; worldRow < GamePanel.MAX_WORLD_ROW; worldRow++) {
                String line = br.readLine();
                String[] tilesID = line.split(" ");

                for (int worldCol = 0; worldCol < GamePanel.MAX_WORLD_COL; worldCol++) {
                    int tileID = Integer.parseInt(tilesID[worldCol]);
                    mapTileNum[worldCol][worldRow] = tileID;
                }
            }

            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Checks if tile is within the player's visible area.
    // Returns true if tile is inside the boundaries, false otherwise
    private boolean isTileVisible(int tileWorldX, int tileWorldY,
                                  int playerWorldX, int playerWorldY,
                                  int playerScreenX, int playerScreenY) {
        int leftBound = playerWorldX - playerScreenX;
        int rightBound = playerWorldX + playerScreenX;
        int upperBound = playerWorldY - playerScreenY;
        int lowerBound = playerWorldY + playerScreenY;

        return  (tileWorldX + GamePanel.TILE_SIZE) > leftBound &&
                (tileWorldX - GamePanel.TILE_SIZE) < rightBound &&
                (tileWorldY + GamePanel.TILE_SIZE) > upperBound &&
                (tileWorldY - GamePanel.TILE_SIZE) < lowerBound;
    }

    /**
     * Renders the game's tile map relative to player position.
     * Only tiles within the visible screen area are drawn.
     * @param g2  Graphics context used for drawing
     */
    private void renderTileMap(Graphics2D g2) {
        Player player = gamePanel.getPlayer();
        int playerWorldX = player.getWorldX();
        int playerWorldY = player.getWorldY();
        int playerScreenX = player.getScreenX();
        int playerScreenY = player.getScreenY();

        for (int worldRow = 0; worldRow < GamePanel.MAX_WORLD_ROW; worldRow++) {
            for (int worldCol = 0; worldCol < GamePanel.MAX_WORLD_COL; worldCol++) {
                int tileNum = mapTileNum[worldCol][worldRow];

                int worldX = worldCol * GamePanel.TILE_SIZE;
                int worldY = worldRow * GamePanel.TILE_SIZE;

                if (isTileVisible(worldX, worldY, playerWorldX, playerWorldY, playerScreenX, playerScreenY)) {
                    int screenX = worldX - playerWorldX + playerScreenX;
                    int screenY = worldY - playerWorldY + playerScreenY;

                    g2.drawImage(tiles.get(tileNum).getImage(), screenX, screenY,
                                GamePanel.TILE_SIZE, GamePanel.TILE_SIZE, null);
                }
            }
        }
    }

    /**
     * Draws the game's tile map area visible by the player to the screen
     * @param g2 Graphics context used for drawing
     */
    public void draw(Graphics2D g2) {
        renderTileMap(g2);
    }

    // Getter methods
    public int[][] getMapTileNum() { return mapTileNum; }
    public ArrayList<Tile> getTiles() { return tiles; }
}
