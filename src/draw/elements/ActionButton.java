package draw.elements;

import java.awt.Color;

import draw.manage.Interactable;
import draw.manage.Manageable;
import draw.painter.Painter;

public class ActionButton extends Draw implements Manageable, Interactable {

	private String text;
	private byte fontSize;

	private float x0, y0, width, height;
	private byte lineWidth;
	private Color color;
	private boolean fill;

	private boolean highlight;
	private Color highlightColor;
	private boolean press;

	public ActionButton(String text, byte fontSize, float x0, float y0,
			float width, float height, byte lineWidth, Color color,
			Color highlightColor, boolean fill) {
		this.text = text;
		this.fontSize = fontSize;
		this.x0 = x0;
		this.y0 = y0;
		this.width = width;
		this.height = height;
		this.lineWidth = lineWidth;
		this.color = color;
		this.highlightColor = highlightColor;
		this.fill = fill;
	}

	public ActionButton(String text, byte fontSize, float x0, float y0,
			float width, float height) {
		this(text, fontSize, x0, y0, width, height, (byte) 2, Color.WHITE,
				Color.LIGHT_GRAY, true);
	}

	public void setText(String text) {
		this.text = text;
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

	public void mouseOver(float x, float y) {
		highlight = (x >= x0 && y >= y0 && x <= x0 + width && y <= y0 + height);
		if (highlight == false)
			press = false;
	}

	public void mousePress() {
		if (highlight)
			press = true;
	}

	public void mouseRelease() {
		press = false;
	}

	public boolean isPressed() {
		return highlight && press;
	}

	public boolean isMouseOver() {
		return highlight;
	}

	public void paint(Painter painter) {
		if (highlight)
			painter.drawRectangle(x0, y0, width, height, lineWidth,
					highlightColor, fill);
		else
			painter.drawRectangle(x0, y0, width, height, lineWidth, color, fill);
		painter.drawRectangle(x0, y0, width, height, lineWidth, Color.BLACK,
				false);
		painter.drawCenteredText(text, fontSize, Color.BLACK, x0, y0, width,
				height, true);
	}

}
