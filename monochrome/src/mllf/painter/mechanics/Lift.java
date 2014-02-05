package mllf.painter.mechanics;

import static mllf.painter.mechanics.Constants.*;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mllf.gameengine.ui.Viewport;
import mllf.gameengine.gameloop.World;
import mllf.gameengine.collision.BodyGenerator;
import mllf.gameengine.entity.PhysicsEntity;
import mllf.gameengine.io.Connection;
import mllf.gameengine.io.Input;
import mllf.gameengine.io.Sensor;

import org.jbox2d.dynamics.contacts.Contact;

import cs195n.Vec2f;

public class Lift extends PhysicsEntity {

	private Sensor sensor;
	private List<PhysicsEntity> collidingObjects;
	private ObjectColor color;
	private float acceleration;
	private Direction dir;
	private float pulse;
	
	/**
	 * We're going to need some concept of height?
	 * So that we can apply some impulse on the "otherEntity"
	 * proportional to its centerPosition relative to the lift's height.
	 * Will have to consider how we're going to draw this on the game.
	 * A gradient?? That would work really well above the initial art
	 * for the "source". Source should be a constant size.
	 * @param
	 * @param w
	 */
	public Lift(Vec2f centerPosition, Vec2f size, ObjectColor color, float acceleration, Direction dir, World w) {
		super(BodyGenerator.generateAAB(centerPosition, size, 0f, true, true, BACKGROUND, NOTHING, w), w);
		this.color = color;
		this.acceleration = acceleration;
		this.dir = dir;
		switch (dir) {
		case UP:
			pulse = centerPosition.plus(size.sdiv(2f)).y;
			break;
		case RIGHT:
			pulse = centerPosition.minus(size.sdiv(2f)).x;
			break;
		case LEFT:
			pulse = centerPosition.plus(size.sdiv(2f)).x;
			break;
		default: // DOWN
			pulse = centerPosition.minus(size.sdiv(2f)).y;
		}
		sensor = new Sensor(
				BodyGenerator.generateAAB(centerPosition, size, 0, true, true, SENSORS, EVERYTHING, w), false,
				world);
		collidingObjects = new ArrayList<>();
		this.addInput("doUpdate", new Input() {

			@Override
			public void run(Map<String, String> args) {
				collidingObjects = sensor.getCollidingObjects();
			}
		});
		sensor.getOutputByName("onBeginContact").connect(
				new Connection(this.getInputByName("doUpdate"), new HashMap<String, String>()));
		sensor.getOutputByName("onEndContact").connect(
				new Connection(this.getInputByName("doUpdate"), new HashMap<String, String>()));
	}

	@Override
	public void beginContact(PhysicsEntity otherEntity, Contact contact) {

	}

	@Override
	public void endContact(PhysicsEntity otherEntity, Contact contact) {

	}

	@Override
	public void tick(float s) {
		if (collidingObjects.size() > 0) {
			for (PhysicsEntity entity : collidingObjects) {
				
				if (!(entity instanceof Paintable) || ((Paintable) entity).getObjectColor() != color) {
					continue;
				}

                if(dir == Direction.UP)
                {
                    float distance = entity.getCenterPosition().plus(entity.getSize()).y - this.getPosition().y;
                    float yForce = distance / this.getSize().y * acceleration;
                    entity.applyForce(new Vec2f(0, -yForce * entity.getBody().m_mass));
                }
                else if(dir == Direction.LEFT)
                {
                    float distance = entity.getPosition().plus(entity.getSize()).x - this.getPosition().x;
                    float xForce = distance / this.getSize().x * acceleration;
                    entity.applyForce(new Vec2f(-xForce * entity.getBody().m_mass, 0));
                }
                else if(dir == Direction.RIGHT)
                {
                    float distance =  this.getPosition().plus(this.getSize()).x - entity.getPosition().x;
                    float xForce = distance / this.getSize().x * acceleration;
                    entity.applyForce(new Vec2f(xForce * entity.getBody().m_mass, 0));
                }
			}
		}
		switch (dir) {
		case UP:
			pulse -= (s/ Constants.PULSE_SPEED)*getSize().y;
			if (pulse < getPosition().y) {
				pulse = getPosition().y + getSize().y;
			}
			break;
		case LEFT:
			pulse -= (s/Constants.PULSE_SPEED)*getSize().x;
			if (pulse < getPosition().x) {
				pulse = getPosition().x + getSize().x;
			}
			break;
		case RIGHT:
			pulse += (s/Constants.PULSE_SPEED)*getSize().x;
			if (pulse > getPosition().x + getSize().x) {
				pulse = getPosition().x;
			}
			break;
		default: //DOWN
			pulse += (s/Constants.PULSE_SPEED)*getSize().y;
			if (pulse > getPosition().y + getSize().y) {
				pulse = getPosition().y;
			}
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
		// Pulse, gradient
		Vec2f screenPulse;
		Rectangle2D.Float pulseRect;
		
		if (dir == Direction.UP || dir == Direction.DOWN) { 
			screenPulse = v.gameToScreen(new Vec2f(getPosition().x, pulse));
			pulseRect = new Rectangle2D.Float(screenPulse.x, screenPulse.y, screenSize.x, 4);	
		} else {
			screenPulse = v.gameToScreen(new Vec2f(pulse, getPosition().y));
			pulseRect = new Rectangle2D.Float(screenPulse.x, screenPulse.y, 4, screenSize.y);	
		}
		g.fill(pulseRect);

	}
	
	public enum Direction {
		LEFT, RIGHT, UP, DOWN;
	}

}
