package world.block;

import list.LinkedList;
import map.Map;
import message.AddParticle;
import player.Camera;
import resource.ColorList;
import world.Battle;
import world.particle.Glow;
import draw.elements.Rectangle;
import draw.painter.Painter;

public class Earth extends Block {

	public static final byte CODE = 1;

	Earth() {
		intersectable = true;
	}

	public void draw(Camera camera, Painter painter) {
		float[] xy = camera.coordToFrame(x, y);
		float[] wh = camera.magnitudeToFrame(1, 1);
		painter.addMidground(new Rectangle(xy[0], xy[1], wh[0], wh[1],
				(byte) 1, ColorList.BROWN, true));
	}

	public void intersect(Battle battle, Map map, LinkedList changes, byte color) {
		changes.add(new AddParticle(Glow.CODE, x, y, (byte) 1, (byte) 0));
	}
}
