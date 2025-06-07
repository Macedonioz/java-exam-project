package game_logic;

import object.Key;
import utils.GameUtils;

import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class UI {
    /* --------------- [UI CONSTANTS] --------------- */

    // FONT SIZES
    private final static float TITLE_SCREEN_FONT_SIZE = 70.0f;
    private final static float MENU_OPTIONS_FONT_SIZE = 35.0f;
    private final static float COMMAND_SCREEN_FONT_SIZE = 50.0f;
    private final static float GAME_FONT_SIZE = 60.0f;
    private final static float PAUSE_FONT_SIZE = 50.0f;
    private final static float TIMED_MESSAGE_FONT_SIZE = 45.0f;
    private final static float ENDING_FONT_SIZE = 75.0f;

    // KEY OBJECT
    private final static int KEY_STRING_X = (int) (GamePanel.TILE_SIZE * 1.8);
    private final static int KEY_STRING_Y = (int) (GamePanel.TILE_SIZE * 1.4);
    private final static int KEY_X = GamePanel.TILE_SIZE / 2;
    private final static int KEY_Y = GamePanel.TILE_SIZE / 2;

    // TIMED MESSAGE
    private final static int TIMED_MESSAGE_X = (int) (GamePanel.TILE_SIZE / 1.5);
    private final static int TIMED_MESSAGE_Y = GamePanel.TILE_SIZE * 3;
    private final static int MESSAGE_DISPLAY_TIME = 90;

    // GAME TIME
    private final static int GAME_TIME_X = GamePanel.TILE_SIZE * 12;
    private final static int GAME_TIME_Y = (int) (GamePanel.TILE_SIZE * 1.4);

    // SPACING
    private final static int TEXT_SPACING = GamePanel.TILE_SIZE;
    private final static int CURSOR_SPACING = GamePanel.TILE_SIZE;

    // IMAGE INDEXES
    private final static int KEY_IMAGE_INDEX = 0;
    private final static int JAVA_IMAGE_INDEX = 1;

    // MAIN MENU
    private final static Color MAIN_MENU_COLOR = new Color(5, 30, 75);

    // MENU FRAME
    private final static Color MENU_FRAME_COLOR = Color.WHITE;
    private final static BasicStroke MENU_FRAME_THICKNESS = new BasicStroke(2);
    private final static int MENU_FRAME_OFFSET_X = GamePanel.TILE_SIZE / 2;
    private final static int MENU_FRAME_OFFSET_Y = GamePanel.TILE_SIZE / 2;

    // GAME TITLE
    private final static String GAME_TITLE = "Java Adventure";
    private final static int TITLE_Y = GamePanel.TILE_SIZE * 3;
    private final static int TITLE_SHADOW_OFFSET = 4;

    // GAME IMAGE
    private final static int GAME_IMAGE_X = GamePanel.SCREEN_WIDTH / 2 - (GamePanel.TILE_SIZE * 2) / 2;
    private final static int GAME_IMAGE_SCALE = GamePanel.TILE_SIZE * 2;

    // MENU OPTIONS
    private static final String[] OPTIONS_LINES = {
            "START GAME",
            "OPTIONS",
            "QUIT"
    };

    // CURSOR
    private final static String CURSOR = ">";

    // COMMAND_SCREEN
    private static final String[] COMMAND_SCREEN_LINES = {
            "<How to play>",
            "Move: [WASD / ArrowKeys]",
            "Pause: [P]",
            "Debug Mode: [']",
            "(Press enter to start game)"
    };
    private final static int INITIAL_COMMANDS_Y = GamePanel.TILE_SIZE * 2;

    // PAUSED STATE
    private final static String PAUSE_MSG = "PAUSED";
    private final static int PAUSE_Y = GamePanel.SCREEN_HEIGHT / 2;

    // ENDING SCREEN
    private final static String ENDING_MSG_1 = "You found the treasure!";
    private final static String ENDING_MSG_2 = "Congratulations!";
    private final static int INITIAL_ENDING_MESSAGE_Y = GamePanel.SCREEN_HEIGHT / 2 - (int) (GamePanel.TILE_SIZE * 1.5);

    /* ---------------------------------------------- */

    private final GamePanel gamePanel;

    // UI FONTS
    private Font primaryFont, secondaryFont;

    // UI IMAGES
    private final ArrayList<BufferedImage> images = new ArrayList<>();;

    // GAME TIME
    private final DecimalFormat decimalFormat = new DecimalFormat("#.##");
    private double playTime;

    // TIMED MESSAGES
    private boolean messageOn = false;
    private String message = "";
    private int messageCounter = 0;

    // GAME STATE
    private boolean isGameFinished = false;

    // COMMAND SELECTION
    private int selectedCommand = 0;
    private static final int MAX_COMMAND_INDEX = 2;

    // TITLE SCREEN STATE
    public enum TitleScreenState {
        MAIN_MENU,
        COMMANDS_SCREEN
    }
    private TitleScreenState titleScreenState = TitleScreenState.MAIN_MENU;

    public UI(GamePanel gamePanel) {
        this.gamePanel = gamePanel;

        loadFonts();
        loadImages();
    }

    /*
     * Loads the fonts used in the UI
     */
    private void loadFonts() {
        this.primaryFont = GameUtils.loadFont("/fonts/BitPotionExt.ttf", GAME_FONT_SIZE, Font.PLAIN);
        this.secondaryFont = GameUtils.loadFont("/fonts/quaver.ttf", ENDING_FONT_SIZE, Font.BOLD);
    }

    /*
     * Loads the images used in the UI
     */
    private void loadImages() {
        // Game objects images
        Key key = new Key(gamePanel);
        images.add(key.getImage());                 // [INDEX 0]

        // Misc sprites images
        try {
            images.add(GameUtils.loadImageSafe("/sprites/misc/java.png"));              // [INDEX 1]

        } catch (Exception e){
            System.err.println(e.getMessage());
        }
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
            switch (gamePanel.getGameState()) {
                case TITLE -> {
                    drawTitleScreen(g2);
                }
                case PLAYING -> {
                    drawPlayingUI(g2);
                }
                case PAUSED -> {
                    drawPauseScreen(g2);
                }
            }
        }
    }

    /*
     * Draws title screen menu and submenus depending on titleScreenState
     * @param g2 The Graphics2D context to draw on
     */
    private void drawTitleScreen(Graphics2D g2) {

        switch (titleScreenState) {
            case TitleScreenState.MAIN_MENU -> {
                drawMainMenuBackground(g2);
                drawMainMenuFrame(g2);
                drawGameTitle(g2);
                drawGameImage(g2);
                drawMenuOptions(g2);
            }
            case TitleScreenState.COMMANDS_SCREEN -> {
                drawCommandScreen(g2);
            }
        }
    }

    /*
     * Draws main menu background (solid color)
     * @param g2 The Graphics2D context to draw on
     */
    private void drawMainMenuBackground(Graphics2D g2) {
        g2.setColor(MAIN_MENU_COLOR);
        g2.fillRect(0, 0, GamePanel.SCREEN_WIDTH, GamePanel.SCREEN_HEIGHT);
    }

    /*
     * Draws main menu frame (Rectangle outline)
     * @param g2 The Graphics2D context to draw on
     */
    private void drawMainMenuFrame(Graphics2D g2) {
        g2.setColor(MENU_FRAME_COLOR);
        g2.setStroke(MENU_FRAME_THICKNESS);

        int frameWidth = GamePanel.SCREEN_WIDTH - (MENU_FRAME_OFFSET_X * 2);
        int frameHeight = GamePanel.SCREEN_HEIGHT - (MENU_FRAME_OFFSET_Y * 2);

        g2.drawRect(MENU_FRAME_OFFSET_X, MENU_FRAME_OFFSET_Y, frameWidth, frameHeight);
    }

    /*
     * Draws main menu game title (with shadow)
     * @param g2 The Graphics2D context to draw on
     */
    private void drawGameTitle(Graphics2D g2) {
        String text = GAME_TITLE;
        g2.setFont(secondaryFont.deriveFont(Font.BOLD, TITLE_SCREEN_FONT_SIZE));
        int y = TITLE_Y;
        int x = getXForCenteredText(text, g2);

        // Shadow
        g2.setColor(Color.DARK_GRAY);
        g2.drawString(text, x + TITLE_SHADOW_OFFSET, y + TITLE_SHADOW_OFFSET);

        // Main title
        g2.setColor(Color.WHITE);
        g2.drawString(text, x, y);
    }

    /*
     * Draws main menu game image (not displayed if image loading failed)
     * @param g2 The Graphics2D context to draw on
     */
    private void drawGameImage(Graphics2D g2) {
        int x = GAME_IMAGE_X;
        int y = TITLE_Y + TEXT_SPACING * 2;

        BufferedImage image = images.get(JAVA_IMAGE_INDEX);
        if (image != null) {
            g2.drawImage(image, x, y, GAME_IMAGE_SCALE, GAME_IMAGE_SCALE, null);
        }
    }

    /*
     * Draws main menu options (with selection cursor)
     * @param g2 The Graphics2D context to draw on
     */
    private void drawMenuOptions(Graphics2D g2) {
        g2.setFont(secondaryFont.deriveFont(MENU_OPTIONS_FONT_SIZE));
        int y = TITLE_Y + TEXT_SPACING * 6;

        String[] options = OPTIONS_LINES;

        for (int i = 0; i < options.length; i++) {
            String text = options[i];

            int x = getXForCenteredText(text, g2);
            g2.drawString(text, x, y);

            if (selectedCommand == i) {
                g2.drawString(CURSOR, x - CURSOR_SPACING, y);
            }

            y += (int) (TEXT_SPACING * 0.9);
        }
    }

    /*
     * Draws command instructions
     * @param g2 The Graphics2D context to draw on
     */
    private void drawCommandScreen(Graphics2D g2) {
        g2.setColor(Color.WHITE);
        g2.setFont(primaryFont.deriveFont(COMMAND_SCREEN_FONT_SIZE));

        int y = INITIAL_COMMANDS_Y;
        int spacing = TEXT_SPACING;

        for (int i = 0; i < COMMAND_SCREEN_LINES.length; i++) {
            String line = COMMAND_SCREEN_LINES[i];
            int x = getXForCenteredText(line, g2);
            g2.drawString(line, x, y);

            // Add extra spacing before the last line
            if (i == COMMAND_SCREEN_LINES.length - 2) {
                y += spacing * 5;                   // extra spacing
            } else {
                y += spacing;
            }
        }
    }

    /*
     * Draws UI of game state when playing
     * @param g2 The Graphics2D context to draw on
     */
    private void drawPlayingUI(Graphics2D g2) {
        drawGameHUD(g2);
        updatePlayTime();
        drawTimedMessageIfOn(g2);
    }

    /*
     * Draws the game HUD elements
     * @param g2 The Graphics2D context to draw on
     */
    private void drawGameHUD(Graphics2D g2) {
        g2.setFont(primaryFont);
        g2.setColor(Color.WHITE);

        drawKeyIconAndCount(g2);
        drawTime(g2);
    }

    /*
     * Draws key icon and number of keys collected
     * @param g2 The Graphics2D context to draw on
     */
    private void drawKeyIconAndCount(Graphics2D g2) {
        BufferedImage keyImage = images.get(KEY_IMAGE_INDEX);

        if (keyImage != null) {
            g2.drawImage(keyImage, KEY_X, KEY_Y, GamePanel.TILE_SIZE, GamePanel.TILE_SIZE, null);
        }

        int numKeys = gamePanel.getPlayer().getNumKeys();
        g2.drawString("x " + numKeys, KEY_STRING_X, KEY_STRING_Y);
    }

    /*
     * Draws in game time
     * @param g2 The Graphics2D context to draw on
     */
    private void drawTime(Graphics2D g2) {
        String formattedTime = decimalFormat.format(playTime);
        g2.drawString("Time: " + formattedTime, GAME_TIME_X, GAME_TIME_Y);
    }

    /*
     * Updates the game play time (decimal format --> #.##)
     */
    private void updatePlayTime() {
        playTime += (double) 1 / GamePanel.FPS;
    }

    /*
     * Displays a temporary message on the game screen.
     * The message will automatically disappear after a set time
     * @param message The message text to display
     */
    public void showMessage(String message) {
        this.message = message;
        this.messageOn = true;
    }

    /*
     * Draws a temporary message if one is currently active.
     * Automatically hides the message after the display time expires
     * @param g2 The Graphics2D context to draw on
     */
    private void drawTimedMessageIfOn(Graphics2D g2) {
        if (messageOn) {
            g2.setFont(g2.getFont().deriveFont(TIMED_MESSAGE_FONT_SIZE));
            g2.drawString(message, TIMED_MESSAGE_X, TIMED_MESSAGE_Y);

            // Increased at every update (60 times per second) until it reaches message display time
            if (++messageCounter > MESSAGE_DISPLAY_TIME) {
                messageCounter = 0;
                messageOn = false;
            }
        }
    }

    /*
     * Draws pause text if game state is on PAUSED
     */
    private void drawPauseScreen(Graphics2D g2) {
        String text = PAUSE_MSG;
        g2.setFont(secondaryFont.deriveFont(PAUSE_FONT_SIZE));
        g2.setColor(Color.WHITE);

        int x = getXForCenteredText(text, g2);
        int y = PAUSE_Y;

        g2.drawString(text, x, y);
    }

    /*
     * Draws the end game screen.
     * Stops the game thread after drawing
     */
    private void drawEndGameScreen(Graphics2D g2) {
        g2.setFont(primaryFont);
        g2.setColor(Color.WHITE);

        int spacing = TEXT_SPACING;

        // Ending message 1
        String text = ENDING_MSG_1;
        int x = getXForCenteredText(text, g2);
        int y = INITIAL_ENDING_MESSAGE_Y;
        g2.drawString(text, x, y); y+= (int) (spacing * 3.5);

        // Final time message
        String timeText = "Your time is: " + decimalFormat.format(playTime);
        x = getXForCenteredText(timeText, g2);
        g2.drawString(timeText, x, y); y+= spacing * 2;

        // Ending message 2
        g2.setFont(secondaryFont);
        g2.setColor(Color.YELLOW);
        text = ENDING_MSG_2;
        x = getXForCenteredText(text, g2);
        g2.drawString(text, x, y);

        gamePanel.stopGameThread();
    }

    /*
     * Returns centered x coordinate for given text
     * @param text The text for which to get centered x position
     */
    private int getXForCenteredText(String text, Graphics2D g2) {
        int textLength = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return (GamePanel.SCREEN_WIDTH / 2) - (textLength / 2);
    }

    /**
     * Decreases the selected command by one.
     * If the selected command would become negative, it wraps around to MAX_COMMAND_INDEX
     */
    public void decreaseSelectedCommand() {
        selectedCommand = ((selectedCommand - 1) + (MAX_COMMAND_INDEX + 1)) % (MAX_COMMAND_INDEX + 1);
        gamePanel.playSoundEffect(Sound.MENU_SELECT);
    }

    /**
     * Increases the selected command by one.
     * If the selected command would exceed MAX_COMMAND_INDEX, it wraps around to zero.
     */
    public void increaseSelectedCommand() {
        selectedCommand = (selectedCommand + 1) % (MAX_COMMAND_INDEX + 1);
        gamePanel.playSoundEffect(Sound.MENU_SELECT);
    }

    /**
     * Marks the game as finished, triggering the end game screen to be displayed.
     */
    public void endGame() {this.isGameFinished = true;}

    /* --------------- [GETTER METHODS] --------------- */

    public int getSelectedCommand() {
        return selectedCommand;
    }

    public TitleScreenState getTitleScreenState () {
        return titleScreenState;
    }

    /* ------------------------------------------------ */


    /* --------------- [SETTER METHODS] --------------- */

    public void setTitleScreenState (TitleScreenState state) {
        this.titleScreenState = state;
    }

    /* ------------------------------------------------ */
}
