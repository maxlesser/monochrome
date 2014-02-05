package mllf.painter.levels;

import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

import mllf.gameengine.resourcemanagement.SaveFileManager;
import mllf.gameengine.ui.Viewport;
import mllf.gameengine.gameloop.World;
import mllf.gameengine.entity.Entity;
import mllf.gameengine.io.Input;
import mllf.painter.mechanics.Constants;

/**
 * Created By: Max Lesser
 * Date: 11/18/13
 * Time: 2:18 AM
 */
public class LevelReferee extends Entity {

    private boolean levelWon;
    private boolean playerDied;

    private int currentLevel;

    public LevelReferee(World w, final int level)
    {
        super(w);

        currentLevel = level;

        addInput("doWinLevel", new Input() {
            @Override
            public void run(Map<String, String> args) {

                HashMap<String, String> oldSave = SaveFileManager.readPlayerSave();

                if(level == Integer.parseInt(oldSave.get("currentLevel")) && level != Constants.NUM_LEVELS*10)
                {
                    int nextLevel = ((Integer.parseInt(oldSave.get("currentLevel")) / 10) + 1) * 10;
                    oldSave.put("currentLevel",  "" + nextLevel);
                    SaveFileManager.writeSave(oldSave);
                    currentLevel = nextLevel;
                }
                else
                {
                    currentLevel = ((currentLevel / 10) + 1) * 10;
                }
                levelWon = true;
            }
        });
        addInput("doUpdateCurrentLevel", new Input() {
            @Override
            public void run(Map<String, String> args) {
                if(Integer.parseInt(args.get("newLevel")) > currentLevel)
                {
                    // write the new level to file now
                    HashMap<String, String> oldSave = SaveFileManager.readPlayerSave();
                    oldSave.put("currentLevel", args.get("newLevel"));
                    SaveFileManager.writeSave(oldSave);
                    currentLevel = Integer.parseInt(args.get("newLevel"));
                }
            }
        });
        addInput("doPlayerDie", new Input() {
            @Override
            public void run(Map<String, String> args) {
                // TODO: do some player die logic here
            }
        });
    }
    
    public int getCurrentLevel() {
    	return currentLevel;
    }

    public boolean isLevelWon() {
        return levelWon;
    }

    public void setLevelWon(boolean levelWon) {
        this.levelWon = levelWon;
    }

    public boolean isPlayerDied() {
        return playerDied;
    }

    public void setPlayerDied(boolean playerDied) {
        this.playerDied = playerDied;
    }

    @Override
    public void tick(float s) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void draw(Graphics2D g, Viewport v) {}
}
