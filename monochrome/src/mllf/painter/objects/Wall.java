package mllf.painter.objects;

import cs195n.Vec2f;
import mllf.gameengine.ui.Viewport;
import mllf.gameengine.gameloop.World;
import mllf.gameengine.collision.BodyGenerator;
import mllf.gameengine.entity.PhysicsEntity;
import mllf.painter.mechanics.Constants;
import org.jbox2d.dynamics.contacts.Contact;
import static mllf.painter.mechanics.Constants.*;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

/**
 * Created By: Max Lesser
 * Date: 12/3/13
 * Time: 1:03 AM
 */
public class Wall extends PhysicsEntity {

    public Wall(Vec2f centerLoc, Vec2f dims, World w)
    {
        super(BodyGenerator.generateAAB(centerLoc, dims, 1, true, false, BOUNDARIES, EVERYTHING, w), w);
    }

    public Wall(Vec2f centerLoc, Vec2f dims, float friction, World w)
    {
        super(BodyGenerator.generateAAB(centerLoc, dims, 1, true, false, BOUNDARIES, EVERYTHING, w), w);
        getBody().getFixtureList().setFriction(friction);
    }

    public Wall(Vec2f centerLoc, Vec2f dims, int category, int collidesWith, World w)
    {
        super(BodyGenerator.generateAAB(centerLoc, dims, 1, true, false, category, collidesWith, w), w);
    }

    @Override
    public void beginContact(PhysicsEntity otherEntity, Contact contact) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void endContact(PhysicsEntity otherEntity, Contact contact) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void tick(float s) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void draw(Graphics2D g, Viewport viewport) {
        Vec2f screenPos = viewport.gameToScreen(getCenterPosition().minus(getSize().pdiv(2, 2)));
        Vec2f screenSize = getSize().smult(viewport.getScale());
        Rectangle2D.Float rect = new Rectangle2D.Float(screenPos.x, screenPos.y, screenSize.x, screenSize.y);
        g.setColor(Constants.WALL_COLOR);

        AffineTransform transform = new AffineTransform();
        transform.rotate(getBody().getAngle(), rect.getX() + rect.width/2, rect.getY() + rect.height/2);
        Shape transformed = transform.createTransformedShape(rect);

        g.fill(transformed);
    }
}
