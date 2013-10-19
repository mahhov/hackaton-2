package world;

import list.LinkedList;
import list.LinkedList.Node;
import message.Heartbeat;
import message.Message;
import message.PlayerMessage;
import network.Util;
import network.player_mask.PlayerMask;

public class Game {
	
	// handles world and players and connections and heartbeat and communications

	private World world;
	private PlayerMask player1;
	private PlayerMask player2;

	private LinkedList changes;

	private boolean running = true;

	private long p1LastHeartbeat, p2LastHeartbeat;
	private boolean p1Disconnected, p2Disconnected;
	private static final short MAX_HEARTBEAT = 1500;

	public Game(PlayerMask player1, PlayerMask player2) {
		this.player1 = player1;
		this.player2 = player2;
	}

	public void begin() {
		world = new World();

		player1.begin();
		player2.begin();

		p1LastHeartbeat = System.currentTimeMillis();
		p1LastHeartbeat = System.currentTimeMillis();

		running = true;
		while (running) {
			updateWorld();
			sendChanges();
			Util.sleep(10);
		}

		player1.stop();
		player2.stop();
	}

	void stop() {
		running = false;
	}

	void updateWorld() {
		PlayerMessage player1Input = player1.getInput();
		PlayerMessage player2Input = player2.getInput();
		long time = System.currentTimeMillis();
		if (player1Input != null && player1Input.getCode() == Heartbeat.CODE) {
			p1LastHeartbeat = time;
			p1Disconnected = false;
			player1Input = null;
		} else if (time - p1LastHeartbeat > MAX_HEARTBEAT)
			p1Disconnected = true;
		if (player2Input != null && player2Input.getCode() == Heartbeat.CODE) {
			p2LastHeartbeat = time;
			p2Disconnected = false;
			player2Input = null;
		} else if (time - p2LastHeartbeat > MAX_HEARTBEAT)
			p2Disconnected = true;

		byte disconnectedStatus;
		if (p1Disconnected || p2Disconnected)
			disconnectedStatus = Heartbeat.BAD;
		else
			disconnectedStatus = Heartbeat.GOOD;

		changes = world.update(player1Input, player2Input);
		changes.add(new Heartbeat(disconnectedStatus));
	}

	void sendChanges() {
		for (Node n : changes) {
			Message c = (Message) n.item;
			player1.feedChange(c);
			player2.feedChange(c);
		}

	}

}
