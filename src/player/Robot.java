package player;

public class Robot extends Player {

	public Robot(byte color, boolean music, boolean sound, boolean visible,
			int x, int y) {
		super(color, music, sound, visible, "", x, y);
	}

	void helpStage() {
		invisibleHelpStage();
	}

	void updateCommand() {
	}

}
