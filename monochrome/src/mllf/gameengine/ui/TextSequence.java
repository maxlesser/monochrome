package mllf.gameengine.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;

import mllf.gameengine.entity.PhysicsEntity;
import cs195n.Vec2f;
import mllf.gameengine.entity.TextBox;
import mllf.gameengine.ui.Viewport;

public class TextSequence {

	private ArrayList<TextBox> textBoxes;
	private int curr;
	private boolean isPlaying;
	public final int WIDTH = 120; // Standard width in screen coords (pixels)
	public final Color COLOR = Color.BLACK;
	
	public TextSequence(PhysicsEntity entity, Font font, String ... text) {
		textBoxes = new ArrayList<TextBox>();
		for (String line : text) {
			textBoxes.add(new TextBox(entity, WIDTH, line, font, COLOR));
		}
	}
	
	public void start() {
		isPlaying = true;
		curr = 0;
	}
	
	public void next() {
		curr++;
		if (curr == textBoxes.size()) {
			stop();
		}
	}
	
	public void stop() {
		isPlaying = false;
		curr = 0;
	}
	
	public boolean isPlaying() {
		return isPlaying;
	}
	
	public void onClick(Vec2f p, Viewport v) {
		if (textBoxes.get(curr).containsPoint(p, v)) {
			next();
		}
	}
	
	public void draw(Graphics2D g, Viewport viewport) {
		if (isPlaying) {
			textBoxes.get(curr).draw(g, viewport);
		}
	}
}
