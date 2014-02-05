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

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.contacts.Contact;

import cs195n.Vec2f;

/**
 * Created By: Max Lesser
 * Date: 11/19/13
 * Time: 9:17 PM
 */
public class CollapsibleDoor extends PhysicsEntity {

	private boolean transitioning;
	private boolean isOpening;
	private float openDistance, counter, transitionTime, sizeWhenOpen, originalSize;
	private Vec2f position;
	private Body oldBody = null;
    private boolean isVertical;

    public CollapsibleDoor(Vec2f centerPosition, Vec2f size, float openDistance, float transitionTime, boolean isOpen, boolean isVertical, World w) {
    	super(BodyGenerator.generateAAB(centerPosition, size, 1f, true, false, BOUNDARIES, EVERYTHING, w), w);
    	zIndex = -1;
        getBody().setFixedRotation(true);
        this.isVertical = isVertical;

        if(isOpen)
        {
            counter = transitionTime;
            originalSize = size.y + openDistance;
            this.openDistance = openDistance;
            sizeWhenOpen = size.y;
            position = getPosition();
        }
        else
        {
            originalSize = size.y;
            this.openDistance = Math.min(openDistance, originalSize);
            sizeWhenOpen = originalSize - openDistance;
            position = getPosition();
        }
        this.transitionTime = transitionTime;

        addInput("doOpen", new Input() {
            @Override
            public void run(Map<String, String> args) {
                transitioning = true;
                isOpening = true;
            }
        });
        addInput("doClose", new Input() {
            @Override
            public void run(Map<String, String> args) {
                transitioning = true;
                isOpening = false;
            }
        });
    }

    @Override
    public void tick(float seconds)
    {
    	if (oldBody != null) {	
    		world.getBodyMapping().remove(oldBody);
    		oldBody = null;
    	}
    	if (transitioning) {
    		if (isOpening) {
    			counter += seconds;
    		} else {
    			counter -= seconds;
    		}
    		Vec2f newSize;
    		if (counter >= transitionTime) {
				newSize = new Vec2f(isVertical ? getSize().x : sizeWhenOpen, isVertical ? sizeWhenOpen : getSize().y);
				transitioning = false;
			} else if (counter <= 0) {
				newSize = new Vec2f(isVertical ? getSize().x : originalSize, isVertical ? originalSize : getSize().y);
				transitioning = false;
			} else {
    			float percentOpen = counter / transitionTime;
    			newSize = new Vec2f(isVertical ? getSize().x : (float) ((1f - Math.sin(percentOpen * Math.PI/2f))* openDistance) + sizeWhenOpen,
                        isVertical ? (float) ((1f - Math.sin(percentOpen * Math.PI/2f))* openDistance) + sizeWhenOpen : getSize().y);
    			
			}
    		world.getBWorld().destroyBody(this.getBody());
			oldBody = this.getBody();
			Body body;
    		this.setBody(body = BodyGenerator.generateAAB(position.plus(newSize.sdiv(2f)), newSize, 1f, true, false,
					BOUNDARIES, EVERYTHING, world));
			this.setSize(newSize);
			world.getBodyMapping().put(body, this);
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
        Vec2f screenPos = viewport.gameToScreen(getCenterPosition().minus(getSize().sdiv(2f)));
        Vec2f screenSize = getSize().smult(viewport.getScale());
        Rectangle2D.Float rect = new Rectangle2D.Float(screenPos.x, screenPos.y, screenSize.x, screenSize.y);
        g.setColor(Constants.COLLAPSIBLEDOOR_COLOR);

        AffineTransform transform = new AffineTransform();
        transform.rotate(getBody().getAngle(), rect.getX() + rect.width/2, rect.getY() + rect.height/2);
        Shape transformed = transform.createTransformedShape(rect);

        g.fill(transformed);
	}
}
