package draw.elements;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Toolkit;

import draw.manage.Manageable;
import draw.painter.Painter;
import draw.painter.PainterJava;

public class Text extends Draw implements Manageable {

	String text;
	byte size;
	float x, y;

	public Text(String text, float x, float y) {
		this(text, (byte) 12, x, y);
	}

	public Text(String text, byte size, float x, float y) {
		this.text = text;
		this.size = size;
		this.x = x;
		this.y = y;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void setWidth(float width) {
	}

	public void setHeight(float height) {
	}

	public float getWidth() {
		Font font = PainterJava.BODY_FONT.deriveFont((float) size);
		@SuppressWarnings("deprecation")
		FontMetrics metric = Toolkit.getDefaultToolkit().getFontMetrics(font);
		float textWidth = metric.stringWidth(text) * 1f / Painter.FRAME_SIZE;
		return textWidth;
	}

	public void paint(Painter painter) {
		painter.drawText(text, size, Color.BLACK, x, y);
	}

}
