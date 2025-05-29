package game_logic;

import object.Key;
import utils.GameUtils;

import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

public class UI {

    // FONT SIZE
    private final static float GAME_FONT_SIZE = 60.0f;
    private final static float ENDING_FONT_SIZE = 75.0f;
    private final static float TIMED_MESSAGE_FONT_SIZE = 45.0f;

    // KEY DISPLAY COORDINATES
    private final static int KEY_STRING_X = (int) (GamePanel.TILE_SIZE * 1.8);
    private final static int KEY_STRING_Y = (int) (GamePanel.TILE_SIZE * 1.4);
    private final static int KEY_X = GamePanel.TILE_SIZE / 2;
    private final static int KEY_Y = GamePanel.TILE_SIZE / 2;

    // MESSAGE DISPLAY COORDINATES + TIME
    private final static int TIMED_MESSAGE_X = (int) (GamePanel.TILE_SIZE / 1.5);
    private final static int TIMED_MESSAGE_Y = GamePanel.TILE_SIZE * 3;
    private final static int MESSAGE_DISPLAY_TIME = 90;

    // TIME DISPLAY COORDINATES
    private final static int GAME_TIME_X = GamePanel.TILE_SIZE * 12;
    private final static int GAME_TIME_Y = (int) (GamePanel.TILE_SIZE * 1.4);

    // ENDING SCREEN MESSAGES DISPLAY COORDINATES
    private final static int TREASURE_MESSAGE_Y_OFFSET = GamePanel.SCREEN_HEIGHT / 2 - (int) (GamePanel.TILE_SIZE * 1.5);
    private static final int TIME_RESULT_MESSAGE_Y_OFFSET = GamePanel.SCREEN_HEIGHT / 2 + GamePanel.TILE_SIZE * 4;
    private final static int CONGRATS_MESSAGE_Y_OFFSET = GamePanel.SCREEN_HEIGHT / 2 + GamePanel.TILE_SIZE * 2;

    private final GamePanel gamePanel;

    private final Font gameFont, endingFont;
    private final BufferedImage keyImage;
    private final DecimalFormat decimalFormat = new DecimalFormat("#.##");

    private boolean messageOn = false;
    private String message = "";
    private int messageCounter = 0;

    private boolean isGameFinished = false;
    private double playTime;

    public UI(GamePanel gamePanel) {
        this.gamePanel = gamePanel;

        this.gameFont = GameUtils.loadFont("/fonts/BitPotionExt.ttf", GAME_FONT_SIZE, Font.PLAIN);
        this.endingFont = GameUtils.loadFont("/fonts/quaver.ttf", ENDING_FONT_SIZE, Font.BOLD);

        Key key = new Key(gamePanel);
        keyImage = key.getImage();
    }

    /**
     * Displays a temporary message on the game screen.
     * The message will automatically disappear after a set time
     * @param message The message text to display
     */
    public void showMessage(String message) {
        this.message = message;
        this.messageOn = true;
    }

    /**
     * Draws the appropriate UI elements based on the game state.
     * Draws the end game screen if the game is finished, the HUD otherwise
     * @param g2 The Graphics2D context to draw on
     */
    public void draw(Graphics2D g2) {
        if (isGameFinished) {
            drawEndGameScreen(g2);
        } else {
            drawGameHUD(g2);
            updatePlayTime();
            drawTimedMessageIfOn(g2);
        }
    }

    /**
     * Draws the end game screen.
     * Stops the game thread after drawing
     * @param g2 The Graphics2D context to draw on
     */
    private void drawEndGameScreen(Graphics2D g2) {
        g2.setFont(gameFont);
        g2.setColor(Color.WHITE);

        drawCenteredText(g2, "You found the treasure!",
                TREASURE_MESSAGE_Y_OFFSET);

        String timeText = "Your time is: " + decimalFormat.format(playTime);
        drawCenteredText(g2, timeText,
                TIME_RESULT_MESSAGE_Y_OFFSET);

        g2.setFont(endingFont);
        g2.setColor(Color.YELLOW);
        drawCenteredText(g2, "Congratulations!",
                CONGRATS_MESSAGE_Y_OFFSET);

        gamePanel.stopGameThread();
    }

    /**
     * Draws the game HUD
     * @param g2 The Graphics2D context to draw on
     */
    private void drawGameHUD(Graphics2D g2) {
        g2.setFont(gameFont);
        g2.setColor(Color.WHITE);

        // Draw key icon and count
        g2.drawImage(keyImage, KEY_X, KEY_Y,
                GamePanel.TILE_SIZE, GamePanel.TILE_SIZE, null);
        g2.drawString("x " + gamePanel.getPlayer().getNumKeys(),
                KEY_STRING_X, KEY_STRING_Y);

        // Draw time
        g2.drawString("Time: " + decimalFormat.format(playTime),
                GAME_TIME_X, GAME_TIME_Y);
    }

    /**
     * Updates the game play time (decimal format --> #.##)
     */
    private void updatePlayTime() {
        playTime += (double) 1 / GamePanel.FPS;
    }

    /**
     * Draws a temporary message if one is currently active.
     * Automatically hides the message after the display time expires
     * @param g2 The Graphics2D context to draw on
     */
    private void drawTimedMessageIfOn(Graphics2D g2) {
        if (messageOn) {
            g2.setFont(g2.getFont().deriveFont(TIMED_MESSAGE_FONT_SIZE));
            g2.drawString(message, TIMED_MESSAGE_X, TIMED_MESSAGE_Y);

            if (++messageCounter > MESSAGE_DISPLAY_TIME) {
                messageCounter = 0;
                messageOn = false;
            }
        }
    }

    /**
     * Draws text centered horizontally on the screen at the specified y-coordinate.
     * @param g2 The Graphics2D context to draw on
     * @param text The text to draw
     * @param y The vertical position of the text
     */
    private void drawCenteredText(Graphics2D g2, String text, int y) {
        int textLength = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = (GamePanel.SCREEN_WIDTH / 2) - (textLength / 2);
        g2.drawString(text, x, y);
    }

    /**
     * Marks the game as finished, triggering the end game screen to be displayed.
     */
    public void endGame() {this.isGameFinished = true;}
}
