package message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SetPlayerControl extends PlayerMessage {

	public static final byte CODE = 8;

	public static final byte UP = 1, DOWN = 2, RIGHT = 4, LEFT = 8;

	public byte playerId;
	public byte command;
	public float commandx, commandy;
	public boolean up, down, right, left;
	
	public SetPlayerControl(ObjectInputStream in) {
		read(in);
	}

	public SetPlayerControl(byte playerId, byte command, float commandx,
			float commandy, boolean up, boolean down, boolean right,
			boolean left) {
		this.playerId = playerId;
		this.command = command;
		this.commandx = commandx;
		this.commandy = commandy;
		this.up = up;
		this.down = down;
		this.right = right;
		this.left = left;
	}

	void read(ObjectInputStream in) {
		try {
			this.playerId = in.readByte();
			this.command = in.readByte();
			this.commandx = in.readFloat();
			this.commandy = in.readFloat();
			byte direction = in.readByte();
			this.up = (direction & UP) != 0;
			this.down = (direction & DOWN) != 0;
			this.right = (direction & RIGHT) != 0;
			this.left = (direction & LEFT) != 0;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void write(ObjectOutputStream out) {
		try {
			out.writeByte(CODE);
			out.writeByte(playerId);
			out.writeByte(command);
			out.writeFloat(commandx);
			out.writeFloat(commandy);
			byte direction = (byte) ((up ? UP : 0) + (down ? DOWN : 0)
					+ (left ? LEFT : 0) + (right ? RIGHT : 0));
			out.writeByte(direction);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public byte getCode() {
		return CODE;
	}
}
