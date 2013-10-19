package draw.elements;

import java.awt.Color;

import draw.manage.Manageable;
import draw.painter.Painter;

public class Rectangle extends Draw implements Manageable {

	private float x0, y0, width, height;
	private byte lineWidth;
	private Color color;
	private boolean fill;

	public Rectangle(float x0, float y0, float width, float height,
			byte lineWidth, Color color, boolean fill) {
		this.x0 = x0;
		this.y0 = y0;
		this.width = width;
		this.height = height;
		this.lineWidth = lineWidth;
		this.color = color;
		this.fill = fill;
	}

	public Rectangle(float x0, float y0, float x1, float y1, byte lineWidth,
			Color color, boolean fill,
			@SuppressWarnings("unused") boolean corners) {
		width = x1 - x0;
		height = y1 - y0;

		if (width > 0)
			this.x0 = x0;
		else {
			width = -width;
			this.x0 = x1;
		}

		if (height > 0)
			this.y0 = y0;
		else {
			height = -height;
			this.y0 = y1;
		}

		this.lineWidth = lineWidth;
		this.color = color;
		this.fill = fill;
	}

	public void setPosition(float x, float y) {
		x0 = x;
		y0 = y;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getWidth() {
		return width;
	}

	public void paint(Painter painter) {
		painter.drawRectangle(x0, y0, width, height, lineWidth, color, fill);
	}

}
