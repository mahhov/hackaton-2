package resource;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import network.Util;
import world.MathUtil;

public class Music {

	public static final Music BGMUSIC = new Music("nightwishTaikatalvi",
			650000, 7230000, true, -10, 0);
	public static final Music ROCKET = new Music("rocket", 40000, 100000,
			false, -13, 0);
	public static final Music RAIL = new Music("machinegun", 12000, 40000,
			false, -21, 0);
	public static final Music RAY = new Music("laserGunCannon", 0, 50000,
			false, -19, 0);
	public static final Music SMALL_SHELL = new Music("laserPulseShot", 12000,
			38000, false, -20, 0);
	public static final Music BIG_SHELL = new Music("grenadeLaunch", 0, 65000,
			false, -17, 0);
	public static final Music UNIT_DESTROY = new Music("robotSmash", 0, 78000,
			false, -18, 0);
	public static final Music UNIT_CREATE = new Music("robotActivate", 0,
			41400, false, -11, 0);
	public static final Music PROMPT_COMMAND = new Music("commander", 0, 37000,
			false, -12, 0);
	public static final Music CONFIRM_COMMAND = new Music("yesMaster", 0,
			50000, false, -11, 0);
	// keep volume between -25 and 0 (except the background music)

	File file;
	int start, end;
	boolean loop;
	float volume, balance;

	// volume from -80 to 6 inclusive
	// balance from -1 to 1 inclusive

	public static void main(String[] arg) {
		Music test = CONFIRM_COMMAND;
		System.out.println(test.getClip().getFrameLength());
		test.start = 000;
		test.end = 50000;
		test.loop = true;
		test.play(0, 0);
		Util.sleep(6000);
		// while (true) {
		// for (int i = 0; i < 100; i++) {
		// if (Math.random() > 1.8)
		// test.play((float) (Math.random() * 20 - 14),
		// (float) (Math.random() * 2 - 1));
		// Util.sleep(100);
		// }
	}

	Music(String wav, int start, int end, boolean loop, float volume,
			float balance) {
		file = new File("sounds/" + wav + ".wav");
		this.start = start;
		this.end = end;
		this.loop = loop;
		this.volume = volume;
		this.balance = balance;
	}

	private Clip getClip() {
		Clip clip = null;
		try {
			AudioInputStream audio = AudioSystem.getAudioInputStream(file);
			AudioFormat format = audio.getFormat();
			if (format.getChannels() == 1) {
				format = new AudioFormat(format.getSampleRate(),
						format.getSampleSizeInBits(), 2, true, false);
				audio = AudioSystem.getAudioInputStream(format, audio);
			}
			clip = AudioSystem.getClip();
			clip.open(audio);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		}
		return clip;
	}

	public void play() {
		play(0, 0);
	}

	public void play(short dx, short dy) {
		short d = MathUtil.distance(dx, dy);
		if (d < 50)
			d = 50;
		float volume = (float) (50 - Math.log(d * d) * 5);
		if (volume < -55)
			volume = -55;
		float balance = dx / 350f;
		if (balance > 1)
			balance = 1;
		if (balance < -1)
			balance = -1;
		play(volume, balance);
	}

	private void play(float volume, float balance) {
		Clip clip = getClip();

		((FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN))
				.setValue(volume + this.volume);
		((FloatControl) clip.getControl(FloatControl.Type.PAN))
				.setValue(balance + this.balance);

		if (loop) {
			clip.setFramePosition(start);
			clip.setLoopPoints(start, end);
			clip.loop(Clip.LOOP_CONTINUOUSLY);
		} else {
			clip.setFramePosition(start);
			clip.start();
		}
	}
}
