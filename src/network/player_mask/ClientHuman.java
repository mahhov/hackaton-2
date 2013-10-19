package network.player_mask;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import message.Message;
import message.Messager;
import network.Lobby;
import player.Human;
import world.Battle;

public class ClientHuman {
	ObjectOutputStream out;
	ObjectInputStream in;

	public boolean running;

	Human human;
	String colorString;

	public ClientHuman(byte color, ObjectOutputStream out,
			ObjectInputStream in, String opponent, int x, int y,
			boolean[] settings) {
		colorString = Battle.getColorString(color);
		human = new Human(color, settings[Lobby.SETTING_MUSIC],
				settings[Lobby.SETTING_SOUND], opponent, x, y);
		this.out = out;
		this.in = in;
	}

	public void update() {
		// get changes from server and feed to player
		try {
			while (in.available() != 0) {
				human.recieveChange(Messager.read(in));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// send client player input
		Message input = human.getInputMessage();
		if (input != null)
			input.write(out);
	}

	public void begin() {
		running = true;
		Thread thread = new Thread(human);
		thread.setName("Remote Human " + colorString);
		thread.start();
	}

	public void stop() {
		human.stop();
	}

}
