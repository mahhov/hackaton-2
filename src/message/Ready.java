package message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Ready extends PlayerMessage {
	public static final byte CODE = 1;

	public Ready(ObjectInputStream in) {
		read(in);
	}

	public Ready() {
	}

	void read(ObjectInputStream in) {
	}

	public void write(ObjectOutputStream out) {
		try {
			out.writeByte(CODE);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public byte getCode() {
		return CODE;
	}
}
