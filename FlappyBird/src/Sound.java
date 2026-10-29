import javax.sound.sampled.*;
import java.io.*;
import java.net.URL;

public class Sound {
    private Clip themeSong;
    private Clip flapClip;
    private Clip failClip;
    private Clip scoreClip;
    private Clip every5Clip;

    public Sound() {
        themeSong = loadClip("theme_song.wav");
        flapClip = loadClip("flapping_wings.WAV");
        failClip = loadClip("fail.wav");
        scoreClip = loadClip("score.WAV");
        every5Clip = loadClip("every_5.wav");
        setThemeSongVolume(-18.0f);
    }

    private Clip loadClip(String fileName) {
        try {
            URL url = getClass().getResource("assets/sound/" + fileName);
            if (url == null) {
                url = getClass().getResource("/assets/sound/" + fileName);
            }
            if (url == null && getClass().getClassLoader() != null) {
                url = getClass().getClassLoader().getResource("assets/sound/" + fileName);
            }
            if (url == null) {
                File file = new File("src/assets/sound/" + fileName);
                if (file.exists()) {
                    url = file.toURI().toURL();
                } else {
                    File file2 = new File("assets/sound/" + fileName);
                    if (file2.exists()) {
                        url = file2.toURI().toURL();
                    }
                }
            }

            if (url == null) {
                System.err.println("Sound file not found: " + fileName);
                return null;
            }

            AudioInputStream ais = AudioSystem.getAudioInputStream(url);
            AudioFormat format = ais.getFormat();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = ais.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            byte[] audioData = baos.toByteArray();
            ais.close();

            AudioInputStream fixedStream = new AudioInputStream(
                new ByteArrayInputStream(audioData),
                format,
                audioData.length / format.getFrameSize()
            );
            Clip clip = AudioSystem.getClip();
            clip.open(fixedStream);
            return clip;
        } catch (Exception e) {
            System.err.println("Error loading sound " + fileName + ": " + e.getMessage());
            return null;
        }
    }

    public void setThemeSongVolume(float volumeDb) {
        setVolume(themeSong, volumeDb);
    }

    // Helper untuk mengatur volume clip
    public void setVolume(Clip clip, float volumeDb) {
        if (clip != null && clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            try {
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                float min = gainControl.getMinimum();
                float max = gainControl.getMaximum();
                float target = Math.max(min, Math.min(max, volumeDb));
                gainControl.setValue(target);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Theme song diputar berulang
    public void playThemeSong() {
        if (themeSong != null) {
            try {
                themeSong.stop();
                themeSong.setFramePosition(0);
                themeSong.loop(Clip.LOOP_CONTINUOUSLY);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Menghentikan theme song saat fail
    public void stopThemeSong() {
        if (themeSong != null) {
            try {
                if (themeSong.isRunning()) {
                    themeSong.stop();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Sound effect untuk setiap jump pada flapping_wings
    public void playFlap() {
        playSound(flapClip);
    }

    // Sound effect fail saat game over
    public void playFail() {
        playSound(failClip);
    }

    // Sound effect skor biasa
    public void playScore() {
        playSound(scoreClip);
    }

    // Sound effect setiap skor 5 / kelipatan 5
    public void playEvery5() {
        playSound(every5Clip);
    }

    private void playSound(Clip clip) {
        if (clip != null) {
            try {
                if (clip.isRunning()) {
                    clip.stop();
                }
                clip.setFramePosition(0);
                clip.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Clip getThemeSong() {
        return themeSong;
    }

    public Clip getFlapClip() {
        return flapClip;
    }

    public Clip getFailClip() {
        return failClip;
    }

    public Clip getScoreClip() {
        return scoreClip;
    }

    public Clip getEvery5Clip() {
        return every5Clip;
    }
}
