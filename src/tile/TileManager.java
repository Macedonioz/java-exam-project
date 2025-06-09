package tile;

import entity.Player;
import game_logic.GamePanel;
import utils.GameUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.Buffer;
import java.util.ArrayList;

public class TileManager {

    /* --------------- [CONSTANTS] --------------- */

    // SPRITE SHEETS
    private static final int DEFAULT_SHEET_COLS = 3;
    private static final int DEFAULT_SHEET_ROWS = 5;

    // COLLISION FLAGS
    private static final boolean SOLID = true;
    private static final boolean NON_SOLID = false;

    private static final boolean[] GRASS_FLAGS = {
            SOLID, SOLID, SOLID,
            SOLID, NON_SOLID, SOLID,
            SOLID, SOLID, SOLID,
            SOLID, SOLID, NON_SOLID,
            SOLID, SOLID, NON_SOLID
    };

    private static final boolean[] WATER_FLAGS = {
            SOLID, SOLID, SOLID,
            SOLID, SOLID, SOLID,
            SOLID, SOLID, SOLID,
            SOLID, SOLID, SOLID,
            SOLID, SOLID, SOLID
    };

    private static final boolean[] PATH_FLAGS = {
            NON_SOLID, NON_SOLID, NON_SOLID,
            NON_SOLID, NON_SOLID, NON_SOLID,
            NON_SOLID, NON_SOLID, NON_SOLID,
            NON_SOLID, NON_SOLID, NON_SOLID,
            NON_SOLID, NON_SOLID, NON_SOLID
    };

    private static final boolean[] BEACH_FLAGS = {
            SOLID, SOLID, SOLID,
            SOLID, NON_SOLID, SOLID,
            SOLID, SOLID, SOLID,
            SOLID, SOLID,
            SOLID, SOLID
    };

    private static final boolean[] BRIDGE_FLAGS = {
            NON_SOLID, NON_SOLID, NON_SOLID,
            NON_SOLID, NON_SOLID, NON_SOLID,
            SOLID, SOLID, SOLID,
            SOLID, NON_SOLID, SOLID,
            SOLID, NON_SOLID, SOLID,
            SOLID, NON_SOLID, SOLID
    };

    /* ------------------------------------------- */

    private final GamePanel gamePanel;

    // TILE LIST
    private final ArrayList<Tile> tiles;

    // TILE INDEXES MAP
    private final int[][] mapTileNum;


    public TileManager(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.tiles = new ArrayList<>();
        this.mapTileNum = new int[GamePanel.MAX_WORLD_COL][GamePanel.MAX_WORLD_ROW];

        loadTiles();
        loadTileMap("res/maps/world01.txt");
    }

    /**
     * Loads tiles from tiles dir in res folder
     */
    public void loadTiles() {

        // [0 -> 3]
        loadTile("/tiles/grass_01.png", false);
        loadTile("/tiles/sand_01.png", false);
        loadTile("/tiles/water_01.png", true);
        loadTile("/tiles/tree_01.png", true);

        // [4 -> 18 (15)]
        loadTilesFromSpriteSheet("/tiles/grass_tiles.png", GamePanel.ORIGINAL_TILE_SIZE, DEFAULT_SHEET_COLS, DEFAULT_SHEET_ROWS, GRASS_FLAGS);
        // [19 -> 33 (15)]
        loadTilesFromSpriteSheet("/tiles/water_tiles.png", GamePanel.ORIGINAL_TILE_SIZE, DEFAULT_SHEET_COLS, DEFAULT_SHEET_ROWS, WATER_FLAGS);
        // [34 -> 48 (15)]
        loadTilesFromSpriteSheet("/tiles/path_tiles.png", GamePanel.ORIGINAL_TILE_SIZE, DEFAULT_SHEET_COLS, DEFAULT_SHEET_ROWS, PATH_FLAGS);
        // [49 -> 61 (12)]
        loadTilesFromSpriteSheet("/tiles/beach_tiles.png", GamePanel.ORIGINAL_TILE_SIZE, DEFAULT_SHEET_COLS, DEFAULT_SHEET_ROWS, BEACH_FLAGS);
        // [62 -> 79 (18)]
        loadTilesFromSpriteSheet("/tiles/bridge_tiles01.png", GamePanel.ORIGINAL_TILE_SIZE, DEFAULT_SHEET_COLS, 6, BRIDGE_FLAGS);
    }

    // Load single tile, setting up image and collision properties
    private void loadTile(String path, boolean collision) {
        Tile tile;
        try {
            tile = new Tile(
                    GameUtils.scaleImage(
                            GameUtils.loadImageSafe(path),
                            GamePanel.TILE_SIZE,
                            GamePanel.TILE_SIZE
                    ),
                    collision
            );

        } catch (IOException e) {
            System.err.println("Error loading tile images:" + e.getMessage());
            tile = new Tile(
                    GameUtils.getPlaceholderImage(GamePanel.TILE_SIZE, GamePanel.TILE_SIZE, Color.GRAY),
                    collision
            );
        }

        tiles.add(tile);
    }

    /*
     * Loads multiple tiles from a sprite sheet located at the specified path.
     * Each tile is sliced, scaled to the game's tile size, and assigned a collision flag.
     * @param path The file path to the sprite sheet image
     * @param tileSize The size (in pixels) of each tile in the sprite sheet
     * @param cols The number of columns in the sprite sheet
     * @param rows The number of rows in the sprite sheet
     * @param collisionFlags An array indicating which tiles have collision.
     *                       If null or shorter than tile count, remaining tiles are set by default to having no collision
     */
    private void loadTilesFromSpriteSheet(String path, int tileSize, int cols, int rows, boolean[] collisionFlags) {
        try {
            BufferedImage spriteSheet = GameUtils.loadImageSafe(path);
            ArrayList<BufferedImage> slicedTiles = GameUtils.sliceSpriteSheet(spriteSheet, tileSize, rows, cols);

            for (int i = 0; i < slicedTiles.size(); i++) {
                BufferedImage scaledTile = GameUtils.scaleImage(slicedTiles.get(i), GamePanel.TILE_SIZE, GamePanel.TILE_SIZE);
                boolean hasCollision = collisionFlags != null && i < collisionFlags.length && collisionFlags[i];            // short circuit

                tiles.add(new Tile(scaledTile, hasCollision));
            }

            System.out.println("Loaded " + slicedTiles.size() + " tiles from " + path);

        } catch (IOException e) {
            System.err.println("Error loading sprite sheet:\n" + e.getMessage());
        }
    }

    /**
     * Loads tiles ID map into a 2D array
     * @param path file path of the world map (.txt) to load
     */
    public void loadTileMap(String path) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));

            for (int worldRow = 0; worldRow < GamePanel.MAX_WORLD_ROW; worldRow++) {
                String line = br.readLine();
                if (line == null) {
                    throw new IOException("Unexpected end of file at row " + worldRow);
                }

                String[] rowTilesID = line.split(" ");

                for (int worldCol = 0; worldCol < GamePanel.MAX_WORLD_COL; worldCol++) {
                    int tileID = Integer.parseInt(rowTilesID[worldCol]);

                    // If tile is invalid, add placeholder tile [ID = 0] instead
                    if (tileID < 0 || tileID >= tiles.size()) {
                        System.err.println("Invalid tile index at (" + worldCol + "," + worldRow + "): " + tileID);
                        System.err.println("Added placeholder tile instead...");
                        mapTileNum[worldCol][worldRow] = 0;
                    } else {
                        mapTileNum[worldCol][worldRow] = tileID;
                    }
                }
            }

            System.out.println("Loaded map from " + path);

            br.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /*
     * Checks if tile is within the player's visible area.
     * @param tileWorldX The tile's world X coordinate
     * @param tileWorldY The tile's world Y coordinate
     * @param playerWorldX The player's world X coordinate
     * @param playerWorldY The player's world Y coordinate
     * @param playerScreenX The player's screen X coordinate
     * @param playerScreenY The player's screen Y coordinate
     * @return true if tile is inside the boundaries,
     *         false otherwise
     */
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
     * Draws the game's tile map area visible by the player to the screen
     * @param g2 Graphics context used for drawing
     */
    public void draw(Graphics2D g2) {
        renderTileMap(g2);
    }

    /*
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

                // Skip invalid tile indexes
                if (tileNum < 0 || tileNum >= tiles.size()) {
                    continue;
                }

                int worldX = worldCol * GamePanel.TILE_SIZE;
                int worldY = worldRow * GamePanel.TILE_SIZE;

                if (isTileVisible(worldX, worldY, playerWorldX, playerWorldY, playerScreenX, playerScreenY)) {
                    int screenX = worldX - playerWorldX + playerScreenX;
                    int screenY = worldY - playerWorldY + playerScreenY;

                    g2.drawImage(tiles.get(tileNum).getImage(), screenX, screenY, null);
                }
            }
        }
    }

    /* --------------- [GETTER METHODS] --------------- */

    public int[][] getMapTileNum() { return mapTileNum; }
    public ArrayList<Tile> getTiles() { return tiles; }

    /* ------------------------------------------------ */

}
