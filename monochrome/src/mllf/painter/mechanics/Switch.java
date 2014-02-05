package mllf.painter.mechanics;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import mllf.gameengine.ui.Viewport;
import mllf.gameengine.gameloop.World;
import mllf.gameengine.entity.PhysicsEntity;
import mllf.gameengine.io.Output;
import mllf.gameengine.io.Sensor;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.contacts.Contact;

import cs195n.Vec2f;

public class Switch extends Sensor {

	
	public Switch(Body body, World world) {
		super(body, false, world);
		this.addOutput("onSwitchOn", new Output());
		this.addOutput("onSwitchOff", new Output());
	}
	
	@Override
	public void beginContact(PhysicsEntity e, Contact contact) {
		if (this.getCollidingObjects().size() == 0) {
			this.getOutputByName("onSwitchOn").run();
		}
		super.beginContact(e, contact);
	}
	
	@Override
	public void endContact(PhysicsEntity e, Contact contact) {
		super.endContact(e, contact);
		if (this.getCollidingObjects().size() == 0) {
			this.getOutputByName("onSwitchOff").run();
		}
	}
	
	@Override
	public void draw(Graphics2D g, Viewport viewport) {
		 Vec2f screenPos = viewport.gameToScreen(getCenterPosition().minus(getSize().pdiv(2, 2)));
         Vec2f screenSize = getSize().smult(viewport.getScale());
         Rectangle2D.Float rect = new Rectangle2D.Float(screenPos.x, screenPos.y, screenSize.x, screenSize.y);
         g.setColor(Constants.SWITCH_COLOR);
         g.setStroke(new BasicStroke(2));
         g.draw(rect);
	}

}
