package player;

import java.awt.Color;
import java.awt.image.BufferedImage;

import resource.ColorList;

import draw.elements.EImage;
import draw.elements.Rectangle;
import draw.elements.Text;
import draw.painter.Painter;

public class Panel {
	public static final byte LENGTH = 7;

	protected static final float SIZE = .08f;
	protected static final float MARGIN = .01f;
	protected static final float TOP = 1 - MARGIN - SIZE;
	protected static final float WIDTH = SIZE + MARGIN;
	protected static final float TEXT_SHIFT = MARGIN / 2;
	protected static final float TEXT_HEIGHT = .02f;
	protected static final float TEXT_WIDTH = .015f;

	private byte highlight;
	private byte length;
	private BufferedImage[] image;
	private String[] text;

	private float bar;

	Panel(BufferedImage[] image, String[] text) {
		highlight = 0;
		length = (byte) text.length;
		this.image = image;
		this.text = text;
	}

	Panel() {
		highlight = 0;

		// laser, rocket, make walls, heal, mass generator, teleport, invisible
		text = new String[] { "Throw Poop", "Pea Shooter", "Make them Tremble",
				"Eat Cake", "Mass Fabricator", "Monkey Fly",
				"Harry's Invisibility Cloak" };

		length = (byte) text.length;

		image = new BufferedImage[length];
	}

	public void setHighlight(byte highlight) {
		this.highlight = highlight;
		if (this.highlight < 0)
			this.highlight = 0;
		if (this.highlight >= length)
			this.highlight = (byte) (length - 1);
	}

	public void moveHighlight(byte direction) {
		highlight += direction;
		if (highlight < 0)
			highlight = 0;
		if (highlight >= length)
			highlight = (byte) (length - 1);
	}

	public byte getHighlight() {
		return highlight;
	}

	public void paint(Painter painter) {
		float x = MARGIN;
		float y = TOP - TEXT_SHIFT;

		for (byte i = 0; i < length; i++) {
			// text
			if (highlight == i) {
				painter.addForeground(new Text(text[i], x + TEXT_SHIFT, y
						- TEXT_HEIGHT * 2));
			}
			// outline
			painter.addForeground(new Rectangle(x, TOP, SIZE, SIZE, (byte) 1,
					Color.LIGHT_GRAY, false));
			// image
			if (image[i] != null)
				painter.addForeground(new EImage(image[i], x, TOP, SIZE, SIZE));
			x += WIDTH;
		}

		// bar
		float barWidth = .03f;
		float barHeight = .3f;
		float barMargin = MARGIN * 3;
		float barFill = barHeight * bar;
		// System.out.println(bar);
		// bar fill
		painter.addForeground(new Rectangle(1 - barMargin - barWidth, 1
				- barMargin - barFill, barWidth, barFill, (byte) 1,
				ColorList.CYAN, true));
		// bar outline
		painter.addForeground(new Rectangle(1 - barMargin - barWidth, 1
				- barMargin - barHeight, barWidth, barHeight, (byte) 2,
				Color.LIGHT_GRAY, false));
	}

	public void setBar(float value) {
		bar = value;
	}

}
