package mllf.painter.ui;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;

import cs195n.Vec2f;
import cs195n.Vec2i;
import mllf.gameengine.gameloop.Application;
import mllf.gameengine.gameloop.World;
import mllf.gameengine.ui.*;
import mllf.gameengine.ui.Graphics;
import mllf.painter.mechanics.Constants;
import mllf.painter.mechanics.Constants.ObjectColor;
import mllf.painter.levels.LevelReferee;

public class LevelScreen extends Screen {
	
	public final String title, subtitle;
	private Viewport viewport;
	private ParallaxBackground bg;
	private World world;
	private List<TextSequence> sequences;
	private Vec2f mousePos;
    private float leftHoldTime, rightHoldTime;
    private boolean leftHolding, rightHolding;
    private LevelReferee referee;
    private Rectangle2D currentColorUI;
    private Rectangle2D powerMeter;
    private Color currentPlayerColor = ObjectColor.RED.drawColor;
    private LevelSelectScreen levelSelect;
	private boolean powerMeterIsVisible;

    public LevelScreen(Application app, String title, String subtitle, LevelReferee ref, LevelSelectScreen levelSelect, World w) {
		super(app);
		
		this.title = title;
		this.subtitle = subtitle;
        referee = ref;
        this.levelSelect = levelSelect;
        world = w;

        sequences = new ArrayList<>();
        
        viewport = new Viewport(new Vec2f(0, 0), new Vec2f(0, 0), new Vec2f(app.getWindowSize()));
		viewport.setScale(Constants.SCALE_MAX);
		viewport.setGamePosition(new Vec2f(0, 0));
		bg = new ParallaxBackground();
		bg.addLayer(new ColorLayer(new Vec2f(0, 0), new Vec2f(app.getWindowSize()), 100, Constants.BACKGROUND_COLOR));
		try {
			bg.addLayer(new ImageLayer(new Vec2f(-20, 150), new Vec2f(1400, 400), 80, ImageIO.read(new File("images/bg.png"))));
			bg.addLayer(new ImageLayer(new Vec2f(-20, 150), new Vec2f(1400, 400), 30, ImageIO.read(new File("images/fg.png"))));
		} catch (IOException e) {

		}

        currentColorUI = new Rectangle2D.Float(50, 50, 50, 50);
        powerMeter = new Rectangle2D.Float(0, 0, 0, 5);

		mousePos = new Vec2f(0,0);
        leftHoldTime = 0f;
        leftHolding = false;
		
		this.centerViewportOnPlayer();
	}
	
	public void centerViewportOnPlayer() {
		Vec2f newPos = world.getPlayer().getCenterPosition().minus(
				viewport.getSize().sdiv(viewport.getScale()).sdiv(2f));
		Vec2f delta = viewport.getGamePosition().minus(newPos);
		bg.move(delta);
		viewport.setGamePosition(newPos);
	}
	
	@Override
	public void onTick(float seconds) {
		world.tick(seconds);
		this.centerViewportOnPlayer();
		leftHoldTime = (leftHolding) ? Math.min(leftHoldTime, Constants.LAUNCH_TIME) + seconds : 0;
        rightHoldTime = (rightHolding) ? Math.min(rightHoldTime, Constants.LAUNCH_TIME) + seconds : 0;
        float longerHold = leftHoldTime > rightHoldTime ? leftHoldTime : rightHoldTime;
        powerMeterIsVisible = longerHold > 0;

        currentPlayerColor = world.getPlayer().getColor().drawColor;
        Vec2f playerScreenPos = viewport.gameToScreen(world.getPlayer().getCenterPosition());
        powerMeter.setRect(new Rectangle2D.Float(playerScreenPos.x, playerScreenPos.y, longerHold * 15 + 5, 5));

        if(referee.isLevelWon())
        {
            app.pushTopScreen(
            		new XFadeTransitionScreen(app, this, new WinScreen(app, this,
            				referee.getCurrentLevel(), levelSelect), Constants.TRANSITION_TIME, 0, true));
        }
	}

	@Override
	public void onDraw(Graphics2D g) {
		Graphics.addAntialias(g);
		bg.draw(g);
		world.draw(g, viewport);
		for (TextSequence sequence : sequences) {
			sequence.draw(g, viewport);
		}
        g.setColor(currentPlayerColor);
        g.fill(currentColorUI);

        if (powerMeterIsVisible) {
	        AffineTransform transform = new AffineTransform();
	        transform.rotate(mousePos.minus(app.getWindowSize().x/2, app.getWindowSize().y/2).angle(), app.getWindowSize().x/2, app.getWindowSize().y/2);
	        Shape transformed = transform.createTransformedShape(powerMeter);
	        g.fill(transformed);
        }

        g.setFont(Constants.FONT.deriveFont(25f));
        g.drawString("BOMBS".substring(0, world.getPlayer().getBombsLeft()), 700, 75);
	}
	
	@Override
	public void onKeyTyped(KeyEvent e) {
	}

    @Override
    public void onKeyPressed(KeyEvent e) {
    	switch(e.getKeyCode()) {
    	case KeyEvent.VK_A:
    		world.getPlayer().isMovingLeft(true);
    		break;
    	case KeyEvent.VK_D:
    		world.getPlayer().isMovingRight(true);
    		break;
    	case KeyEvent.VK_W:
    		world.getPlayer().isJumping(true);
    		break;
    	case KeyEvent.VK_E:
    		world.getPlayer().getOutputByName("onInteract").run();
    		break;
    	default:
    		
    	}
    }

    @Override
    public void onKeyReleased(KeyEvent e) {
    	switch (e.getKeyCode()) {
    	case KeyEvent.VK_A:
    		world.getPlayer().isMovingLeft(false);
    		break;
    	case KeyEvent.VK_D:
    		world.getPlayer().isMovingRight(false);
    		break;
    	case KeyEvent.VK_W:
    		world.getPlayer().isJumping(false);
    		break;
    	case KeyEvent.VK_SPACE:
    		world.getPlayer().explodeFirstBomb();
    		break;
    	case KeyEvent.VK_TAB:
    		world.getPlayer().switchColors();
    		break;
    	case KeyEvent.VK_ESCAPE:
    		app.pushTopScreen(new TransitionScreen(app, this, app.getScreenBelow(1), Constants.TRANSITION_TIME, 1, false));
    		levelSelect.setSelectedLevel(referee.getCurrentLevel());
    		break;
    	}
    }

	@Override
	public void onMouseClicked(MouseEvent e) {
		for (TextSequence sequence : sequences) {
			sequence.onClick(new Vec2f(e.getX(), e.getY()), viewport);
		}
	}

	@Override
	public void onMousePressed(MouseEvent e) {
		if (SwingUtilities.isLeftMouseButton(e)) leftHolding = true;
		if (SwingUtilities.isRightMouseButton(e)) rightHolding = true;
	}

	@Override
	public void onMouseReleased(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            leftHolding = false;
            world.getPlayer().firePaint(leftHoldTime, viewport.screenToGame(new Vec2f(e.getX(), e.getY())).minus(world.getPlayer().getCenterPosition()));
        }
        if (SwingUtilities.isRightMouseButton(e)) {
        	rightHolding = false;
            world.getPlayer().fireGrenade(rightHoldTime, viewport.screenToGame(new Vec2f(e.getX(), e.getY())).minus(world.getPlayer().getCenterPosition()));
        }
    }

	@Override
	public void onMouseDragged(MouseEvent e) {
        mousePos = new Vec2f(e.getX(), e.getY());
    }

	@Override
	public void onMouseMoved(MouseEvent e) {
		mousePos = new Vec2f(e.getX(), e.getY());
	}

	@Override
	public void onMouseWheelMoved(MouseWheelEvent e) {
		Vec2f mousePosInGame = viewport.screenToGame(mousePos);
		float scale = viewport.getScale();
		if (e.getWheelRotation() < 0) { // Scroll up
			scale *= Constants.SCALE_FACTOR;
			if (scale > Constants.SCALE_MAX) {
				scale = Constants.SCALE_MAX;
			}
		} else if (e.getWheelRotation() > 0) { // Scroll down
			scale /= Constants.SCALE_FACTOR;
			if (scale < Constants.SCALE_MIN) {
				scale = Constants.SCALE_MIN;
			}
		} 
		if (scale != viewport.getScale()) {
			viewport.setScale(scale);
			viewport.setGamePosition(viewport.getGamePosition().minus(viewport.screenToGame(mousePos).minus(mousePosInGame)));
		}
	}

	@Override
	public void onResize(Vec2i newSize) {
		this.centerViewportOnPlayer();
	}

    @Override
    public void refresh() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
