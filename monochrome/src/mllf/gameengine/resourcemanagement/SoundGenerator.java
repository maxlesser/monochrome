package mllf.gameengine.resourcemanagement;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundGenerator {

	public SoundGenerator() {

	}
	
	public static void playClip(final String clipPath, final float volume) {
		File file = new File(clipPath);
		if (file.exists()) {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        Clip clip = AudioSystem.getClip();
                        clip.open(AudioSystem.getAudioInputStream(new File(clipPath)));
                        FloatControl gainControl =
                                (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                        if(volume >= -80)
                        {
                            gainControl.setValue(volume);
                            clip.start();
                        }
                    } catch (LineUnavailableException | IOException
                            | UnsupportedAudioFileException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
		}
        else
        {
            System.out.println("No such file: " + file.getName());
        }
	}

}
