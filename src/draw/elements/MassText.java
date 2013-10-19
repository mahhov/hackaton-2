package draw.elements;

import draw.painter.Painter;

public class MassText extends Draw {

	private String[] text;

	public MassText(String[] text) {
		this.text = text;
	}

	public void paint(Painter painter) {
		painter.drawMassText(text);
	}

}
