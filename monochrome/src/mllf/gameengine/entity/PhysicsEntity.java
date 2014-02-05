package mllf.gameengine.entity;

import mllf.gameengine.gameloop.World;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.contacts.Contact;

import cs195n.Vec2f;

/**
 * Created By: Max Lesser
 * Date: 11/18/13
 * Time: 2:23 AM
 */
public abstract class PhysicsEntity extends Entity {

    private Body body;
    private Vec2f size;

    public PhysicsEntity(Body b, World w)
    {
        super(w);
        body = b;
        size = Vec2f.fromVec2(body.getFixtureList().getAABB(0).getExtents()).smult(2);
        w.getBodyMapping().put(body, this);
    }

    public void setBody(Body b) {
    	body = b;
    }
    
    public Body getBody() {
        return body;
    }

    public void setSize(Vec2f size) {
    	this.size = size;
    }
    
    // gets the full bounding box dimensions of the body
    public Vec2f getSize()
    {
        return size;
    }

    // gets the center of the body
    public Vec2f getCenterPosition()
    {
        return Vec2f.fromVec2(body.getPosition());
    }

    public Vec2f getPosition()
    {
    	return getCenterPosition().minus(size.sdiv(2));
    }
    
    public void applyImpulse(Vec2f p)
    {
        body.applyLinearImpulse(p.toVec2(), getCenterPosition().toVec2());
    }
    
    public void applyForce(Vec2f f)
    {
    	body.applyForce(f.toVec2(), getCenterPosition().toVec2());
    }

    public abstract void beginContact(PhysicsEntity otherEntity, Contact contact);
    public abstract void endContact(PhysicsEntity otherEntity, Contact contact);
}
