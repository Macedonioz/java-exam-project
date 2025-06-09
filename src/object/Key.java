package object;

import entity.Player;
import game_logic.GamePanel;
import game_logic.Sound;
import utils.GameUtils;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class Key extends GameObject {

    public Key(GamePanel gamePanel) {
        super("Key", gamePanel);

        // Load images
        try {
            BufferedImage img = GameUtils.scaleImage(GameUtils.loadImageSafe("/objects/key.png"),
                    GamePanel.TILE_SIZE, GamePanel.TILE_SIZE);
            this.setImage(img);

        } catch (IOException e) {
            System.err.println("Error loading image:\n" + e.getMessage());
        }
    }

    @Override
    public void onPlayerCollision(GamePanel gamePanel) {
        gamePanel.playSE(Sound.PICK_UP_KEY);
        gamePanel.getGameObjects().remove(this);
        gamePanel.getPlayer().addKey();

        gamePanel.getUi().showMessage("You got a key!");
    }
}
