package message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class MovePlayer extends Message {
	public static final byte CODE = 7;

	public static final byte IDLE = 0, WALK_LEFT = 1, WALK_RIGHT = 2,
			JUMP_LEFT = 3, JUMP_RIGHT = 4, DEFENDING_LEFT = 5,
			DEFENDING_RIGHT = 6, CASTING_LEFT = 7, CASTING_RIGHT = 8;

	public byte playerId;
	public float x, y;
	public byte state;
	public byte dir;
	public short life;
	// public boolean damaged;
	public short command, commandReload;

	public MovePlayer(ObjectInputStream in) {
		read(in);
	}

	public MovePlayer(byte playerId, float x, float y, byte state, byte dir,
			short life, byte command, short commandReload, boolean damaged) {
		this.playerId = playerId;
		this.x = x;
		this.y = y;
		this.state = state;
		this.dir = dir;
		this.life = life;
		this.command = command;
		this.commandReload = commandReload;
		// this.damaged = damaged;
	}

	void read(ObjectInputStream in) {
		try {
			playerId = in.readByte();
			x = in.readFloat();
			y = in.readFloat();
			state = in.readByte();
			dir = in.readByte();
			life = in.readShort();
			command = in.readByte();
			commandReload = in.readShort();
			// if (in.readByte() == 1)
			// damaged = true;
			// else
			// damaged = false;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void write(ObjectOutputStream out) {
		try {
			out.writeByte(CODE);
			out.writeByte(playerId);
			out.writeFloat(x);
			out.writeFloat(y);
			out.writeByte(state);
			out.writeByte(dir);
			out.writeShort(life);
			out.writeByte(command);
			out.writeShort(commandReload);
			// if (damaged)
			// out.writeByte(1);
			// else
			// out.writeByte(0);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public byte getCode() {
		return CODE;
	}
}
