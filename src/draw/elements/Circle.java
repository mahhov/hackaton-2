package draw.elements;

import java.awt.Color;

import draw.painter.Painter;

public class Circle extends Draw {

	private float cx, cy;
	private float diameter;
	private short startAngle, spanAngle;
	private byte lineWidth;
	private Color color;
	private boolean fill;

	public Circle(float cx, float cy, float diameter, byte lineWidth,
			Color color) {
		this(cx, cy, diameter, (short) 0, (short) 360, lineWidth, color, false);
	}

	public Circle(float cx, float cy, float diameter, Color color, boolean fill) {
		this(cx, cy, diameter, (short) 0, (short) 360, (byte) 1, color, fill);
	}

	public Circle(float cx, float cy, float diameter, short startAngle,
			short endAngle, byte lineWidth, Color color, boolean fill) {
		this.cx = cx;
		this.cy = cy;
		this.diameter = diameter;
		this.startAngle = startAngle;
		this.spanAngle = (short) (endAngle - startAngle);
		this.lineWidth = lineWidth;
		this.color = color;
		this.fill = fill;
	}

	public void paint(Painter painter) {
		painter.drawCircle(cx - diameter / 2, cy - diameter / 2, diameter,
				diameter, startAngle, spanAngle, lineWidth, color, fill);
	}

}
