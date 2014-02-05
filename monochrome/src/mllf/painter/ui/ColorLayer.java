package mllf.painter.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import mllf.gameengine.ui.Layer;
import cs195n.Vec2f;

public class ColorLayer extends Layer {
	
	private Color color;

	public ColorLayer(Vec2f position, Vec2f size, int depth, Color color) {
		super(position, size, depth);
		this.color = color;
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(color);
		Rectangle2D.Float rect = new Rectangle2D.Float(position.x, position.y, size.x, size.y);
		g.draw(rect);
		g.fill(rect);
	}

}
