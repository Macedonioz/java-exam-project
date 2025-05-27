package object;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Key extends GameObject {

    public Key(GamePanel gamePanel) {
        super("Key", gamePanel);

        try {
            BufferedImage img = ImageIO.read(getClass().getResourceAsStream("/objects/key.png"));
            this.setImage(img);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
