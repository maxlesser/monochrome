package mllf.gameengine.io;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import cs195n.Vec2f;
import mllf.gameengine.ui.Viewport;
import mllf.gameengine.gameloop.World;
import mllf.gameengine.entity.PhysicsEntity;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.contacts.Contact;

public class Sensor extends PhysicsEntity {

	private BufferedImage image;
	private boolean isVisible;
	private List<PhysicsEntity>  collidingObjects;
	
	public Sensor(Body body, boolean isVisible, World world) {
		super(body, world);
		this.addOutput("onBeginContact", new Output());
		this.addOutput("onEndContact", new Output());
		collidingObjects = new ArrayList<>();

        this.isVisible = isVisible;
	}
	
	public Sensor(Body body, World world, BufferedImage image) {
		super(body, world);
		this.addOutput("onBeginContact", new Output());
		this.addOutput("onEndContact", new Output());
		this.image = image;
	}

    @Override
	public void beginContact(PhysicsEntity e, Contact contact) {
		this.getOutputByName("onBeginContact").run();
		collidingObjects.add(e);
	}
	
    @Override
	public void endContact(PhysicsEntity e, Contact contact) {
		this.getOutputByName("onEndContact").run();
		collidingObjects.remove(e);
	}
	
	public void setVisible(boolean b) {
		isVisible = b;
	}
	
	public List<PhysicsEntity> getCollidingObjects() {
		return collidingObjects;
	}

	@Override
	public void draw(Graphics2D g, Viewport viewport) {
		if (isVisible) {
            Vec2f screenPos = viewport.gameToScreen(getCenterPosition().minus(getSize().pdiv(2, 2)));
            Vec2f screenSize = getSize().smult(viewport.getScale());
            Rectangle2D.Float rect = new Rectangle2D.Float(screenPos.x, screenPos.y, screenSize.x, screenSize.y);
            g.setColor(new Color(255, 243, 0));

			if (image != null) {
				//g.drawImage(g, viewport);
			}
            else
            {
                g.draw(rect);
            }
		}
	}

	@Override
	public void tick(float n) {
		
	}
}
