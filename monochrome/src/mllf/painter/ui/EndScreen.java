package mllf.painter.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import mllf.gameengine.gameloop.Application;
import mllf.gameengine.ui.Graphics;
import mllf.gameengine.Rectangle;
import mllf.gameengine.ui.Screen;
import mllf.gameengine.ui.Text;
import cs195n.Vec2f;
import cs195n.Vec2i;
import mllf.painter.mechanics.Constants;

public class EndScreen extends Screen {
	
	private Rectangle bg;
	private Text titleText, infoText;
	
	public EndScreen(Application app, boolean isPlayerWin) {
		super(app);
		bg = new Rectangle(new Vec2f(0, 0), new Vec2f(app.getWindowSize().x, app.getWindowSize().y));
		bg.setColor(Constants.BG_COLOR);
		if (isPlayerWin) {
			titleText = new Text("YOU WIN", new Vec2f(0, 0), new Vec2f(0, 0), Constants.FONT.deriveFont(Font.BOLD), Color.WHITE);
		} else {
			titleText = new Text("YOU LOSE", new Vec2f(0, 0), new Vec2f(0, 0), Constants.FONT.deriveFont(Font.BOLD), Color.WHITE);
		}
		titleText.setColor(Color.WHITE);
		infoText = new Text("press enter to continue", new Vec2f(0, 0), new Vec2f(0, 0), Constants.FONT, Color.WHITE);
		infoText.setColor(Color.WHITE);
		this.positionAndSizeText(app.getWindowSize());
	}
	
	public void positionAndSizeText(Vec2i window) {
		titleText.setSize(new Vec2f(window.x / 4, 0));
		titleText.setPosition(new Vec2f(window.x * 3 / 8, window.y / 2));
		infoText.setSize(new Vec2f(window.x / 4, 0));
		infoText.setPosition(new Vec2f(window.x * 3 / 8, window.y * 11 / 20));
	}

	@Override
	public void onTick(float secondsSincePreviousTick) {
		
	}

	@Override
	public void onDraw(Graphics2D g) {
		Graphics.addTextAntialias(g);
		bg.draw(g);
		titleText.draw(g);
		infoText.draw(g);
	}

	@Override
	public void onKeyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onKeyPressed(KeyEvent e) {
		if (e.getKeyCode() == 10) { // Enter key
			app.popTopScreen(2);
		}
	}

	@Override
	public void onKeyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMousePressed(MouseEvent e) {
		
	}

	@Override
	public void onMouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMouseWheelMoved(MouseWheelEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResize(Vec2i newSize) {
		this.positionAndSizeText(newSize);
		bg.setSize(new Vec2f(newSize));
	}

    @Override
    public void refresh() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
