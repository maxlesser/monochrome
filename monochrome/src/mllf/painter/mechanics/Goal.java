package mllf.painter.mechanics;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import mllf.gameengine.ui.Viewport;
import mllf.gameengine.gameloop.World;
import mllf.gameengine.io.Sensor;

import org.jbox2d.dynamics.Body;

import cs195n.Vec2f;

public class Goal extends Sensor {

	public Goal(Body body, World world) {
		super(body, false, world);
	}

	@Override
	public void draw(Graphics2D g, Viewport viewport) {
		 Vec2f screenPos = viewport.gameToScreen(getCenterPosition().minus(getSize().pdiv(2, 2)));
         Vec2f screenSize = getSize().smult(viewport.getScale());
         Rectangle2D.Float rect = new Rectangle2D.Float(screenPos.x, screenPos.y, screenSize.x, screenSize.y);
         g.setColor(Constants.GOAL_COLOR);
         g.setStroke(new BasicStroke(2));

         g.draw(rect);
	}

}
