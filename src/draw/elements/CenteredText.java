package draw.elements;

import java.awt.Color;

import draw.painter.Painter;

public class CenteredText extends Text {

	private float width, height;
	private boolean centerHorizontally;
	private boolean outline;

	public CenteredText(String text, byte size, float x, float y, float width,
			float height, boolean centerHorizontally, boolean outline) {
		super(text, size, x, y);
		if (width != -1)
			this.width = width;
		else
			this.width = super.getWidth();
		this.height = height;
		this.centerHorizontally = centerHorizontally;
		this.outline = outline;
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
		if (outline)
			painter.drawRectangle(x, y, width, height, (byte) 3, Color.BLACK,
					false);
		painter.drawCenteredText(text, size, Color.BLACK, x, y, width, height,
				centerHorizontally);
	}

}
