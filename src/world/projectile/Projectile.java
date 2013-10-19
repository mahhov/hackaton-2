package world.projectile;

import list.Findable;
import list.LinkedList;
import list.LinkedList.Node;
import map.Map;
import message.AddParticle;
import message.RemoveProjectile;
import message.SetProjectile;
import player.Camera;
import world.Battle;
import world.MathUtil;
import draw.models.Model;
import draw.painter.Painter;

public class Projectile implements Findable {
	public static short COUNT = 0;

	public short x, y;
	public short x2, y2; // more detail, actualx= x+x2/COORD2

	public short goalx, goaly;
	float angleCos, angleSin;
	public ProjectileData data;
	public byte color;
	public short id; // starts from 1 for convenience

	Model model;

	public Projectile(short x, short y, short goalx, short goaly,
			byte projectileDataCode, byte color) {
		this(x, y, goalx, goaly, projectileDataCode, color, ++COUNT);
	}

	public Projectile(short x, short y, short goalx, short goaly,
			byte projectileDataCode, byte color, short id) {
		this.id = id;

		this.x = x;
		this.y = y;
		x2 = 0;
		y2 = 0;
		this.goalx = goalx;
		this.goaly = goaly;
		short distance = MathUtil.distance(x, y, goalx, goaly);
		angleCos = (goalx - x) * 1f / distance;
		angleSin = (goaly - y) * 1f / distance;
		data = ProjectileData.get(projectileDataCode);
		model = data.createModel();
		model.setColorScheme(color);
		this.color = color;
	}

	public boolean identical(Object id) {
		return (Short) id == this.id;
	}

	public void update(Battle battle, Map map, LinkedList changes, Node node) {
		move(battle, map, changes, node);
	}

	void move(Battle battle, Map map, LinkedList changes, Node node) {
		if ((x == goalx && y == goaly)) {
			battle.removeProjectile(node);
			changes.add(new RemoveProjectile(x, y, id));
			if (data.damage >= 0)
				battle.damage(x, y, data.range, data.damage, color);
			else
				battle.progress(x, y, data.range, (short) -data.damage, color); // progress
			short m = 3;
			changes.add(new AddParticle(x, y, (short) 0, (float) (Math.random()
					* m - m / 2), (float) (Math.random() * m - m / 2), 2,
					(short) 100, (float) (Math.random() * Math.PI * 2)));
			return;
		}

		short newx;
		short newy;

		float d = MathUtil.distance(x, y, goalx, goaly);
		x2 += angleCos * Battle.COORD2 * data.speed;
		y2 += angleSin * Battle.COORD2 * data.speed;
		newx = (short) (x + x2 / Battle.COORD2);
		newy = (short) (y + y2 / Battle.COORD2);
		x2 = (byte) (x2 % Battle.COORD2);
		y2 = (byte) (y2 % Battle.COORD2);
		if (d < data.speed || MathUtil.distance(newx, newy, goalx, goaly) > d) {
			newx = goalx;
			newy = goaly;
			x2 = 0;
			y2 = 0;
		}

		changes.add(new SetProjectile(id, x, y, newx, newy));
		map.setProjectile(id, x, y, newx, newy);
	}

	public void draw(Camera camera, Painter painter) {
		model.update((byte) 0, x, y, angleCos, angleSin);
		model.paint(camera, painter);
	}
}
