package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class HostConnector implements Runnable {
	private ServerSocket serverSocket;
	private Socket clientSocket;
	private boolean connectToClient;

	ObjectInputStream clientIn;
	ObjectOutputStream clientOut;
	String username;

	HostConnector() {
		username = "";
	}

	public void run() {
		try {
			serverSocket.setSoTimeout(3000);
			while (connectToClient) {
				try {
					clientSocket = serverSocket.accept();
					clientOut = new ObjectOutputStream(
							clientSocket.getOutputStream());
					clientIn = new ObjectInputStream(
							clientSocket.getInputStream());

					while (clientIn.available() == 0) {
						Util.sleep(100);
					}
					char[] tusername = new char[Broadcaster.USERNAME_LENGTH];
					for (byte i = 0; i < tusername.length; i++)
						if (clientIn.available() != 0)
							tusername[i] = clientIn.readChar();
						else
							tusername[i] = 0;
					username = new String(tusername);
				} catch (SocketTimeoutException e) {
				}
			}
		} catch (SocketException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void connectToClient(ServerSocket serverSocket) {
		connectToClient = true;
		this.serverSocket = serverSocket;
		clientSocket = null;
		new Thread(this).start();
	}

	boolean connected() {
		return username != "";
	}

	void stop() {
		connectToClient = false;
	}

}
