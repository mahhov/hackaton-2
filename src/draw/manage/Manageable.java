package draw.manage;

import list.ListItem;

public interface Manageable extends ListItem {

	abstract void setPosition(float x, float y);

	abstract void setWidth(float width);

	abstract void setHeight(float height);

	abstract float getWidth();
}
