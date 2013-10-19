package message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class UpdatePoints extends Message {
	public static final byte CODE = 11;

	public short[] points;

	public UpdatePoints(ObjectInputStream in) {
		read(in);
	}

	public UpdatePoints(short point1, short point2) {
		this.points = new short[] { point1, point2 };
	}

	void read(ObjectInputStream in) {
		try {
			points = new short[2];
			points[0] = in.readShort();
			points[1] = in.readShort();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void write(ObjectOutputStream out) {
		try {
			out.writeByte(CODE);
			out.writeShort(points[0]);
			out.writeShort(points[1]);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public byte getCode() {
		return CODE;
	}
}
