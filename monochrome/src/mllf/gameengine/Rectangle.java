package mllf.gameengine;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import cs195n.Vec2f;

/**
 * The non-game world equivalent of an AAB. Used primarily for non-game screen GUI elements.
 * 
 * @author lfiorant
 */
public class Rectangle {
	
	private Vec2f position, size;
	private Color color;
	private BasicStroke stroke;
	private boolean isFilled;
	private BufferedImage image;

	public Rectangle(Vec2f position, Vec2f size) {
		this.position = position;
		this.size = size;
		color = Color.BLACK;
		stroke = new BasicStroke();
		isFilled = true;
	}
	
	public Rectangle(Vec2f position, Vec2f size, BufferedImage image) {
		this.position = position;
		this.size = size;
		color = Color.BLACK;
		stroke = new BasicStroke();
		isFilled = true;
		this.image = image;
	}
	
	public void setPosition(Vec2f position) {
		this.position = position;
	}
	
	public void setSize(Vec2f size) {
		this.size = size;
	}
	
	public void setColor(Color c) {
		color = c;
	}
	
	public void setStroke(BasicStroke s) {
		stroke = s;
	}
	
	public void setFill(boolean b) {
		isFilled = b;
	}
	
	public Vec2f getPosition() {
		return position;
	}
	
	public Vec2f getSize() {
		return size;
	}
	
	public Color getColor() {
		return color;
	}
	
	public BasicStroke getStroke() {
		return stroke;
	}
	
	public boolean getFill() {
		return isFilled;
	}
	
	public void draw(Graphics2D g) {
		if (image == null) {
			Rectangle2D.Float rect = new Rectangle2D.Float(position.x, position.y, size.x, size.y);
			g.setColor(color);
			g.setStroke(stroke);
			g.draw(rect);
			if (isFilled) {
				g.fill(rect);
			}
		} else {
			g.drawImage(
					image,
					(int) position.x,
					(int) position.y,
					(int) size.x,
					(int) size.y,
					null);
		}
	}
}