package game_logic;

import entity.Player;
import object.GameObject;
import tile.TileManager;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.net.URL;
import java.util.ArrayList;

public class Sound {

    /* --------------- [CONSTANTS] --------------- */

    // SOUND NUM
    private static final int NUM_SOUNDS_TRACKS = 30;

    // SOUND INDEXES
    public static final int GAME_THEME = 0;
    public static final int PICK_UP_KEY = 1;
    public static final int POWER_UP = 2;
    public static final int VICTORY = 3;
    public static final int MENU_SELECT = 4;

    // VOLUME
    public static final int DEFAULT_VOLUME_SCALE = 3;
    private static final float[] VOLUME_LEVELS = {
            -80f,               // Mute
            -20f,
            -12f,
            -5f,                // Default
            1f,
            6f                  // Max
    };

    /* ------------------------------------------- */

    // AUDIO INTERFACE
    private Clip clip;

    // AUDIO RESOURCES
    private final URL[] soundUrl = new URL[NUM_SOUNDS_TRACKS];

    // VOLUME CONTROL
    private FloatControl volumeControl;
    private int volumeScale = DEFAULT_VOLUME_SCALE;
    private float volume;


    public Sound() {
        setSoundUrls();
    }


    /*
     * Set URLs for all available soundtracks.
     */
    private void setSoundUrls() {
        soundUrl[GAME_THEME] = getClass().getResource("/sounds/game_theme01.wav");
        soundUrl[PICK_UP_KEY] = getClass().getResource("/sounds/pick_up_key.wav");
        soundUrl[POWER_UP] = getClass().getResource("/sounds/power_up.wav");
        soundUrl[VICTORY] = getClass().getResource("/sounds/victory_theme.wav");
        soundUrl[MENU_SELECT] = getClass().getResource("/sounds/menu_select.wav");
    }

    /**
     * Loads and prepares the specified sound file for playback
     * @param soundID The ID of the sound to load
     */
    public void loadAudio(int soundID) {
        try {
            if (soundUrl[soundID] == null) {
                throw new IllegalArgumentException("Sound resource not found for ID: " + soundID);
            }

            AudioInputStream ais = AudioSystem.getAudioInputStream(soundUrl[soundID]);
            clip = AudioSystem.getClip();
            clip.open(ais);

            volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            adjustVolume();

        } catch (Exception e) {
            System.err.println("Failed to load sound:\n" + e.getMessage());
        }
    }

    /**
     * Plays currently loaded song
     */
    public void play() {
        if (clip != null) {
            clip.start();
        }
    }

    /**
     * Continuously loops currently loaded song
     */
    public void loop() {
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    /**
     * Stops currently playing song
     */
    public void stop() {
        if (clip != null) {
            clip.stop();
        }
    }

    /**
     * Adjust the audio volume based on the current volume scale.
     */
    public void adjustVolume() {
        if (volumeScale >= 0 && volumeScale < VOLUME_LEVELS.length) {
            volume = VOLUME_LEVELS[volumeScale];
            volumeControl.setValue(volume);
        } else {
            throw new IllegalArgumentException("Invalid volume scale: " + volumeScale);
        }
    }

    /**
     * Decrease the volume scale by one, if not already at the minimum.
     */
    public void decreaseVolumeScale() {
        if (volumeScale > 0) {
            volumeScale--;
        }
    }

    /**
     * Increase the volume scale by one, if not already at the maximum.
     */
    public void increaseVolumeScale() {
        if (volumeScale < VOLUME_LEVELS.length - 1) {
            volumeScale++;
        }
    }


    /* --------------- [GETTER METHODS] --------------- */

    public int getVolumeScale() {
        return volumeScale;
    }

    /* ------------------------------------------------ */
}
