package mllf.painter.objects;

import static mllf.painter.mechanics.Constants.EVERYTHING;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

import mllf.painter.mechanics.Paintable;
import org.jbox2d.dynamics.Filter;
import org.jbox2d.dynamics.contacts.Contact;

import cs195n.Vec2f;

import mllf.gameengine.ui.Viewport;
import mllf.gameengine.gameloop.World;
import mllf.gameengine.collision.BodyGenerator;
import mllf.gameengine.entity.PhysicsEntity;
import mllf.painter.mechanics.Constants.ObjectColor;

public class Ball extends PhysicsEntity implements Paintable {

	private ObjectColor color;
	
	public Ball(Vec2f centerPosition, float radius, float density, ObjectColor color, World w) {
		super(BodyGenerator.generateCircle(centerPosition, radius, density, false, false, color.category, EVERYTHING, w), w);
		zIndex = 2;
        getBody().setBullet(true);
        this.color = color;
	}

	@Override
	public void beginContact(PhysicsEntity otherEntity, Contact contact) {

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
		Ellipse2D.Float ellipse = new Ellipse2D.Float(screenPos.x, screenPos.y, screenSize.x, screenSize.y);
		g.setColor(color.drawColor);
		
		AffineTransform transform = new AffineTransform();
		transform.rotate(getBody().getAngle(), ellipse.getX() + ellipse.width/2, ellipse.getY() + ellipse.height/2);
		Shape transformed = transform.createTransformedShape(ellipse);
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
