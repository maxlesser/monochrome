package mllf.gameengine.ui;

import cs195n.Vec2f;

public class Viewport {

	private Vec2f screenPos; // Top left corner of Viewport in screen coordinates
	private Vec2f gamePos; // Top left corner of Viewport in game coordinates
	private Vec2f size; // Size of viewport in screen coordinates.
	private float scale;
	
	public Viewport(Vec2f screenPos, Vec2f gamePos, Vec2f size) {
		this.screenPos = screenPos;
		this.gamePos = gamePos;
		this.size = size;
		scale = 10f;
	}
	
	public Vec2f gameToScreen(Vec2f pos) {
		pos = pos.minus(gamePos);
		pos = pos.smult(scale);
		pos = pos.plus(screenPos);
		return pos;
	}
	
	public Vec2f screenToGame(Vec2f pos) {
		pos = pos.minus(screenPos);
		pos = pos.sdiv(scale);
		pos = pos.plus(gamePos);
		return pos;
	}
	
	public void setScreenPosition(Vec2f screenPos) {
		this.screenPos = screenPos;
	}
	
	public void setGamePosition(Vec2f gamePos) {
		this.gamePos = gamePos;
	}
	
	public void setSize(Vec2f size) {
		this.size = size;
	}
	
	public void setScale(float scale) {
		this.scale = scale;
	}

	public Vec2f getScreenPosition() {
		return screenPos;
	}
	
	public Vec2f getGamePosition() {
		return gamePos;
	}
	
	public Vec2f getSize() {
		return size;
	}
	
	public float getScale() {
		return scale;
	}
	
}
