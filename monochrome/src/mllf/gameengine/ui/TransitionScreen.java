package mllf.gameengine.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Rectangle2D;

import cs195n.Vec2i;
import mllf.gameengine.gameloop.Application;

public class TransitionScreen extends Screen {
	
	private Application app;
	private float duration, counter, half;
	private int numScreensToPop, alpha;
	private Screen newScreen, oldScreen;
	private boolean fadingOut, pushNewScreen;

	public TransitionScreen(Application app, Screen oldScreen,  Screen newScreen,  float duration,
			int numScreensToPop, boolean pushNewScreen) {
		super(app);
		this.app = app;
		this.oldScreen = oldScreen;
		this.newScreen = newScreen;
		this.duration = duration;
		this.numScreensToPop = numScreensToPop;
		this.pushNewScreen = pushNewScreen;
		half = duration / 2f;
		alpha = 255;
		fadingOut = true;
		app.popTopScreen(numScreensToPop);
		if (pushNewScreen) {
			app.pushTopScreen(newScreen);
		}
	}

	@Override
	public void onTick(float seconds) {
		counter += seconds;
		if (counter > half && fadingOut) { // Transitioning
			alpha = 255;
			fadingOut = false;
		} else if (counter < duration) {
			alpha = (int) (Math.sin((counter / duration) * Math.PI) * 255);
		} else { // Complete
			app.popTopScreenNoRefresh();
		}

	}

	@Override
	public void onDraw(Graphics2D g) {
		if (fadingOut) {
			oldScreen.onDraw(g);
		} else {
			newScreen.onDraw(g);
		}
		Rectangle2D.Float rect = new Rectangle2D.Float(0, 0, app.getWindowSize().x, app.getWindowSize().y);
		g.setColor(new Color(0, 0, 0, alpha));
		g.fill(rect);
	}

	@Override
	public void onKeyTyped(KeyEvent e) {

	}

	@Override
	public void onKeyPressed(KeyEvent e) {

	}

	@Override
	public void onKeyReleased(KeyEvent e) {

	}

	@Override
	public void onMouseClicked(MouseEvent e) {

	}

	@Override
	public void onMousePressed(MouseEvent e) {

	}

	@Override
	public void onMouseReleased(MouseEvent e) {

	}

	@Override
	public void onMouseDragged(MouseEvent e) {

	}

	@Override
	public void onMouseMoved(MouseEvent e) {

	}

	@Override
	public void onMouseWheelMoved(MouseWheelEvent e) {

	}

	@Override
	public void onResize(Vec2i newSize) {

	}

	@Override
	public void refresh() {

	}

}
