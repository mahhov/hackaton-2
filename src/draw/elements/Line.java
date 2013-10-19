package draw.elements;

import java.awt.Color;

import draw.painter.Painter;

public class Line extends Draw {

	private float x0, y0, x1, y1;
	private byte width;
	private Color color;

	public Line(float x0, float y0, float x1, float y1, byte width) {
		this(x0, y0, x1, y1, width, Color.BLACK);
	}

	public Line(float x0, float y0, float x1, float y1, byte width, Color color) {
		this.x0 = x0;
		this.y0 = y0;
		this.x1 = x1;
		this.y1 = y1;
		this.width = width;
		this.color = color;
	}

	public void paint(Painter painter) {
		painter.drawLine(x0, y0, x1, y1, width, color);
	}

}
