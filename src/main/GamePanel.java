package main;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable{
    //TODO change size to liking

    // SCREEN SETTINGS
    final int originalTileSize = 16;                        // 16x16 default tile size
    final int scale = 3;

    final int tileSize = originalTileSize * scale;          // 48x48 actual tile size
    final int maxScreenCol = 16;                            // 4x3 ratio
    final int maxScreenRow = 12;
    final int screenWidth = maxScreenCol * tileSize;        // 768x576 pixels
    final int screenHeight = maxScreenRow * tileSize;

    // FPS
    int FPS = 60;

    KeyHandler gameKeyHandler = new KeyHandler();
    Thread gameThread;

    // Set player's default position
    int playerX = 100;
    int playerY = 100;
    int playerSpeed = 4;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);               // component drawing will be done in an offscreen painting buffer (improves game's rendering performance)
        this.addKeyListener(gameKeyHandler);
        this.setFocusable(true);                    // GamePanel can be "focused" to receive key input // TODO controllare documentazione
    }

    public void startGameThread() {

        gameThread = new Thread(this);
        // starting the thread causes the run method to be automatically called in that separately executing thread
        gameThread.start();
    }

    @Override
    public void run() {
        // GAME LOOP
        // TODO check all

        double drawInterval = 1_000_000_000 / FPS;                  // Interval between frames (nanos in one second / FPS) [0.1666s for 60 FPS]
        double nextDrawTime = System.nanoTime() + drawInterval;

        while(gameThread != null) {

            update();

            repaint();


            try {
                double remainingTIme = nextDrawTime - System.nanoTime();
                remainingTIme /= 1_000_000;             // convert to milliseconds

                if (remainingTIme < 0)
                    remainingTIme = 0;

                Thread.sleep((long) remainingTIme);

                nextDrawTime += drawInterval;

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void update() {
        // TODO refactoring (getter, switch case)
        if (gameKeyHandler.upPressed) {
            playerY -= playerSpeed;                 // Y values increases as they go down
        } else if (gameKeyHandler.downPressed) {
            playerY += playerSpeed;
        } else if (gameKeyHandler.leftPressed) {
            playerX -= playerSpeed;                 // X values increases as they go right
        } else if (gameKeyHandler.rightPressed) {
            playerX += playerSpeed;
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;            // extends Graphics providing more advanced features

        g2d.setColor(Color.white);
        g2d.fillOval(   playerX, playerY, tileSize, tileSize);

        g2d.dispose();                  // dispose (deallocate) Graphics object and releases any system resources that it is using
    }
}
