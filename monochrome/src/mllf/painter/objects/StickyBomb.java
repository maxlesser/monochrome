package mllf.painter.objects;

import static mllf.painter.mechanics.Constants.*;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

import mllf.gameengine.entity.SoundSource;
import mllf.gameengine.ui.Viewport;
import mllf.gameengine.gameloop.World;
import mllf.gameengine.collision.BodyGenerator;
import mllf.gameengine.entity.PhysicsEntity;
import mllf.painter.mechanics.Constants.ObjectColor;

import mllf.painter.mechanics.Paintable;
import org.jbox2d.dynamics.contacts.Contact;
import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.dynamics.joints.WeldJointDef;

import cs195n.Vec2f;

/**
 * Created By: Max Lesser
 * Date: 11/25/13
 * Time: 12:21 PM
 */
public class StickyBomb extends PhysicsEntity implements Paintable {

    private PhysicsEntity stuckOnto = null;
    private boolean hasStuck = false;

    private boolean createJoint = false;
    private boolean destroyJoint = false;
    private boolean hasExploded = false;
    private WeldJointDef weldJointDef;
    private Joint weldJoint;
    private float stuckTimer = 0;

    private int stuckColor = 0;

    private ObjectColor color;

    private Player player;

    public StickyBomb(Vec2f centerPosition, Vec2f velocity, ObjectColor color, World w, Player p)
    {
        super(BodyGenerator.generateCircle(centerPosition, 1, 1f, false, false, color.category, BOUNDARIES | OBJECTS | PROJECTILES, w), w);
        zIndex = 2;
        getBody().setLinearVelocity(velocity.toVec2());
        getBody().setBullet(true);
        this.color = color;
        player = p;
    }

    public void explode()
    {
        destroyJoint = true;
        player.removeBomb(this);
    }

    @Override
    public void tick(float s) {
        if(hasStuck)
            stuckTimer += s;
        if(!hasExploded)
        {
            if(createJoint)
            {
                weldJoint = world.getBWorld().createJoint(weldJointDef);
                createJoint = false;
            }
            if(destroyJoint)
            {
                if(weldJoint != null)
                    world.getBWorld().destroyJoint(weldJoint);
                world.remove(this);
                Explosion exp;
                if(color == ObjectColor.RED)
                    exp = new Explosion(getCenterPosition(), new Vec2f(1, 1), 1000, 1f, ObjectColor.RED, world);
                else
                    exp = new Explosion(getCenterPosition(), new Vec2f(1, 1), 1000, 1f, ObjectColor.BLUE, world);
                world.addEntity(exp);
                SoundSource sp = new SoundSource(getCenterPosition(), "sounds/blast.wav");
                sp.play(world.getPlayer());
                hasExploded = true;
                player.removeBomb(this);
            }
        }
    }

    public boolean hasExploded()
    {
        return hasExploded;
    }

    @Override
    public void draw(Graphics2D g, Viewport viewport) {
        Vec2f screenPos = viewport.gameToScreen(getCenterPosition().minus(1, 1));
        float screenSize = 1 * 2 * viewport.getScale();
        Ellipse2D.Float ellipse = new Ellipse2D.Float(screenPos.x, screenPos.y, screenSize, screenSize);

        AffineTransform transform = new AffineTransform();
        //transform.rotate(body.getAngle(), ellipse.getX() + ellipse.width/2, ellipse.getY() + ellipse.height/2);
        Shape transformed = transform.createTransformedShape(ellipse);

        g.setColor(color.drawColor);
        g.setStroke(new BasicStroke());
        g.fill(transformed);
    }

    @Override
    public void beginContact(PhysicsEntity e, Contact contact) {
        if(hasStuck && stuckTimer > 0.5) {
            destroyJoint = true;
        } else {
            if(!(e instanceof Bullet))
            {
                hasStuck = true;
                createJoint = true;
                weldJointDef = new WeldJointDef();
                weldJointDef.bodyA = e.getBody();
                weldJointDef.bodyB = getBody();
                weldJointDef.initialize(e.getBody(), getBody(), getCenterPosition().toVec2());
                stuckOnto = e;
                stuckColor = 150;
            }
        }
    }

    @Override
    public void endContact(PhysicsEntity e, Contact contact) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setObjectColor(ObjectColor color) {
    	
    }

	@Override
	public ObjectColor getObjectColor() {
		return color;
	}
}
