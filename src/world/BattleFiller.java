package world;

import list.LinkedList;
import map.Tile;
import world.block.Block;

public class BattleFiller {

	LinkedList dynamicBlocks, staticBlocks;
	Tile[][] grid;

	public BattleFiller() {
		dynamicBlocks = new LinkedList();
		staticBlocks = new LinkedList();
	}

	public void addDynamicBlock(Block b) {
		dynamicBlocks.add(b);
	}

	public void addStaticBlock(Block b) {
		staticBlocks.add(b);
	}

	public void setGrid(Tile[][] grid) {
		this.grid = grid;
	}

}
