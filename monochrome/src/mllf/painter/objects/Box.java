package mllf.painter.objects;

import static mllf.painter.mechanics.Constants.EVERYTHING;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import mllf.gameengine.ui.Viewport;
import mllf.gameengine.gameloop.World;
import mllf.gameengine.collision.BodyGenerator;
import mllf.gameengine.entity.PhysicsEntity;
import mllf.gameengine.io.Output;
import mllf.painter.mechanics.Constants.ObjectColor;

import mllf.painter.mechanics.Paintable;
import org.jbox2d.dynamics.Filter;
import org.jbox2d.dynamics.contacts.Contact;

import cs195n.Vec2f;

public class Box extends PhysicsEntity implements Paintable {
	
	private ObjectColor color;

	public Box(Vec2f centerPosition, Vec2f size, float density, ObjectColor color, World w) {
		super(BodyGenerator.generateAAB(centerPosition, size, density, false, false, color.category, EVERYTHING, w), w);
		zIndex = 2;
        getBody().setBullet(true);
		this.color = color;
		this.addOutput("onCollidesPaintBall", new Output());
		this.addOutput("onCollidesBullet", new Output());
	}

	@Override
	public void beginContact(PhysicsEntity otherEntity, Contact contact) {
		if (otherEntity instanceof PaintBall) {
			this.getOutputByName("onCollidesPaintBall").run();
		}
		if (otherEntity instanceof Bullet) {
			this.getOutputByName("onCollidesBullet").run();
		}
	}

	@Override
	public void endContact(PhysicsEntity otherEntity, Contact contact) {

	}

	@Override
	public void tick(float s) {

	}

	@Override
	public void draw(Graphics2D g, Viewport viewport) {
		Vec2f screenPos = viewport.gameToScreen(getCenterPosition().minus(getSize().pdiv(2, 2)));
		Vec2f screenSize = getSize().smult(viewport.getScale());
		Rectangle2D.Float rect = new Rectangle2D.Float(screenPos.x, screenPos.y, screenSize.x, screenSize.y);
		g.setColor(color.drawColor);
		
		AffineTransform transform = new AffineTransform();
		transform.rotate(getBody().getAngle(), rect.getX() + rect.width/2, rect.getY() + rect.height/2);
		Shape transformed = transform.createTransformedShape(rect);
		g.fill(transformed);
	}

	@Override
	public void setObjectColor(ObjectColor color) {
		this.color = color;
		Filter filter = this.getBody().getFixtureList().getFilterData();
		filter.categoryBits = color.category;
		this.getBody().getFixtureList().setFilterData(filter);
	}

	@Override
	public ObjectColor getObjectColor() {
		return color;
	}

}
