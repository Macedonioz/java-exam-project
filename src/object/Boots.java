package object;

import entity.Player;
import game_logic.GamePanel;
import game_logic.Sound;
import utils.GameUtils;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class Boots extends GameObject {

    public Boots(GamePanel gamePanel) {
        super("Boots", gamePanel);

        // Load image
        try {
            BufferedImage img = GameUtils.scaleImage(GameUtils.loadImageSafe("/objects/boots.png"),
                                                     GamePanel.TILE_SIZE, GamePanel.TILE_SIZE);
            this.setImage(img);
        } catch (IOException e) {
            System.err.println("Error loading image:\n" + e.getMessage());
        }
    }

    @Override
    public void onPlayerCollision(GamePanel gamePanel) {
        gamePanel.playSE(Sound.POWER_UP);
        gamePanel.getGameObjects().remove(this);

        gamePanel.getPlayer().setSpeed((int) (gamePanel.getPlayer().getSpeed() * Player.SPEED_BOOST_MULTIPLIER));
        gamePanel.getUi().showMessage("Speed up!");
    }
}
