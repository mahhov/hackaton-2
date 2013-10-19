package network.player_mask;

import message.Message;
import message.PlayerMessage;
import network.Lobby;
import player.Human;
import world.Battle;

public class LocalHumanMask extends PlayerMask {

	Human human;
	String colorString;

	public LocalHumanMask(byte color, String opponent, int x, int y,
			boolean[] settings) {
		colorString = Battle.getColorString(color);
		human = new Human(color, settings[Lobby.SETTING_MUSIC],
				settings[Lobby.SETTING_SOUND], opponent, x, y);
	}

	public PlayerMessage getInput() {
		return human.getInputMessage();
	}

	public void feedChange(Message m) {
		human.recieveChange(m);
	}

	public void begin() {
		Thread thread = new Thread(human);
		thread.setName("Local Human " + colorString);
		thread.start();
	}

	public void stop() {
		human.stop();
	}

}
