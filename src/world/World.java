package world;

import list.LinkedList;
import message.PlayerMessage;
import message.Ready;
import message.SetStage;

public class World {
	
	// handles battle and initiation and future-implemented game stages
	
	private boolean init;
	private Battle battle;
	private boolean readyRed, readyBlue;

	public static final byte HELP = 0, BATTLE = 2, STOP = 3, DISCONNECTED = 4;

	public World() {
	}

	// return linked list of changes
	public LinkedList update(PlayerMessage redInput, PlayerMessage blueInput) {
		// INIT MESSAGE
		// first time init message to client and server
		if (!init) {
			return init(redInput, blueInput);
		}

		// UPDATE
		return battle.update(blueInput, redInput);
	}

	private LinkedList init(PlayerMessage clientInput, PlayerMessage serverInput) {
		LinkedList changes = new LinkedList();

		if (serverInput != null && serverInput.getCode() == Ready.CODE)
			readyRed = true;
		if (clientInput != null && clientInput.getCode() == Ready.CODE)
			readyBlue = true;

		if (readyRed && readyBlue) {
			init = true;
			battle = new Battle();
			changes.add(new SetStage(BATTLE));
			changes.add(battle.init());
		}

		return changes;
	}
}
