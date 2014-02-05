package mllf.painter.objects;

import static mllf.painter.mechanics.Constants.*;

import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

import mllf.gameengine.ui.Viewport;
import mllf.gameengine.gameloop.World;
import mllf.gameengine.collision.BodyGenerator;
import mllf.gameengine.entity.PhysicsEntity;

import mllf.painter.mechanics.Constants;
import mllf.painter.mechanics.Paintable;
import org.jbox2d.collision.WorldManifold;
import org.jbox2d.dynamics.contacts.Contact;

import cs195n.Vec2f;

public class PaintBall extends PhysicsEntity {
	
	private boolean hasContact;
	private Vec2f normal, collisionPoint, velocity;
	private ObjectColor color;

	public PaintBall(Vec2f position, Vec2f velocity, ObjectColor color, World w) {
		super(BodyGenerator.generateCircle(position, 0.5f, 1f, false, false, color.projectileCategory, BOUNDARIES | OBJECTS, w), w);
		zIndex = 2;
		getBody().setLinearVelocity(velocity.toVec2());
		this.color = color;
	}

	@Override
	public void tick(float s) {
		if (hasContact) {
			for (double rad = -Math.PI  * 3/8; rad <= Math.PI * 3/8; rad += Math.PI / 16) {
				double angle = Math.atan2(normal.y, normal.x) + rad; // Get angles relative to normal.
				Vec2f direction = Vec2f.fromPolar((float)angle, 1f);
				float maxVelocity = velocity.mag() * Constants.SPLASH_FACTOR;
                //SoundSource sp = new SoundSource(getCenterPosition(), "sounds/waterdrop.wav");
                //sp.play(world.getPlayer());
				world.addEntity(new SplashParticle(
						collisionPoint, direction.smult((float) (Math.random() * maxVelocity)), color, world));
			}
			world.remove(this);
		}
	}

	@Override
	public void draw(Graphics2D g, Viewport viewport) {
		Vec2f screenPos = viewport.gameToScreen(getPosition());
        float screenSize = getSize().x * viewport.getScale();
        Ellipse2D.Float ellipse = new Ellipse2D.Float(screenPos.x, screenPos.y, screenSize, screenSize);
        g.setColor(color.drawColor);
        g.fill(ellipse);
	}

	@Override
	public void beginContact(PhysicsEntity otherEntity, Contact contact) {
		 WorldManifold worldManifold = new WorldManifold();
		 contact.getWorldManifold(worldManifold);
		 normal = Vec2f.fromVec2(worldManifold.normal);
		 collisionPoint = Vec2f.fromVec2(worldManifold.points[0]);
		 hasContact = true;
		 velocity = Vec2f.fromVec2(this.getBody().m_linearVelocity);
		 if (otherEntity instanceof Paintable) {
			 ((Paintable) otherEntity).setObjectColor(color);
			 
		 }
	}

	@Override
	public void endContact(PhysicsEntity e, Contact contact) {
		
	}
}
