package map;

import list.LinkedList;
import list.LinkedList.Node;
import message.AddParticle;
import player.Camera;
import world.Battle;
import world.MathUtil;
import world.block.Block;
import world.particle.FlyingShard;
import world.particle.Particle;
import draw.painter.Painter;

public class Map {

	public static final short TILE_SIZE = 30;

	private Tile[][] grid;
	public MiniPlayer p1, p2;

	public Map(MapLoader loader) {
		grid = loader.makeTileGrid();
		Battle.setDimensions(loader.width, loader.height);
		p1 = new MiniPlayer(loader.spawnData[0], loader.spawnData[1],
				Battle.RED);
		p2 = new MiniPlayer(loader.spawnData[2], loader.spawnData[3],
				Battle.BLUE);
	}

	// for when makeTileGrid has already been done by Battle
	public Map(Tile[][] grid, MapLoader loader) {
		this.grid = grid;
		Battle.setDimensions(loader.width, loader.height);
		p1 = new MiniPlayer(loader.spawnData[0], loader.spawnData[1],
				Battle.RED);
		p2 = new MiniPlayer(loader.spawnData[2], loader.spawnData[3],
				Battle.BLUE);
	}

	public void paint(int curFrame, Camera camera, Painter painter) {
		short[] topLeft = camera.frameToCoord(0, 0);
		short[] bottomRight = camera.frameToCoord(1, 1);

		if (topLeft[0] < 0)
			topLeft[0] = 0;
		if (topLeft[1] < 0)
			topLeft[1] = 0;
		if (bottomRight[0] > Battle.width - 1)
			bottomRight[0] = (short) (Battle.width - 1);
		if (bottomRight[1] > Battle.height - 1)
			bottomRight[1] = (short) (Battle.height - 1);

		for (short x = topLeft[0]; x <= bottomRight[0]; x++)
			for (short y = topLeft[1]; y <= bottomRight[1]; y++)
				grid[x][y].paint(curFrame, this, camera, painter);

		p1.paint(camera, painter);
		p2.paint(camera, painter);
	}

	private short gridify(float c) {
		// add .5f before calling gridify iff c is top/left
		// otherwise, if c is center, don't shift
		return (short) (c);
	}

	private short[] gridify(float x, float y) {
		return new short[] { gridify(x), gridify(y) };
	}

	// Block

	public void addBlock(Block block, float x, float y) {
		grid[gridify(x)][gridify(y)].addBlock(block);
	}

	public void addBlock(Block block) {
		addBlock(block, gridify(block.x), gridify(block.y));
	}

	public Block removeBlock(short id, short x, short y) {
		return grid[x][y].removeBlock(id);
	}

	public Block removeBlock(short id, float x, float y) {
		return removeBlock(id, gridify(x), gridify(y));
	}

	public Block moveBlock(short id, short oldx, short oldy, float x, float y) {
		Block b = grid[oldx][oldy].removeBlock(id);
		grid[gridify(x)][gridify(y)].addBlock(b);

		b.x = x;
		b.y = y;
		return b;
	}

	/*
	 * public Block robustUnitSearch(short id, short x, short y) { short[] chunk
	 * = getChunk(x, y); Unit u = grid[chunk[0]][chunk[1]].findUnit(id); if (u
	 * != null) return u;
	 * 
	 * short leftx = (short) (chunk[0] > 0 ? chunk[0] - 1 : 0); short rightx =
	 * (short) (chunk[0] < gridWidth - 2 ? chunk[0] + 1 : gridWidth - 1); short
	 * topy = (short) (chunk[1] > 0 ? chunk[1] - 1 : 0); short bottomy = (short)
	 * (chunk[1] < gridHeight - 2 ? chunk[1] + 1 : gridHeight - 1); for (short
	 * chunkx = leftx; chunkx <= rightx; chunkx++) for (short chunky = topy;
	 * chunky <= bottomy; chunky++) if (chunkx != chunk[0] || chunky !=
	 * chunk[1]) { u = grid[chunkx][chunky].findUnit(id); if (u != null) return
	 * u; }
	 * 
	 * System.out.println("robustUnitSearch return null"); return null; }
	 */

	/*
	 * // PROJECTILE
	 * 
	 * public void addProjectile(Projectile p) { short[] chunk = getChunk(p.x,
	 * p.y); grid[chunk[0]][chunk[1]].addProjectile(p); }
	 * 
	 * public void setProjectile(short id, short oldx, short oldy, short x,
	 * short y) { short[] chunkold = getChunk(oldx, oldy); short[] chunknew =
	 * getChunk(x, y);
	 * 
	 * Projectile p; if (chunkold[0] != chunknew[0] || chunkold[1] !=
	 * chunknew[1]) { p = grid[chunkold[0]][chunkold[1]].removeProjectile(id);
	 * grid[chunknew[0]][chunknew[1]].addProjectile(p); } else p =
	 * grid[chunkold[0]][chunkold[1]].findProjectile(id);
	 * 
	 * p.x = x; p.y = y; }
	 * 
	 * public void removeProjectile(short id, short x, short y) { short[] chunk
	 * = getChunk(x, y); grid[chunk[0]][chunk[1]].removeProjectile(id); }
	 */

	// PARTICLE

	public void addParticle(Particle pa) {
		grid[gridify(pa.x)][gridify(pa.y)].addParticle(pa);
	}

	// does not actually change particle x,y
	// because this is called from particle to move it's map location
	// after the particle has already moved its actual location
	public void moveParticle(Node n, float oldx, float oldy, float x, float y) {
		short[] old = gridify(oldx, oldy);
		short[] newc = gridify(x, y);

		if (old[0] != newc[0] || old[1] != newc[1]) {
			Particle pa = grid[old[0]][old[1]].removeParticle(n);
			grid[newc[0]][newc[1]].addParticle(pa);
		}
	}

	// PLAYER

	public void setPlayer(byte playerId, float x, float y, byte state,
			byte dir, short life) {
		MiniPlayer p = getPlayer(playerId);
		p.x = x;
		p.y = y;
		p.state = state;
		p.dir = dir;
		p.life = life;
		// p.damageDuration = MiniPlayer.DAMAGE_DURATION;
	}

	public MiniPlayer getPlayer(byte color) {
		if (color == Battle.RED)
			return p1;
		return p2;
	}

	public MiniPlayer getOtherPlayer(byte color) {
		if (color == Battle.RED)
			return p2;
		return p1;
	}

	boolean intersectPoint(Battle battle, LinkedList changes, byte color,
			float x0, float x1, float y) {
		if (x0 < 0 || x1 >= Battle.width || y < 0 || y >= Battle.height)
			return true;

		short lx, rx, ty, by;
		lx = gridify(x0 - .5f);
		rx = gridify(x1 + .5f);
		ty = gridify(y - .5f);
		by = gridify(y + .5f);

		if (lx < 0)
			lx = 0;
		if (ty < 0)
			ty = 0;
		if (rx > Battle.width - 1)
			rx = (short) (Battle.width - 1);
		if (by > Battle.height - 1)
			by = (short) (Battle.height - 1);

		for (short xx = lx; xx <= rx; xx++)
			for (short yy = ty; yy <= by; yy++)
				if (grid[xx][yy].intersect(battle, this, changes, color, x0,
						x1, y))
					return true;

		return false;
	}

	// OPTIMIZE ORDER OF INTERSECTION CHECKING

	public float intersectBubbleUp(Battle battle, LinkedList changes,
			byte color, float x0, float x1, float y) {
		if (y >= Battle.height)
			return Battle.height;

		float rety = -1;

		short lx, rx, ty, by;
		lx = gridify(x0 - .5f);
		rx = gridify(x1 + .5f);
		ty = gridify(y - .5f);
		by = gridify(y + .5f);

		if (lx < 0)
			lx = 0;
		if (ty < 0)
			ty = 0;
		if (rx > Battle.width - 1)
			rx = (short) (Battle.width - 1);
		if (by > Battle.height - 1)
			by = (short) (Battle.height - 1);

		float tempy;
		for (short xx = lx; xx <= rx; xx++)
			for (short yy = ty; yy <= by; yy++) {
				tempy = grid[xx][yy].intersectBubbleUp(battle, this, changes,
						color, x0, x1, y);
				if (tempy != -1 && (tempy < rety || rety == -1))
					rety = tempy;
			}

		return rety;
	}

	public float intersectBubbleDown(Battle battle, LinkedList changes,
			byte color, float x0, float x1, float y) {
		if (y < 0)
			return 0;

		float rety = -1;

		short lx, rx, ty, by;
		lx = gridify(x0 - .5f);
		rx = gridify(x1 + .5f);
		ty = gridify(y - .5f);
		by = gridify(y + .5f);

		if (lx < 0)
			lx = 0;
		if (ty < 0)
			ty = 0;
		if (rx > Battle.width - 1)
			rx = (short) (Battle.width - 1);
		if (by > Battle.height - 1)
			by = (short) (Battle.height - 1);

		float tempy;
		for (short xx = lx; xx <= rx; xx++)
			for (short yy = ty; yy <= by; yy++) {
				tempy = grid[xx][yy].intersectBubbleDown(battle, this, changes,
						color, x0, x1, y);
				if (tempy != -1 && (tempy > rety || rety == -1))
					rety = tempy;
			}

		return rety;
	}

	public float intersectBubbleRight(Battle battle, LinkedList changes,
			byte color, float x, float y0, float y1) {
		if (x < 0)
			return 0;

		float retx = -1;

		short lx, rx, ty, by;
		lx = gridify(x - .5f);
		rx = gridify(x + .5f);
		ty = gridify(y0 - .5f);
		by = gridify(y1 + .5f);

		if (lx < 0)
			lx = 0;
		if (ty < 0)
			ty = 0;
		if (rx > Battle.width - 1)
			rx = (short) (Battle.width - 1);
		if (by > Battle.height - 1)
			by = (short) (Battle.height - 1);

		float tempx;
		for (short xx = lx; xx <= rx; xx++)
			for (short yy = ty; yy <= by; yy++) {
				tempx = grid[xx][yy].intersectBubbleRight(battle, this,
						changes, color, x, y0, y1);
				if (tempx != -1 && (tempx > retx || retx == -1))
					retx = tempx;
			}

		return retx;
	}

	public float intersectBubbleLeft(Battle battle, LinkedList changes,
			byte color, float x, float y0, float y1) {
		if (x > Battle.width)
			return Battle.width;

		float retx = -1;

		short lx, rx, ty, by;
		lx = gridify(x - .5f);
		rx = gridify(x + .5f);
		ty = gridify(y0 - .5f);
		by = gridify(y1 + .5f);

		if (lx < 0)
			lx = 0;
		if (ty < 0)
			ty = 0;
		if (rx > Battle.width - 1)
			rx = (short) (Battle.width - 1);
		if (by > Battle.height - 1)
			by = (short) (Battle.height - 1);

		float tempx;
		for (short xx = lx; xx <= rx; xx++)
			for (short yy = ty; yy <= by; yy++) {
				tempx = grid[xx][yy].intersectBubbleLeft(battle, this, changes,
						color, x, y0, y1);
				if (tempx != -1 && (tempx < retx || retx == -1))
					retx = tempx;
			}

		return retx;
	}

	public void slam(float x, float y, byte slamPower, LinkedList changes,
			byte color) {
		byte count;
		if (slamPower > Byte.MAX_VALUE / 5)
			count = Byte.MAX_VALUE;
		else
			count = (byte) (slamPower * 5);
		changes.add(new AddParticle(FlyingShard.CODE, x, y, count,
				FlyingShard.OP_RAINBOW));

		MiniPlayer p = getOtherPlayer(color);
		float distance = MathUtil.distanceFloat(x - p.x, y - p.y) + .6f;
		if (distance < 1)
			distance = 1;
		short damage = (short) (slamPower / (distance / 2));
		if (damage > 0) {
			p.knockback(slamPower * (p.x - x) / distance / distance / 50,
					slamPower * (-10f) / distance / distance / 50);
			p.damage(changes, damage);
		}
	}
}
