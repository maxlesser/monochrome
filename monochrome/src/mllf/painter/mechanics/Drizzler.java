package mllf.painter.mechanics;

import static mllf.painter.mechanics.Constants.*;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import mllf.gameengine.ui.Viewport;
import mllf.gameengine.gameloop.World;
import mllf.gameengine.collision.BodyGenerator;
import mllf.gameengine.entity.PhysicsEntity;

import mllf.painter.objects.PaintBall;
import org.jbox2d.dynamics.contacts.Contact;

import cs195n.Vec2f;

public class Drizzler extends PhysicsEntity {

	private ObjectColor color;
	private float counter;
	private Vec2f spawnPoint;
    private float interval;
	
	public Drizzler(Vec2f centerPosition, Vec2f size, ObjectColor color, World w) {
		super(BodyGenerator.generateAAB(centerPosition, size, 0f, true, true, BACKGROUND, NOTHING, w), w);
		this.color = color;
		this.spawnPoint = centerPosition.minus(new Vec2f(0, size.y / 2));
        interval = Constants.DRIZZLER_INTERVAL;
	}

    public Drizzler(Vec2f centerPosition, Vec2f size, ObjectColor color, float interval, World w) {
        super(BodyGenerator.generateAAB(centerPosition, size, 0f, true, true, BACKGROUND, NOTHING, w), w);
        this.color = color;
        this.spawnPoint = centerPosition.minus(new Vec2f(0, size.y / 2));
        this.interval = interval;
    }
	
	public void drip() {
		world.addEntity(new PaintBall(spawnPoint, new Vec2f(0, 0), color, world));
	}

	@Override
	public void beginContact(PhysicsEntity otherEntity, Contact contact) {

	}

	@Override
	public void endContact(PhysicsEntity otherEntity, Contact contact) {

	}

	@Override
	public void tick(float s) {
		counter += s;
		if (counter > interval) {
			counter = 0;
			this.drip();
		}
	}

	@Override
	public void draw(Graphics2D g, Viewport v) {
		Vec2f screenPos = v.gameToScreen(getPosition());
		Vec2f screenSize = getSize().smult(v.getScale());
		g.setColor(color.drawColor);
		g.setStroke(new BasicStroke(3f));
		Rectangle2D.Float rect = new Rectangle2D.Float(screenPos.x, screenPos.y, screenSize.x, screenSize.y);
		g.draw(rect);
	}

}
