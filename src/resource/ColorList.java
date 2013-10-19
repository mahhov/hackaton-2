package resource;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import world.Battle;

public class ColorList {

	public static final byte LEFT_DIR = 0, RIGHT_DIR = 1;
	public static final byte GLIDING = 0, HANGING = 1, JUMPING = 2,
			LOOKING = 3, WALKING_PRE = 4, WALKING_POST = 5, SLAM = 6;
	public static final BufferedImage PLAYER_IMAGE[][] = createPlayerImage();

	private static BufferedImage[][] createPlayerImage() {
		String[][] imageFiles = new String[7][2];
		imageFiles[GLIDING][LEFT_DIR] = "gliding left";
		imageFiles[GLIDING][RIGHT_DIR] = "gliding right";
		imageFiles[HANGING][LEFT_DIR] = "hanging left";
		imageFiles[HANGING][RIGHT_DIR] = "hanging right";
		imageFiles[JUMPING][LEFT_DIR] = "jumping";
		imageFiles[JUMPING][RIGHT_DIR] = "jumping";
		imageFiles[LOOKING][LEFT_DIR] = "looking left";
		imageFiles[WALKING_PRE][LEFT_DIR] = "looking left 3";
		imageFiles[WALKING_POST][LEFT_DIR] = "looking left 2";
		imageFiles[LOOKING][RIGHT_DIR] = "looking right";
		imageFiles[WALKING_PRE][RIGHT_DIR] = "looking right 3";
		imageFiles[WALKING_POST][RIGHT_DIR] = "looking right 2";
		imageFiles[SLAM][LEFT_DIR] = "slam";
		imageFiles[SLAM][RIGHT_DIR] = "slam";

		BufferedImage[][] img = new BufferedImage[imageFiles.length][2];
		try {
			for (byte i = 0; i < imageFiles.length; i++)
				for (byte j = 0; j < 2; j++) {
					// System.out.println("loading " + imageFiles[i]);
					img[i][j] = ImageIO.read(new File("imgs2/"
							+ imageFiles[i][j] + ".png"));
					img[i][j] = makeColorTransparent(img[i][j], Color.white);
				}
		} catch (IOException e) {
			System.out.println("trouble loading image");
		}

		return img;

		// int iw = 100, ih = 200;
		// BufferedImage img = new BufferedImage(iw, ih,
		// BufferedImage.TYPE_INT_ARGB);
		// Graphics2D g = img.createGraphics();

		// int w = 50;
		// int h = 50;
		// g.setColor(new Color(220, 0, 0));
		// g.fillArc(((iw - w) / 2), (h / 2), w, h, 0, 360);

		// // g.setColor(new Color(0, 255, 0));
		// // g.fillRect(0, 0, 100, 200);
		// g.setColor(new Color(197, 20, 97));
		// g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 40));
		// float h = g.getFontMetrics().getHeight();
		// g.drawString("\\*/", 0, h);
		// g.drawString(" |", 0, 2 * h);
		// g.drawString("/ \\", 0, 3 * h);
		// // g.drawChars(new char[] { 'A' }, 0, 0, 0, 0);
	}

	public static final Color RED = new Color(0xC51461);
	public static final Color BLUE = new Color(0x1461C5);
	public static final Color CYAN = new Color(0x399E82);
	public static final Color OUTLINE = new Color(0xC323C9);

	public static final Color BROWN = new Color(0x585240);
	public static final Color WHITE = new Color(190, 190, 190);

	public static final byte PRIMARY = 0, SECONDARY = 1, TERNARY = 2;
	public static final Color[] SCHEME_RED = new Color[] {
			new Color(200, 50, 50), new Color(150, 50, 50),
			new Color(100, 50, 50) };
	public static final Color[] SCHEME_BLUE = new Color[] {
			new Color(50, 50, 200), new Color(50, 50, 150),
			new Color(50, 50, 100) };
	public static final Color[] SCHEME_GRAY = new Color[] {
			new Color(150, 150, 150), new Color(100, 100, 100),
			new Color(50, 50, 50) };

	public static final Color[] SCHEME_STAT_RED = new Color[] {
			new Color(0xC56114), SCHEME_RED[2] };
	public static final Color[] SCHEME_STAT_BLUE = new Color[] { BLUE,
			SCHEME_BLUE[2] };

	public static Color[] getColorScheme(byte color) {
		if (color == Battle.RED)
			return SCHEME_RED;
		if (color == Battle.BLUE)
			return SCHEME_BLUE;
		return SCHEME_GRAY;
	}

	public static BufferedImage makeColorTransparent(BufferedImage img,
			final Color color) {
		final ImageFilter filter = new RGBImageFilter() {
			// the color we are looking for (white)... Alpha bits are set to
			// opaque
			public int markerRGB = color.getRGB() | 0xFF000000;

			public final int filterRGB(final int x, final int y, final int rgb) {
				if ((rgb | 0xFF000000) == markerRGB) {
					// Mark the alpha bits as zero - transparent
					return 0x00FFFFFF & rgb;
				} else if ((rgb | 0xFF000000) == 0xFF000000) {
					return 0xFFFFFFFF;
				} else {
					// nothing to do
					return rgb;
				}
			}
		};

		ImageProducer ip = new FilteredImageSource(img.getSource(), filter);
		Image temp = Toolkit.getDefaultToolkit().createImage(ip);

		BufferedImage bufferedImage = new BufferedImage(temp.getWidth(null),
				temp.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = bufferedImage.createGraphics();
		g2.drawImage(temp, 0, 0, null);
		g2.dispose();
		return bufferedImage;
	}

	public static Color[] getStatusColorScheme(byte color) {
		if (color == Battle.RED)
			return SCHEME_STAT_RED;
		if (color == Battle.BLUE)
			return SCHEME_STAT_BLUE;
		return SCHEME_GRAY;
	}
}