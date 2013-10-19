package world.particle;

import java.awt.Color;

import list.LinkedList.Node;
import map.Map;
import player.Camera;
import resource.ColorList;
import draw.elements.Circle;
import draw.painter.Painter;

public class ShrinkingBubble extends Particle {
	public static final byte CODE = 0;

	// private static final Color COLOR = Color.GRAY;
	private static final float MAX_SIZE = .5f;
	private static final float SIZE_DECREASE_RATE = .001f;

	private float size;
	private Color color;

	ShrinkingBubble() {
		size = MAX_SIZE;
		life = (int) (MAX_SIZE / SIZE_DECREASE_RATE + 1);
		color = ColorList.BLUE;
		float RANGE = .1f;
		x = (float) (Math.random() * RANGE - RANGE / 2);
		y = (float) (Math.random() * RANGE - RANGE / 2);
	}

	public void update(Map map, Node n) {
		size -= SIZE_DECREASE_RATE;
	}

	public void draw(Camera camera, Painter painter) {
		float[] xy = camera.coordToFrame(x, y);
		float d = camera.magnitudeToFrame(size);
		painter.addMidground(new Circle(xy[0], xy[1], d, color, true));
	}

}
