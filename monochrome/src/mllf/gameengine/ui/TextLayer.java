package mllf.gameengine.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import mllf.gameengine.ui.FixedText;

import cs195n.Vec2f;
import mllf.gameengine.ui.Layer;

public class TextLayer extends Layer {
	
	private FixedText text;

	
	public TextLayer(String text, Vec2f center, int depth, Font font, Color color) {
		super(new Vec2f(0,0), new Vec2f(0,0), depth);
		this.text = new FixedText(text, center, font, color);
	}
	
	@Override
	public void move(Vec2f diff) {
		text.setCenter(text.getCenter().plus(diff));
	}
	
	public void setFontSize(float size) {
		text.setFontSize(size);
	}
	
	public String getText() {
		return text.getText();
	}

	@Override
	public void draw(Graphics2D g) {
		text.draw(g);
	}

}
