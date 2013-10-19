package message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class MoveBlock extends Message {
	public static final byte CODE = 4;

	public short id;
	public short oldx, oldy;
	public float x, y;

	public MoveBlock(ObjectInputStream in) {
		read(in);
	}

	public MoveBlock(short id, short oldx, short oldy, float x, float y) {
		this.id = id;
		this.oldx = oldx;
		this.oldy = oldy;
		this.x = x;
		this.y = y;
	}

	void read(ObjectInputStream in) {
		try {
			id = in.readShort();
			oldx = in.readShort();
			oldy = in.readShort();
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
			out.writeShort(oldx);
			out.writeShort(oldy);
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
