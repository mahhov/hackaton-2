package world.block;

import list.Findable;
import list.LinkedList;
import map.Map;
import player.Camera;
import world.Battle;
import draw.painter.Painter;

public class Block implements Findable {
	private static short COUNT = 0;

	protected boolean dynamic; // default to static

	boolean intersectable;

	public short id;
	public float x, y; // corner coordinates

	public static Block createBlock(short id, short code, float x, float y) {
		Block b;

		switch (code) {
			case Earth.CODE:
				b = new Earth();
				break;
			case GlowEarth.CODE:
				b = new GlowEarth();
				break;
			case Laser.CODE:
				b = new Laser();
				break;
			case Rocket.CODE:
				b = new Rocket();
				break;
			default:
				b = new Empty();
		}

		b.id = id;
		b.x = x;
		b.y = y;

		return b;
	}

	public static Block createBlock(byte code, float x, float y) {
		return createBlock(COUNT++, code, x, y);
	}

	public boolean identical(Object id) {
		return (Short) id == this.id;
	}

	public boolean intersectable(byte color) {
		return intersectable;
	}

	public void intersect(Battle battle, Map map, LinkedList changes, byte color) {
	}

	// for dynamic blocks
	public void update(Battle battle, Map map, LinkedList changes) {
	}

	public void draw(Camera camera, Painter painter) {
	}

	// used by map loader since map loader is shared by both server and local
	// client
	public static void resetCount() {
		COUNT = 0;
	}

	public boolean isDynamic() {
		return dynamic;
	}

}
