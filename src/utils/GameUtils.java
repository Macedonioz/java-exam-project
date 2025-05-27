package utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class GameUtils {
    /**
     * Loads image safely from given path
     * @param path file path from which to open image
     * @return the image if loading was succesfull
     * @throws FileNotFoundException if file was not found
     * @throws IOException if image format is invalid
     */
    public static BufferedImage loadImageSafe(String path) throws IOException {
        try (InputStream is = GameUtils.class.getResourceAsStream(path)) {
            if (is == null) {
                throw new FileNotFoundException("File not found: " + path);
            }

            BufferedImage image = ImageIO.read(is);
            if (image == null) {
                throw new IOException("Invalid image format!");
            }
            return image;
        }
    }

    /**
     * Slice given sprite sheet into its single sprites.
     * Supports sprite sheets with uniform tiles and no padding between frames.
     * @param sheet the sprite sheet to slice
     * @param tileSize the width and height of each tile (usually ORIGINAL_TILE_SIZE)
     * @param rows number of rows in the sprite sheet
     * @param spritesPerRow number of sprites per row
     * @return an array containing the individual sprites (as BufferedImages)
     */
    public static BufferedImage[] sliceSpriteSheet(BufferedImage sheet, int tileSize, int rows, int spritesPerRow) {
        BufferedImage[] sprites = new BufferedImage[rows * spritesPerRow];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < spritesPerRow; j++) {
                int frameIndex = (i * spritesPerRow) + j;
                sprites[frameIndex] = sheet.getSubimage(j * tileSize, i * tileSize, tileSize, tileSize);
            }
        }

        return sprites;
    }
}
