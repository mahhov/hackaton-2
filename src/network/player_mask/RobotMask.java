package network.player_mask;

import message.Message;
import message.PlayerMessage;
import network.Lobby;
import player.Robot;
import world.Battle;

public class RobotMask extends PlayerMask {

	Robot robot;
	String colorString;

	// invisible
	public RobotMask(byte color) {
		colorString = Battle.getColorString(color);
		robot = new Robot(color, false, false, false, 0, 0);
	}

	// visible
	public RobotMask(byte color, int x, int y, boolean[] settings) {
		colorString = Battle.getColorString(color);
		robot = new Robot(color, settings[Lobby.SETTING_MUSIC],
				settings[Lobby.SETTING_SOUND], true, x, y);
	}

	public PlayerMessage getInput() {
		return robot.getInputMessage();
	}

	public void feedChange(Message m) {
		robot.recieveChange(m);
	}

	public void begin() {
		Thread thread = new Thread(robot);
		thread.setName("Robot " + colorString);
		thread.start();
	}

	public void stop() {
		robot.stop();
	}

}
