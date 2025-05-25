package tile;

import entity.Player;
import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class TileManager {

    private GamePanel gamePanel;
    private ArrayList<Tile> tiles;
    private int[][] mapTileNum;

    public TileManager(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.tiles = new ArrayList<>();
        this.mapTileNum = new int[GamePanel.MAX_WORLD_COL][GamePanel.MAX_WORLD_ROW];

        loadTiles();
        loadTileMap("/maps/world01.txt");
    }

    // funzione privata di loadTiles
    private BufferedImage loadTile(String path) throws IOException {
        InputStream is = getClass().getResourceAsStream(path);
        if (is == null)
            throw new IOException("Tile not found: " + path);
        return ImageIO.read(is);
    }

    // TODO gestire sprite sheet (più tardi)
    public void loadTiles() {
        try {
            tiles.add(new Tile(loadTile("/tiles/grass_01.png"), false));
            tiles.add(new Tile(loadTile("/tiles/path_01.png"), false));
            tiles.add(new Tile(loadTile("/tiles/water_01.png"), false));
            tiles.add(new Tile(loadTile("/tiles/tree_01.png"), false));

        } catch (IOException e) {
            e.printStackTrace();
            // TODO add placeholder tiles
        }
    }

    // TODO add security measures (split, parse)
    public void loadTileMap(String path) {
//        // Validate input path
//        if (path == null || path.trim().isEmpty()) {
//            throw new IllegalArgumentException("Map path cannot be null or empty");
//        }

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

    public void render(Graphics2D g2) {
        renderTileMap(g2);
    }
}
