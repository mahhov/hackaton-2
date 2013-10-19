package network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

class Broadcaster {

	static final String BROADCAST_IP = "224.0.172.46";
	static final int BROADCAST_PORT = 130;
	static final byte[] BROADCAST_CODE = {57, 48, 2};
	static final byte USERNAME_LENGTH = 10;
	static final byte BROADCAST_MESSAGE_LENGTH = (byte) (BROADCAST_CODE.length
			+ 4 + USERNAME_LENGTH + 1);

	private byte[] broadcastMessage;

	private DatagramSocket broadcastSocket;
	private DatagramPacket broadcastPacket;

	public Broadcaster(int lobbyPort, String username) {
		try {
			InetAddress group = InetAddress.getByName(BROADCAST_IP);
			broadcastSocket = new DatagramSocket(0);
			setBroadcastMessage(lobbyPort, username);
			broadcastPacket = new DatagramPacket(broadcastMessage,
					broadcastMessage.length, group, BROADCAST_PORT);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void setBroadcastMessage(int lobbyPort, String username) {
		broadcastMessage = new byte[BROADCAST_MESSAGE_LENGTH];
		for (byte i = 0; i < BROADCAST_CODE.length; i++)
			broadcastMessage[i] = BROADCAST_CODE[i];

		byte start = (byte) BROADCAST_CODE.length;
		byte[] m = Util.intToByte(lobbyPort);
		for (byte i = 0; i < 4; i++)
			broadcastMessage[start + i] = m[i];

		start += 4;
		m = Util.stringToByte(username, USERNAME_LENGTH);
		for (byte i = 0; i < USERNAME_LENGTH; i++)
			broadcastMessage[start + i] = m[i];
	}

	void broadcast(boolean hosting) {
		try {
			broadcastMessage[BROADCAST_CODE.length + 4 + USERNAME_LENGTH] = (byte) (hosting
					? 1
					: 0);
			broadcastSocket.send(broadcastPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
