package object;

import main.GamePanel;
import utils.GameUtils;

import javax.imageio.ImageIO;
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
