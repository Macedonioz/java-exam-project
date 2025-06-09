package game_logic;

import object.Key;
import utils.GameUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class UI {
    /* --------------- [UI CONSTANTS] --------------- */

    // FONT SIZES
    private final static float TITLE_SCREEN_FONT_SIZE = 70.0f;
    private final static float MENU_COMMANDS_FONT_SIZE = 35.0f;
    private final static float COMMAND_SCREEN_FONT_SIZE = 50.0f;
    private final static float GAME_FONT_SIZE = 60.0f;
    private final static float PAUSE_FONT_SIZE = 50.0f;
    private final static float OPTIONS_SCREEN_FONT_SIZE = 40.0f;
    private final static float TIMED_MESSAGE_FONT_SIZE = 45.0f;
    private final static float ENDING_FONT_SIZE = 75.0f;
    private final static float ENDING_SUB_FONT_SIZE = 40.0f;

    // SPACING
    private final static int TEXT_SPACING = GamePanel.TILE_SIZE;
    private final static int CURSOR_SPACING = GamePanel.TILE_SIZE;

    // CURSOR
    private final static String CURSOR = ">";

    // COMMANDS NUM
    public static final int MENU_COMMANDS_NUM = 2;
    public static final int MAIN_OPTION_COMMANDS_NUM = 4;
    public static final int END_CONFIRM_OPTION_COMMANDS_NUM = 2;

    // SELECTED COMMAND INDEXES
    public final static int TITLE_START_GAME_COMMAND = 0;                               // Title state
    public final static int TITLE_QUIT_COMMAND = 1;
    public final static int OPTIONS_MUSIC_COMMAND = 0;                                  // Options state
    public final static int OPTIONS_SE_COMMAND = 1;
    public final static int OPTIONS_QUIT_COMMAND = 2;
    public final static int OPTIONS_BACK_COMMAND = 3;
    public final static int QUIT_CONFIRM_COMMAND = 0;                                   // End confirmation
    public final static int QUIT_CANCEL_COMMAND = 1;

    // SUBSTATE INDEXES
    public final static int SUB_OPTION_MAIN = 0;                                   // End confirmation
    public final static int SUB_OPTION_END_CONFIRM = 1;

    // IMAGE INDEXES
    private final static int KEY_IMAGE_INDEX = 0;
    private final static int JAVA_IMAGE_INDEX = 1;

    // SUBWINDOW
    private final static Color DEFAULT_SUBWINDOW_COLOR = new Color(0, 0, 0, 210);
    private final static int DEFAULT_SUBWINDOW_ARC = 35;
    private final static Color DEFAULT_SUBWINDOW_FRAME_COLOR = new Color(225, 225, 225);
    private final static Stroke DEFAULT_SUBWINDOW_FRAME_THICKNESS = new BasicStroke(4);
    private final static int DEFAULT_SUBWINDOW_FRAME_OFFSET_X = 6;
    private final static int DEFAULT_SUBWINDOW_FRAME_OFFSET_Y = 6;
    private final static int DEFAULT_SUBWINDOW_FRAME_ARC = 25;


    // TITLE SCREEN STATE
    public enum TitleScreenState {
        MAIN_MENU,
        COMMANDS_SCREEN;
    }

    // TITLE STATE
    private final static Color MAIN_MENU_COLOR = new Color(5, 30, 75);
    private final static Color MENU_FRAME_COLOR = Color.WHITE;                                                  // Frame
    private final static BasicStroke MENU_FRAME_THICKNESS = new BasicStroke(2);
    private final static int MENU_FRAME_OFFSET_X = GamePanel.TILE_SIZE / 2;
    private final static int MENU_FRAME_OFFSET_Y = GamePanel.TILE_SIZE / 2;
    private final static String GAME_TITLE = "Java Adventure";                                                  // Game title
    private final static int TITLE_Y = GamePanel.TILE_SIZE * 3;
    private final static int TITLE_SHADOW_OFFSET = 4;
    private final static int GAME_IMAGE_X = GamePanel.SCREEN_WIDTH / 2 - (GamePanel.TILE_SIZE * 2) / 2;         // Game image
    private final static int GAME_IMAGE_SCALE = GamePanel.TILE_SIZE * 2;
    private static final String[] COMMAND_LINES = {                                                             // Commands
            "START GAME",
            "QUIT"
    };

    // COMMAND SCREEN
    private static final String[] COMMAND_SCREEN_LINES = {
            "<How to play>",
            "Move: [WASD / ArrowKeys]",
            "Pause: [P]",
            "Options: [Esc]",
            "Debug Mode: [']",
            "(Press enter to start game)"
    };
    private final static int INITIAL_COMMANDS_Y = GamePanel.TILE_SIZE * 2;

    // PLAYING STATE
    private final static int KEY_STRING_X = (int) (GamePanel.TILE_SIZE * 1.8);                      // Key object
    private final static int KEY_STRING_Y = (int) (GamePanel.TILE_SIZE * 1.4);
    private final static int KEY_X = GamePanel.TILE_SIZE / 2;
    private final static int KEY_Y = GamePanel.TILE_SIZE / 2;
    private final static int TIMED_MESSAGE_X = (int) (GamePanel.TILE_SIZE / 1.5);                   // Timed Message
    private final static int TIMED_MESSAGE_Y = GamePanel.TILE_SIZE * 3;
    private final static int MESSAGE_DISPLAY_TIME = 90;
    private final static int GAME_TIME_X = GamePanel.TILE_SIZE * 12;                                // Game time
    private final static int GAME_TIME_Y = (int) (GamePanel.TILE_SIZE * 1.4);

    // PAUSED STATE
    private final static String PAUSE_MSG = "PAUSED";
    private final static int PAUSE_Y = GamePanel.SCREEN_HEIGHT / 2;

    // OPTION STATE
    private final static int OPTIONS_FRAME_X = GamePanel.TILE_SIZE * 4;                             // Options subwindow
    private final static int OPTIONS_FRAME_Y = GamePanel.TILE_SIZE;
    private final static int OPTIONS_FRAME_WIDTH = GamePanel.TILE_SIZE * 8;
    private final static int OPTIONS_FRAME_HEIGHT = GamePanel.TILE_SIZE * 10;
    private static final int OPTIONS_MAIN = 0;                                                      // Substate indexes
    private static final int OPTIONS_END_GAME_CONFIRMATION = 1;
    private static final String[] OPTIONS_SCREEN_LINES = {                                          // Options lines
            "Options",
            "Music",
            "SE",
            "Quit game",
            "Back"
    };
    private static final int OPTIONS_LINES_OFFSET_Y = GamePanel.TILE_SIZE;
    private static final int OPTIONS_LINES_OFFSET_X = GamePanel.TILE_SIZE;
    private final static BasicStroke VOLUME_SLIDER_THICKNESS = new BasicStroke(3);            // Volume sliders
    private final static int VOLUME_SLIDER_X_OFFSET = (int) (GamePanel.TILE_SIZE * 4.5);
    private final static int VOLUME_SLIDER_Y_OFFSET = GamePanel.TILE_SIZE * 2 + 28;
    private final static int VOLUME_SLIDER_WIDTH = 120;
    private final static int VOLUME_SLIDER_HEIGHT = 24;
    private final static int VOLUME_SLIDER_SCALE_WIDTH = 24;
    private static final String[] END_GAME_CONFIRM_LINES = {                                        // Options end game confirm lines (substate 1)
            "Quit the game and \nreturn to the title screen?",
            "Yes",
            "No"
    };
    private final static int END_GAME_CONFIRM_TEXT_OFFSET_X = GamePanel.TILE_SIZE / 2;
    private final static int END_GAME_CONFIRM_TEXT_OFFSET_Y = GamePanel.TILE_SIZE * 3;

    // ENDING STATE
    private static final String[] ENDING_SCREEN_LINES = {
            "Congratulations!",
            "You found the treasure!",
            "Your time is: ",
            "<Press Enter to return to the title screen>"
    };
    private final static int INITIAL_ENDING_MESSAGE_Y = GamePanel.SCREEN_HEIGHT / 2 - (int) (GamePanel.TILE_SIZE * 3);

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

    // COMMAND SELECTION
    private int selectedCommand = 0;

    // SUBSTATE
    private int subState = 0;

    // TS STATE
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
            images.add(GameUtils.loadImageSafe("/sprites/misc/java.png"));          // [INDEX 1]

        } catch (Exception e){
            System.err.println(e.getMessage());
        }
    }

    /**
     * Draws the appropriate UI elements based on the game state.
     * @param g2 The Graphics2D context to draw on
     */
    public void draw(Graphics2D g2) {

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
            case OPTIONS -> {
                drawOptionsScreen(g2);
            }
            case ENDING -> {
                drawEndGameScreen(g2);
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
                drawMenuCommands(g2);
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
     * Draws main menu commands (with selection cursor)
     * @param g2 The Graphics2D context to draw on
     */
    private void drawMenuCommands(Graphics2D g2) {
        g2.setFont(secondaryFont.deriveFont(MENU_COMMANDS_FONT_SIZE));
        int y = (int) (TITLE_Y + TEXT_SPACING * 6.5);

        String[] commandLines = COMMAND_LINES;

        for (int i = 0; i < commandLines.length; i++) {
            String text = commandLines[i];

            int x = getXForCenteredText(text, g2);
            g2.drawString(text, x, y);

            if (selectedCommand == i) {
                g2.drawString(CURSOR, x - CURSOR_SPACING, y);

                if (gamePanel.getGameKeyHandler().isEnterPressed()) {
                    switch (selectedCommand) {
                        case UI.TITLE_START_GAME_COMMAND -> { setTitleScreenState(UI.TitleScreenState.COMMANDS_SCREEN);
                        }
                        case UI.TITLE_QUIT_COMMAND -> System.exit(0);
                    }
                }
            }

            y += (int) (TEXT_SPACING * 0.9);
        }

        gamePanel.getGameKeyHandler().resetEnterKeyState();
    }

    // TODO here

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

        if (gamePanel.getGameKeyHandler().isEnterPressed()) {
            gamePanel.setGameState(GamePanel.GameState.PLAYING);
            gamePanel.playMusic(Sound.GAME_THEME);
            setTitleScreenState(UI.TitleScreenState.MAIN_MENU);
        }

        gamePanel.getGameKeyHandler().resetEnterKeyState();
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

    /**
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
     * @param g2 The Graphics2D context to draw on
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
     * Draws the entire options screen, including the background subwindow
     * and the current options menu based on the substate.
     * @param g2 The Graphics2D context used for rendering.
     */
    private void drawOptionsScreen(Graphics2D g2) {

        // SUB WINDOW
        int frameX = OPTIONS_FRAME_X;
        int frameY = OPTIONS_FRAME_Y;
        int frameWidth = OPTIONS_FRAME_WIDTH;
        int frameHeight = OPTIONS_FRAME_HEIGHT;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight, g2);

        g2.setColor(Color.WHITE);
        g2.setFont(primaryFont.deriveFont(OPTIONS_SCREEN_FONT_SIZE));

        switch (subState) {
            case OPTIONS_MAIN -> { drawOptionsMain(frameX, frameY, g2); }
            case OPTIONS_END_GAME_CONFIRMATION -> { drawOptionsEndGameConfirmation(frameX, frameY, g2); }
        }

        gamePanel.getGameKeyHandler().resetEnterKeyState();
    }

    /*
     * Draws the main options menu
     * @param frameX The x coordinate of the options window's top-left corner
     * @param frameY The y coordinate of the options window's top-left corner
     * @param g2 The Graphics2D context used for rendering
     */
    public void drawOptionsMain(int frameX, int frameY, Graphics2D g2) {
        int x, y;
        String text;
        int spacing = TEXT_SPACING;

        // TITLE
        text = OPTIONS_SCREEN_LINES[0];
        x = getXForCenteredText(text, g2);
        y = frameY + OPTIONS_LINES_OFFSET_Y;
        g2.drawString(text, x, y);  y += spacing * 2;

        // MUSIC
        text = OPTIONS_SCREEN_LINES[1];
        x = frameX + OPTIONS_LINES_OFFSET_X;
        g2.drawString(text, x, y);
        if (selectedCommand == OPTIONS_MUSIC_COMMAND) {
            g2.drawString(CURSOR, x - (CURSOR_SPACING / 2), y);
        }
        y += spacing;

        // SE
        text = OPTIONS_SCREEN_LINES[2];
        g2.drawString(text, x, y);
        if (selectedCommand == OPTIONS_SE_COMMAND) {
            g2.drawString(CURSOR, x - (CURSOR_SPACING / 2), y);
        }
        y += spacing;

        // QUIT GAME
        text = OPTIONS_SCREEN_LINES[3];
        g2.drawString(text, x, y);
        if (selectedCommand == OPTIONS_QUIT_COMMAND) {
            g2.drawString(CURSOR, x - (CURSOR_SPACING / 2), y);
            if(gamePanel.getGameKeyHandler().isEnterPressed()) {
                gamePanel.resetGame();
            }
        }
        y += spacing * 2;

        // BACK
        text = OPTIONS_SCREEN_LINES[4];
        g2.drawString(text, x, y);
        if (selectedCommand == OPTIONS_BACK_COMMAND) {
            g2.drawString(CURSOR, x - (CURSOR_SPACING / 2), y);
            if(gamePanel.getGameKeyHandler().isEnterPressed()) {
                gamePanel.setGameState(GamePanel.GameState.PLAYING);
                selectedCommand = 0;
            }
        }

        // VOLUME SLIDERS
        x = frameX + VOLUME_SLIDER_X_OFFSET;
        y = frameY + VOLUME_SLIDER_Y_OFFSET;
        g2.setStroke(VOLUME_SLIDER_THICKNESS);
        int volumeWidth;

        // Music
        g2.drawRect(x, y, VOLUME_SLIDER_WIDTH, VOLUME_SLIDER_HEIGHT);
        volumeWidth = VOLUME_SLIDER_SCALE_WIDTH * gamePanel.getMusic().getVolumeScale();
        g2.fillRect(x, y, volumeWidth, VOLUME_SLIDER_HEIGHT);   y += spacing;

        // Sound effects
        g2.drawRect(x, y, VOLUME_SLIDER_WIDTH, VOLUME_SLIDER_HEIGHT);
        volumeWidth = VOLUME_SLIDER_SCALE_WIDTH * gamePanel.getSE().getVolumeScale();
        g2.fillRect(x, y, volumeWidth, VOLUME_SLIDER_HEIGHT);
    }

    /*
     * Draws the end game confirmation screen, with options to confirm/cancel quitting the game
     * @param frameX The x coordinate of the confirmation window's top-left corner
     * @param frameY The y coordinate of the confirmation window's top-left corner
     * @param g2 The Graphics2D context used for rendering
     */
    public void drawOptionsEndGameConfirmation(int frameX, int frameY, Graphics2D g2) {
        int x, y;
        String text;
        int spacing = TEXT_SPACING;

        // CONFIRM QUESTION
        text = END_GAME_CONFIRM_LINES[0];
        x = frameX + END_GAME_CONFIRM_TEXT_OFFSET_X;
        y = frameY + END_GAME_CONFIRM_TEXT_OFFSET_Y;

        for (String line : text.split("\n")) {
            g2.drawString(line, x, y);
            y += spacing;
        }
        y += spacing * 3;

        // YES
        text = END_GAME_CONFIRM_LINES[1];
        x = getXForCenteredText(text, g2);
        g2.drawString(text, x, y);

        if (selectedCommand == QUIT_CONFIRM_COMMAND) {
            g2.drawString(CURSOR, x - (CURSOR_SPACING / 2), y);
            if (gamePanel.getGameKeyHandler().isEnterPressed()) {
                subState = 0;
                gamePanel.setGameState(GamePanel.GameState.TITLE);
                gamePanel.stopMusic();
            }
        }
        y += spacing;

        // NO
        text = END_GAME_CONFIRM_LINES[2];
        x = getXForCenteredText(text, g2);
        g2.drawString(text, x, y);

        if (selectedCommand == QUIT_CANCEL_COMMAND) {
            g2.drawString(CURSOR, x - (CURSOR_SPACING / 2), y);
            if (gamePanel.getGameKeyHandler().isEnterPressed()) {
                subState = 0;
                selectedCommand = 3;
            }
        }
    }

    /*
     * Draws the end game screen
     * @param g2 The Graphics2D context used for rendering
     */
    private void drawEndGameScreen(Graphics2D g2) {
        int x, y;
        String text;
        int spacing = TEXT_SPACING;

        g2.setFont(secondaryFont);
        g2.setColor(Color.YELLOW);

        // Ending message 1
        text = ENDING_SCREEN_LINES[0];
        x = getXForCenteredText(text, g2);
        y = INITIAL_ENDING_MESSAGE_Y;
        g2.drawString(text, x, y); y+= (int) (spacing * 1.4);

        g2.setFont(primaryFont);
        g2.setColor(Color.WHITE);

        // Ending message 2
        text = ENDING_SCREEN_LINES[1];
        x = getXForCenteredText(text, g2);
        g2.drawString(text, x, y); y+= (int) (spacing * 4.5);

        // Final time message
        text = ENDING_SCREEN_LINES[2] + decimalFormat.format(playTime);
        x = getXForCenteredText(text, g2);
        g2.drawString(text, x, y); y+= spacing;

        g2.setFont(primaryFont.deriveFont(ENDING_SUB_FONT_SIZE));

        // Back to title screen message
        text = ENDING_SCREEN_LINES[3];
        x = getXForCenteredText(text, g2);
        g2.drawString(text, x, y);

        if (gamePanel.getGameKeyHandler().isEnterPressed()) {
            gamePanel.resetGame();
        }
    }

    /*
     * Returns centered x coordinate for given text
     * @param text The text for which to get centered x position
     */
    private int getXForCenteredText(String text, Graphics2D g2) {
        int textLength = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return (GamePanel.SCREEN_WIDTH / 2) - (textLength / 2);
    }

    /*
     * Draws a styled subwindow of given size.
     * @param x The x coordinate of the top-left corner
     * @param y The y coordinate of the top-left corner
     * @param width The width of the subwindow
     * @param height The height of the subwindow
     * @param g2 The Graphics2D context used for rendering.
     */
    private void drawSubWindow(int x, int y, int width, int height, Graphics2D g2) {
        g2.setColor(DEFAULT_SUBWINDOW_COLOR);
        g2.fillRoundRect(x, y, width, height, DEFAULT_SUBWINDOW_ARC, DEFAULT_SUBWINDOW_ARC);

        g2.setColor(DEFAULT_SUBWINDOW_FRAME_COLOR);
        g2.setStroke(DEFAULT_SUBWINDOW_FRAME_THICKNESS);
        g2.drawRoundRect(x + DEFAULT_SUBWINDOW_FRAME_OFFSET_X, y + DEFAULT_SUBWINDOW_FRAME_OFFSET_Y,
                    width - (DEFAULT_SUBWINDOW_FRAME_OFFSET_X * 2), height - (DEFAULT_SUBWINDOW_FRAME_OFFSET_Y * 2),
                          DEFAULT_SUBWINDOW_FRAME_ARC, DEFAULT_SUBWINDOW_FRAME_ARC);
    }

    /**
     * Decreases the selected command by one.
     * If the selected command would become negative, it wraps around to maxCommands
     * @param maxCommands max command index
     */
    public void decreaseSelectedCommand(int maxCommands) {
        if (selectedCommand == 0) {
            selectedCommand = maxCommands - 1;
        } else {
            selectedCommand--;
        }
    }

    /**
     * Increases the selected command by one.
     * If the selected command would exceed maxCommands, it wraps around to zero.
     * @param maxCommands max command index
     */
    public void increaseSelectedCommand(int maxCommands) {
        selectedCommand = (selectedCommand + 1) % (maxCommands);
    }

    /**
     * Reset UI state
     */
    public void reset() {
        playTime = 0;
        subState = 0;
        selectedCommand = 0;
        titleScreenState = TitleScreenState.MAIN_MENU;
    }

    /* --------------- [GETTER METHODS] --------------- */

    public int getSelectedCommand() {
        return selectedCommand;
    }
    public TitleScreenState getTitleScreenState () {
        return titleScreenState;
    }
    public int getSubState() { return subState; }
    /* ------------------------------------------------ */


    /* --------------- [SETTER METHODS] --------------- */

    public void setTitleScreenState (TitleScreenState state) {
        this.titleScreenState = state;
    }

    /* ------------------------------------------------ */
}
