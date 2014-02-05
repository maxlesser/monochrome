package mllf.gameengine.ui;

import java.awt.Graphics2D;

/**
 * Drawable should be implemented by any visual class.
 * 
 * @author lfiorant
 */
public interface Overlayable {
	public void drawOnTop(Graphics2D g, Viewport viewport);
}
