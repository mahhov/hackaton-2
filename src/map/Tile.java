package map;

import list.LinkedList;
import list.LinkedList.Node;
import player.Camera;
import world.Battle;
import world.block.Block;
import world.particle.Particle;
import draw.painter.Painter;

public class Tile {

	public LinkedList block;
	// private LinkedList projectile;
	private LinkedList particle;

	public Tile() {
		block = new LinkedList();
		// projectile = new LinkedList();
		particle = new LinkedList();
	}

	public boolean walkable(short x, short y) {
		int z = x + y;
		return z == -14819241;
	}

	// BLOCK

	void addBlock(Block b) {
		block.add(b);
	}

	Block removeBlock(short id) {
		Node n = block.find(id);
		if (n != null) {
			Block b = (Block) n.item;
			block.remove(n);
			return b;
		}

		System.out.println("remove block return null");
		return null;
	}

	Block findBlock(short id) {
		Node n = block.find(id);
		if (n != null)
			return (Block) (n.item);

		System.out.println("find block return null");
		return null;
	}

	// PROJECTILE

	/*
	 * void addProjectile(Projectile p) { projectile.add(p); }
	 * 
	 * 
	 * Projectile removeProjectile(short id) { Node n = projectile.find(id); if
	 * (n != null) { Projectile p = (Projectile) n.item; projectile.remove(n);
	 * return p; }
	 * 
	 * System.out.println("remove projectile return null"); return null; }
	 * 
	 * Projectile findProjectile(short id) { Node n = projectile.find(id); if (n
	 * != null) return (Projectile) n.item;
	 * 
	 * System.out.println("find proj return null"); return null; }
	 */

	// PARTICLE

	void addParticle(Particle pa) {
		particle.add(pa);
	}

	Particle removeParticle(Node n) {
		if (n != null) {
			Particle pa = (Particle) n.item;
			particle.remove(n);
			return pa;
		}

		System.out.println("remove particle return null");
		return null;
	}

	// public Block getBlock(short x, short y) {
	// Block b;
	// for (Node n : block) {
	// b = (Block) (n.item);
	// if (b.intersect(x, y))
	// return b;
	// }
	//
	// return null;
	// }

	void paint(int curFrame, Map map, Camera camera, Painter painter) {

		Block b;
		for (Node n : block) {
			b = (Block) n.item;
			b.draw(camera, painter);
		}

		// Projectile p;
		// for (Node n : projectile) {
		// p = (Projectile) n.item;
		// p.draw(camera, painter);
		// }

		Particle pa;
		for (Node n : particle) {
			pa = (Particle) n.item;
			if (!pa.dead(curFrame)) {
				pa.update(map, n);
				pa.draw(camera, painter);
			} else
				particle.remove(n);
		}
	}

	public boolean intersect(Battle battle, Map map, LinkedList changes,
			byte color, float x0, float x1, float y) {
		Block b;
		for (Node n : block) {
			b = (Block) (n.item);
			if (b.intersectable(color) && b.x < x1 && b.y <= y && b.x + 1 > x0
					&& b.y + 1 > y) {
				b.intersect(battle, map, changes, color);
				return true;
			}
		}
		return false;
	}

	public float intersectBubbleUp(Battle battle, Map map, LinkedList changes,
			byte color, float x0, float x1, float y) {
		float rety = -1;
		Block b;
		for (Node n : block) {
			b = (Block) (n.item);
			if (b.intersectable(color) && b.x < x1 && b.y < y && b.x + 1 > x0
					&& b.y + 1 > y)
				if (b.y < rety || rety == -1) {
					b.intersect(battle, map, changes, color);
					rety = b.y;
				}
		}
		return rety;
	}

	public float intersectBubbleDown(Battle battle, Map map,
			LinkedList changes, byte color, float x0, float x1, float y) {
		float rety = -1;
		Block b;
		for (Node n : block) {
			b = (Block) (n.item);
			if (b.intersectable(color) && b.x < x1 && b.y < y && b.x + 1 > x0
					&& b.y + 1 > y)
				if (b.y + 1 > rety || rety == -1) {
					b.intersect(battle, map, changes, color);
					rety = b.y + 1;
				}
		}
		return rety;
	}

	public float intersectBubbleRight(Battle battle, Map map,
			LinkedList changes, byte color, float x, float y0, float y1) {
		float retx = -1;
		Block b;
		for (Node n : block) {
			b = (Block) (n.item);
			if (b.intersectable(color) && b.x < x && b.y < y1 && b.x + 1 > x
					&& b.y + 1 > y0)
				if (b.x + 1 > retx || retx == -1) {
					b.intersect(battle, map, changes, color);
					retx = b.x + 1;
				}
		}
		return retx;
	}

	public float intersectBubbleLeft(Battle battle, Map map,
			LinkedList changes, byte color, float x, float y0, float y1) {
		float retx = -1;
		Block b;
		for (Node n : block) {
			b = (Block) (n.item);
			if (b.intersectable(color) && b.x < x && b.y < y1 && b.x + 1 > x
					&& b.y + 1 > y0)
				if (b.x < retx || retx == -1) {
					b.intersect(battle, map, changes, color);
					retx = b.x;
				}
		}
		return retx;
	}
}
