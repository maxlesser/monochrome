package mllf.painter.levels;

import static mllf.painter.mechanics.Constants.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import mllf.gameengine.gameloop.Application;
import mllf.gameengine.gameloop.World;
import mllf.gameengine.collision.BodyGenerator;
import mllf.gameengine.entity.Entity;
import mllf.gameengine.entity.PhysicsEntity;
import mllf.gameengine.io.Connection;
import mllf.gameengine.io.Relay;
import mllf.gameengine.io.Sensor;
import mllf.painter.mechanics.Constants;
import mllf.painter.mechanics.Constants.ObjectColor;
import mllf.painter.mechanics.*;
import mllf.painter.mechanics.Lift.Direction;

import mllf.painter.objects.*;
import mllf.painter.ui.LevelScreen;
import mllf.painter.ui.LevelSelectScreen;
import org.jbox2d.common.Vec2;

import cs195n.CS195NLevelReader;
import cs195n.CS195NLevelReader.InvalidLevelException;
import cs195n.LevelData;
import cs195n.Vec2f;


/**
 * Created By: Max Lesser
 * Date: 11/12/13
 * Time: 8:37 PM
 */
public class LevelParser {

    public static LevelScreen createLevelScreen(Application app, int level, LevelSelectScreen levelSelect) throws LevelParseException
    {
        World world = new World();
        LevelReferee ref = new LevelReferee(world, level);
        
        /*
         * Map of level builder names to classes
         * Comments above each class indicate the necessary properties in the NLF
         * All properties should be lower cased
         */
        HashMap<String, Class<?>> classes = new HashMap<String, Class<?>>();
        classes.put("Polygon", Polygon.class);

        // visible:boolean
        classes.put("Sensor", Sensor.class);
        classes.put("Relay", Relay.class);

        //density:float
        //static:boolean
        /*
        Inputs:
            openUpStay:opendistance:float. Stays up until openDownClosed is called.
            openDownStay:opendistance:float. Stays down until openUpClosed is called.
            openUpTimed:
                totalWait:float
                    total time it waits while fully open
            openDownTimed:
                totalWait:float
                    total time it waits while fully open
        });
         */
        classes.put("Door", Door.class);

        // density:float
        classes.put("Player", Player.class);

        // text:semicolon separated strings
        classes.put("Sign", Sign.class);

        // clip:string
        classes.put("Speaker", Speaker.class);

        // color:enum
        // direction:enum
        // acceleration:float
        classes.put("Lift", Lift.class);
        
        // color:enum
        // density:float
        classes.put("Ball", Ball.class);

        // color:enum
        // density:float
        classes.put("Box", Box.class);

        // color:enum
        classes.put("Drizzler", Drizzler.class);


        classes.put("Wall", Wall.class);
        classes.put("ColorWall", ColorWall.class);
        
        //opendistance:float
        //time:float
        //isopen:boolean
        //isvertical:boolean
        classes.put("CollapsibleDoor", CollapsibleDoor.class);
        
        classes.put("Switch", Switch.class);
        classes.put("Goal", Goal.class);

        /*
         * Initialize world objects from .nlf file
         */
        LevelData levelData = null;
        try {
			levelData = CS195NLevelReader.readLevel(new File(Constants.LEVEL_DIR + level + ".nlf"));
		} catch (FileNotFoundException e1) {
			throw new LevelParseException("Could not find level " + level + " file.");
		} catch (InvalidLevelException e1) {
			throw new LevelParseException("Invalid level format.");
		}

        /*
         * Map of properties from level file
         */
        Map<String, String> levelInfo = levelData.getProperties();
        String title = levelInfo.get("title") != null ? levelInfo.get("title") : "";
        String subtitle = levelInfo.get("subtitle") != null ? levelInfo.get("subtitle") : "";

        /*
         * Set initial world properties
         */

        // World properties can be taken from the NLF File
        world.getBWorld().setGravity(new Vec2(0, 10));
        
        try {
        	/*
        	 * Map of entity names (as defined in .nlf file) to actual Entity instances
        	 */
            HashMap<String, Entity> entityNames = new HashMap<String, Entity>();

            for(LevelData.EntityData e : levelData.getEntities())
            {
                if(classes.containsKey(e.getEntityClass()))
                {
                    if(classes.get(e.getEntityClass()) == Polygon.class)
                    {
                    	LevelData.ShapeData data = e.getShapes().get(0);
                        Map<String, String> properties = e.getProperties();
                        Polygon p = new Polygon(
                        		data.getCenter(),
                                new ArrayList<Vec2f>(data.getVerts()),
                                1f,
                                parseObjectColor(properties.get("color")),
                                world);
                        entityNames.put(e.getName(), p);
                        world.addEntity(p);
                    }
                    else if(classes.get(e.getEntityClass()) == Player.class)
                    {
                        LevelData.ShapeData data = e.getShapes().get(0);
                        Map<String, String> properties = e.getProperties();
                        Player p = new Player(data.getCenter(), new Vec2f(data.getWidth(), data.getHeight()),
                                Float.parseFloat(properties.get("density")), world);
                        entityNames.put(e.getName(), p);
                        world.addEntity(p);
                        world.setPlayer(p);
                    }
                    else if(classes.get(e.getEntityClass()) == Wall.class)
                    {
                        LevelData.ShapeData data = e.getShapes().get(0);
                        Map<String, String> properties = e.getProperties();
                        if(properties.containsKey("friction"))
                        {
                            Wall wall = new Wall(
                                    data.getCenter(),
                                    new Vec2f(data.getWidth(), data.getHeight()),
                                    Float.parseFloat(properties.get("friction")),
                                    world);
                            entityNames.put(e.getName(), wall);
                            world.addEntity(wall);
                        }
                        else
                        {
                            Wall wall = new Wall(
                                    data.getCenter(),
                                    new Vec2f(data.getWidth(), data.getHeight()),
                                    world);
                            entityNames.put(e.getName(), wall);
                            world.addEntity(wall);
                        }
                    }
                    else if(classes.get(e.getEntityClass()) == ColorWall.class)
                    {
                        LevelData.ShapeData data = e.getShapes().get(0);
                        Map<String, String> properties = e.getProperties();
                        ColorWall wall = new ColorWall(
                                data.getCenter(),
                                new Vec2f(data.getWidth(), data.getHeight()),
                                parseObjectColor(properties.get("color")),
                                world);
                        entityNames.put(e.getName(), wall);
                        world.addEntity(wall);
                    }
                    else if(classes.get(e.getEntityClass()) == Sensor.class)
                    {
                        LevelData.ShapeData data = e.getShapes().get(0);
                        Map<String, String> properties = e.getProperties();
                        Sensor s = null;
                        ArrayList<PhysicsEntity> triggers = new ArrayList<>();
                        triggers.add(world.getPlayer());

                        if(data.getType() == LevelData.ShapeData.Type.BOX) {
                            s = new Sensor(
                                    BodyGenerator.generateAAB(
                                            data.getCenter(),
                                            new Vec2f(data.getWidth(), data.getHeight()),
                                            0,
                                            true,
                                            true,
                                            SENSORS,
                                            EVERYTHING,
                                            world),
                                    Boolean.parseBoolean(properties.get("visible")),
                                    world);
                        } else if(data.getType() == LevelData.ShapeData.Type.CIRCLE) {
                            s = new Sensor(
                                    BodyGenerator.generateCircle(
                                            data.getCenter(),
                                            data.getRadius(),
                                            0,
                                            true,
                                            true,
                                            SENSORS,
                                            EVERYTHING,
                                            world),
                                    Boolean.parseBoolean(properties.get("visible")),
                                    world);
                        } else {
                            s = new Sensor(
                                    BodyGenerator.generatePolygon(
                                            data.getCenter(),
                                            data.getVerts(),
                                            0,
                                            true,
                                            true,
                                            SENSORS,
                                            EVERYTHING,
                                            world),
                                    Boolean.parseBoolean(properties.get("visible")),
                                    world);

                        }
                        entityNames.put(e.getName(), s);
                        world.addEntity(s);
                    }
                    else if(classes.get(e.getEntityClass()) == Switch.class)
                    {
                        LevelData.ShapeData data = e.getShapes().get(0);
                        Switch s = null;
                        ArrayList<PhysicsEntity> triggers = new ArrayList<>();
                        triggers.add(world.getPlayer());

                        if(data.getType() == LevelData.ShapeData.Type.BOX) {
                            s = new Switch(
                                    BodyGenerator.generateAAB(
                                            data.getCenter(),
                                            new Vec2f(data.getWidth(), data.getHeight()),
                                            0,
                                            true,
                                            true,
                                            SENSORS,
                                            EVERYTHING,
                                            world),
                                    world);
                        } else if(data.getType() == LevelData.ShapeData.Type.CIRCLE) {
                            s = new Switch(
                                    BodyGenerator.generateCircle(
                                            data.getCenter(),
                                            data.getRadius(),
                                            0,
                                            true,
                                            true,
                                            SENSORS,
                                            EVERYTHING,
                                            world),
                                    world);
                        } else {
                            s = new Switch(
                                    BodyGenerator.generatePolygon(
                                            data.getCenter(),
                                            data.getVerts(),
                                            0,
                                            true,
                                            true,
                                            SENSORS,
                                            EVERYTHING,
                                            world),
                                    world);

                        }
                        entityNames.put(e.getName(), s);
                        world.addEntity(s);
                    }
                    else if(classes.get(e.getEntityClass()) == Goal.class)
                    {
                        LevelData.ShapeData data = e.getShapes().get(0);
                        Goal g = null;
                        ArrayList<PhysicsEntity> triggers = new ArrayList<>();
                        triggers.add(world.getPlayer());

                        if(data.getType() == LevelData.ShapeData.Type.BOX) {
                            g = new Goal(
                                    BodyGenerator.generateAAB(
                                            data.getCenter(),
                                            new Vec2f(data.getWidth(), data.getHeight()),
                                            0,
                                            true,
                                            true,
                                            SENSORS,
                                            PLAYER,
                                            world),
                                    world);
                        } else if(data.getType() == LevelData.ShapeData.Type.CIRCLE) {
                            g = new Goal(
                                    BodyGenerator.generateCircle(
                                            data.getCenter(),
                                            data.getRadius(),
                                            0,
                                            true,
                                            true,
                                            SENSORS,
                                            PLAYER,
                                            world),
                                    world);
                        } else {
                            g = new Goal(
                                    BodyGenerator.generatePolygon(
                                            data.getCenter(),
                                            data.getVerts(),
                                            0,
                                            true,
                                            true,
                                            SENSORS,
                                            PLAYER,
                                            world),
                                    world);

                        }
                        entityNames.put(e.getName(), g);
                        world.addEntity(g);
                    }
                    else if(classes.get(e.getEntityClass()) == Relay.class)
                    {
                        Relay r = new Relay(world);
                        entityNames.put(e.getName(), r);
                    }
                    else if (classes.get(e.getEntityClass()) == Sign.class)
                    {
                    	LevelData.ShapeData data = e.getShapes().get(0);
                    	Sign s = new Sign(
                    			data.getCenter(),
                    			new Vec2f(data.getWidth(), data.getHeight()),
                    			world,
                    			e.getProperties().get("text").split(";"));
                    	entityNames.put(e.getName(), s);
                    	world.addEntity(s);
                    }
                    else if(classes.get(e.getEntityClass()) == Door.class)
                    {
                        LevelData.ShapeData data = e.getShapes().get(0);
                        Map<String, String> properties = e.getProperties();
                        Door door = new Door(data.getCenter(), new Vec2f(data.getWidth(), data.getHeight()), Float.parseFloat(properties.get("opendistance")), world);
                        entityNames.put(e.getName(), door);
                        world.addEntity(door);
                    }
                    else if (classes.get(e.getEntityClass()) == Speaker.class)
                    {
                    	 LevelData.ShapeData data = e.getShapes().get(0);
                         Map<String, String> properties = e.getProperties();
                         Speaker speaker = new Speaker(data.getCenter(), new Vec2f(data.getWidth(), data.getHeight()),
                        		 properties.get("clip"), world);
                         entityNames.put(e.getName(), speaker);
                         world.addEntity(speaker);
                    }
                    else if (classes.get(e.getEntityClass()) == Lift.class)
                    {
                    	LevelData.ShapeData data = e.getShapes().get(0);
                    	Map<String, String> properties = e.getProperties();
                    	Lift lift = new Lift(data.getCenter(),
                    			new Vec2f(data.getWidth(), data.getHeight()),
                    			parseObjectColor(properties.get("color")),
                    			Float.parseFloat(properties.get("acceleration")),
                    			parseDirection(properties.get("direction")),
                    			world);
                    	entityNames.put(e.getName(), lift);
                    	world.addEntity(lift);
                    }
                    else if (classes.get(e.getEntityClass()) == Ball.class)
                    {
                    	LevelData.ShapeData data = e.getShapes().get(0);
                    	Map<String, String> properties = e.getProperties();
                    	Ball ball = new Ball(
                    			data.getCenter(), data.getRadius(),
                    			Float.parseFloat(properties.get("density")), parseObjectColor(properties.get("color")),
                    			world);
                    	entityNames.put(e.getName(), ball);
                    	world.addEntity(ball);
                    }
                    else if (classes.get(e.getEntityClass()) == Box.class)
                    {
                    	LevelData.ShapeData data = e.getShapes().get(0);
                    	Map<String, String> properties = e.getProperties();
                    	Box box = new Box(
                    			data.getCenter(), new Vec2f(data.getWidth(), data.getHeight()),
                    			Float.parseFloat(properties.get("density")), parseObjectColor(properties.get("color")),
                    			world);
                    	entityNames.put(e.getName(), box);
                    	world.addEntity(box);
                    }
                    else if (classes.get(e.getEntityClass()) == Drizzler.class)
                    {
                    	LevelData.ShapeData data = e.getShapes().get(0);
                    	Map<String, String> properties = e.getProperties();
                        if(properties.containsKey("interval"))
                        {
                            Drizzler drizzler = new Drizzler(data.getCenter(), new Vec2f(data.getWidth(), data.getHeight()),
                                    parseObjectColor(properties.get("color")), Float.parseFloat(properties.get("interval")), world);
                            entityNames.put(e.getName(), drizzler);
                            world.addEntity(drizzler);
                        }
                        else
                        {
                            Drizzler drizzler = new Drizzler(data.getCenter(), new Vec2f(data.getWidth(), data.getHeight()),
                                    parseObjectColor(properties.get("color")), world);
                            entityNames.put(e.getName(), drizzler);
                            world.addEntity(drizzler);
                        }
                    }
                    else if (classes.get(e.getEntityClass()) == CollapsibleDoor.class)
                    {
                    	LevelData.ShapeData data = e.getShapes().get(0);
                    	Map<String, String> properties = e.getProperties();
                    	CollapsibleDoor door = new CollapsibleDoor(
                    			data.getCenter(), new Vec2f(data.getWidth(), data.getHeight()),
                    			Float.parseFloat(properties.get("opendistance")),
                    			Float.parseFloat(properties.get("time")),
                                Boolean.parseBoolean(properties.get("isopen")),
                                Boolean.parseBoolean(properties.get("isvertical")), world);
                    	entityNames.put(e.getName(), door);
                    	world.addEntity(door);
                    }
                }
            }

            entityNames.put("ref", ref);

            for(LevelData.ConnectionData cd : levelData.getConnections())
            {
                if(entityNames.containsKey(cd.getSource()) && entityNames.containsKey(cd.getTarget()))
                {
                    Connection c = new Connection(entityNames.get(cd.getTarget()).getInputByName(cd.getTargetInput()), cd.getProperties());
                    entityNames.get(cd.getSource()).getOutputByName(cd.getSourceOutput()).connect(c);
                }
            }
        } catch (NullPointerException | NumberFormatException e) {
            e.printStackTrace();
        	throw new LevelParseException("Invalid level data.");
        }


        return new LevelScreen(app, title, subtitle, ref, levelSelect, world);
    }
    
    private static ObjectColor parseObjectColor(String str) {
    	switch (str.toUpperCase()) {
    	case "RED":
    		return ObjectColor.RED;
    	case "BLUE":
    		return ObjectColor.BLUE;
    	default:
    		return ObjectColor.GRAY;
    	}
    }
    
    private static Direction parseDirection(String str) {
    	switch (str.toUpperCase()) {
    	case "LEFT":
    		return Direction.LEFT;
    	case "RIGHT":
    		return Direction.RIGHT;
    	case "UP":
    		return Direction.UP;
    	default:
    		return Direction.DOWN;
    	}
    }
}
