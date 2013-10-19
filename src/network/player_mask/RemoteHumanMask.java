package network.player_mask;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import message.Message;
import message.Messager;
import message.PlayerMessage;

public class RemoteHumanMask extends PlayerMask {
	ObjectOutputStream out;
	ObjectInputStream in;

	public RemoteHumanMask(ObjectOutputStream out, ObjectInputStream in) {
		this.out = out;
		this.in = in;
	}

	public PlayerMessage getInput() {
		try {
			if (in.available() != 0)
				return (PlayerMessage) Messager.read(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void feedChange(Message m) {
		m.write(out);
	}

	public void begin() {
	}

	public void stop() {
	}

}
