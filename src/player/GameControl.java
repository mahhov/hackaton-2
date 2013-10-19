package player;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class GameControl extends Control {
	final int FRAME_SIZE;

	private Keys key;
	private Mouse mouse;

	// mouse
	public static final int LEFT = 1, MIDDLE = 2, RIGHT = 0;
	public static final int SCROLL_DOWN = 1, SCROLL_UP = -1;

	// keys (remember to update Keys.convertAwtKeyCodeToKey)
	public static final int NUM_KEYS = 22;
	public static final int KEY_ESC = 0, KEY_W = 1, KEY_A = 2, KEY_S = 3,
			KEY_D = 4, KEY_Q = 5, KEY_E = 6, KEY_SHIFT = 7, KEY_CTRL = 8,
			KEY_1 = 9, KEY_2 = 10, KEY_3 = 11, KEY_4 = 12, KEY_5 = 13,
			KEY_6 = 14, KEY_7 = 15, KEY_8 = 16, KEY_9 = 17, KEY_0 = 18,
			KEY_ENTER = 19, KEY_PLUS = 20, KEY_MINUS = 21;

	private class Keys {

		int[] keyState;

		Keys() {
			keyState = new int[NUM_KEYS];
		}

		void press(int k) {
			if (k != -1 && keyState[k] != 2)
				keyState[k] = 1;
		}

		void release(int k) {
			if (k != -1)
				keyState[k] = 3;
		}

		private int get(int k) {
			int t = keyState[k];
			if (t == PRESS)
				keyState[k] = DOWN;
			else if (t == RELEASE)
				keyState[k] = UP;
			return t;
		}

		int convertAwtKeyCodeToKey(int keyCode) {
			switch (keyCode) {
				case KeyEvent.VK_ESCAPE:
					return KEY_ESC;
				case KeyEvent.VK_W:
					return KEY_W;
				case KeyEvent.VK_A:
					return KEY_A;
				case KeyEvent.VK_S:
					return KEY_S;
				case KeyEvent.VK_D:
					return KEY_D;
				case KeyEvent.VK_Q:
					return KEY_Q;
				case KeyEvent.VK_E:
					return KEY_E;
				case KeyEvent.VK_SHIFT:
					return KEY_SHIFT;
				case KeyEvent.VK_CONTROL:
					return KEY_CTRL;
				case KeyEvent.VK_1:
					return KEY_1;
				case KeyEvent.VK_2:
					return KEY_2;
				case KeyEvent.VK_3:
					return KEY_3;
				case KeyEvent.VK_4:
					return KEY_4;
				case KeyEvent.VK_5:
					return KEY_5;
				case KeyEvent.VK_6:
					return KEY_6;
				case KeyEvent.VK_7:
					return KEY_7;
				case KeyEvent.VK_8:
					return KEY_7;
				case KeyEvent.VK_9:
					return KEY_9;
				case KeyEvent.VK_0:
					return KEY_0;
				case KeyEvent.VK_ENTER:
					return KEY_ENTER;
				case KeyEvent.VK_EQUALS:
					return KEY_PLUS;
				case KeyEvent.VK_MINUS:
					return KEY_MINUS;
			}

			return -1;
		}

		// int convertLwjglKeyCodeToKey(int keyCode) {
		// switch (keyCode) {
		// case Keyboard.KEY_ESCAPE:
		// return KEY_ESC;
		// case Keyboard.KEY_W:
		// return KEY_W;
		// case Keyboard.KEY_A:
		// return KEY_A;
		// case Keyboard.KEY_S:
		// return KEY_S;
		// case Keyboard.KEY_D:
		// return KEY_D;
		// case Keyboard.KEY_Q:
		// return KEY_Q;
		// case Keyboard.KEY_E:
		// return KEY_E;
		// case Keyboard.KEY_LSHIFT:
		// return KEY_SHIFT;
		// case Keyboard.KEY_LCONTROL:
		// return KEY_CTRL;
		// case Keyboard.KEY_1:
		// return KEY_1;
		// case Keyboard.KEY_2:
		// return KEY_2;
		// case Keyboard.KEY_3:
		// return KEY_3;
		// case Keyboard.KEY_4:
		// return KEY_4;
		// case Keyboard.KEY_5:
		// return KEY_5;
		// case Keyboard.KEY_6:
		// return KEY_6;
		// case Keyboard.KEY_7:
		// return KEY_7;
		// case Keyboard.KEY_8:
		// return KEY_7;
		// case Keyboard.KEY_9:
		// return KEY_9;
		// case Keyboard.KEY_0:
		// return KEY_0;
		// case Keyboard.KEY_RETURN:
		// return KEY_ENTER;
		// case Keyboard.KEY_SPACE:
		// return KEY_SPACE;
		// case Keyboard.KEY_LMENU:
		// return KEY_ALT;
		// }
		//
		// return -1;
		// }
	}

	private class Mouse {
		float x, y;
		int scroll;
		int[] mouseState;

		Mouse() {
			mouseState = new int[3];
		}

		void move(int x, int y) {
			this.x = 1f * x / FRAME_SIZE;
			this.y = 1f * y / FRAME_SIZE;
		}

		void scroll(int amount) {
			scroll += amount;
		}

		void setButton(int button, int value) {
			mouseState[button] = value;
		}

		private int get(int m) {
			int t = mouseState[m];
			if (t == PRESS)
				mouseState[m] = DOWN;
			else if (t == RELEASE)
				mouseState[m] = UP;
			return t;
		}

		private int getScroll() {
			int t = scroll;
			scroll = 0;
			return t;
		}

		int convertAwtButtonToButton(int button) {
			switch (button) {
				case MouseEvent.BUTTON1:
					return LEFT;
				case MouseEvent.BUTTON3:
					return RIGHT;
				case MouseEvent.BUTTON2:
					return MIDDLE;
				default:
					return -1;
			}
		}

		int convertLwjglButtonToButton(int button) {
			switch (button) {
				case 0:
					return GameControl.LEFT;
				case 1:
					return GameControl.RIGHT;
				case 2:
					return GameControl.MIDDLE;
				default:
					return -1; // for no mouse button (mouse move)
			}
		}

	}

	public GameControl(int frameSize) {
		FRAME_SIZE = frameSize;

		key = new Keys();
		mouse = new Mouse();
	}

	public int getKeyState(int k) {
		return key.get(k);
	}

	public float getMouseX() {
		return mouse.x;
	}

	public float getMouseY() {
		return mouse.y;
	}

	public boolean getMousePress() {
		return isDown(getMouseState(LEFT));
	}

	public boolean getMouseRelease() {
		return isUp(getMouseState(LEFT));
	}

	int getMouseState(int m) {
		return mouse.get(m);
	}

	int getMouseScroll() {
		return mouse.getScroll();
	}

	// PUBLIC KEY EVENT TRIGGERS

	// public void keyPressLwjgl(int keyCode) {
	// key.press(key.convertLwjglKeyCodeToKey(keyCode));
	// }
	//
	// public void keyReleaseLwjgl(int keyCode) {
	// key.release(key.convertLwjglKeyCodeToKey(keyCode));
	// }

	public void keyPressAwt(int keyCode) {
		key.press(key.convertAwtKeyCodeToKey(keyCode));
	}

	public void keyReleaseAwt(int keyCode) {
		key.release(key.convertAwtKeyCodeToKey(keyCode));
	}

	// KEY EVENT LISTENERS

	public void keyPressed(KeyEvent e) {
		keyPressAwt(e.getKeyCode());
		// key.press(key.convertKeyCodeToKey(e.getKeyCode()));
	}

	public void keyReleased(KeyEvent e) {
		keyReleaseAwt(e.getKeyCode());
		// key.release(key.convertKeyCodeToKey(e.getKeyCode()));
	}

	// PUBLIC MOUSE EVENT TRIGGERS

	public void mouseMove(int mouseX, int mouseY) {
		mouse.move(mouseX, mouseY);
	}

	public void mousePressLwjgl(int button) {
		button = mouse.convertLwjglButtonToButton(button);
		if (button != -1)
			mouse.setButton(button, PRESS);
	}

	public void mouseReleaseLwjgl(int button) {
		button = mouse.convertLwjglButtonToButton(button);
		if (button != -1)
			mouse.setButton(button, RELEASE);
	}

	public void mousePressAwt(int button) {
		mouse.setButton(mouse.convertAwtButtonToButton(button), PRESS);
	}

	public void mouseReleaseAwt(int button) {
		mouse.setButton(mouse.convertAwtButtonToButton(button), RELEASE);
	}

	public void mouseScroll(int amount) {
		mouse.scroll(amount);
	}

	// MOUSE EVENT LISTENERS

	public void mouseDragged(MouseEvent e) {
		mouseMove(e.getX(), e.getY());
	}

	public void mouseMoved(MouseEvent e) {
		mouseMove(e.getX(), e.getY());
	}

	public void mousePressed(MouseEvent e) {
		mousePressAwt(e.getButton());
		// mouse.setButton(e.getButton(), PRESS);
	}

	public void mouseReleased(MouseEvent e) {
		mouseReleaseAwt(e.getButton());
		// mouse.setButton(e.getButton(), RELEASE);
	}

	public void mouseWheelMoved(MouseWheelEvent e) {
		// positive getWheelRotation is scroll down
		mouseScroll(e.getWheelRotation());
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

	// HELPER

	public static boolean isDown(int state) {
		return state == PRESS || state == DOWN;
	}

	public static boolean isUp(int state) {
		return state == RELEASE || state == UP;
	}

}
