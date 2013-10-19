package player;

import map.MiniPlayer;
import message.MovePlayer;
import message.SetPlayerControl;
import world.MathUtil;

public class Human extends Player {

	Panel panel;

	public Human(byte color, boolean music, boolean sound, String opponent,
			int x, int y) {
		super(color, music, sound, true, opponent, x, y);
		panel = new Panel();
		commandUse = -4;
	}

	void recieveChange(MovePlayer change) {
		super.recieveChange(change);
		switch (change.command) {
			case MiniPlayer.LAZOR:
				panel.setBar(1 - 1f * change.commandReload
						/ MiniPlayer.LASER_RELOAD_TIME);
				break;
			case MiniPlayer.ROCKET:
				panel.setBar(1 - 1f * change.commandReload
						/ MiniPlayer.ROCKET_RELOAD_TIME);
				break;
		}
	}

	void updateCommand() {
		// boolean ctrl = GameControl.isDown(control
		// .getKeyState(GameControl.KEY_CTRL));
		// boolean shift = GameControl.isDown(control
		// .getKeyState(GameControl.KEY_SHIFT));

		// keyboard wasd
		int upState = control.getKeyState(GameControl.KEY_W);
		boolean up = GameControl.isDown(upState);
		boolean upRelease = GameControl.RELEASE == upState;
		boolean down = GameControl.isDown(control
				.getKeyState(GameControl.KEY_S));
		boolean left = GameControl.isDown(control
				.getKeyState(GameControl.KEY_A));
		boolean right = GameControl.isDown(control
				.getKeyState(GameControl.KEY_D));

		// mouse click/release
		if (commandUse == -2)
			commandUse = -1;

		float mousex = control.getMouseX();
		float mousey = control.getMouseY();
		float[] mouseCur = MathUtil.inBoundsFloat(camera.frameToFloatCoord(
				mousex, mousey));

		int leftMouseState = control.getMouseState(GameControl.LEFT);
		if (GameControl.isDown(leftMouseState))
			commandUse = -3;
		else if (commandUse == -3 && GameControl.isUp(leftMouseState))
			commandUse = -2;

		// panel highlight
		byte oldhighlight = panel.getHighlight();
		byte numKey = getNumkeyDown();
		if (numKey != -1)
			panel.setHighlight((byte) (numKey - 1));
		else if (control.getKeyState(GameControl.KEY_Q) == Control.PRESS)
			panel.moveHighlight((byte) -1);
		else if (control.getKeyState(GameControl.KEY_E) == Control.PRESS)
			panel.moveHighlight((byte) 1);
		if (commandUse == -4 || oldhighlight != panel.getHighlight())
			commandUse = panel.getHighlight();

		if (left || right || up || down || upRelease || commandUse != -1) {
			message = new SetPlayerControl(color, commandUse, mouseCur[0],
					mouseCur[1], up, down, right, left);
			send = true;
		}
	}

	private byte getNumkeyDown() {
		if (GameControl.isDown(control.getKeyState(GameControl.KEY_1)))
			return 1;
		if (GameControl.isDown(control.getKeyState(GameControl.KEY_2)))
			return 2;
		if (GameControl.isDown(control.getKeyState(GameControl.KEY_3)))
			return 3;
		if (GameControl.isDown(control.getKeyState(GameControl.KEY_4)))
			return 4;
		if (GameControl.isDown(control.getKeyState(GameControl.KEY_5)))
			return 5;
		if (GameControl.isDown(control.getKeyState(GameControl.KEY_6)))
			return 6;
		if (GameControl.isDown(control.getKeyState(GameControl.KEY_7)))
			return 7;
		if (GameControl.isDown(control.getKeyState(GameControl.KEY_8)))
			return 8;
		if (GameControl.isDown(control.getKeyState(GameControl.KEY_9)))
			return 9;
		if (GameControl.isDown(control.getKeyState(GameControl.KEY_0)))
			return 0;
		return -1;
	}

	void paint() {
		curFrame++;
		map.paint(curFrame, camera, painter);
		panel.paint(painter);
		stats.paint(painter);
		painter.paint();
	}

}
