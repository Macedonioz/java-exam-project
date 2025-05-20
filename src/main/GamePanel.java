package main;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    //TODO change size to liking

    // SCREEN SETTINGS
    final int originalTileSize = 16;                        // 16x16 default tile size
    final int scale = 3;

    final int tileSize = originalTileSize * scale;          // 48x48 actual tile size
    final int maxScreenCol = 16;                            // 4x3 ratio
    final int maxScreenRow = 12;
    final int screenWidth = maxScreenCol * tileSize;        // 768x576 pixels
    final int screenHeight = maxScreenRow * tileSize;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);       // component drawing will be done in an offscreen painting buffer (improves game's rendering performance)


    }
}
