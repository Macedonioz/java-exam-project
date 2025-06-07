package object;

import game_logic.GamePanel;
import utils.GameUtils;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class Key extends GameObject {

    public Key(GamePanel gamePanel) {
        super("Key", gamePanel);

        try {
            BufferedImage img = GameUtils.scaleImage(GameUtils.loadImageSafe("/objects/key.png"),
                    GamePanel.TILE_SIZE, GamePanel.TILE_SIZE);
            this.setImage(img);

        } catch (IOException e) {
            System.err.println("Error loading image:\n" + e.getMessage());
        }
    }
}
