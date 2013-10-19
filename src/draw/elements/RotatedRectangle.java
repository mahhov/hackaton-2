package draw.elements;

import java.awt.Color;

import draw.painter.Painter;

public class RotatedRectangle extends Draw {

	private float x[], y[];
	private byte lineWidth;
	private Color color;
	private boolean fill;

	public RotatedRectangle(float cx, float cy, float width, float height,
			float angleCos, float angleSin, byte lineWidth, Color color,
			boolean fill) {
		this.lineWidth = lineWidth;
		this.color = color;
		this.fill = fill;

		width /= 2;
		height /= 2;
		x = new float[4];
		y = new float[4];

		x[0] = cx + height * angleCos - width * angleSin;
		y[0] = cy + height * angleSin + width * angleCos;

		x[1] = cx + height * angleCos + width * angleSin;
		y[1] = cy + height * angleSin - width * angleCos;

		x[2] = cx - height * angleCos + width * angleSin;
		y[2] = cy - height * angleSin - width * angleCos;

		x[3] = cx - height * angleCos - width * angleSin;
		y[3] = cy - height * angleSin + width * angleCos;
	}

	public void paint(Painter painter) {
		painter.drawPolygon(x, y, lineWidth, color, 1, fill);
	}

}
