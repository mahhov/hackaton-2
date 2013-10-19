package player;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;

public abstract class Control implements MouseMotionListener, KeyListener,
		MouseListener, MouseWheelListener {
	public static final int PRESS = 1, DOWN = 2, RELEASE = 3, UP = 0;

	public abstract float getMouseX();

	public abstract float getMouseY();

	public abstract boolean getMousePress();

	public abstract boolean getMouseRelease();
}
