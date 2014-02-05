package mllf.gameengine.gameloop;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.EmptyStackException;
import java.util.Stack;

import cs195n.SwingFrontEnd;
import cs195n.Vec2i;
import mllf.gameengine.ui.Screen;

/**
 * Application maintains a stack of screens, and passes all events it receives from the FrontEnd to
 * the top screen on the stack.
 * 
 * @author lfiorant
 */
public class Application extends SwingFrontEnd {

	private Stack<Screen> screens;
	private Vec2i windowSize;
	
	public Application(String title, boolean fullscreen, Vec2i windowSize) {
		super(title, fullscreen, windowSize);
		this.windowSize = windowSize;
		screens = new Stack<Screen>();
	}
	
	public void pushTopScreen(Screen s) {
		screens.push(s);
	}
	
	public void popTopScreen() {
		screens.pop();
		if (screens.peek() != null) {
			screens.peek().onResize(windowSize);
            screens.peek().refresh();
		}
	}
	
	public void popTopScreen(int numScreens) {
		for (int i = 0; i < numScreens; i++) {
			this.popTopScreen();
		}
	}
	
	public Screen getTopScreen() {
		try {
			return screens.peek();
		} catch (EmptyStackException e) {
			return null;
		}
	}
	
	public Vec2i getWindowSize() {
		return windowSize;
	}
	
	public Screen getScreenBelow(int numLevelsBelow) {
		return screens.get(screens.size() - 1 - numLevelsBelow);
	}
	
	/**
	 * Convert to seconds for convenience.
	 */
	@Override
	protected void onTick(long nanosSincePreviousTick) {
		float secondsSincePreviousTick = nanosSincePreviousTick / 1000000000f;
		screens.peek().onTick(secondsSincePreviousTick);
	}

	/**
	 * Turn on anti-aliasing because beauty.
	 */
	@Override
	protected void onDraw(Graphics2D g) {
		screens.peek().onDraw(g);
	}

	@Override
	protected void onKeyTyped(KeyEvent e) {
		screens.peek().onKeyTyped(e);
	}

	@Override
	protected void onKeyPressed(KeyEvent e) {
		screens.peek().onKeyPressed(e);
	}

	@Override
	protected void onKeyReleased(KeyEvent e) {
		screens.peek().onKeyReleased(e);
	}

	@Override
	protected void onMouseClicked(MouseEvent e) {
		screens.peek().onMouseClicked(e);
	}

	@Override
	protected void onMousePressed(MouseEvent e) {
		screens.peek().onMousePressed(e);
	}

	@Override
	protected void onMouseReleased(MouseEvent e) {
		screens.peek().onMouseReleased(e);
	}

	@Override
	protected void onMouseDragged(MouseEvent e) {
		screens.peek().onMouseDragged(e);
	}

	@Override
	protected void onMouseMoved(MouseEvent e) {
		screens.peek().onMouseMoved(e);
	}

	@Override
	protected void onMouseWheelMoved(MouseWheelEvent e) {
		screens.peek().onMouseWheelMoved(e);
	}

	/**
	 * Resize the new top screen, since it won't be aware of any resizes that have occurred.
	 */
	@Override
	protected void onResize(Vec2i newSize) {
		windowSize = newSize;
		screens.peek().onResize(newSize);
	}

	public void popTopScreenNoRefresh() {
		screens.pop();
		if (screens.peek() != null) {
			screens.peek().onResize(windowSize);
		}
		
	}

}
