package mllf.painter.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.Timer;

import mllf.gameengine.gameloop.Application;
import mllf.gameengine.ui.ImageLayer;
import mllf.gameengine.ui.ParallaxBackground;
import mllf.gameengine.resourcemanagement.SaveFileManager;
import mllf.gameengine.ui.Screen;
import mllf.gameengine.ui.TextLayer;
import mllf.gameengine.ui.TransitionScreen;
import mllf.gameengine.ui.FixedText;
import cs195n.Vec2f;
import cs195n.Vec2i;
import mllf.painter.mechanics.Constants;
import mllf.painter.levels.LevelParser;

public class MenuScreen extends Screen {
	
	private ParallaxBackground bg;
	private List<TextLayer> menuItems;
	private TextLayer titleText;
	private FixedText infoText;
	private final float noSelectSize = 30f;
	private final float selectSize = 45f;
	private final int menuItemDist = 60;
	private int midpointX, midpointY;
	private int counter, selection;
	private float prevPosition;
	private boolean transitionLock;

	public MenuScreen(Application app) {
		super(app);
		
		/*
		 * Initialize background layers
		 */
		refresh();
	}

	/**
	 * Animate transition between menu items
	 */
	private void transitionBG(final int yDiff) {
		transitionLock = true;
		prevPosition = 0;
		counter = 0;
		final int delay = 15;
		final int transitionTime = 250;
		final Timer transitionTimer = new Timer(delay, null);
		transitionTimer.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (counter + delay > transitionTime) {
					counter = transitionTime;
					transitionTimer.stop();
					transitionLock = false;
				} else {
					counter += delay;
				}
				float newPosition = (float) Math.sin(((double) counter / (double) transitionTime) * (Math.PI / 2)) * yDiff;
				bg.move(new Vec2f(0, newPosition - prevPosition));
				prevPosition = newPosition;
			}
		});
		transitionTimer.start();
	}
	
	/**
	 * Take action when the user presses "Enter", based on the currently selected menu item
	 */
	private void select(String menuText) {
		switch(menuText) {
		case "Continue Saved Game":
			this.startGame();
			break;
		case "Start New Game":
//            HashMap<String, String> oldSave = SaveFileManager.readPlayerSave();
//            oldSave.put("currentLevel", "10");
//            SaveFileManager.writeSave(oldSave);
            LevelSelectScreen ls = new LevelSelectScreen(app);
            app.pushTopScreen(ls);

            try {
                app.pushTopScreen(new TransitionScreen(
                        app,
                        this,
                        new StartScreen(
                                app, LevelParser.createLevelScreen(app, 10, ls),
                                10, ls),
                        Constants.TRANSITION_TIME,
                        0,
                        true));
            } catch(Exception e)
            {

            }




			break;
		case "Quit":
			System.exit(0);
		}
	}
	
	private void startGame()
	    {
			LevelSelectScreen ls = new LevelSelectScreen(app);
	        app.pushTopScreen(new TransitionScreen(app, this, ls, Constants.TRANSITION_TIME, 0, true));
	    }

	@Override
	public void onTick(float secondsSincePreviousTick) {}

	@Override
	public void onDraw(Graphics2D g) {
		bg.draw(g);
		infoText.draw(g);
	}

	@Override
	public void onKeyTyped(KeyEvent e) {}

	@Override
	public void onKeyPressed(KeyEvent e) {
		switch(e.getKeyCode()) {
		case KeyEvent.VK_W:
		case KeyEvent.VK_UP:
			if (!transitionLock && selection - 1 >= 0)  {
				menuItems.get(selection).setFontSize(noSelectSize);
				selection--;
				this.transitionBG(menuItemDist);
				menuItems.get(selection).setFontSize(selectSize);
			}
			break;
		case KeyEvent.VK_S:
		case KeyEvent.VK_DOWN:
			if (!transitionLock && selection + 1 < menuItems.size()) {
				menuItems.get(selection).setFontSize(noSelectSize);
				selection++;
				this.transitionBG(-menuItemDist);
				menuItems.get(selection).setFontSize(selectSize);
			}	
    		break;
        case KeyEvent.VK_SPACE:
		case KeyEvent.VK_ENTER:
			this.select(menuItems.get(selection).getText());
			break;
    	default:
    		
    	}
	}

	@Override
	public void onKeyReleased(KeyEvent e) {}

	@Override
	public void onMouseClicked(MouseEvent e) {}

	@Override
	public void onMousePressed(MouseEvent e) {
//		startGame();
	}

	@Override
	public void onMouseReleased(MouseEvent e) {}

	@Override
	public void onMouseDragged(MouseEvent e) {}

	@Override
	public void onMouseMoved(MouseEvent e) {}

	@Override
	public void onMouseWheelMoved(MouseWheelEvent e) {}

	@Override
	public void onResize(Vec2i newSize) {}

    // initialization/refresh
    @Override
    public void refresh() {
    	bg = new ParallaxBackground();
    	bg.addLayer(new ColorLayer(new Vec2f(0, 0), new Vec2f(app.getWindowSize()), 100, Constants.BACKGROUND_COLOR));
		try {
			bg.addLayer(new ImageLayer(new Vec2f(-20, 260), new Vec2f(1400, 400), 80, ImageIO.read(new File("images/bg.png"))));
			bg.addLayer(new ImageLayer(new Vec2f(-20, 260), new Vec2f(1400, 400), 50, ImageIO.read(new File("images/fg.png"))));
		} catch (IOException e) {

		}
        Vec2i window = app.getWindowSize();
        midpointX = window.x * 1/2;
        midpointY = window.y * 1/2;
		/*
		 * This will likely be replaced with an image of our title/logo/game name
		 */
        titleText = new TextLayer(
                "Monochrome", new Vec2f(midpointX, window.y * 200/600),
                0, Constants.FONT.deriveFont(Font.BOLD, 65f), Constants.WALL_COLOR);
        bg.addLayer(titleText);

		/*
		 * Initialize menu items
		 */
        menuItems = new ArrayList<TextLayer>();
        selection = 0;
        int offsetForContinue = 0;
        // Check for saved game
        Map<String, String> save = SaveFileManager.readPlayerSave();
        if (save != null && save.get("currentLevel") != null) {
            int currLevel = Integer.parseInt(save.get("currentLevel"));
            if (currLevel != 10) {
                menuItems.add(
                        new TextLayer("Continue Saved Game", new Vec2f(midpointX, midpointY), 0,
                                Constants.FONT, Constants.WALL_COLOR));
                offsetForContinue = menuItemDist; // Add one level of padding if continue item is present
            }
        }
        menuItems.add(
                new TextLayer("Start New Game", new Vec2f(midpointX, midpointY + offsetForContinue), 0, Constants.FONT, Constants.WALL_COLOR));
        menuItems.add(
                new TextLayer("Quit", new Vec2f(midpointX, midpointY + offsetForContinue + menuItemDist), 0, Constants.FONT, Constants.WALL_COLOR));
        for (TextLayer l: menuItems) {
            l.setFontSize(noSelectSize);
            bg.addLayer(l);
        }
        menuItems.get(selection).setFontSize(selectSize);

        /*
         * Info text in case we need it, currently empty
         */
        infoText = new FixedText("", new Vec2f(window.x * 400/800, window.y * 520/600),
        		Constants.FONT.deriveFont(Constants.FONT_SIZE), Color.WHITE);
    }
}
