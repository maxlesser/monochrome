package mllf.painter.ui;

import cs195n.Vec2f;
import cs195n.Vec2i;
import mllf.gameengine.gameloop.Application;
import mllf.gameengine.resourcemanagement.SaveFileManager;
import mllf.gameengine.ui.*;
import mllf.painter.mechanics.Constants;
import mllf.painter.levels.LevelParser;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created By: Max Lesser
 * Date: 12/3/13
 * Time: 4:35 PM
 */
public class LevelSelectScreen extends Screen {

    private ArrayList<Rectangle2D> levelImages;
    private int selectedLevel;
    private int latestLevel;

    private ParallaxBackground bg;
    private java.util.List<TextLayer> menuItems;
    private TextLayer titleText;
    private FixedText infoText;
    private final float noSelectSize = 30f;
    private final float selectSize = 45f;
    private final int menuItemDist = 200;
    private int midpointX, midpointY;
    private int counter, selection;
    private float prevPosition;
    private boolean transitionLock;

    public LevelSelectScreen(Application app)
    {
        super(app);

        // Call refresh to initialize
        refresh();
        setSelectedLevel(latestLevel);
    }

    public void setSelectedLevel(int level) {
    	latestLevel = Integer.parseInt(SaveFileManager.readPlayerSave().get("currentLevel"));
    	if (level <= latestLevel) {
    		bg.move(new Vec2f(-menuItemDist * (level/10 - 1), 0));
    		menuItems.get(selection).setFontSize(noSelectSize);
    		selection = level/10 - 1;
    		selectedLevel = level;
    		menuItems.get(selection).setFontSize(selectSize);
    	}
    }
    
    @Override
    public void onTick(float secondsSincePreviousTick) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onDraw(Graphics2D g) {
        bg.draw(g);
    }

    @Override
    public void onKeyTyped(KeyEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onKeyPressed(KeyEvent e) {
    	if (!transitionLock) {
	        if(e.getKeyChar() == 'a' || e.getKeyCode() == KeyEvent.VK_LEFT)
	        {
	            if(selectedLevel > 10)
	            {
	                menuItems.get(selection).setFontSize(noSelectSize);
	                selection--;
	                this.transitionBG(menuItemDist);
	                menuItems.get(selection).setFontSize(selectSize);
	                selectedLevel = selectedLevel - 10;
	            }
	        }
	        else if(e.getKeyChar() == 'd' || e.getKeyCode() == KeyEvent.VK_RIGHT)
	        {
	            if(selectedLevel < latestLevel)
	            {
	                menuItems.get(selection).setFontSize(noSelectSize);
	                selection++;
	                this.transitionBG(-menuItemDist);
	                menuItems.get(selection).setFontSize(selectSize);
	                selectedLevel = selectedLevel + 10;
	            }
	        }
    	}
        if(e.getKeyChar() == '\n' || e.getKeyChar() == ' ')
        {
            try {
                app.pushTopScreen(
                		new TransitionScreen(
                				app,
                				this,
                				new StartScreen(
                						app, LevelParser.createLevelScreen(app, selectedLevel, this),
                						selectedLevel, this),
                				Constants.TRANSITION_TIME,
                				0,
                				true));
            } catch (Exception ex) {
                ex.printStackTrace();
            	System.out.println(ex.getMessage());
            }
        }
        else if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
        {
            app.pushTopScreen(new TransitionScreen(app, this, app.getScreenBelow(1), Constants.TRANSITION_TIME, 1, false));
        }
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
        try {
            latestLevel = Integer.parseInt(SaveFileManager.readPlayerSave().get("currentLevel"));

            levelImages = new ArrayList<>();
            selectedLevel = 10;

            bg = new ParallaxBackground();
            bg.addLayer(new ColorLayer(new Vec2f(0, 0), new Vec2f(app.getWindowSize()), 100, Constants.BACKGROUND_COLOR));
    		try {
    			bg.addLayer(new ImageLayer(new Vec2f(-20, 210), new Vec2f(1400, 400), 80, ImageIO.read(new File("images/bg.png"))));
    			bg.addLayer(new ImageLayer(new Vec2f(-20, 210), new Vec2f(1400, 400), 50, ImageIO.read(new File("images/fg.png"))));
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
                    80, Constants.FONT.deriveFont(Font.BOLD, 65f), Constants.WALL_COLOR);
            bg.addLayer(titleText);

            /*
             * Initialize menu items
             */
            menuItems = new ArrayList<TextLayer>();
            selection = 0;

            for(int x = 0; x < latestLevel/10; x++)
            {
                menuItems.add(
                        new TextLayer("Level " + (x+1), new Vec2f(midpointX + menuItemDist*x, midpointY), 0, Constants.FONT, Constants.WALL_COLOR));
            }
            for (TextLayer l: menuItems) {
                l.setFontSize(noSelectSize);
                bg.addLayer(l);
            }
            menuItems.get(selection).setFontSize(selectSize);

            /*
             * Info text in case we need it, currently empty
             */
            infoText = new FixedText("", new Vec2f(window.x * 400/800, window.y * 520/600),
            		Constants.FONT.deriveFont(Constants.FONT_SIZE), Constants.WALL_COLOR);


        } catch (Exception e)
        {
            // do something smart here
        }
    }

    /**
     * Animate transition between menu items
     */
    private void transitionBG(final int xDiff) {
        transitionLock = true;
        prevPosition = 0;
        counter = 0;
        final int delay = 15;
        final int transitionTime = 250;
        final javax.swing.Timer transitionTimer = new javax.swing.Timer(delay, null);
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
                float newPosition = (float) Math.sin(((double) counter / (double) transitionTime) * (Math.PI / 2)) * xDiff;
                bg.move(new Vec2f(newPosition - prevPosition, 0));
                prevPosition = newPosition;
            }
        });
        transitionTimer.start();
    }
}
