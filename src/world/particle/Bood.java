package world.particle;

import java.awt.Color;

import list.LinkedList.Node;
import map.Map;
import map.MiniPlayer;
import player.Camera;
import world.Battle;
import draw.elements.Circle;
import draw.painter.Painter;

public class Bood extends Particle {
	public static final byte CODE = 2;

	private Color color;

	private float vy;

	public final static byte SUPER_BLOODY = 1;

	Bood(byte op) {
		if (op == SUPER_BLOODY) {
			life = 550;
			color = new Color((int) (Math.random() * 100f + 100), 0,
					(int) (Math.random() * 100f));
			float RANGE = 4f;
			x = (float) (MiniPlayer.WIDTH / 2 + Math.random() * RANGE - RANGE / 2);
			y = (float) (MiniPlayer.HEIGHT / 2 + Math.random() * RANGE - RANGE / 2);
			vy = (float) (.03f + Math.random() * .06f);
		} else {
			life = 150;
			color = new Color((int) (Math.random() * 100f + 100), 0,
					(int) (Math.random() * 100f));
			float RANGE = 2f;
			x = (float) (MiniPlayer.WIDTH / 2 + Math.random() * RANGE - RANGE / 2);
			y = (float) (MiniPlayer.HEIGHT / 2 + Math.random() * RANGE - RANGE / 2);
			vy = (float) (.03f + Math.random() * .06f);
		}
	}

	public void update(Map map, Node n) {
		float newy = y + vy;
		if (newy >= Battle.height) {
			newy = (Battle.height - .001f);
		}

		map.moveParticle(n, x, y, x, newy);

		y = newy;
	}

	public void draw(Camera camera, Painter painter) {
		float[] xy = camera.coordToFrame(x, y);
		float d = camera.magnitudeToFrame(.6f);
		painter.addMidground(new Circle(xy[0], xy[1], d, color, true));
	}

}
