package draw.elements;

import java.awt.Color;

import draw.painter.Painter;

public class Polygon extends Draw {

	float x[], y[];
	byte thickness;
	Color color;
	float light;
	boolean fill;

	public Polygon(float[] x, float[] y, byte thickness, Color color,
			float light, boolean fill) {
		this.x = x;
		this.y = y;
		this.thickness = thickness;
		this.color = color;
		this.light = light;
		this.fill = fill;
	}

	public Polygon(float cx, float cy, float forwardx, float forwardy,
			float sidex, float sidey, byte thickness, Color color, boolean fill) {
		this.thickness = thickness;
		this.color = color;
		this.fill = fill;

		x = new float[] { cx + forwardx, cx + sidex, cx - forwardx, cx - sidex };
		y = new float[] { cy + forwardy, cy + sidey, cy - forwardy, cy - sidey };

		light = 1;
	}

	public void paint(Painter painter) {
		painter.drawPolygonIfInScreen(x, y, thickness, color, light, fill);
	}

}
