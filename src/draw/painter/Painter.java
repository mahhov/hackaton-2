package draw.painter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import list.Queue;
import draw.elements.Draw;

public abstract class Painter extends JFrame implements Drawer {
	public static final int FRAME_SIZE = 800;

	public int count;
	private boolean pause;

	boolean fade;

	Queue<Draw> foreground;
	Queue<Draw> midground;
	Queue<Draw> background;
	Queue<Draw> overlay;

	String[] write;

	Painter(int x, int y) {
		super();
		setLocation(x, y);

		setResizable(false);
		setUndecorated(true);
		setPreferredSize(new Dimension(FRAME_SIZE, FRAME_SIZE));
		pack();
		setVisible(true);

		foreground = new Queue<Draw>();
		midground = new Queue<Draw>();
		background = new Queue<Draw>();
		overlay = new Queue<Draw>();

		write = new String[5];
		for (int i = 0; i < write.length; i++)
			write[i] = "";
	}

	// toggle pause
	public void pause() {
		pause = !pause;
		dispose();
		setUndecorated(!pause);
		setVisible(true);
	}

	public void unpause() {
		if (pause)
			pause();
	}

	public void write(String s, int line) {
		if (s != null && line < write.length && line >= 0)
			write[line] = s;
		else
			System.out.println("painter.write error");
	}

	public void setFade(boolean fade) {
		this.fade = fade;
	}

	public abstract void paint();

	public void addForeground(Draw d) {
		foreground.add(d);
	}

	public void addMidground(Draw d) {
		midground.add(d);
	}

	public void addBackground(Draw d) {
		background.add(d);
	}

	public void addOverlay(Draw d) {
		overlay.add(d);
	}

	void paintLayers() {
		Draw d;
		while ((d = background.remove()) != null)
			d.paint(this);
		while ((d = midground.remove()) != null)
			d.paint(this);
		while ((d = foreground.remove()) != null)
			d.paint(this);
		while ((d = overlay.remove()) != null)
			d.paint(this);
	}

	public boolean inScreen(float[] x, float[] y) {
		for (byte i = 0; i < x.length; i++)
			if (x[i] > 0 && x[i] < 1 && y[i] > 0 && y[i] < 1)
				return true;
		return false;
	}

	public abstract void drawLine(float x0, float y0, float x1, float y1,
			int thickness, Color color);

	public abstract void drawRectangle(float x0, float y0, float width,
			float height, int thickness, Color color, boolean fill);

	public abstract void drawCircle(float cx, float cy, float width,
			float height, short startAngle, short spanAngle, int thickness,
			Color color, boolean fill);

	public void drawPolygonIfInScreen(float[] x, float[] y, int thickness,
			Color color, float light, boolean fill) {
		if (inScreen(x, y))
			drawPolygon(x, y, thickness, color, light, fill);
	}

	public abstract void drawPolygon(float[] x, float[] y, int thickness,
			Color color, float light, boolean fill);

	public abstract void drawMassText(String[] text);

	public abstract void drawText(String text, float size, Color color,
			float x, float y);

	public abstract void drawCenteredText(String text, float size, Color color,
			float x, float y, float width, float height,
			boolean horizontallyAligned);

	public abstract void drawImage(BufferedImage image, float x0, float y0,
			float width, float height);

}
