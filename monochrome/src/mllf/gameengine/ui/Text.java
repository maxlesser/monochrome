package mllf.gameengine.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import cs195n.Vec2f;
import mllf.gameengine.ui.Graphics;

/**
 * A text object whose size is defined by a bounding box. The maximum font size that fills the
 * bounding box without crossing the bounds is chosen using binary search.
 * If text is instantiated with no x bound or no y bound, the text's size will only be constrained
 * by the other non-zero bound. This is useful when line length is an issue but height is not.
 * 
 * If the displayed text is changed, the font does not get resized. This is to avoid dynamically
 * changing font size for any in-game text that is updated. However, there exists a concept of
 * "maxText", which represents the estimated longest string that will inhabit the bounding box. This
 * maximum-length string defines the Font that is used at this current size; the Font only gets 
 * resized if the bounding box is resized.
 * 
 * @author lfiorant
 */
public class Text {
	
	private final static float START_SIZE = 200f;
	private String maxText, text;
	private Vec2f position, size, actualSize;
	private Font font;
	private Color color;
	private boolean needsResize, isVisible = true;

	public Text(String maxText, String text, Vec2f position, Vec2f size, Font font, Color color) {
		this.maxText = maxText;
		this.text = text;
		this.position = position;
		this.size = size;
		this.font = font;
		this.color = color;
		needsResize = true;
	}
	
	public Text(String text, Vec2f position, Vec2f size, Font font, Color color) {
		this(text, text, position, size, font, color);
	}
	
	public void setPosition(Vec2f pos) {
		position = pos;
	}
	
	public void setSize(Vec2f size) {
		this.size = size;
		needsResize = true;
	}
	
	public void setColor(Color c) {
		color = c;
	}
	
	public void setText(String t) {
		text = t;
	}
	
	public void setVisible(boolean b) {
		isVisible = b;
	}
	
	public Vec2f getPosition() {
		return position;
	}
	
	public Vec2f getSize() {
		return size;
	}
	/**
	 * Returns the size of the text as determined by FontMetrics. Special case for first draw
	 * when FontMetrics is inaccessible due to not having a Graphics2D object.
	 * @return
	 */
	public Vec2f getActualSize() {
		return (actualSize != null) ? actualSize : size;
	}
	
	public Color getColor() {
		return color;
	}
	/**
	 * Calculates the optimal font size depending on the number of non-zero bounds provided.
	 */
	private Font getSizedFont(Graphics2D g) {
		float currSize = START_SIZE;
		float diff = START_SIZE / 2;
		Font newFont = font.deriveFont(currSize);
		FontMetrics fm = g.getFontMetrics(newFont);
		int height = fm.getHeight();
		int width = fm.stringWidth(maxText);
		
		if (size.x == 0 && size.y == 0) {
			return font;
		} else if (size.x == 0) {
			return this.calculateSizeFromYBound(g, currSize, diff, newFont, fm, height);
		} else if (size.y == 0) {
			return this.calculateSizeFromXBound(g, currSize, diff, newFont, fm, width);
		} else {
			return this.calculateSizeFromBounds(g, currSize, diff, newFont, fm, height, width);
		}
	}
	
	private Font calculateSizeFromYBound(Graphics2D g, float currSize, float diff, Font newFont,
			FontMetrics fm, int height) {
		while (!(height <= size.y && g.getFontMetrics(newFont.deriveFont(currSize + 1f)).getHeight()
				> size.y)) {
			if (height > size.y) {
				currSize += diff;
			} else {
				currSize -= diff;
			}
			diff /= 2;
			newFont = newFont.deriveFont(currSize);
			fm = g.getFontMetrics(newFont);
			height = fm.getHeight();
			if (diff < 1) {
				break;
			}
		}
		return newFont;
	}
	
	private Font calculateSizeFromXBound(Graphics2D g, float currSize, float diff, Font newFont,
			FontMetrics fm, int width) {
		while (!(width <= size.x && g.getFontMetrics(newFont.deriveFont(currSize + 1f)).stringWidth(maxText)
				> size.x)) {
			if (width > size.x) {
				currSize -= diff;
			} else {
				currSize += diff;
			}
			diff /= 2;
			newFont = newFont.deriveFont(currSize);
			fm = g.getFontMetrics(newFont);
			width = fm.stringWidth(maxText);
			if (diff < 1) {
				break;
			}
		}
		return newFont;
	}
	
	private Font calculateSizeFromBounds(Graphics2D g, float currSize, float diff, Font newFont,
			FontMetrics fm, int height, int width) {		
		while (!(width <= size.x && height <= size.y
				&& (g.getFontMetrics(newFont.deriveFont(currSize + 1f)).stringWidth(maxText)
				> size.x || g.getFontMetrics(newFont.deriveFont(currSize + 1f)).getHeight()
				> size.y))) {
			if (width > size.x || height > size.y) {
				currSize -= diff;
			} else {
				currSize += diff;
			}
			diff /= 2;
			newFont = newFont.deriveFont(currSize);
			fm = g.getFontMetrics(newFont);
			height = fm.getHeight();
			width = fm.stringWidth(maxText);
			if (diff < 1) {
				break;
			}
		}
		return newFont;
	}
	
	public void draw(Graphics2D g) {
		Graphics.addTextAntialias(g);
		if (needsResize) {
			font = this.getSizedFont(g);
			actualSize = new Vec2f(g.getFontMetrics(font).stringWidth(text),
					g.getFontMetrics(font).getHeight());
			needsResize = false;
		}
		if (isVisible) {
			g.setColor(color);
			g.setFont(font);
			g.drawString(text, position.x, position.y);
		}
	}

}
