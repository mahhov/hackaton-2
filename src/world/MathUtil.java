package world;

public class MathUtil {
	/*
	 * public static final float sqrt3 = (float) Math.sqrt(3);
	 * 
	 * public static float cos(float angle) { return (float) Math.cos(angle); }
	 * 
	 * public static float sin(float angle) { return (float) Math.sin(angle); }
	 */

	public static short distance(short dx, short dy) {
		return (short) Math.sqrt(dx * dx + dy * dy);
	}

	public static float distanceFloat(float dx, float dy) {
		return (float) Math.sqrt(dx * dx + dy * dy);
	}

	/*
	 * public static short distance(short x1, short y1, short x2, short y2) {
	 * short dx = (short) (x1 - x2); short dy = (short) (y1 - y2); return
	 * (short) Math.sqrt(dx * dx + dy * dy); }
	 * 
	 * public static int distanceInt(float dx, float dy) { return (int)
	 * Math.sqrt(dx * dx + dy * dy); }
	 * 
	 * public static float distanceFloat(float dx, float dy) { return (float)
	 * Math.sqrt(dx * dx + dy * dy); }
	 * 
	 * public static int distanceSquared(short x1, short y1, short x2, short y2)
	 * { short dx = (short) (x1 - x2); short dy = (short) (y1 - y2); return (dx
	 * * dx + dy * dy); }
	 * 
	 * public static float distanceFloat(short x1, short y1, short x12, short
	 * y12, short x2, short y2) { float dx = (x1 + x12 * 1f / Battle.COORD2 -
	 * x2); float dy = (y1 + y12 * 1f / Battle.COORD2 - y2); return (float)
	 * Math.sqrt(dx * dx + dy * dy); }
	 * 
	 * public static float distanceFloat(short x1, short y1, short x2, short y2)
	 * { short dx = (short) (x1 - x2); short dy = (short) (y1 - y2); return
	 * (float) Math.sqrt(dx * dx + dy * dy); }
	 */
	/*
	 * public static float angle(short x1, short y1, short x2, short y2) {
	 * return (float) Math.atan2(y2 - y1, x2 - x1); }
	 * 
	 * public static float angle(short x1, short y1, short x12, short y12, short
	 * x2, short y2) { return (float) Math.atan2(y2 - y1 - y12 * 1f /
	 * Battle.COORD2, x2 - x1 - x12 * 1f / Battle.COORD2); }
	 * 
	 * public static short crossproduct(short x1, short y1, short x2, short y2)
	 * { return (short) (x1 * y2 - y1 * x2); }
	 */

	public static String booleanToString(boolean b) {
		if (b)
			return "on";
		return "off";
	}

	public static short[] inBounds(short x, short y) {
		if (x < 0)
			x = 0;
		if (x > Battle.width - 1)
			x = (short) (Battle.width - 1);
		if (y < 0)
			y = 0;
		if (y > Battle.height - 1)
			y = (short) (Battle.height - 1);
		return new short[] { x, y };
	}

	public static short[] inBounds(short[] xy) {
		return inBounds(xy[0], xy[1]);
	}

	public static float[] inBoundsFloat(float x, float y) {
		if (x < 0)
			x = 0;
		if (x > Battle.width - 1)
			x = (Battle.width - 1);
		if (y < 0)
			y = 0;
		if (y > Battle.height - 1)
			y = (Battle.height - 1);
		return new float[] { x, y };
	}

	public static float[] inBoundsFloat(float[] xy) {
		return inBoundsFloat(xy[0], xy[1]);
	}

	public static boolean isOutBounds(float x, float y) {
		if (x < 0)
			return true;
		if (x > Battle.width - 1)
			return true;
		if (y < 0)
			return true;
		if (y > Battle.height - 1)
			return true;
		return false;
	}

	/*
	 * public static short min(short a, short b, short c, short d) { short min =
	 * a; if (b < min) min = b; if (c < min) min = c; if (d < min) min = d;
	 * return min; }
	 */
	/*
	 * public static short max(short a, short b, short c, short d) { short max =
	 * a; if (b > max) max = b; if (c > max) max = c; if (d > max) max = d;
	 * return max; }
	 */

	public static boolean isZero(float n) {
		float e = .00001f;
		if (n < 0)
			n = -n;
		return n < e;
	}

	// public static float trimax(int a, int b, int c) {
	// int r = a > b ? a : b;
	// if (c > r)
	// return c;
	// return r;
	// }
}
