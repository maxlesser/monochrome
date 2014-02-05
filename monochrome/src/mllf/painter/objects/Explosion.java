package mllf.painter.objects;

import java.awt.Graphics2D;
import java.util.ArrayList;

import mllf.gameengine.ui.Viewport;
import mllf.gameengine.gameloop.World;
import mllf.gameengine.entity.Entity;
import cs195n.Vec2f;
import mllf.painter.mechanics.Constants;

/**
* Created By: Max Lesser
* Date: 10/jbox2d/13
* Time: 5:33 PM
*/
public class Explosion extends Entity {

    private World world;

    private Vec2f location;
    private Vec2f dimensions;

    private float time;
    private float fullTime;

    private ArrayList<Bullet> strokes;

    public Explosion(Vec2f loc, Vec2f dim, float power, float full, Constants.ObjectColor color, World w)
    {
        super(w);
        world = w;

        location = loc;
        dimensions = dim;
        time = 0f;
        fullTime = full;

        strokes = new ArrayList<>();

        for(int x = 0; x < 100; x++)
        {
            float angle = (float)Math.toRadians((x / 100f) * 360);
            Vec2f rayDir = new Vec2f((float)Math.sin(angle), (float)Math.cos(angle));

            Bullet b = new Bullet(loc, rayDir, power, color, w);

            strokes.add(b);
            world.addEntity(b);
        }
    }

    @Override
    public void tick(float nanos)
    {
        int timeLeft = (int)((fullTime - time) * 1000);
        if(timeLeft < 256)
        {
            for(Bullet b : strokes)
            {
                b.setTransparency(timeLeft);
            }
        }

        if(time > fullTime)
        {
            world.remove(this);
            for(Entity aab : strokes)
            {
                world.remove(aab);
            }
        }
        time += nanos;
    }
    

   	public void draw(Graphics2D g, Viewport v)
   	{
   		
   	}
}
