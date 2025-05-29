package object;

import game_logic.GamePanel;
import utils.GameUtils;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class Chest extends GameObject {

    public Chest(GamePanel gamePanel) {
        super("Chest", gamePanel);

        try {
            BufferedImage img = GameUtils.loadImageSafe("/objects/chest.png");
            this.setImage(img);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.setCollision(true);
    }
}
