package world;

import java.awt.Color;

import list.LinkedList;
import list.LinkedList.Node;
import map.Map;
import map.MapLoader;
import map.MiniPlayer;
import message.Init;
import message.Message;
import message.PlayerMessage;
import message.SetPlayerControl;
import message.UpdatePoints;
import world.block.Block;

public class Battle {

	// main gameplay stage

	public static short width, height;

	public static final byte RED = 0, BLUE = 1, NEUTRAL = 2;

	LinkedList dynamicBlocks;
	LinkedList staticBlocks;

	public short score[];

	Map map; // also holds mini-players

	Battle() {
	}

	Init init() {
		MapLoader mapLoader = new MapLoader("map1");
		BattleFiller filler = mapLoader.makeTileGridAndBattleFiller(this);
		dynamicBlocks = filler.dynamicBlocks;
		staticBlocks = filler.staticBlocks;
		score = new short[2];
		map = new Map(filler.grid, mapLoader);

		return mapLoader.getInitMessage();
	}

	public static void setDimensions(short gridWidth, short gridHeight) {
		Battle.width = gridWidth;
		Battle.height = gridHeight;
	}

	public void addStaticBlock(Block b) {
		staticBlocks.add(b);
		map.addBlock(b);
	}

	public void addDynamicBlock(Block b) {
		dynamicBlocks.add(b);
		map.addBlock(b);
	}

	public void removeDynamicBlock(Block b) {
		// Block b = (Block) bnode.item;
		map.removeBlock(b.id, b.x, b.y);
		// dynamicBlocks.remove(bnode); // more efficient?
		dynamicBlocks.findAndRemove(b.id);
	}

	public void removeStaticBlock(Node bnode) {
		Block b = (Block) bnode.item;
		map.removeBlock(b.id, b.x, b.y);
		staticBlocks.findAndRemove(b.id);
	}

	public UpdatePoints addScore(byte winner) {
		score[winner]++;
		return new UpdatePoints(score[0], score[1]);
	}

	/*
	 * public void addProjectile(Projectile p) { projectile.add(p);
	 * map.addProjectile(p); }
	 * 
	 * public void removeProjectile(Node pnode) { Projectile p = (Projectile)
	 * pnode.item; map.removeProjectile(p.id, p.x, p.y);
	 * projectile.remove(pnode); }
	 */

	/*
	 * public void addParticle(Particle pa) { particle.add(pa);
	 * map.addParticle(pa); }
	 * 
	 * public void removeParticle(Node panode) { Particle pa = (Particle)
	 * panode.item; map.removeParticle(pa.id, pa.x, pa.y);
	 * particle.remove(panode); }
	 */

	private void processInput(PlayerMessage input, LinkedList changes) {
		// split player input into -> movement, use tool 1, use tool n...

		switch (input.getCode()) {
			case SetPlayerControl.CODE:
				SetPlayerControl in = (SetPlayerControl) input; // inSetPlayerControl
				MiniPlayer p = map.getPlayer(in.playerId);
				if (in.left)
					p.moveLeft();
				else if (in.right)
					p.moveRight();

				if (in.up)
					p.jump();
				else
					p.stopJump();

				if (in.down)
					p.down();

				p.doCommand(in.command, in.commandx, in.commandy);

				break;
			default:
				System.out.println("unrecognized player input "
						+ input.getCode());
		}

	}

	LinkedList update(PlayerMessage input1, PlayerMessage input2) {
		LinkedList changes = new LinkedList();

		if (input1 != null)
			processInput(input1, changes);
		if (input2 != null)
			processInput(input2, changes);

		Message m;

		// // projectiles
		// Projectile p;
		// for (Node n : projectile) {
		// p = (Projectile) n.item;
		// p.update(this, map, changes, n);
		// }

		// blocks
		Block b;
		for (Node n : dynamicBlocks) {
			b = (Block) n.item;
			b.update(this, map, changes);
		}

		// players
		map.p1.update(this, map, changes);
		map.p2.update(this, map, changes);

		return changes;
	}

	public static Color getColor(byte color) {
		if (color == RED)
			return Color.RED;
		if (color == BLUE)
			return Color.BLUE;
		return Color.GRAY;
	}

	public static String getColorString(byte color) {
		if (color == RED)
			return "Red";
		if (color == BLUE)
			return "Blue";
		return "GRAY";
	}

	public static byte getOtherColor(byte color) {
		return (byte) (1 - color);
	}

}
