package map;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import message.Init;
import world.Battle;
import world.BattleFiller;
import world.block.Block;
import world.block.Earth;
import world.block.Empty;
import world.block.GlowEarth;

public class MapLoader {

	public short width, height;
	public byte[][] mapData;
	public short[] spawnData;

	public MapLoader(short width, short height, byte[][] mapData,
			short[] spawnData) {
		this.width = width;
		this.height = height;

		this.mapData = mapData.clone(); // is cloning necessary?
		this.spawnData = spawnData.clone();
	}

	public MapLoader(String file) {
		ShortReader reader = new ShortReader(file);
		width = reader.readLineShort();
		height = reader.readLineShort();
		mapData = new byte[width][height];

		spawnData = new short[4];
		for (byte i = 0; i < spawnData.length; i++)
			spawnData[i] = reader.readLineShort();

		byte code;
		for (short y = 0; y < height; y++)
			for (short x = 0; x < width; x++) {
				code = reader.readGrid();
				mapData[x][y] = code;
			}

		reader.close();
	}

	private class ShortReader {
		private BufferedReader in;

		private ShortReader(String file) {
			String mapFile = "maps/" + file + ".txt";
			try {
				in = new BufferedReader(new FileReader(mapFile));
			} catch (FileNotFoundException e) {
				System.out.println("could not open map file : " + mapFile);
				e.printStackTrace();
			}
		}

		private short readLineShort() {
			try {
				return Short.parseShort(in.readLine());
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return -1;
		}

		private byte readGrid() {
			try {
				while (true) {
					int read = in.read();
					switch (read) {
						case '.':
							return Empty.CODE;
						case 'e':
						case 'E':
							return Earth.CODE;
						case 'g':
						case 'G':
							return GlowEarth.CODE;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return -1;
		}

		private void close() {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public Tile[][] makeTileGrid() {
		Block.resetCount();
		Tile[][] grid = new Tile[width][height];
		for (short x = 0; x < width; x++)
			for (short y = 0; y < height; y++) {
				grid[x][y] = new Tile();
				grid[x][y].addBlock(Block.createBlock(mapData[x][y], x, y));
			}
		return grid;
	}

	public BattleFiller makeTileGridAndBattleFiller(Battle battle) {
		Block.resetCount();

		BattleFiller filler = new BattleFiller();

		Tile[][] grid = new Tile[width][height];
		for (short x = 0; x < width; x++)
			for (short y = 0; y < height; y++) {
				Block block = Block.createBlock(mapData[x][y], x, y);
				if (block.isDynamic())
					filler.addDynamicBlock(block);
				else
					filler.addStaticBlock(block);
				grid[x][y] = new Tile();
				grid[x][y].addBlock(block);
			}
		filler.setGrid(grid);

		return filler;
	}

	public Init getInitMessage() {
		return new Init(width, height, mapData, spawnData);
	}
}
