package world.particle;

import java.awt.Color;

import list.LinkedList.Node;
import map.Map;
import player.Camera;
import resource.ColorList;
import world.MathUtil;
import draw.elements.Rectangle;
import draw.painter.Painter;

public class FadingSquare extends Particle {
	public static final byte CODE = 4;

	// private static final float MAX_B = 255f / MathUtil.trimax(
	// ColorList.BLUE.getRed(), ColorList.BLUE.getGreen(),
	// ColorList.BLUE.getBlue());

	private static final float MAX_SIZE = .3f;
	private static final float FADE_RATE = .01f;

	private float size;
	private float size0;
	private float bright;
	private float fadeRate;

	public static final byte OP_SMALL = 0;
	public static final byte OP_LARGE = 1;

	FadingSquare(byte op) {
		size = MAX_SIZE;
		if (op == OP_LARGE) {
			size0 = size * 1.6f;
			fadeRate = FADE_RATE / 2;
		} else {
			size0 = size * 1.3f;
			fadeRate = FADE_RATE;
		}
		life = (int) (.9f / fadeRate);
		bright = 1f;
	}

	public void update(Map map, Node n) {
		bright -= fadeRate;
	}

	public void draw(Camera camera, Painter painter) {
		float[] xy = camera.coordToFrame(x, y);
		float d = camera.magnitudeToFrame(size0);
		if (bright > .8f)
			painter.addMidground(new Rectangle(xy[0], xy[1], d, d, (byte) 1,
					getColor(1), true));
		else {
			d = camera.magnitudeToFrame(size);
			painter.addMidground(new Rectangle(xy[0], xy[1], d, d, (byte) 1,
					getColor(bright), false));
		}
	}

	private Color getColor(float bright) {
		if (bright < 0) {
			System.out.println("fading square has negative color");
			return Color.BLACK;
		}
		return new Color((int) (ColorList.BLUE.getRed() * bright),
				(int) (ColorList.BLUE.getGreen() * bright),
				(int) (ColorList.BLUE.getBlue() * bright));
	}

	// private Color getColorBright() {
	// if (bright < 0) {
	// System.out.println("fading square has negative color");
	// return Color.BLACK;
	// }
	// float b = bright * 1.5f;
	// if (b > MAX_B)
	// b = MAX_B;
	// return new Color((int) (ColorList.BLUE.getRed() * b),
	// (int) (ColorList.BLUE.getGreen() * b),
	// (int) (ColorList.BLUE.getBlue() * b));
	// }
}
