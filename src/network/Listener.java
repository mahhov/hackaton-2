package network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import list.LinkedList;
import list.LinkedList.Node;

public class Listener implements Runnable {

	private boolean listen;

	private LinkedList partnerGames;
	private LinkedList partnerPlayers;
	private Partner[] sentPartner;
	private byte selectedPartnerI;

	final static short MAX_PARTNER_AGE = 500;

	Listener() {
		partnerGames = new LinkedList();
		partnerPlayers = new LinkedList();

	}

	public void run() {
		listen = true;
		try {
			MulticastSocket listenSocket = new MulticastSocket(
					Broadcaster.BROADCAST_PORT);
			InetAddress group = InetAddress.getByName(Broadcaster.BROADCAST_IP);
			listenSocket.joinGroup(group);

			byte[] listenBuf = new byte[Broadcaster.BROADCAST_MESSAGE_LENGTH];
			DatagramPacket listenPacket = new DatagramPacket(listenBuf,
					listenBuf.length);

			String listenIp = "";
			int listenPort = -1;
			String listenUsername = "";
			boolean listenHosting = false;

			while (listen) {
				listenSocket.receive(listenPacket);
				while (partnerFound(listenBuf)) {
					listenPort = getPort(listenBuf);
					listenUsername = getUsername(listenBuf);
					listenHosting = getHosting(listenBuf);
					listenIp = listenPacket.getAddress().getHostAddress();
					Partner p = new Partner(listenUsername, listenIp,
							listenPort, listenHosting);
					LinkedList curList = null;
					if (listenHosting)
						curList = partnerGames;
					else
						curList = partnerPlayers;
					Node n = curList.find(p);
					if (n == null) {
						curList.add(p);
					} else
						((Partner) n.item).renew();

					listenSocket.receive(listenPacket);

				}

				Util.sleep(100);
			}

			listenSocket.leaveGroup(group);
			listenSocket.close();

		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	Partner findPartnerFromI(byte i) {
		if (i == -1)
			return null;
		return sentPartner[i];
	}

	byte findI() {
		return selectedPartnerI;
	}

	String[] getGameNames(Partner selectedGame) {
		// remove aged games
		Partner p;
		for (Node n : partnerGames) {
			p = (Partner) n.item;
			if (p.old())
				partnerGames.remove(n);
		}

		// initialize holders
		sentPartner = new Partner[partnerGames.length];
		String[] names = new String[partnerGames.length];
		selectedPartnerI = -1;
		byte i = 0;

		// fill holders
		for (Node n : partnerGames) {
			p = (Partner) n.item;
			if (p.identical(selectedGame))
				selectedPartnerI = i;
			sentPartner[i] = p;
			names[i++] = p.name;
		}

		return names;
	}

	byte getPlayerCount() {
		// remove aged players
		Partner p;
		for (Node n : partnerPlayers) {
			p = (Partner) n.item;
			if (p.old())
				partnerPlayers.remove(n);
		}

		return (byte) (partnerPlayers.length + partnerGames.length);
	}

	public void stop() {
		listen = false;
	}

	private boolean partnerFound(byte[] b) {
		if (b.length != Broadcaster.BROADCAST_MESSAGE_LENGTH)
			return false;
		for (int i = 0; i < Broadcaster.BROADCAST_CODE.length; i++)
			if (b[i] != Broadcaster.BROADCAST_CODE[i])
				return false;
		return true;
	}

	private int getPort(byte[] b) {
		byte[] p = new byte[4];
		for (int i = 0; i < 4; i++)
			p[i] = b[i + Broadcaster.BROADCAST_CODE.length];
		return Util.byteToInt(p);
	}

	private String getUsername(byte[] b) {
		byte[] p = new byte[Broadcaster.USERNAME_LENGTH];
		for (int i = 0; i < p.length; i++)
			p[i] = b[i + Broadcaster.BROADCAST_CODE.length + 4];
		return Util.byteToString(p);
	}

	private boolean getHosting(byte[] b) {
		return b[Broadcaster.BROADCAST_CODE.length + 4
				+ Broadcaster.USERNAME_LENGTH] == 1;
	}
}
