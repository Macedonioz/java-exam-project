package game_logic;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

public class Sound {
    private static final int NUM_SOUNDS_TRACKS = 30;

    public static final int GAME_THEME = 0;
    public static final int PICK_UP_KEY = 1;
    public static final int POWER_UP = 2;
    public static final int VICTORY = 3;

    private Clip clip;
    private final URL[] soundUrl = new URL[NUM_SOUNDS_TRACKS];

    public Sound() {
        setSoundUrls();
    }

    /**
     * Set URLs for all available soundtracks.
     */
    private void setSoundUrls() {
        soundUrl[GAME_THEME] = getClass().getResource("/sounds/game_theme01.wav");
        soundUrl[PICK_UP_KEY] = getClass().getResource("/sounds/pick_up_key.wav");
        soundUrl[POWER_UP] = getClass().getResource("/sounds/power_up.wav");
        soundUrl[VICTORY] = getClass().getResource("/sounds/victory_theme.wav");
    }

    /**
     * Loads and prepares the specified sound file for playback
     * @param soundID The ID of the sound to load
     */
    public void setFile(int soundID) {
        try {
            if (soundUrl[soundID] == null) {
                throw new IllegalArgumentException("Sound resource not found for ID: " + soundID);
            }

            AudioInputStream ais = AudioSystem.getAudioInputStream(soundUrl[soundID]);
            clip = AudioSystem.getClip();
            clip.open(ais);

        } catch (Exception e) {
            System.err.println("Failed to load sound:\n" + e.getMessage());
        }
    }

    public void play() {
        if (clip != null) {
            clip.start();
        }
    }
    public void loop() {
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }
    public void stop() {
        if (clip != null) {
            clip.stop();
        }
    }
}
