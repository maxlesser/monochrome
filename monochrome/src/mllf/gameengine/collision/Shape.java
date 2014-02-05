package mllf.gameengine.collision;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import mllf.gameengine.ui.Viewport;
import cs195n.Vec2f;

/**
 * Abstract superclass for any shape that can be defined by a position and a bounding box.
 * 
 * @author lfiorant
 */
public abstract class Shape {

	protected Color color;
	protected BasicStroke stroke;
	protected boolean isFilled;
    protected org.jbox2d.collision.shapes.Shape shape;
	
	public Shape(Vec2f position, Vec2f size) {
		color = Color.BLACK;
		stroke = new BasicStroke();
		isFilled = true;
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

	public Color getColor() {
		return color;
	}
	
	public BasicStroke getStroke() {
		return stroke;
	}
	
	public boolean getFill() {
		return isFilled;
	}
	
	public abstract void draw(Graphics2D g, Viewport viewport);
	public abstract void tick(float seconds);
    public abstract Vec2f getLocation();
    public abstract Vec2f getSize();
}
