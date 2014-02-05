package mllf.painter.mechanics;

import static mllf.painter.mechanics.Constants.*;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.Map;

import mllf.gameengine.ui.Viewport;
import mllf.gameengine.gameloop.World;
import mllf.gameengine.collision.BodyGenerator;
import mllf.gameengine.entity.PhysicsEntity;
import mllf.gameengine.io.Input;

import org.jbox2d.dynamics.contacts.Contact;

import cs195n.Vec2f;

/**
 * Created By: Max Lesser
 * Date: 11/19/13
 * Time: 9:17 PM
 */
public class Door extends PhysicsEntity {

    private boolean timedOpen = false;
    private boolean open = false;
    private Vec2f openDirection;
    private float openDistance;

    private float openCounter = 0;
    private float totalWait;

    public Door(Vec2f centerPosition, Vec2f size, float openDist, World w) {
    	super(BodyGenerator.generateAAB(centerPosition, size, 1f, true, false, BOUNDARIES, EVERYTHING, w), w);
        getBody().setFixedRotation(true);
        openDistance = openDist;

        addInput("openUpStay", new Input() {
            @Override
            public void run(Map<String, String> args) {
                open = true;
                totalWait = 0.5f;
                openCounter = 0;

                openDirection = new Vec2f(0, -1);
            }
        });
        addInput("openDownStay", new Input() {
            @Override
            public void run(Map<String, String> args) {
                open = true;
                totalWait = 0.5f;
                openCounter = 0;

                openDirection = new Vec2f(0, 1);
            }
        });
        addInput("openUpTimed", new Input() {
            @Override
            public void run(Map<String, String> args) {
                timedOpen = true;
                open = true;
                openCounter = 0;
                totalWait = Float.parseFloat(args.get("totalwait"));

                openDirection = new Vec2f(0, -1);
            }
        });
        addInput("openDownTimed", new Input() {
            @Override
            public void run(Map<String, String> args) {
                timedOpen = true;
                open = true;
                openCounter = 0;
                totalWait = Float.parseFloat(args.get("totalwait"));

                openDirection = new Vec2f(0, 1);
            }
        });
    }

    @Override
    public void tick(float seconds)
    {
        if(timedOpen)
        {
            openCounter += seconds;
            if(openCounter > totalWait)
            {
                openDirection = openDirection.smult(-1);
                open = true;
                timedOpen = false;
            }
        }
        if(open)
        {
            open = false;
            getBody().setTransform(getCenterPosition().plus(openDirection.smult(openDistance)).toVec2(), 0);
        }
    }

	@Override
	public void beginContact(PhysicsEntity otherEntity, Contact contact) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endContact(PhysicsEntity otherEntity, Contact contact) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Graphics2D g, Viewport viewport) {
        Vec2f screenPos = viewport.gameToScreen(getCenterPosition().minus(getSize().pdiv(2, 2)));
        Vec2f screenSize = getSize().smult(viewport.getScale());
        Rectangle2D.Float rect = new Rectangle2D.Float(screenPos.x, screenPos.y, screenSize.x, screenSize.y);
        g.setColor(Constants.DOOR_COLOR);

        AffineTransform transform = new AffineTransform();
        transform.rotate(getBody().getAngle(), rect.getX() + rect.width/2, rect.getY() + rect.height/2);
        Shape transformed = transform.createTransformedShape(rect);

        g.fill(transformed);
	}
}
