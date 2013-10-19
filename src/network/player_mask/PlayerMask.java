package network.player_mask;

import message.Message;
import message.PlayerMessage;

public abstract class PlayerMask {

	public abstract PlayerMessage getInput();

	public abstract void feedChange(Message m);

	public abstract void begin();

	public abstract void stop();

}
