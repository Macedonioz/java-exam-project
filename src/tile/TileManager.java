package tile;

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

    // TODO enum tileID
//    public enum TileID {
//        GRASS(0), PATH(1), WATER(2), TREE(3);
//        private final int id;
//        TileID(int id) { this.id = id; }
//        public int getID() { return id; }
//    }

    public TileManager(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.tiles = new ArrayList<>();
        this.mapTileNum = new int[GamePanel.MAX_SCREEN_COL][GamePanel.MAX_SCREEN_ROW];

        loadTiles();
        loadTileMap("/maps/map01.txt");
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

            for (int row = 0; row < GamePanel.MAX_SCREEN_ROW; row++) {
                String line = br.readLine();
                String[] tilesID = line.split(" ");

                for (int col = 0; col < GamePanel.MAX_SCREEN_COL; col++) {
                    int tileID = Integer.parseInt(tilesID[col]);
                    mapTileNum[col][row] = tileID;
                }
            }

            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void renderTileMap(Graphics2D g2) {
        int x = 0, y = 0;

        for (int row = 0; row < GamePanel.MAX_SCREEN_ROW; row++) {
            for (int col = 0; col < GamePanel.MAX_SCREEN_COL; col++) {
                int tileNum = mapTileNum[col][row];
                g2.drawImage(tiles.get(tileNum).getImage(), x, y, GamePanel.TILE_SIZE, GamePanel.TILE_SIZE, null);
                x += GamePanel.TILE_SIZE;
            }
            x = 0;
            y += GamePanel.TILE_SIZE;
        }
    }

    public void render(Graphics2D g2) {
        renderTileMap(g2);
    }
}
