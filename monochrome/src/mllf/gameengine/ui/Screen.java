package mllf.gameengine.ui;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import cs195n.Vec2i;
import mllf.gameengine.gameloop.Application;

/**
 * Models a game screen / "mode". Receives events from the Application.
 * 
 * @author lfiorant
 */
public abstract class Screen {

	protected Application app;
	
	public Screen(Application app) {
		this.app = app;
	}
	
	public abstract void onTick(float secondsSincePreviousTick);
	
	public abstract void onDraw(Graphics2D g);
	
	public abstract void onKeyTyped(KeyEvent e);
	
	public abstract void onKeyPressed(KeyEvent e);
	
	public abstract void onKeyReleased(KeyEvent e);
	
	public abstract void onMouseClicked(MouseEvent e);
	
	public abstract void onMousePressed(MouseEvent e);
	
	public abstract void onMouseReleased(MouseEvent e);
	
	public abstract void onMouseDragged(MouseEvent e);
	
	public abstract void onMouseMoved(MouseEvent e);
	
	public abstract void onMouseWheelMoved(MouseWheelEvent e);
	
	public abstract void onResize(Vec2i newSize);

    public abstract void refresh();
}
