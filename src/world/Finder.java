package world;

import list.LinkedList;
import list.LinkedList.Node;
import list.ListItem;
import map.Map;
import world.unit.Unit;

public class Finder {

	// GEOMETRIC MATH STUFF TO HELP FIND UNITS

	@SuppressWarnings("unused")
	private static class FindNearest {
		// FOR FINDING UNIT OF GIVEN COLOR NEAREST TO GIVEN COORDINATE
		// DOES NOT CONSIDER UNIT SIZES
		// NOT USED WHEN LAST CHECKED
		
		private static Map map;
		private static byte oppositeColor; // find nearest with not
											// oppositeColor
		private static short tempdistance;
		private static byte nearDistance;
		private static Unit nearUnit;
		private static short originx, originy;

		static short[] find(Map map, byte oppositeColor, short originx,
				short originy, byte maximumDistance) {
			FindNearest.map = map;
			FindNearest.oppositeColor = oppositeColor;
			FindNearest.originx = originx;
			FindNearest.originy = originy;

			nearDistance = maximumDistance;
			nearUnit = null;

			short[] chunk = map.getChunk(originx, originy);
			short chunkLeft = chunk[0] == 0 ? 0 : (short) (chunk[0] - 1);
			short chunkRight = chunk[0] == map.gridWidth - 1 ? chunk[0]
					: (short) (chunk[0] + 1);
			short chunkTop = chunk[1] == 0 ? 0 : (short) (chunk[1] - 1);
			short chunkBottom = chunk[1] == map.gridHeight - 1 ? chunk[1]
					: (short) (chunk[1] + 1);

			// search containing and adjacent chunk
			for (short chunkx = chunkLeft; chunkx <= chunkRight; chunkx++)
				for (short chunky = chunkTop; chunky <= chunkBottom; chunky++)
					searchChunk(chunkx, chunky);

			// search surrounding searched areas
			short surroundDistance = Battle.MAP_CHUNK_SIZE;
			while (nearDistance > surroundDistance
					&& surroundDistance < maximumDistance) {
				// left
				if (chunkLeft > 0) {
					chunkLeft--;
					for (short chunky = chunkTop; chunky <= chunkBottom; chunky++)
						searchChunk(chunkLeft, chunky);
				}
				// right
				if (nearDistance > surroundDistance
						&& chunkRight < map.gridWidth - 1) {
					chunkRight++;
					for (short chunky = chunkTop; chunky <= chunkBottom; chunky++)
						searchChunk(chunkRight, chunky);
				}
				// top
				if (nearDistance > surroundDistance && chunkTop > 0) {
					chunkTop--;
					for (short chunkx = chunkLeft; chunkx <= chunkRight; chunkx++)
						searchChunk(chunkx, chunkTop);
				}
				// bottom
				if (nearDistance > surroundDistance
						&& chunkBottom < map.gridHeight - 1) {
					chunkBottom++;
					for (short chunkx = chunkLeft; chunkx <= chunkRight; chunkx++)
						searchChunk(chunkx, chunkBottom);
				}
				surroundDistance += Battle.MAP_CHUNK_SIZE;
			}

			if (nearUnit != null)
				return new short[] { nearUnit.x, nearUnit.y };
			return null;
		}

		private static void searchChunk(short chunkx, short chunky) {
			Unit u;
			for (Node n : map.grid[chunkx][chunky].unit) {
				u = (Unit) n.item;
				if (u.color != oppositeColor
						&& (tempdistance = u.distance(originx, originy)) < nearDistance) {
					nearDistance = (byte) tempdistance;
					nearUnit = u;
				}
			}
		}
	}

	public static class FindSmallestAngle {
		// FOR FINDING Unit OF GIVEN COLOR WITHIN GIVEN RANGE OF GIVEN
		// COORDINATES AND NEAREST GIVEN ANGLE
		// PRIORITY GIVEN TO NON-NEUTRAL UNITS

		private static Map map;
		private static byte oppositeColor; // find nearest with not
											// oppositeColor
		private static short originx, originy;
		private static float originangle;
		private static float bestangle;
		private static float tempangle;
		private static short foundx, foundy;
		private static boolean foundNeutral;
		private static short range;
		private static short searchRange;

		public static short[] find(Map map, byte oppositeColor, short originx,
				short originy, float originangle, short range) {
			FindSmallestAngle.map = map;
			FindSmallestAngle.oppositeColor = oppositeColor;
			FindSmallestAngle.originx = originx;
			FindSmallestAngle.originy = originy;
			FindSmallestAngle.originangle = originangle;
			FindSmallestAngle.range = range;
			FindSmallestAngle.searchRange = (short) (range + Unit.MAX_UNIT_SIZE
					* Unit.UNIT_SCALE);

			bestangle = 7; // 2 * pi
			foundx = -1;

			short left = (short) (originx - searchRange);
			if (left < 0)
				left = 0;
			short top = (short) (originy - searchRange);
			if (top < 0)
				top = 0;
			short right = (short) (originx + searchRange);
			if (right > Battle.width - 1)
				right = (short) (Battle.width - 1);
			short bottom = (short) (originy + searchRange);
			if (bottom > Battle.height - 1)
				bottom = (short) (Battle.height - 1);

			short[] chunkTopLeft = map.getChunk(left, top);
			short[] chunkBottomRight = map.getChunk(right, bottom);

			for (short chunkx = chunkTopLeft[0]; chunkx <= chunkBottomRight[0]; chunkx++)
				for (short chunky = chunkTopLeft[1]; chunky <= chunkBottomRight[1]; chunky++)
					searchChunk(chunkx, chunky);

			if (foundx == -1)
				return null;
			return new short[] { foundx, foundy };
		}

		private static void searchChunk(short chunkx, short chunky) {
			Unit u;
			for (Node n : map.grid[chunkx][chunky].unit) {
				u = (Unit) n.item;
				short d;
				if (u.color != oppositeColor
						&& (d = u.distance(originx, originy)) < range + u.size
								* Unit.UNIT_SCALE) {
					// could use u.x2 and u.y2 if more accuracy was needed
					tempangle = MathUtil.angle(originx, originy, u.x, u.y)
							- originangle;
					if (tempangle < 0)
						tempangle = -tempangle;
					if (tempangle < bestangle
							&& (u.color != Battle.NEUTRAL || foundx == -1 || foundNeutral)) {
						foundNeutral = u.color == Battle.NEUTRAL;
						short d2 = (short) (d - u.size * Unit.UNIT_SCALE);
						short dx = (short) (u.x - originx), dy = (short) (u.y - originy);
						foundx = (short) (originx + dx * d2 / d);
						foundy = (short) (originy + dy * d2 / d);
						bestangle = tempangle;
					}
				}
			}
		}
	}

	public static class FindNearby {

		// FOR FINDING UNITS WITHIN GIVEN RANGE OF GIVEN COORDINATE WITH GIVEN
		// COLOR

		private static Map map;
		private static short originx, originy;
		private static LinkedList found;
		private static short range;
		private static boolean[] colors;

		public static LinkedList find(Map map, short originx, short originy,
				short range, boolean[] colors) {
			FindNearby.map = map;
			FindNearby.originx = originx;
			FindNearby.originy = originy;
			FindNearby.range = (short) (range + Unit.MAX_UNIT_SIZE
					* Unit.UNIT_SCALE);
			short searchRange = (short) (range + Unit.MAX_UNIT_SIZE
					* Unit.UNIT_SCALE);
			FindNearby.colors = colors;

			found = new LinkedList();

			short left = (short) (originx - searchRange);
			if (left < 0)
				left = 0;
			short top = (short) (originy - searchRange);
			if (top < 0)
				top = 0;
			short right = (short) (originx + searchRange);
			if (right > Battle.width - 1)
				right = (short) (Battle.width - 1);
			short bottom = (short) (originy + searchRange);
			if (bottom > Battle.height - 1)
				bottom = (short) (Battle.height - 1);

			short[] chunkTopLeft = map.getChunk(left, top);
			short[] chunkBottomRight = map.getChunk(right, bottom);

			for (short chunkx = chunkTopLeft[0]; chunkx <= chunkBottomRight[0]; chunkx++)
				for (short chunky = chunkTopLeft[1]; chunky <= chunkBottomRight[1]; chunky++)
					searchChunk(chunkx, chunky);

			return found;
		}

		private static void searchChunk(short chunkx, short chunky) {
			Unit u;
			short d;
			for (Node n : map.grid[chunkx][chunky].unit) {
				u = (Unit) n.item;
				if (colors[u.color]
						&& (d = (short) (u.distance(originx, originy) - u.size
								* Unit.UNIT_SCALE)) < range) {
					found.add(new UnitDistancePair(u, d));
				}
			}
		}
	}

	public static class CountNearby {

		// COUNT UNITS WITHIN GIVEN RANGE OF GIVEN COORDINATE OF EACH COLOR
		// USES CENTERS RATHER THAN CENTER+SIZE

		private static Map map;
		private static short originx, originy;
		private static short[] count;
		private static short range;

		public static short[] count(Map map, short originx, short originy,
				short range) {
			CountNearby.map = map;
			CountNearby.originx = originx;
			CountNearby.originy = originy;
			CountNearby.range = range;

			count = new short[2];

			short left = (short) (originx - range);
			if (left < 0)
				left = 0;
			short top = (short) (originy - range);
			if (top < 0)
				top = 0;
			short right = (short) (originx + range);
			if (right > Battle.width - 1)
				right = (short) (Battle.width - 1);
			short bottom = (short) (originy + range);
			if (bottom > Battle.height - 1)
				bottom = (short) (Battle.height - 1);

			short[] chunkTopLeft = map.getChunk(left, top);
			short[] chunkBottomRight = map.getChunk(right, bottom);

			for (short chunkx = chunkTopLeft[0]; chunkx <= chunkBottomRight[0]; chunkx++)
				for (short chunky = chunkTopLeft[1]; chunky <= chunkBottomRight[1]; chunky++)
					searchChunk(chunkx, chunky);

			return count;
		}

		private static void searchChunk(short chunkx, short chunky) {
			Unit u;
			for (Node n : map.grid[chunkx][chunky].unit) {
				u = (Unit) n.item;
				if (u.distance(originx, originy) < range
						&& u.color != Battle.NEUTRAL) {
					count[u.color]++;
				}
			}
		}
	}

	public static class FindCollision {

		// FOR HELPING PREVENT UNIT COLLISIONS

		private static Map map;
		private static short id; // id of unit moving
		private static short x, y;
		private static byte size; // size of unit moving
		private static byte searchRange; // size + MAX_SIZE

		public static Unit find(Map map, short id, short x, short y, byte size) {
			FindCollision.map = map;
			FindCollision.id = id;
			FindCollision.x = x;
			FindCollision.y = y;
			FindCollision.size = size;
			FindCollision.searchRange = (byte) ((size + Unit.MAX_UNIT_SIZE) * Unit.UNIT_SCALE);

			short left = (short) (x - searchRange);
			if (left < 0)
				left = 0;
			short top = (short) (y - searchRange);
			if (top < 0)
				top = 0;
			short right = (short) (x + searchRange);
			if (right > Battle.width - 1)
				right = (short) (Battle.width - 1);
			short bottom = (short) (y + searchRange);
			if (bottom > Battle.height - 1)
				bottom = (short) (Battle.height - 1);

			short[] chunkTopLeft = map.getChunk(left, top);
			short[] chunkBottomRight = map.getChunk(right, bottom);

			Unit u;
			for (short chunkx = chunkTopLeft[0]; chunkx <= chunkBottomRight[0]; chunkx++)
				for (short chunky = chunkTopLeft[1]; chunky <= chunkBottomRight[1]; chunky++)
					if ((u = searchChunk(chunkx, chunky)) != null)
						return u;

			return null;
		}

		private static Unit searchChunk(short chunkx, short chunky) {
			Unit u;
			for (Node n : map.grid[chunkx][chunky].unit) {
				u = (Unit) n.item;
				if (u.id != id
						&& u.distance(x, y) < (size + u.size) * Unit.UNIT_SCALE) {
					return u;
				}
			}
			return null;
		}
	}

	public static class UnitDistancePair implements ListItem {

		public Unit unit;
		public short distance;

		private UnitDistancePair(Unit unit, short distance) {
			this.unit = unit;
			if (distance < 1)
				distance = 1;
			this.distance = distance;
		}
	}

	public static boolean isWalkable(Map map, short x, short y, byte size) {
		size *= Unit.UNIT_SCALE;
		short left = (short) (x - size);
		if (left < 0)
			left = 0;
		short top = (short) (y - size);
		if (top < 0)
			top = 0;
		short right = (short) (x + size);
		if (right > Battle.width - 1)
			right = (short) (Battle.width - 1);
		short bottom = (short) (y + size);
		if (bottom > Battle.height - 1)
			bottom = (short) (Battle.height - 1);

		short[] chunkTopLeft = map.getChunk(left, top);
		short[] chunkBottomRight = map.getChunk(right, bottom);

		for (short chunkx = chunkTopLeft[0]; chunkx <= chunkBottomRight[0]; chunkx++)
			for (short chunky = chunkTopLeft[1]; chunky <= chunkBottomRight[1]; chunky++)
				if (!map.grid[chunkx][chunky].walkable())
					return false;

		return true;
	}
}
