package mllf.gameengine.ui;

import java.awt.Graphics2D;

import cs195n.Vec2f;

public abstract class Layer {

	protected Vec2f position, size;
	protected int depth; // Value between 0 and 100 inclusive. 0 moves 1:1 with viewport, 100 is static.
	
	public Layer(Vec2f position, Vec2f size, int depth) {
		this.position = position;
		this.size = size;
		this.depth = depth;
	}
	
	public void move(Vec2f diff) {
		position = position.plus(diff);
	}
	
	public int getDepth() {
		return depth;
	}

	public abstract void draw(Graphics2D g);
}
