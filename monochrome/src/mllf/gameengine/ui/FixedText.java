package mllf.gameengine.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import cs195n.Vec2f;

/**
 * Fixed size text. Text will always be drawn at the determined font size.
 * 
 * @author lfiorant
 */
public class FixedText {
	
	private String text;
	private Vec2f center;
	private Font font;
	private Color color;
	private boolean isVisible = true;

	public FixedText(String text, Vec2f center, Font font, Color color) {
		this.text = text;
		this.center = center;
		this.color = color;
		this.font = font;
	}
	
	public void setCenter(Vec2f center) {
		this.center = center;
	}
	
	public void setFontSize(float newSize) {
		font = font.deriveFont(newSize);
		
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
	
	public String getText() {
		return text;
	}
	
	public Vec2f getCenter() {
		return center;
	}
	
	public Color getColor() {
		return color;
	}
	
	public void draw(Graphics2D g) {
		Graphics.addTextAntialias(g);
		if (isVisible) {
			g.setColor(color);
			g.setFont(font);
			FontMetrics fm = g.getFontMetrics(font);
			int width = fm.stringWidth(text);
			Vec2f position = center.minus(new Vec2f(width/2, 0));
			g.drawString(text, position.x, position.y);
		}
	}

}
