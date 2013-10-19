package player;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class TypingControl extends Control {
	final int FRAME_SIZE;

	private char key;
	private boolean delete, backspace, enter;
	private float mousex, mousey;
	private byte mouseState;

	public TypingControl(int frameSize) {
		FRAME_SIZE = frameSize;
	}

	public float getMouseX() {
		return mousex;
	}

	public float getMouseY() {
		return mousey;
	}

	public boolean getMousePress() {
		if (mouseState == Control.PRESS) {
			mouseState = Control.DOWN;
			return true;
		}
		return false;
	}

	public boolean getMouseRelease() {
		if (mouseState == Control.RELEASE) {
			mouseState = Control.UP;
			return true;
		}
		return false;
	}

	public char getKey() {
		char r = key;
		key = 0;
		return r;
	}

	public boolean getDelete() {
		return delete;
	}

	public boolean getBackspace() {
		return backspace;
	}

	public boolean getEnter() {
		return enter;
	}

	public void resetToggleKeys() {
		delete = false;
		backspace = false;
		enter = false;
	}

	// KEY EVENT LISTENERS

	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
			if (e.isControlDown())
				delete = true;
			else
				backspace = true;
		} else if (e.getKeyCode() == KeyEvent.VK_DELETE)
			delete = true;
		else if (e.getKeyCode() == KeyEvent.VK_ENTER)
			enter = true;
		else {
			char tkey = e.getKeyChar();
			if (Character.isLetter(tkey) || Character.isDigit(tkey))
				key = tkey;
		}
	}

	// MOUSE EVENT LISTENERS

	public void mousePressed(MouseEvent e) {
		mouseState = Control.PRESS;
	}

	public void mouseReleased(MouseEvent e) {
		mouseState = Control.RELEASE;
	}

	public void mouseDragged(MouseEvent e) {
		mousex = e.getX() * 1f / FRAME_SIZE;
		mousey = e.getY() * 1f / FRAME_SIZE;
	}

	public void mouseMoved(MouseEvent e) {
		mousex = e.getX() * 1f / FRAME_SIZE;
		mousey = e.getY() * 1f / FRAME_SIZE;
	}

	// NOT USED

	public void keyTyped(KeyEvent e) {
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseWheelMoved(MouseWheelEvent e) {
	}

	public void keyPressed(KeyEvent e) {
	}

}
