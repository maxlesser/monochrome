package mllf.gameengine.entity;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import mllf.gameengine.ui.Viewport;
import cs195n.Vec2f;
import mllf.gameengine.ui.Graphics;

public class TextBox {
	
	private String text;
	private Font font;
	private Color color;
	private float width, height;
	private boolean initialized;
	private ArrayList<Line> lines;
	private final int padding = 2;
	private int standardLineHeight;
	private final int yOffset = 7;
	private PhysicsEntity entity;

	public TextBox(PhysicsEntity entity, float width, String text, Font font, Color color) {
		this.entity = entity;
		this.width = width;
		this.text = text;
		this.font = font;
		this.color = color;
		lines = new ArrayList<>();
		
	}
	
	public TextBox cloneWithText(String text) {
		return new TextBox(entity, width, text, font, color);
	}

	public void arrangeText(Graphics2D g) {
		initialized = true;
		FontMetrics fm = g.getFontMetrics(font);
		standardLineHeight = fm.getHeight();
		String[] words = text.split(" ");
		String currLine = "";
		for (String word : words) {
			if (fm.stringWidth(currLine) + fm.stringWidth(word) < (width - 2 * padding)) {
				currLine += (" "  + word);
			} else {
				lines.add(new Line(currLine, fm.stringWidth(currLine)));
				currLine = word;
			}
		}
		// Add the last line
		lines.add(new Line(currLine, fm.stringWidth(currLine)));
		height = lines.size() * fm.getHeight() + (2 * padding);
	}
	
	private Vec2f calculateGamePosition(Viewport viewport) {
		return new Vec2f(
				entity.getCenterPosition().x - (width / viewport.getScale() / 2),
				entity.getCenterPosition().y - (entity.getSize().y / 2) - (yOffset / viewport.getScale()) - (height / viewport.getScale())
				);
	}
	
	public boolean containsPoint(Vec2f screenPoint, Viewport v) {
		Vec2f screenPos = v.gameToScreen(calculateGamePosition(v));
		return screenPos.x <= screenPoint.x
				&& screenPoint.x <= screenPos.x + width
				&& screenPos.y <= screenPoint.y
				&& screenPoint.y <= screenPos.y + height;
	}
	
	public void draw(Graphics2D g, Viewport viewport) {
	Graphics.addTextAntialias(g);
	if (!initialized) {
		this.arrangeText(g);
	}
		Vec2f screenPos = viewport.gameToScreen(calculateGamePosition(viewport));
		// Draw text box background
		g.setStroke(new BasicStroke(2));
		g.setColor(Color.BLACK);
		g.drawRect((int) screenPos.x, (int) screenPos.y, (int) width + padding + 1, (int) height + 1);
		g.setColor(Color.WHITE);
		g.fillRect((int) screenPos.x, (int) screenPos.y, (int) width + padding, (int) height);
		
		// Draw text
		g.setColor(color);
		g.setFont(font);
		for (int i = 0; i < lines.size(); i++) {
			Line line = lines.get(i);
			g.drawString(
					line.text,
					screenPos.x + (width / 2) - line.width / 2,
					screenPos.y + padding + (int) ((i + 0.8) * standardLineHeight));
		}
	}
	
	private class Line {
		
		public final String text;
		public final int width;
		
		public Line(String text, int width) {
			this.text = text;
			this.width = width;
		}
	}
}
