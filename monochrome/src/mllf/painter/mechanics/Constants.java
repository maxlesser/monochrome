package mllf.painter.mechanics;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import mllf.gameengine.gameloop.World;

public class Constants {
	
	/*
	 * Directories
	 */
	public static final String SOUND_DIR = "sounds/";
	public static final String IMG_DIR = "images/";
	public static final String LEVEL_DIR = "levels/";
	public static final String FONT_DIR = "fonts/";
	
	public static final float FONT_SIZE = 16f;
	public static final Font FONT, TEXTBOX_FONT;
	static {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Font newFont, newTBFont;
		try {
			ge.registerFont(newFont = Font.createFont(Font.TRUETYPE_FONT, new File(FONT_DIR + "AldoTheApache.ttf")));
			newFont = newFont.deriveFont(Font.PLAIN, FONT_SIZE);
		} catch (FontFormatException | IOException e) {
			newFont = new Font("Arial", Font.PLAIN, (int) FONT_SIZE);
		}
		FONT = newFont;
		try {
			ge.registerFont(newFont = Font.createFont(Font.TRUETYPE_FONT, new File(FONT_DIR + "Square.ttf")));
			newTBFont = newFont.deriveFont(Font.PLAIN, FONT_SIZE);
		} catch (FontFormatException | IOException e) {
			newTBFont = new Font("Arial", Font.PLAIN, (int) FONT_SIZE);
		}
		TEXTBOX_FONT = newTBFont;
	}
	public static final float TRANSITION_TIME = 0.7f;
	
	/*
	 * Number of levels
	 */
	public static final int NUM_LEVELS;
	static {
		File currentDir = new File(LEVEL_DIR);
		String[] levelList = currentDir.list(new FilenameFilter() {			
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".nlf");
			}
		});
		int highestLevel = 0;
		for (String level : levelList) {
			int levelNum = Integer.parseInt(level.substring(0, level.length()-4));
			highestLevel = Math.max(highestLevel, levelNum);
		}
		NUM_LEVELS = highestLevel / 10;
	}
	
	/*
	 * Player movement
	 */
	public static final float PLAYER_MOVEMENT = 5000f;
	
	
	/*
	 * Charge up time + min/maximum launch velocity for bombs / paint balls
	 */
	public static final float LAUNCH_TIME = 1f;
	public static final float MIN_LAUNCH = 10f;
	public static final float MAX_LAUNCH = 40f;
	
	/*
	 * Splash velocity
	 */
	public static final float SPLASH_FACTOR = 0.58f;
	public static final float SPLASH_MAX = 13f;

	/*
	 * Collision categories
	 */
	public static final int EVERYTHING = 0xFFFF; // Should not be used as a category, only as a mask
	public static final int NOTHING = 0x0000; // Should not be used as a category, only as a mask
	public static final int BOUNDARIES = 0x0001;
	public static final int SENSORS = 0x0002;
	public static final int PLAYER = 0x0004;
	public static final int BACKGROUND = 0x0010;
	public static final int RED_OBJECTS = 0x0020;
	public static final int BLUE_OBJECTS = 0x0040;
	public static final int GRAY_OBJECTS = 0x0080;
	public static final int OBJECTS = RED_OBJECTS | BLUE_OBJECTS | GRAY_OBJECTS;
    public static final int RED_PROJECTILES  = 0x0100;
    public static final int BLUE_PROJECTILES  = 0x0200;
    public static final int PROJECTILES = RED_PROJECTILES | BLUE_PROJECTILES;
    
    /*
	 * Colors
	 */
    public static final Color BACKGROUND_COLOR = Color.decode("#fbfaf8");
	public static final Color COLLAPSIBLEDOOR_COLOR = Color.decode("#47463E");
	public static final Color DOOR_COLOR = Color.decode("#47463E");
	public static final Color GOAL_COLOR = Color.decode("#F7DE5D");
	public static final Color PLAYER_COLOR = Color.decode("#94816A");
	public static final Color SIGN_COLOR = Color.decode("#D9D5BB");
	public static final Color START_WIN_COLOR = Color.decode("#3F3E37");
	public static final Color SWITCH_COLOR = Color.decode("#877DB0");
	public static final Color WALL_COLOR = Color.decode("#3F3E37");

	public static final Color RED_COLOR = Color.decode("#F56157");
	public static final Color BLUE_COLOR = Color.decode("#91CCBD");
	public static final Color GRAY_COLOR = Color.decode("#7A796C");
	
	public static final Color DARK_RED_COLOR = Color.decode("#C74F47");
	public static final Color DARK_BLUE_COLOR = Color.decode("#7DB0A3");

	public static enum ObjectColor {
		RED (RED_OBJECTS, RED_COLOR, DARK_RED_COLOR, RED_PROJECTILES),
		BLUE (BLUE_OBJECTS, BLUE_COLOR, DARK_BLUE_COLOR, BLUE_PROJECTILES),
		GRAY (GRAY_OBJECTS, GRAY_COLOR, GRAY_COLOR, 0);
		
		public final int category;
		public final Color drawColor;
		public final Color darkDrawColor;
        public final int projectileCategory;
		ObjectColor(int collidesWith, Color c, Color c2, int projectileCategory) {
			this.category = collidesWith;
			this.drawColor = c;
			this.darkDrawColor = c2;
            this.projectileCategory = projectileCategory;
		}
	}
	
	public static ObjectColor getOpposite(ObjectColor color) {
		return (color == ObjectColor.RED) ? ObjectColor.BLUE : ObjectColor.RED;
	}
	
	
	/*
	 * Grav lift acceleration
	 */
	public static final float LIFT_ACCEL = World.GRAVITY.y * 5f;
	public static final float PULSE_SPEED = 1f;
	
	/*
	 * Drizzler
	 */
	public static final float DRIZZLER_INTERVAL = 0.2f;
	
	
	
	public static final Color BG_COLOR = Color.GRAY;
	public static final int MAX_VIEWPORT_SIZE = 700;
	public static final float SCALE_FACTOR = 1.20f;
	public static final float SCALE_MAX = 6f; // Scrolling closer
	public static final float SCALE_MIN = 3f; // Scrolling out
	

}
