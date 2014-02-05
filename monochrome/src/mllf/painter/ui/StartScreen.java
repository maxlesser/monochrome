package mllf.painter.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Rectangle2D;

import mllf.gameengine.gameloop.Application;
import mllf.gameengine.ui.Screen;
import mllf.gameengine.ui.TransitionScreen;
import mllf.gameengine.ui.XFadeTransitionScreen;
import mllf.gameengine.ui.FixedText;
import cs195n.Vec2f;
import cs195n.Vec2i;
import mllf.painter.mechanics.Constants;

public class StartScreen extends Screen {

	private Application app;
	private Screen levelScreen;
	private FixedText titleText, subtitleText, levelText, infoText;
	private int currentLevel;
	private LevelSelectScreen levelSelect;
	
	public StartScreen(Application app, LevelScreen levelScreen, int currLevel, LevelSelectScreen levelSelect) {
		super(app);
		this.app = app;
		this.levelScreen = levelScreen;
		this.currentLevel = currLevel;
		this.levelSelect = levelSelect;
		Vec2i window = app.getWindowSize();
		
		levelText = new FixedText(
				"Level " + currentLevel/10, new Vec2f(window.x * 400/800f, window.y * 250f/600f), Constants.FONT.deriveFont(20f),
				Color.WHITE);
		titleText = new FixedText(
				levelScreen.title, new Vec2f(window.x * 400/800f, window.y * 320f/600f), Constants.FONT.deriveFont(50f),
				Color.WHITE);
		subtitleText = new FixedText(
				levelScreen.subtitle, new Vec2f(window.x * 400/800f, window.y * 315f/600f), Constants.FONT.deriveFont(25f),
				Color.WHITE);
		infoText = new FixedText(
				"Esc to return                               Space to continue",
				new Vec2f(window.x * 400f/800f, window.y * 420f/600f), Constants.FONT.deriveFont(20f), Color.WHITE);
	}

	@Override
	public void onTick(float secondsSincePreviousTick) {
		
	}

	@Override
	public void onDraw(Graphics2D g) {
		levelScreen.onDraw(g);
		Vec2i window = app.getWindowSize();
		Rectangle2D.Float transparency = new Rectangle2D.Float(0, 0, window.x, window.y);
		g.setColor(new Color(255, 255, 255, 150));
		g.fill(transparency);
		Rectangle2D.Float rect = new Rectangle2D.Float(
				window.x * 125f/800f, window.y * 150f/600f, window.x * 550f/800f, window.y * 300f/600f);
		g.setColor(Constants.START_WIN_COLOR);
		g.fill(rect);
		levelText.draw(g);
		titleText.draw(g);
		subtitleText.draw(g);
		infoText.draw(g);
	}

	@Override
	public void onKeyTyped(KeyEvent e) {
		
	}

	@Override
	public void onKeyPressed(KeyEvent e) {
		
	}

	@Override
	public void onKeyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			app.pushTopScreen(new TransitionScreen(app, this, levelSelect, Constants.TRANSITION_TIME, 1, false));
			levelSelect.setSelectedLevel(currentLevel);
		} else if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_SPACE) {
	        app.pushTopScreen(new XFadeTransitionScreen(app, this, levelScreen, Constants.TRANSITION_TIME, 1, true));
		}
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
