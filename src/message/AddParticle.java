package message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class AddParticle extends Message {
	public static final byte CODE = 10;

	public byte particleCode;
	public float x, y;
	public byte count;
	public byte op;

	public AddParticle(ObjectInputStream in) {
		read(in);
	}

	public AddParticle(byte particleCode, float x, float y, byte count, byte op) {
		this.particleCode = particleCode;
		this.x = x;
		this.y = y;
		this.count = count;
		this.op = op;
	}

	void read(ObjectInputStream in) {
		try {
			particleCode = in.readByte();
			x = in.readFloat();
			y = in.readFloat();
			count = in.readByte();
			op = in.readByte();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void write(ObjectOutputStream out) {
		try {
			out.writeByte(CODE);
			out.writeByte(particleCode);
			out.writeFloat(x);
			out.writeFloat(y);
			out.writeByte(count);
			out.writeByte(op);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public byte getCode() {
		return CODE;
	}
}
