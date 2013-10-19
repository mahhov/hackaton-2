package message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class RemoveBlock extends Message {
	public static final byte CODE = 6;

	public short id;
	public short x, y;

	public RemoveBlock(ObjectInputStream in) {
		read(in);
	}

	public RemoveBlock(short id, short x, short y) {
		this.id = id;
		this.x = x;
		this.y = y;
	}

	void read(ObjectInputStream in) {
		try {
			id = in.readShort();
			x = in.readShort();
			y = in.readShort();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void write(ObjectOutputStream out) {
		try {
			out.writeByte(CODE);
			out.writeShort(id);
			out.writeShort(x);
			out.writeShort(y);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public byte getCode() {
		return CODE;
	}
}
