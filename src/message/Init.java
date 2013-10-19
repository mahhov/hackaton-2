package message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Init extends Message {
	public static final byte CODE = 0;

	public short width, height;
	public byte[][] mapData;
	public short[] spawnData;

	public Init(ObjectInputStream in) {
		read(in);
	}

	public Init(short width, short height, byte[][] mapData, short[] spawnData) {
		this.width = width;
		this.height = height;
		this.mapData = mapData; // .clone();
		this.spawnData = spawnData; // .clone();
	}

	void read(ObjectInputStream in) {
		try {
			width = in.readShort();
			height = in.readShort();
			mapData = new byte[width][height];
			for (short x = 0; x < width; x++)
				for (short y = 0; y < height; y++)
					mapData[x][y] = in.readByte();
			spawnData = new short[4];
			for (byte i = 0; i < spawnData.length; i++)
				spawnData[i] = in.readShort();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void write(ObjectOutputStream out) {
		try {
			out.writeByte(CODE);
			out.writeShort(width);
			out.writeShort(height);
			for (short x = 0; x < width; x++)
				for (short y = 0; y < height; y++)
					out.writeByte(mapData[x][y]);
			for (byte i = 0; i < spawnData.length; i++)
				out.writeShort(spawnData[i]);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public byte getCode() {
		return CODE;
	}

}
