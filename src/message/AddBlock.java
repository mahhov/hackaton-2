package message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class AddBlock extends Message {
	public static final byte CODE = 3;

	public short id;
	public byte blockCode;
	public float x, y;

	public AddBlock(ObjectInputStream in) {
		read(in);
	}

	public AddBlock(short id, byte blockCode, float x, float y) {
		this.id = id;
		this.blockCode = blockCode;
		this.x = x;
		this.y = y;
	}

	void read(ObjectInputStream in) {
		try {
			id = in.readShort();
			blockCode = in.readByte();
			x = in.readFloat();
			y = in.readFloat();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void write(ObjectOutputStream out) {
		try {
			out.writeByte(CODE);
			out.writeShort(id);
			out.writeByte(blockCode);
			out.writeFloat(x);
			out.writeFloat(y);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public byte getCode() {
		return CODE;
	}
}
