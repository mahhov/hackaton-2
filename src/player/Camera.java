package player;

import map.MiniPlayer;
import world.Battle;
import world.MathUtil;

public class Camera {
	private static final short MIN_ZOOM = 1, MAX_ZOOM = 32;

	private float x, y, zoom;
	private float vzoom;
	private static final float ZOOM_FRICTION = .9f;

	private MiniPlayer followPlayer;

	public Camera(MiniPlayer followPlayer) {
		x = (short) (Battle.width / 2);
		y = (short) (Battle.height / 2);
		zoom = MAX_ZOOM / 2;

		this.followPlayer = followPlayer;
	}

	void update(GameControl control) {
		pan();
		zoom(control);
	}

	private void pan() {
		float tox = followPlayer.x + MiniPlayer.WIDTH / 2;
		float toy = followPlayer.y + MiniPlayer.HEIGHT / 2;

		byte n = (byte) (zoom);
		x = (x * n + tox) / (n + 1);
		y = (y * n + toy) / (n + 1);
	}

	private void zoom(GameControl control) {
		byte scroll = (byte) control.getMouseScroll();
		float zoomSpeed;
		if (scroll == 0)
			zoomSpeed = .16f; // keyboard
		else
			zoomSpeed = .6f; // mouse wheel

		// zoom in
		if (scroll == GameControl.SCROLL_DOWN
				|| GameControl
						.isDown(control.getKeyState(GameControl.KEY_PLUS))) {
			vzoom -= zoomSpeed;
		} else
		// zoom out
		if (scroll == GameControl.SCROLL_UP
				|| GameControl.isDown(control
						.getKeyState(GameControl.KEY_MINUS))) {
			vzoom += zoomSpeed;
		}

		vzoom *= ZOOM_FRICTION;
		if (!MathUtil.isZero(vzoom)) {
			zoom += vzoom;
			if (zoom < MIN_ZOOM)
				zoom = MIN_ZOOM;
			if (zoom > MAX_ZOOM)
				zoom = MAX_ZOOM;
		}
	}

	public short[] frameToCoord(float mousex, float mousey) {
		// convert mouse coordinates to map coordinates

		short coordx = (short) (2 * zoom * (mousex - .5f) + x);
		short coordy = (short) (2 * zoom * (mousey - .5f) + y);

		return new short[] { coordx, coordy };
	}

	public float[] frameToFloatCoord(float mousex, float mousey) {
		// convert mouse coordinates to map coordinates

		float coordx = 2 * zoom * (mousex - .5f) + x;
		float coordy = 2 * zoom * (mousey - .5f) + y;

		return new float[] { coordx, coordy };
	}

	public float[] coordToFrame(float x, float y) {
		// convert coord to screen

		x = (x - this.x) / (2 * zoom) + .5f;
		y = (y - this.y) / (2 * zoom) + .5f;

		return new float[] { x, y };
	}

	public float[] magnitudeToFrame(float w, float h) {
		// convert width/height in coord to screen

		w = w / (2 * zoom);
		h = h / (2 * zoom);

		return new float[] { w, h };
	}

	public float magnitudeToFrame(float s) {
		// convert width/height in coord to screen

		return s / (2 * zoom);
	}
}
