package world.block;

import java.awt.Color;

import player.Camera;
import draw.elements.Rectangle;
import draw.painter.Painter;

public class GlowEarth extends Block {

	public static final byte CODE = 2;

	private byte glowing;

	GlowEarth() {
		intersectable = true;
	}

	public void draw(Camera camera, Painter painter) {
		float[] xy = camera.coordToFrame(x, y);
		float[] wh = camera.magnitudeToFrame(1, 1);
		if (glowing == 0)
			painter.addMidground(new Rectangle(xy[0], xy[1], wh[0], wh[1],
					(byte) 1, new Color(0x290f1F), true));
		else {
			glowing--;
			painter.addMidground(new Rectangle(xy[0], xy[1], wh[0], wh[1],
					(byte) 1, new Color(50, 75, 200), true));
		}
	}

	// somehow get this update comunicated to clients
	public void stepedOn() {
		glowing = 10;
	}
}
