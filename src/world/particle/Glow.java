package world.particle;

import java.awt.Color;

import list.LinkedList.Node;
import map.Map;
import player.Camera;
import draw.elements.Rectangle;
import draw.painter.Painter;

public class Glow extends Particle {
	public static final byte CODE = 5;

	Glow() {
		life = 5;
	}

	public void update(Map map, Node n) {
	}

	public void draw(Camera camera, Painter painter) {
		float[] xy = camera.coordToFrame(x, y);
		float d = camera.magnitudeToFrame(1);
		painter.addMidground(new Rectangle(xy[0], xy[1], d, d, (byte) 1,
				new Color(0, 150, 0), true));
	}
}
