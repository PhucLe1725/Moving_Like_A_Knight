package movinglikeaknight;

import java.io.IOException;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class MusicPlayer {
    private Map<String, Clip> musicClips;

    public MusicPlayer() {
        musicClips = new HashMap<>();
    }

    public void loop(String filename) {
        try {
            File file = new File(System.getProperty("user.dir") + "\\data\\music\\" + filename);
            if (file.exists()) {
                if (!musicClips.containsKey(filename)) {
                    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
                    Clip musicClip = AudioSystem.getClip();
                    musicClip.open(audioInputStream);

                    FloatControl volume = (FloatControl) musicClip.getControl(FloatControl.Type.MASTER_GAIN);
                    volume.setValue(-40);

                    musicClips.put(filename, musicClip);
                }

                Clip clip = musicClips.get(filename);
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            } else {
                return;
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            return;
        }
    }

    public void play(String filename) {
        try {
            File file = new File("C:\\Users\\admin\\eclipse-workspace\\Moving_Like_A_Knight\\data\\music\\" + filename);
            if (file.exists()) {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
                Clip musicClip = AudioSystem.getClip();
                musicClip.open(audioInputStream);

                FloatControl volume = (FloatControl) musicClip.getControl(FloatControl.Type.MASTER_GAIN);
                volume.setValue(-30);

                musicClip.start();
            } else {
                return;
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            return;
        }
    }
    
    public void stop(String filename) {
        if (musicClips.containsKey(filename)) {
            Clip clip = musicClips.get(filename);
            if (clip.isRunning()) {
                clip.stop();
                clip.setFramePosition(0);
            }
        }
    }
}
