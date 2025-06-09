package object;

import entity.Player;
import game_logic.GamePanel;
import game_logic.Sound;
import utils.GameUtils;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class Chest extends GameObject {

    private BufferedImage closedImage;
    private BufferedImage openImage;
    private boolean isOpen = false;

    public Chest(GamePanel gamePanel) {
        super("Chest", gamePanel);

        // Load images
        try {
            closedImage = GameUtils.scaleImage(
                    GameUtils.loadImageSafe("/objects/chest_closed.png"),
                    GamePanel.TILE_SIZE, GamePanel.TILE_SIZE
            );
            openImage = GameUtils.scaleImage(
                    GameUtils.loadImageSafe("/objects/chest_open.png"),
                    GamePanel.TILE_SIZE, GamePanel.TILE_SIZE
            );

            this.setImage(closedImage);

        } catch (IOException e) {
            System.err.println("Error loading image:\n" + e.getMessage());
        }

        this.setCollision(true);
    }

    @Override
    public void onPlayerCollision(GamePanel gamePanel) {

        if (gamePanel.getPlayer().getNumKeys() >= Player.REQUIRED_KEYS) {
            gamePanel.stopMusic();
            gamePanel.playSE(Sound.VICTORY);
            gamePanel.setGameState(GamePanel.GameState.ENDING);
            this.open();
        } else {
            int remaining = Player.REQUIRED_KEYS - gamePanel.getPlayer().getNumKeys();
            gamePanel.getUi().showMessage("You need " + remaining + " more key" + (remaining > 1 ? "s" : "") + " to open the chest!");
        }
    }

    public void open() {
        isOpen = true;
        this.setImage(openImage);
    }

    public void close() {
        isOpen = false;
        this.setImage(closedImage);
    }


}
