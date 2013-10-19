package draw.painter;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import player.GameControl;

public class LwjglInputRedirect {

	final int FRAME_SIZE;
	GameControl control;

	LwjglInputRedirect(int frameSize, GameControl control) {
		FRAME_SIZE = frameSize;
		this.control = control;
	}

	void redirect() {
		// mouse move
		control.mouseMove(Mouse.getX(), FRAME_SIZE - Mouse.getY() - 1);
		// mouse scroll
		control.mouseScroll(-Mouse.getDWheel());
		// mouse click
		while (Mouse.next()) {
			if (Mouse.getEventButtonState())
				control.mousePressLwjgl(Mouse.getEventButton());
			else
				control.mouseReleaseLwjgl(Mouse.getEventButton());
		}

		// keyboard
		while (Keyboard.next())
			if (Keyboard.getEventKeyState())
				control.keyPressLwjgl(Keyboard.getEventKey());
			else
				control.keyReleaseLwjgl(Keyboard.getEventKey());
	}

}
