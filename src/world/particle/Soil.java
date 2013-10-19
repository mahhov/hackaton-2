package world.particle;

import java.awt.Color;

import list.LinkedList.Node;
import map.Map;
import map.MiniPlayer;
import player.Camera;
import draw.elements.Circle;
import draw.painter.Painter;

public class Soil extends Particle {
	public static final byte CODE = 3;

	private byte fade = 0;

	Soil() {
		life = 30;
		x = (float) (Math.random() * 1 - .5f);
		y = MiniPlayer.HEIGHT + (float) (Math.random() * 1 - .5f);
	}

	public void update(Map map, Node n) {
		fade++;
	}

	public void draw(Camera camera, Painter painter) {
		float[] xy = camera.coordToFrame(x, y);
		float d = camera.magnitudeToFrame(.3f);
		if (fade > 30) {
			System.out.println("bad fade for Soil Particle");
		} else {
			int g = 150 - 4 * fade;
			Color color = new Color(0, g, 0);
			painter.addMidground(new Circle(xy[0], xy[1], d, (byte) 3, color));
		}
	}
}
