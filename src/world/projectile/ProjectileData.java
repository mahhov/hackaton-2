package world.projectile;

import java.awt.Color;
import java.security.KeyStore.Builder;

import player.Camera;
import draw.elements.Circle;
import draw.elements.Line;
import draw.painter.Painter;

public class ProjectileData {
	public static final ProjectileData NONE = new ProjectileData((byte) 120,
			(short) 0, (byte) 0);
	public static final ProjectileData RAY = new ProjectileData((byte) 100,
			Assault.DAMAGE, (byte) 2);
	public static final ProjectileData SMALL_SHELL = new ProjectileData(
			(byte) 10, Swarm.DAMAGE, (byte) 5);
	public static final ProjectileData ROCKET = new ProjectileData((byte) 3,
			Support.DAMAGE, (byte) 25);
	public static final ProjectileData RAIL = new ProjectileData((byte) 4,
			Riot.DAMAGE, (byte) 15);
	public static final ProjectileData BIG_SHELL = new ProjectileData((byte) 2,
			Artillery.DAMAGE, (byte) 50);
	public static final ProjectileData DEATH_EXPLOSION = new ProjectileData(
			(byte) 0, (byte) 50, (byte) 120);
	public static final ProjectileData BUILD = new ProjectileData((byte) 2,
			Builder.DAMAGE, (byte) 1);

	public final byte CODE;
	private static byte count = 0;

	byte speed;
	short damage;
	byte range; // area range, not distance

	ProjectileData(byte speed, short damage, byte range) {
		this.CODE = count++;
		this.speed = (byte) (speed / 2);
		this.damage = damage;
		this.range = range;
	}

	static ProjectileData get(byte code) {
		if (code == NONE.CODE)
			return NONE;
		if (code == RAY.CODE)
			return RAY;
		if (code == SMALL_SHELL.CODE)
			return SMALL_SHELL;
		if (code == ROCKET.CODE)
			return ROCKET;
		if (code == RAIL.CODE)
			return RAIL;
		if (code == BIG_SHELL.CODE)
			return BIG_SHELL;
		if (code == DEATH_EXPLOSION.CODE)
			return DEATH_EXPLOSION;
		if (code == BUILD.CODE)
			return BUILD;

		System.out.println("unrecognized projectileData code");
		return null;
	}

	public static void draw(Projectile p, Camera camera, Painter painter) {
		p.model.update((byte) 0, p.x, p.y, p.angleCos, p.angleSin);
		p.model.paint(camera, painter);
	}

	// not used, replaced by Projectile.draw(Camera3D, Painter);
	public static void draw(Projectile p, short viewx0, short viewy0,
			float size, Painter painter) {
		float x = (p.x - viewx0) * size;
		float y = (p.y - viewy0) * size;

		if (p.data.CODE == NONE.CODE) {
			System.out.println("trying to draw a NONE projectile");

		} else if (p.data.CODE == RAY.CODE) {
			float gx = (p.goalx - viewx0) * size;
			float gy = (p.goaly - viewy0) * size;
			painter.addForeground(new Line(x, y, gx, gy, (byte) (size * 800)));

		} else if (p.data.CODE == SMALL_SHELL.CODE) {
			painter.addForeground(new Circle(x, y, size * 2, Color.BLACK, true));

		} else if (p.data.CODE == ROCKET.CODE) {
			float d = 4 * size;
			painter.addForeground(new Line(x, y, x - p.angleCos * d, y
					- p.angleSin * d, (byte) (size * 1600)));

		} else if (p.data.CODE == RAIL.CODE) {
			float d = 8 * size;
			painter.addForeground(new Line(x, y, x - p.angleCos * d, y
					- p.angleSin * d, (byte) (size * 600)));
			d = 3 * size;
			painter.addForeground(new Line(x, y, x - p.angleCos * d, y
					- p.angleSin * d, (byte) (size * 800)));

		} else if (p.data.CODE == BIG_SHELL.CODE) {
			painter.addForeground(new Circle(x, y, size * 8, Color.BLACK, true));

		} else if (p.data.CODE == DEATH_EXPLOSION.CODE) {
			painter.addForeground(new Circle(x, y,
					size * DEATH_EXPLOSION.range, Color.RED, true));

		} else
			System.out.println("unrecognized projectileData code");
	}

	Model createModel() {
		if (CODE == NONE.CODE) {
			return null;

		} else if (CODE == RAY.CODE) {
			return new Cuboid(10);

		} else if (CODE == SMALL_SHELL.CODE) {
			return new Cuboid(3);

		} else if (CODE == ROCKET.CODE) {
			return new Cuboid(6);

		} else if (CODE == RAIL.CODE) {
			return new Cuboid(3);

		} else if (CODE == BIG_SHELL.CODE) {
			return new Cuboid(10);

		} else if (CODE == DEATH_EXPLOSION.CODE) {
			return new Cuboid(20);

		} else if (CODE == BUILD.CODE) {
			return new Cuboid(2);

		}

		System.out.println("unrecognized projectileData code");
		return null;
	}

}
