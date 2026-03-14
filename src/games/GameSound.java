package games;

import java.io.File;
import javax.sound.sampled.*;

public class GameSound implements Runnable{		
	protected static File welcomeFrameSound;
	protected static File mainGameSound;
	protected static File lowFuelSound;
	protected static File thrusterSound;
	protected static File crashSound;
	protected static File successSound;
	protected static Clip welcomeFrameSoundClip;
	protected static Clip mainGameSoundClip;
	protected static Clip lowFuelSoundClip;
	protected static Clip thrusterSoundClip;
	protected static Clip crashSoundClip;
	protected static Clip successSoundClip;
	protected AudioInputStream welcomeFrameAudioInputStream;
	protected AudioInputStream mainGameSoundAudioInputStream;
	protected AudioInputStream lowFuelSoundAudioInputStream;
	protected AudioInputStream thrusterSoundAudioInputStream;
	protected AudioInputStream crashSoundAudioInputStream;
	protected AudioInputStream successSoundAudioInputStream;
	protected static FloatControl gainControl;
	
	public GameSound() {
		welcomeFrameSound = new File("sound\\welcomeFrameSound.wav");
		mainGameSound = new File("sound\\mainGameSound.wav");
		lowFuelSound = new File("sound\\lowFuelSound.wav");
		thrusterSound = new File("sound\\thrusterSound.wav");
		crashSound = new File("sound\\crashSound.wav");
		successSound = new File("sound\\successSound.wav");
	}
	@Override
	public void run() {
		try {
			welcomeFrameAudioInputStream = AudioSystem.getAudioInputStream(welcomeFrameSound);
			welcomeFrameSoundClip = AudioSystem.getClip();
			welcomeFrameSoundClip.open(welcomeFrameAudioInputStream);
			mainGameSoundAudioInputStream = AudioSystem.getAudioInputStream(mainGameSound);
			mainGameSoundClip = AudioSystem.getClip();
			mainGameSoundClip.open(mainGameSoundAudioInputStream);
			lowFuelSoundAudioInputStream = AudioSystem.getAudioInputStream(lowFuelSound);
			lowFuelSoundClip = AudioSystem.getClip();
			lowFuelSoundClip.open(lowFuelSoundAudioInputStream);
			thrusterSoundAudioInputStream = AudioSystem.getAudioInputStream(thrusterSound);
			thrusterSoundClip = AudioSystem.getClip();
			thrusterSoundClip.open(thrusterSoundAudioInputStream);
			crashSoundAudioInputStream = AudioSystem.getAudioInputStream(crashSound);
			crashSoundClip = AudioSystem.getClip();
			crashSoundClip.open(crashSoundAudioInputStream);
			successSoundAudioInputStream = AudioSystem.getAudioInputStream(successSound);
			successSoundClip = AudioSystem.getClip();
			successSoundClip.open(successSoundAudioInputStream);
			gainControl = (FloatControl)welcomeFrameSoundClip.getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue(-5.0f);
			welcomeFrameSoundClip.loop(Clip.LOOP_CONTINUOUSLY);		
			gainControl = (FloatControl)mainGameSoundClip.getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue(-10.0f);
			gainControl = (FloatControl)lowFuelSoundClip.getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue(-15.0f);
			gainControl = (FloatControl)thrusterSoundClip.getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue(-5.0f);
			gainControl = (FloatControl)crashSoundClip.getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue(-5.0f);
			gainControl = (FloatControl)successSoundClip.getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue(-5.0f);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
