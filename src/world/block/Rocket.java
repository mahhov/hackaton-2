package world.block;

import list.LinkedList;
import map.Map;
import map.MiniPlayer;
import message.AddParticle;
import message.MoveBlock;
import message.RemoveBlock;
import player.Camera;
import resource.ColorList;
import world.Battle;
import world.MathUtil;
import world.particle.FadingSquare;
import world.particle.FlyingShard;
import world.particle.ShrinkingBubble;
import draw.elements.Rectangle;
import draw.painter.Painter;

public class Rocket extends Block {

	public static final byte CODE = 4;

	byte color;
	private float vx, vy;
	private short life;

	Rocket() {
		life = 700;
		intersectable = true;
		dynamic = true;
	}

	public void draw(Camera camera, Painter painter) {
		float[] xy = camera.coordToFrame(x, y);
		float[] wh = camera.magnitudeToFrame(.3f, .3f);
		painter.addMidground(new Rectangle(xy[0], xy[1], wh[0], wh[1],
				(byte) 1, ColorList.BLUE, true));
	}

	public void update(Battle battle, Map map, LinkedList changes) {
		if (life-- <= 0 || MathUtil.isOutBounds(x + vx, y + vy)) {
			kill(battle, changes);
			return;
		}
		float[] xy = MathUtil.inBoundsFloat(x + vx, y + vy);
		if (!(map.intersectBubbleDown(battle, changes, color, x, x + .3f, y) == -1
				&& map.intersectBubbleUp(battle, changes, color, x, x + .3f,
						y + .3f) == -1
				&& map.intersectBubbleLeft(battle, changes, color, x + .3f, y,
						y + .3f) == -1 && map.intersectBubbleRight(battle,
				changes, color, x, y, y + .3f) == -1)) {
			kill(battle, changes);
			return;
		}

		// should only move block when necessary
		changes.add(new MoveBlock(id, (short) x, (short) y, xy[0], xy[1]));
		changes.add(new AddParticle(FadingSquare.CODE, x, y, (byte) 1,
				FadingSquare.OP_LARGE));
		map.moveBlock(id, (short) x, (short) y, xy[0], xy[1]);
		x = xy[0];
		y = xy[1];
	}

	public void setVelocityColor(float x, float y, byte color) {
		vx = x - this.x;
		vy = y - this.y;
		float d = MathUtil.distanceFloat(vx, vy) * 8 * 4;
		vx /= d;
		vy /= d;
		this.color = color;
	}

	public boolean intersectable(byte color) {
		return this.color != color;
	}

	public void intersect(Battle battle, Map map, LinkedList changes, byte color) {
		// check color
		if (this.color != color) {
			if (life < 0)
				System.out.println("why is dead laser attacking players?");
			life = 0;
			// damage + knockback
			MiniPlayer p = map.getPlayer(color);
			p.damage(changes, (short) 30);
			p.knockback(vx * 10, vy * 10);
		}
	}

	void kill(Battle battle, LinkedList changes) {
		battle.removeDynamicBlock(this);
		changes.add(new RemoveBlock(id, (short) x, (short) y));
		changes.add(new AddParticle(FlyingShard.CODE, x, y, (byte) 20,
				FlyingShard.OP_BLUE));
	}
}
