package draw.painter;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import player.GameControl;

public class PainterLwjgl extends Painter {

	private LwjglInputRedirect inputRedirect;
	private boolean closeDisplay;

	public PainterLwjgl(GameControl control, int x, int y) {
		super(x, y);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				closeDisplay = true;
			}
		});

		inputRedirect = new LwjglInputRedirect(FRAME_SIZE, control);
	}

	private void createDisplay() {
		try {
			Display.setDisplayMode(new DisplayMode(FRAME_SIZE, FRAME_SIZE));
			Canvas c = new Canvas();
			c.setSize(FRAME_SIZE, FRAME_SIZE);
			add(c);
			Display.setParent(c);
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}

	// toggle pause
	public void pause() {
	}

	public void unpause() {
	}

	public void paint() {
		if (!Display.isCreated())
			createDisplay();
		if (closeDisplay) {
			Display.destroy();
			System.exit(0);
		} else {
			paintLayers();

			Display.update();
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
			inputRedirect.redirect();
		}
	}

	public void drawLine(float x0, float y0, float x1, float y1, int thickness,
			Color color) {
	}

	public void drawRectangle(float x0, float y0, float width, float height,
			int thickness, Color color, boolean fill) {
	}

	public void drawCircle(float cx, float cy, float width, float height,
			short startAngle, short spanAngle, int thickness, Color color,
			boolean fill) {
	}

	public void drawPolygon(float[] x, float[] y, int thickness, Color color,
			float light, boolean fill) {
		GL11.glColor3f(color.getRed() * light / 255, color.getGreen() * light
				/ 255, color.getBlue() * light / 255);
		GL11.glBegin(GL11.GL_POLYGON);
		for (byte i = 0; i < x.length; i++)
			GL11.glVertex2d(x[i] * 2 - 1, -y[i] * 2 + 1);
		GL11.glEnd();
	}

	public void drawMassText(String[] text) {
	}

	public void drawText(String text, float size, Color color, float x, float y) {
	}

	public void drawCenteredText(String text, float size, Color color, float x,
			float y, float width, float height, boolean horizontallyAligned) {
	}

}