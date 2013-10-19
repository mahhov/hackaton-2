package player;

import map.Map;
import map.MapLoader;
import message.AddBlock;
import message.AddParticle;
import message.Heartbeat;
import message.Init;
import message.Message;
import message.MoveBlock;
import message.MovePlayer;
import message.PlayerMessage;
import message.Ready;
import message.RemoveBlock;
import message.SetStage;
import message.UpdatePoints;
import network.Util;
import resource.MusicPlayer;
import world.Battle;
import world.World;
import world.block.Block;
import world.particle.Particle;
import draw.elements.MassText;
import draw.painter.Painter;
import draw.painter.PainterJava;

public abstract class Player implements Runnable {

	// privative some of the variables

	protected byte color;
	protected byte commandUse; // -2 = just released, -1 = released, -3
								// pressed, >0 changed to, -4 for first init

	protected int curFrame;

	Map map;
	Painter painter;
	GameControl control;
	Camera camera;
	Stats stats;

	MusicPlayer music;
	private boolean visible;
	private String opponent;

	boolean send;
	PlayerMessage message;

	private byte stage;
	private long lastHeartbeat;
	private static final short MAX_HEARTBEAT = 1500;
	private static final short HEARTBEAT_SEND_TIME = 100;
	private short heartbeatSendTime;

	public Player(byte color, boolean music, boolean sound, boolean visible,
			String opponent, int x, int y) {
		this.color = color;
		commandUse = -1;
		stats = new Stats(color);
		this.opponent = opponent;

		if (visible) {
			control = new GameControl(Painter.FRAME_SIZE);
			painter = new PainterJava(control, x, y); // PIXEL!
		}
		this.music = new MusicPlayer(!music, !sound);
		this.visible = visible;

		curFrame = Integer.MIN_VALUE + 2;
	}

	public PlayerMessage getInputMessage() {
		heartbeatSendTime--;
		if (send && message != null) { // can message be null when send is true?
			send = false;
			return message;
		}
		if (heartbeatSendTime <= 0) { // should heartbeat get higher priority
			heartbeatSendTime = HEARTBEAT_SEND_TIME;
			return new Heartbeat(Heartbeat.GOOD);
		}
		return null;
	}

	public void recieveChange(Message change) {
		switch (change.getCode()) {
			case Init.CODE:
				recieveChange((Init) change);
				break;
			case Heartbeat.CODE:
				recieveChange((Heartbeat) change);
				break;
			case SetStage.CODE:
				recieveChange((SetStage) change);
				break;

			case AddBlock.CODE:
				recieveChange((AddBlock) change);
				break;
			case MoveBlock.CODE:
				recieveChange((MoveBlock) change);
				break;
			case RemoveBlock.CODE:
				recieveChange((RemoveBlock) change);
				break;

			case AddParticle.CODE:
				recieveChange((AddParticle) change);
				break;

			case MovePlayer.CODE:
				recieveChange((MovePlayer) change);
				break;
			case UpdatePoints.CODE:
				recieveChange((UpdatePoints) change);
				break;

			default:
				System.out.println("message not recognized by Player "
						+ change.getCode());
		}
	}

	void recieveChange(Init change) {
		Battle.setDimensions(change.width, change.height);
		map = new Map(new MapLoader(change.width, change.height,
				change.mapData, change.spawnData));
		camera = new Camera(map.getPlayer(color));
	}

	void recieveChange(Heartbeat change) {
		if (visible) {
			if (change.status == Heartbeat.BAD)
				painter.write("other player has connection problems", 1);
			else
				painter.write("", 1);
		}
		lastHeartbeat = System.currentTimeMillis();
	}

	void recieveChange(SetStage change) {
		stage = change.stage;
	}

	void recieveChange(AddBlock change) {
		Block block = Block.createBlock(change.id, change.blockCode, change.x,
				change.y);
		map.addBlock(block, change.x, change.y);
	}

	void recieveChange(MoveBlock change) {
		map.moveBlock(change.id, change.oldx, change.oldy, change.x, change.y);
	}

	void recieveChange(RemoveBlock change) {
		map.removeBlock(change.id, change.x, change.y);
	}

	void recieveChange(AddParticle change) {
		Particle pa;
		for (byte i = 0; i < change.count; i++) {
			pa = Particle.createParticle(change.particleCode, change.x,
					change.y, change.op, curFrame);
			map.addParticle(pa);
		}
	}

	void recieveChange(MovePlayer change) {
		map.setPlayer(change.playerId, change.x, change.y, change.state,
				change.dir, change.life);
	}

	void recieveChange(UpdatePoints change) {
		stats.points = change.points[color];
		stats.enemyPoints = change.points[Battle.getOtherColor(color)];
	}

	public void run() {
		lastHeartbeat = System.currentTimeMillis();
		if (visible)
			while (stage != World.STOP)
				switch (stage) {
					case World.HELP:
						helpStage();
						break;
					case World.BATTLE:
						battleStage();
						break;
					case World.DISCONNECTED:
						painter.write("connection lost", 1);
						painter.paint();
						break;
				}
		else {
			while (stage != World.STOP)
				switch (stage) {
					case World.HELP:
						invisibleHelpStage();
						break;
					case World.BATTLE:
						invisibleBattleStage();
						break;
					case World.DISCONNECTED:
						stage = World.STOP;
				}
		}
	}

	void helpStage() {
		painter.pause();
		painter.write(Battle.getColorString(color) + " Team", 0);
		painter.write("Press Escape to toggle window frame", 1);
		painter.write("Vs. " + opponent, 2);
		painter.write("Press Enter When Ready", 3);
		String[] text = new String[8];
		text[0] = "<-- place instructions here -->";
		text[1] = "<-- place instructions here -->";
		text[2] = "<-- place instructions here -->";
		text[3] = "<-- place instructions here -->";
		text[4] = "<-- place instructions here -->";
		text[5] = "<-- place instructions here -->";
		text[6] = "<-- place instructions here -->";
		text[7] = "<-- place instructions here -->";
		MassText drawText = new MassText(text);
		while (stage == World.HELP) {
			disconnected();
			if (control.getKeyState(GameControl.KEY_ESC) == GameControl.PRESS)
				painter.pause();
			if (control.getKeyState(GameControl.KEY_ENTER) == GameControl.PRESS) {
				send = true;
				message = new Ready();
				painter.write("Waiting for Response", 3);
			}
			painter.addForeground(drawText);
			painter.paint();
			Util.sleep(10);
		}
		painter.write("", 1);
		painter.write("", 2);
		painter.write("", 3);
		painter.unpause();
	}

	void invisibleHelpStage() {
		send = true;
		message = new Ready();
		Util.sleep(100);
		return;
	}

	private void battleStage() {
		painter.setFade(true);
		music.playBackroundMusic();
		long time;
		while (stage == World.BATTLE) {
			time = System.currentTimeMillis();
			disconnected();
			if (control.getKeyState(GameControl.KEY_ESC) == GameControl.PRESS)
				painter.pause();
			camera.update(control);
			updateCommand();
			paint();
			time = System.currentTimeMillis() - time;
			painter.write("ms: " + time, 4);
			Util.sleep(10);
		}
	}

	private void invisibleBattleStage() {
		while (stage == World.BATTLE) {
			disconnected();
			updateCommand();
			Util.sleep(10);
		}
	}

	private void disconnected() {
		if ((System.currentTimeMillis() - lastHeartbeat) > MAX_HEARTBEAT
				&& visible) {
			// stage = World.DISCONNECTED;
			painter.write("connection lost", 1);
		}
	}

	public void stop() {
		stage = World.STOP;
	}

	abstract void updateCommand();

	void paint() {
		curFrame++;
		map.paint(curFrame, camera, painter);
		stats.paint(painter);
		painter.paint();
	}
}
