package mllf.painter.objects;

import static mllf.painter.mechanics.Constants.*;

import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

import mllf.gameengine.ui.Viewport;
import mllf.gameengine.gameloop.World;
import mllf.gameengine.collision.BodyGenerator;
import mllf.gameengine.entity.PhysicsEntity;

import org.jbox2d.dynamics.contacts.Contact;

import cs195n.Vec2f;

public class SplashParticle extends PhysicsEntity {
	
	private boolean hasContact, canContact;
	private int immunityCount;
	private ObjectColor color;

	public SplashParticle(Vec2f position, Vec2f velocity, ObjectColor color, World w) {
		super(BodyGenerator.generateCircle(position, 0.14f, 1, false, false, PROJECTILES, BOUNDARIES | OBJECTS, w),
				w);
		zIndex = 2;
		this.color = color;
		getBody().setLinearVelocity(velocity.toVec2());
	}
	
	@Override
	public void tick(float s) {
		if (immunityCount == 3) {
			if (Vec2f.fromVec2(this.getBody().m_linearVelocity).y >= 0) canContact = true;
		} else {
			immunityCount++;
		}
		if (hasContact && canContact) {
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
		hasContact = true;
	}

	@Override
	public void endContact(PhysicsEntity otherEntity, Contact contact) {
		hasContact = false;
	}
}
