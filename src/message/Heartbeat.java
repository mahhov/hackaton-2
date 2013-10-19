package message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Heartbeat extends PlayerMessage {

	public static final byte CODE = 2;

	public static final byte GOOD = 0, BAD = 1;
	public byte status;

	public Heartbeat(ObjectInputStream in) {
		read(in);
	}

	public Heartbeat(byte status) {
		this.status = status;
	}

	void read(ObjectInputStream in) {
		try {
			status = in.readByte();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void write(ObjectOutputStream out) {
		try {
			out.writeByte(CODE);
			out.writeByte(status);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public byte getCode() {
		return CODE;
	}
}
