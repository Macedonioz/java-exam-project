package utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class GameUtils {
    /**
     * Loads image safely from given path
     * @param path file path from which to open image
     * @return the image if loading was successfull
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
     * Fully transparent sprites are ignored
     * @param sheet the sprite sheet to slice
     * @param tileSize the width and height of each tile (usually ORIGINAL_TILE_SIZE)
     * @param rows number of rows in the sprite sheet
     * @param spritesPerRow number of sprites per row
     * @return an ArrayList containing the individual sprites (as BufferedImages)
     */
    public static ArrayList<BufferedImage> sliceSpriteSheet(BufferedImage sheet, int tileSize, int rows, int spritesPerRow) {
        ArrayList<BufferedImage> sprites = new ArrayList<>();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < spritesPerRow; j++) {
                int x = j * tileSize;
                int y = i * tileSize;

                // Check bounds to avoid RasterFormatException
                if (x + tileSize <= sheet.getWidth() && y + tileSize <= sheet.getHeight()) {
                    BufferedImage sprite = sheet.getSubimage(x, y, tileSize, tileSize);

                    // Check if sprite is fully transparent
                    if (!isFullyTransparent(sprite)) {
                        sprites.add(sprite);
                    }
                }
            }
        }

        return sprites;
    }

    // Helper function that checks if given image is fully transparent
    private static boolean isFullyTransparent(BufferedImage image) {
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {

                int pixel = image.getRGB(x, y);                                  // color values of single pixel
                int alpha = new Color(pixel, true).getAlpha();          // pixel transparency (visible if alpha value != 0)
                if (alpha != 0) {
                    return false;       // if there is any visible pixel
                }
            }
        }
        return true;        // if every pixel is transparent
    }

    /**
     * Loads a custom font from the specified res path and applies given style and size.
     * If font loading fails, a placeholder font is returned instead
     * @param path The relative path to the font file
     * @param fontSize The desired font size
     * @param fontStyle The style of the font (Font.PLAIN, Font.BOLD, Font.ITALIC)
     * @return The loaded Font object, a placeholder font if it could not be loaded.
     */
    public static Font loadFont(String path, float fontSize, int fontStyle) {
        Font font = null;

        try (InputStream is = GameUtils.class.getResourceAsStream(path)) {
            if (is != null) {
                font = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(fontStyle, fontSize);
            } else {
                throw new FileNotFoundException("File not found: " + path);
            }

        } catch (FontFormatException | IOException e) {
            System.err.println("Error loading font:\n" + e.getMessage());

            // Placeholder font if loading fails
            System.err.println("Using placeholder font instead.");
            font = new Font("SansSerif", fontStyle, 12).deriveFont(fontSize * 0.6f);
        }

        return font;
    }

    /**
     * Scale image at given path based on param width and height
     * @param originalImage The original image to scale
     * @param newWidth The new image width
     * @param newHeight The new image width height
     * @return The scaled image
     */
    public static BufferedImage scaleImage(BufferedImage originalImage, int newWidth, int newHeight) {

        BufferedImage scaledImage = new BufferedImage(newWidth, newHeight, originalImage.getType());
        Graphics2D g2 = scaledImage.createGraphics();
        g2.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
        g2.dispose();

        return scaledImage;
    }

    /**
     * Returns a solid color placeholder image of the specified size.
     * This method generates a BufferedImage filled entirely with the given color.
     * @param width The width of the placerholder image
     * @param height The height of the placegolder image
     * @param color The color used to fill the image
     * @return The placeholder image
     */
    public static BufferedImage getPlaceholderImage(int width, int height, Color color) {

        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = img.createGraphics();
        g2.setColor(color);
        g2.fillRect(0, 0, width, height);
        g2.dispose();

        return img;
    }
}
