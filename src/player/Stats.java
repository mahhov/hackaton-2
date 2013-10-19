package player;

import java.awt.Color;

import resource.ColorList;

import world.Battle;
import draw.elements.Rectangle;
import draw.painter.Painter;

public class Stats {
	short points;
	short enemyPoints;
	short cap = 15;

	Color myColor, myColorback;
	Color enemyColor, enemyColorback;

	Stats(byte color) {
		points = 0;
		enemyPoints = 0;
		// myColor = Battle.getColor(color);
		// enemyColor = Battle.getColor(Battle.getOtherColor(color));
		myColor = ColorList.getStatusColorScheme(color)[0];
		myColorback = ColorList.getStatusColorScheme(color)[1];
		enemyColor = ColorList
				.getStatusColorScheme(Battle.getOtherColor(color))[0];
		enemyColorback = ColorList.getStatusColorScheme(Battle
				.getOtherColor(color))[1];
	}

	public void paint(Painter painter) {
		float height = .020f;
		float width = .3f;
		float margin = .008f;
		float left = 1 - width - margin;
		float top = margin;

		// your points
		painter.addForeground(new Rectangle(left, top, width, height, (byte) 1,
				myColorback, true));
		float fill = 1f * points / cap;
		painter.addForeground(new Rectangle(left, top, width * fill, height,
				(byte) 1, myColor, true));
		painter.addForeground(new Rectangle(left, top, width, height, (byte) 2,
				Color.WHITE, false));

		// enemy points
		top += height + margin;
		painter.addForeground(new Rectangle(left, top, width, height, (byte) 1,
				enemyColorback, true));
		fill = 1f * enemyPoints / cap;
		painter.addForeground(new Rectangle(left, top, width * fill, height,
				(byte) 1, enemyColor, true));
		painter.addForeground(new Rectangle(left, top, width, height, (byte) 2,
				Color.WHITE, false));
	}
}
