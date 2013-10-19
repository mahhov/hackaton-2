package draw.manage;

import list.LinkedList;
import list.LinkedList.Node;
import player.Control;
import draw.elements.Draw;
import draw.painter.Painter;

public class Manager {
	private static final float PADDING = .01f;

	private LinkedList elements;
	private LinkedList interactableElements;
	private float left, right;
	private byte x, y;
	private float[] divy, divx;

	public Manager(float margin) {
		elements = new LinkedList();
		interactableElements = new LinkedList();
		setMargins(margin);
	}

	private void add(Manageable m) {
		if (m instanceof Interactable) {
			interactableElements.add(m);
		} else
			elements.add(m);
	}

	public void setMargins(float margin) {
		left = margin;
		right = 1 - margin;
		divy = new float[] { margin, 1 - margin };
		divx = new float[] { left, right };
	}

	public void divideVertical(float... divide) {
		divy = divide;
		divx = new float[] { left, right };
		y = 0;
		x = 0;
	}

	public void divideHorizontal(float... divide) {
		divx = divide;
		x = 0;
	}

	public void add(Manageable m, boolean fill) {
		addWithoutIncrement(m, fill);
		skip();
		add(m);
	}

	public void skip() {
		x++;
		if (x == divx.length - 1) {
			divideHorizontal(left, right);
			y++;
		}
	}

	private void addWithoutIncrement(Manageable m, boolean fill) {
		m.setPosition(divx[x] + PADDING, divy[y] + PADDING);
		if (fill) {
			m.setWidth(divx[x + 1] - divx[x] - PADDING * 2);
			m.setHeight(divy[y + 1] - divy[y] - PADDING * 2);
		}
	}

	public void addToRow(boolean fillHeight, float extraPadding,
			byte rightAlign, Manageable... m) {
		// rightAlign is the number of elements right aligned
		byte leftAlign = (byte) (m.length - rightAlign);
		Manageable mm;

		float penx = divx[x] + PADDING + extraPadding;
		for (byte i = 0; i < leftAlign; i++) {
			mm = m[i];
			mm.setPosition(penx, divy[y] + PADDING);
			penx += mm.getWidth() + PADDING * 1.5f;
			if (fillHeight)
				mm.setHeight(divy[y + 1] - divy[y] - PADDING * 2);
			add(mm);
		}

		penx = divx[x + 1] + PADDING - extraPadding;
		for (byte i = 0; i < rightAlign; i++) {
			mm = m[m.length - 1 - i];
			penx -= mm.getWidth() + PADDING * 1.5f;
			mm.setPosition(penx, divy[y] + PADDING);
			if (fillHeight)
				mm.setHeight(divy[y + 1] - divy[y] - PADDING * 2);
			add(mm);
		}

		x++;
		if (x == divx.length - 1) {
			divideHorizontal(left, right);
			y++;
		}
	}

	public void mouse(Control control) {
		mouseOver(control.getMouseX(), control.getMouseY());
		if (control.getMousePress())
			mousePress();
		else if (control.getMouseRelease())
			mouseRelease();
	}

	private void mouseOver(float x, float y) {
		for (Node n : interactableElements)
			((Interactable) n.item).mouseOver(x, y);
	}

	private void mousePress() {
		for (Node n : interactableElements)
			((Interactable) n.item).mousePress();
	}

	private void mouseRelease() {
		for (Node n : interactableElements)
			((Interactable) n.item).mouseRelease();
	}

	public void addToDraw(Painter p) {
		for (Node n : elements)
			p.addForeground((Draw) n.item);
		for (Node n : interactableElements)
			p.addForeground((Draw) n.item);
	}

}
