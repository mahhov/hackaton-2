package message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SetStage extends Message {
	public static final byte CODE = 9;

	public byte stage;

	public SetStage(ObjectInputStream in) {
		read(in);
	}

	public SetStage(byte stage) {
		this.stage = stage;
	}

	void read(ObjectInputStream in) {
		try {
			stage = in.readByte();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void write(ObjectOutputStream out) {
		try {
			out.writeByte(CODE);
			out.writeByte(stage);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public byte getCode() {
		return CODE;
	}
}
