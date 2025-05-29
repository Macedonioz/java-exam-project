package object;

import game_logic.GamePanel;
import utils.GameUtils;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class Key extends GameObject {

    public Key(GamePanel gamePanel) {
        super("Key", gamePanel);

        try {
            BufferedImage img = GameUtils.loadImageSafe("/objects/key.png");
            this.setImage(img);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
