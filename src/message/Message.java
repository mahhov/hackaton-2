package message;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import list.ListItem;

public abstract class Message implements ListItem {

	public abstract byte getCode();

	abstract void read(ObjectInputStream in);

	public abstract void write(ObjectOutputStream out);
}
