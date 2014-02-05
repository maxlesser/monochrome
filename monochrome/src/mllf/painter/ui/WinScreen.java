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
import mllf.gameengine.ui.FixedText;
import cs195n.Vec2f;
import cs195n.Vec2i;
import mllf.painter.mechanics.Constants;
import mllf.painter.levels.LevelParseException;
import mllf.painter.levels.LevelParser;

public class WinScreen extends Screen {

	private Application app;
	private Screen overlaidScreen;
	private FixedText winText, infoText;
	private boolean isLastLevel;
	private int nextLevel;
	private LevelSelectScreen levelSelect;
	
	public WinScreen(Application app, Screen screenToOverlay, int nextLevel, LevelSelectScreen levelSelect) {
		super(app);
		this.app = app;
		this.overlaidScreen = screenToOverlay;
		this.nextLevel = nextLevel;
		this.levelSelect = levelSelect;
		Vec2i window = app.getWindowSize();
		isLastLevel = nextLevel/10 > Constants.NUM_LEVELS;
		if (isLastLevel) {
			winText = new FixedText(
					"GAME COMPLETE", new Vec2f(window.x * 400/800f, window.y * 300f/600f), Constants.FONT.deriveFont(50f),
					Color.WHITE);
			infoText = new FixedText(
					"Congratulations! Press esc to return.",
					new Vec2f(window.x * 400f/800f, window.y * 400f/600f), Constants.FONT.deriveFont(25f), Color.WHITE);
		} else {
			winText = new FixedText(
					"LEVEL COMPLETE", new Vec2f(window.x * 400/800f, window.y * 300f/600f), Constants.FONT.deriveFont(50f),
					Color.WHITE);
			infoText = new FixedText(
					"Esc to return                               Space to continue",
					new Vec2f(window.x * 400f/800f, window.y * 420f/600f), Constants.FONT.deriveFont(20f), Color.WHITE);
		}
	}

	@Override
	public void onTick(float secondsSincePreviousTick) {
		
	}

	@Override
	public void onDraw(Graphics2D g) {
		overlaidScreen.onDraw(g);
		Vec2i window = app.getWindowSize();
		Rectangle2D.Float transparency = new Rectangle2D.Float(0, 0, window.x, window.y);
		g.setColor(new Color(255, 255, 255, 150));
		g.fill(transparency);
		Rectangle2D.Float rect = new Rectangle2D.Float(
				window.x * 125f/800f, window.y * 150f/600f, window.x * 550f/800f, window.y * 300f/600f);
		g.setColor(Constants.START_WIN_COLOR);
		g.fill(rect);
		winText.draw(g);
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
			app.pushTopScreen(new TransitionScreen(app, this, levelSelect, Constants.TRANSITION_TIME, 2, false));
			levelSelect.setSelectedLevel(nextLevel - 10);
		} else if ((e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_SPACE) && !isLastLevel) {
	        LevelScreen level = null;
	        try {
	            level = LevelParser.createLevelScreen(app, nextLevel, levelSelect);
	        } catch (LevelParseException x) {
	            app.pushTopScreen(new TransitionScreen(app, this, levelSelect, Constants.TRANSITION_TIME, 2, false));
                levelSelect.setSelectedLevel(nextLevel/10 - 1);
	        }
	        app.pushTopScreen(
	        		new TransitionScreen(
	        				app,
	        				this,
	        				new StartScreen(app, level, nextLevel, levelSelect),
	        				Constants.TRANSITION_TIME,
	        				2,
	        				true));
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
