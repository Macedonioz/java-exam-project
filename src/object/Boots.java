package object;

import game_logic.GamePanel;
import utils.GameUtils;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class Boots extends GameObject {

    public Boots(GamePanel gamePanel) {
        super("Boots", gamePanel);

        try {
            BufferedImage img = GameUtils.loadImageSafe("/objects/boots.png");
            this.setImage(img);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.setCollision(true);
    }
}
