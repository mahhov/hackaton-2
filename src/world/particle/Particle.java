package world.particle;

import list.LinkedList.Node;
import list.ListItem;
import map.Map;
import player.Camera;
import world.MathUtil;
import draw.painter.Painter;

public abstract class Particle implements ListItem {

	public float x, y;
	int life;

	public static Particle createParticle(byte code, float x, float y, byte op,
			int curFrame) {
		Particle pa = null;
		switch (code) {
			case ShrinkingBubble.CODE:
				pa = new ShrinkingBubble();
				break;
			case FlyingShard.CODE:
				pa = new FlyingShard(op);
				break;
			case Bood.CODE:
				pa = new Bood(op);
				break;
			case Soil.CODE:
				pa = new Soil();
				break;
			case FadingSquare.CODE:
				pa = new FadingSquare(op);
				break;
			case Glow.CODE:
				pa = new Glow();
				break;
			default:
				System.out.println("unrecognized particle code");
		}

		pa.life += curFrame;
		float[] xy = MathUtil.inBoundsFloat(pa.x + x, pa.y + y);
		pa.x = xy[0];
		pa.y = xy[1];

		return pa;
	}

	// return true if should be removed
	public boolean dead(int curFrame) {
		return (life < curFrame);
	}

	void kill() {
		life = Integer.MIN_VALUE;
	}

	public abstract void update(Map map, Node n);

	public abstract void draw(Camera camera, Painter painter);

}
