package world.particle;

import java.awt.Color;

import list.LinkedList.Node;
import map.Map;
import player.Camera;
import resource.ColorList;
import world.Battle;
import draw.elements.Circle;
import draw.painter.Painter;

public class FlyingShard extends Particle {
	public static final byte CODE = 1;

	private static final float FRICTION = .99f;
	private static final float GRAVITY = .08f;
	private static final float MAX_V = 1f;

	public static final byte OP_RAINBOW = 0;
	public static final byte OP_BLUE = 1;
	public static final byte OP_RED = 2;

	private Color color;

	private float vx, vy;

	FlyingShard(byte op) {
		life = 100;
		switch (op) {
			case OP_BLUE:
				color = ColorList.BLUE;
				break;
			case OP_RED:
				color = ColorList.RED;
				break;
			default:
				color = new Color((int) (Math.random() * 255),
						(int) (Math.random() * 255),
						(int) (Math.random() * 255));
		}
		vx = (float) (Math.random() * MAX_V - MAX_V / 2);
		vy = (float) (Math.random() * MAX_V - MAX_V);
	}

	public void update(Map map, Node n) {
		float newx;
		float newy;

		newx = x + vx / 5;
		newy = y + vy / 5;
		vx *= FRICTION;
		vy = vy + GRAVITY / 4;

		if (newx < 0) {
			kill();
			newx = 0;
		} else if (newx >= Battle.width) {
			kill();
			newx = (Battle.width - .001f);
		}
		if (newy < 0) {
			kill();
			newy = 0;
		} else if (newy >= Battle.height) {
			kill();
			newy = (Battle.height - .001f);
		}

		map.moveParticle(n, x, y, newx, newy);

		x = newx;
		y = newy;
	}

	public void draw(Camera camera, Painter painter) {
		float[] xy = camera.coordToFrame(x, y);
		float d = camera.magnitudeToFrame(.3f);
		painter.addMidground(new Circle(xy[0], xy[1], d, color, true));
	}

}
