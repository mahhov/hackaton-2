package network;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import network.player_mask.ClientHuman;
import network.player_mask.LocalHumanMask;
import network.player_mask.PlayerMask;
import network.player_mask.RemoteHumanMask;
import network.player_mask.RobotMask;
import player.TypingControl;
import world.Battle;
import world.Game;
import world.MathUtil;
import draw.elements.ActionButton;
import draw.elements.ActionList;
import draw.elements.CenteredText;
import draw.manage.Manager;
import draw.painter.Painter;
import draw.painter.PainterJava;

public class Lobby {

	private Broadcaster broadcaster;
	private Listener listener;
	private ServerSocket serverSocket;
	private Painter painter;
	private String username;

	private boolean[] settings;
	public static final byte SETTING_MUSIC = 0, SETTING_SOUND = 1;

	private byte state;
	private static final byte LOGIN = 0, FIND_GAME = 1, EXIT = -1;

	public static void main(String[] args) {
		// turnOnLogging();
		new Lobby();
	}

	private Lobby() {
		state = 0;
		settings = new boolean[] { true, true };
		settings = new boolean[2];
		TypingControl control = new TypingControl(Painter.FRAME_SIZE);
		painter = new PainterJava(control);
		painter.pause();
		while (state != EXIT) {
			switch (state) {
				case LOGIN:
					username = inputUsername(control);
					state = FIND_GAME;
					break;
				case FIND_GAME:
					beginListening();
					findGame(control);
					stopListening();
					break;
				default:
					state = EXIT;
			}
		}
	}

	@SuppressWarnings("unused")
	private static void turnOnLogging() {
		try {
			PrintStream out = new PrintStream(new FileOutputStream("log.txt"));
			System.setOut(out);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private String inputUsername(TypingControl control) {
		Manager manager = new Manager(.17f);
		manager.divideVertical(.14f, .2f, .30f, .34f, .39f, .40f, .45f, .93f,
				.98f);

		manager.add(new CenteredText("Watermellon", (byte) 35, 0, 0, 0, 0, true,
				false), true);
		manager.skip();

		manager.add(new CenteredText("Please enter login username (max length "
				+ Broadcaster.USERNAME_LENGTH + ")", (byte) 20, 0, 0, 0, 0,
				true, false), true);
		CenteredText input = new CenteredText("", (byte) 20, 0, 0, 0, 0, false,
				true);
		manager.add(input, true);

		manager.skip();
		ActionButton random = new ActionButton("Random", (byte) 15, 0, 0, .1f,
				0);
		ActionButton login = new ActionButton("Login", (byte) 15, 0, 0, .1f, 0);
		manager.addToRow(true, .05f, (byte) 2, random, login);

		manager.skip();
		manager.add(
				new CenteredText(
						"Multiplayer was only tested on LAN, so don't be surprised if it breaks",
						(byte) 12, 0, 0, 0, 0, true, false), true);

		char username[] = new char[Broadcaster.USERNAME_LENGTH];
		byte i = 0;
		while (state == LOGIN) {
			manager.mouse(control);

			if (random.isPressed())
				return "r4NdOM_" + (byte) (Math.random() * 100);

			if (i > 0) {
				if (control.getBackspace())
					username[--i] = 0;
				if (control.getDelete())
					for (; i > 0;)
						username[--i] = 0;
				if (control.getEnter() || login.isPressed())
					return new String(username);
			}
			control.resetToggleKeys();
			char key = control.getKey();
			if (i < Broadcaster.USERNAME_LENGTH && key != 0)
				username[i++] = key;
			input.setText(": " + new String(username));

			manager.addToDraw(painter);
			painter.paint();
			Util.sleep(50);
		}

		return "ERROR_";
	}

	private void beginListening() {
		listener = new Listener();
		new Thread(listener).start();
	}

	private void stopListening() {
		listener.stop();
		listener = null;
	}

	private void findGame(TypingControl control) {
		try {
			serverSocket = new ServerSocket(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		broadcaster = new Broadcaster(serverSocket.getLocalPort(), username);

		Manager manager = new Manager(.09f);
		manager.divideVertical(.10f, .14f, .89f, .87f, .91f, .96f);
		CenteredText lobby = new CenteredText("Lobby", (byte) 30, 0, 0, .15f,
				0, false, false);
		CenteredText name = new CenteredText(username.trim(), (byte) 20, 0, 0,
				-1, 0, false, false);
		ActionButton logout = new ActionButton("X", (byte) 12, 0, 0, .02f, .02f);
		manager.addToRow(true, 0, (byte) 2, lobby, name, logout);

		ActionList list = new ActionList(new String[0], (byte) 15, .05f, .03f,
				0, 0, 0, 0, (byte) 2, Color.WHITE, Color.LIGHT_GRAY,
				Color.DARK_GRAY, true);
		CenteredText playerCount = new CenteredText("Players in Lobby: 0 ",
				(byte) 12, 0, 0, -1, .02f, false, false);
		manager.add(list, true);
		manager.skip();
		manager.addToRow(false, 0, (byte) 1, playerCount);

		ActionButton join = new ActionButton("Join Game", (byte) 12, 0, 0, .1f,
				0);
		ActionButton host = new ActionButton("Host Game", (byte) 12, 0, 0, .1f,
				0);
		ActionButton singlePlayer = new ActionButton("Single Player",
				(byte) 12, 0, 0, .13f, 0);
		ActionButton zeroPlayer = new ActionButton("Watch Bot v. Bot",
				(byte) 12, 0, 0, .16f, 0);
		ActionButton settings = new ActionButton("Help & Settings", (byte) 12,
				0, 0, .15f, 0);
		manager.addToRow(true, .02f, (byte) 1, join, host, singlePlayer,
				zeroPlayer, settings);

		while (state == FIND_GAME) {
			broadcaster.broadcast(false);

			playerCount.setText("Players in Lobby: "
					+ listener.getPlayerCount());
			Partner selectedGame = listener
					.findPartnerFromI(list.getSelected());
			String names[] = listener.getGameNames(selectedGame);
			byte i = listener.findI();
			list.resetText(names, i);
			manager.mouse(control);

			if (logout.isPressed()) {
				state = LOGIN;
			}
			if (selectedGame != null && join.isPressed()) {
				joinGame(selectedGame);
				beginListening();
			}
			if (host.isPressed()) {
				hostGame(control);
				beginListening();
			}
			if (singlePlayer.isPressed()) {
				startSinglePlayer();
				beginListening();
			}
			if (zeroPlayer.isPressed()) {
				startZeroPlayer();
				beginListening();
			}
			if (settings.isPressed()) {
				settings(control);
				beginListening();
			}

			manager.addToDraw(painter);
			painter.paint();
			Util.sleep(50);
		}
	}

	private void joinGame(Partner partner) {
		System.out.println("joining multiplayer");
		stopListening();
		painter.setVisible(false);
		try {
			Socket socket = new Socket(partner.ip, partner.port);
			ObjectOutputStream out = new ObjectOutputStream(
					socket.getOutputStream());
			ObjectInputStream in = new ObjectInputStream(
					socket.getInputStream());
			out.writeChars(username);
			out.flush();
			ClientHuman p1 = new ClientHuman(Battle.BLUE, out, in,
					partner.name, painter.getX(), painter.getY(), settings);
			p1.begin();
			while (p1.running) {
				p1.update();
				Util.sleep(10);
			}
			p1.stop();
			in.close();
			out.close();
			socket.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void startSinglePlayer() {
		System.out.println("single player");
		stopListening();
		painter.setVisible(false);
		PlayerMask p1 = new LocalHumanMask(Battle.RED, "SuperBot3000",
				painter.getX(), painter.getY(), settings);
		PlayerMask p2 = new RobotMask(Battle.BLUE);
		new Game(p1, p2).begin();
	}

	private void startZeroPlayer() {
		System.out.println("zero player");
		stopListening();
		painter.setVisible(false);
		PlayerMask p1 = new RobotMask(Battle.RED, painter.getX(),
				painter.getY(), settings);
		PlayerMask p2 = new RobotMask(Battle.BLUE);
		new Game(p1, p2).begin();
	}

	private void hostGame(TypingControl control) {
		stopListening();

		HostConnector hostConnector = new HostConnector();
		hostConnector.connectToClient(serverSocket);

		CenteredText text = new CenteredText("Waiting for Player to join ...",
				(byte) 20, .1f, .1f, .8f, .2f, true, false);
		ActionButton back = new ActionButton("Cancel", (byte) 15, .45f, .9f,
				.1f, .03f);
		while (!hostConnector.connected()) {
			broadcaster.broadcast(true);
			for (byte i = 0; i < 1; i++) {
				back.mouseOver(control.getMouseX(), control.getMouseY());
				if (control.getMousePress())
					back.mousePress();
				else if (control.getMouseRelease())
					back.mouseRelease();

				if (back.isPressed()) {
					hostConnector.stop();
					return;
				}

				painter.addForeground(text);
				painter.addForeground(back);
				painter.paint();
				Util.sleep(50);
			}
		}
		System.out.println("Hosting multiplayer");
		painter.setVisible(false);
		PlayerMask p1 = new LocalHumanMask(Battle.RED, hostConnector.username,
				painter.getX(), painter.getY(), settings);
		PlayerMask p2 = new RemoteHumanMask(hostConnector.clientOut,
				hostConnector.clientIn);
		new Game(p1, p2).begin();
	}

	private void settings(TypingControl control) {
		stopListening();
		Manager manager = new Manager(.39f);
		manager.divideVertical(.1f, .25f, .33f, .41f, .79f);

		manager.add(new CenteredText("Settings", (byte) 20, 0, 0, 0, 0, true,
				false), true);
		manager.skip();

		ActionButton music = new ActionButton("Music is "
				+ MathUtil.booleanToString(settings[SETTING_MUSIC]), (byte) 15,
				0, 0, .2f, .05f);
		ActionButton sound = new ActionButton("Sound is "
				+ MathUtil.booleanToString(settings[SETTING_SOUND]), (byte) 15,
				0, 0, .2f, .05f);
		manager.add(music, false);
		manager.add(sound, false);

		manager.divideHorizontal(.44f);
		ActionButton back = new ActionButton("Done", (byte) 15, 0, 0, .1f, .03f);
		manager.add(back, false);

		while (true) {
			for (byte i = 0; i < 100; i++) {
				manager.mouse(control);
				if (music.isPressed()) {
					music.mouseRelease();
					settings[SETTING_MUSIC] = !settings[SETTING_MUSIC];
					music.setText("Music is "
							+ MathUtil.booleanToString(settings[SETTING_MUSIC]));
				}
				if (sound.isPressed()) {
					sound.mouseRelease();
					settings[SETTING_SOUND] = !settings[SETTING_SOUND];
					sound.setText("Sound is "
							+ MathUtil.booleanToString(settings[SETTING_SOUND]));
				}
				if (back.isPressed()) {
					return;
				}

				manager.addToDraw(painter);
				painter.paint();
				Util.sleep(50);
			}
		}
	}
}
