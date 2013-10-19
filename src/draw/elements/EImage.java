package draw.elements;

import java.awt.image.BufferedImage;

import draw.painter.Painter;

public class EImage extends Draw {

	private float x0, y0, width, height;
	private BufferedImage image;

	public EImage(BufferedImage image, float x0, float y0, float width,
			float height) {
		this.image = image;
		this.x0 = x0;
		this.y0 = y0;
		this.width = width;
		this.height = height;
	}

	public void paint(Painter painter) {
		painter.drawImage(image, x0, y0, width, height);
	}

}
