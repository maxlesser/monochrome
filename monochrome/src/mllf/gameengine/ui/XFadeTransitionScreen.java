package mllf.gameengine.ui;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import cs195n.Vec2i;
import mllf.gameengine.gameloop.Application;

public class XFadeTransitionScreen extends Screen {
	
	private Application app;
	private float duration, counter, alpha;
	private int numScreensToPop;
	private Screen newScreen, oldScreen;
	private boolean pushNewScreen;

	public XFadeTransitionScreen(Application app, Screen oldScreen,  Screen newScreen,  float duration,
			int numScreensToPop, boolean pushNewScreen) {
		super(app);
		this.app = app;
		this.oldScreen = oldScreen;
		this.newScreen = newScreen;
		this.duration = duration;
		this.numScreensToPop = numScreensToPop;
		this.pushNewScreen = pushNewScreen;
		alpha = 1;
	}

	@Override
	public void onTick(float seconds) {
		
		counter += seconds;
		alpha = 1f - (float) (Math.sin((counter / duration) * Math.PI / 2f));
		if (counter >= duration) { // Complete
			app.popTopScreen(numScreensToPop + 1);
			if (pushNewScreen) app.pushTopScreen(newScreen);
		}

	}

	@Override
	public void onDraw(Graphics2D g) {
		AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
		g.setComposite(composite);
		oldScreen.onDraw(g);
		composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f - alpha);
		g.setComposite(composite);
		newScreen.onDraw(g);
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
