package mllf.painter.objects;

import cs195n.Vec2f;
import mllf.gameengine.ui.Viewport;
import mllf.gameengine.gameloop.World;
import mllf.gameengine.collision.BodyGenerator;
import mllf.gameengine.entity.PhysicsEntity;
import mllf.painter.mechanics.Constants;
import mllf.painter.mechanics.Paintable;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.Filter;
import org.jbox2d.dynamics.contacts.Contact;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.util.ArrayList;

/**
 * Created By: Max Lesser
 * Date: 12/11/13
 * Time: 6:34 AM
 */
public class Polygon extends PhysicsEntity implements Paintable {

    private Constants.ObjectColor color;

    public Polygon(Vec2f centerPosition, ArrayList<Vec2f> verts, float density, Constants.ObjectColor color, World w)
    {
        super(BodyGenerator.generatePolygon(centerPosition, verts, density, false, false, color.category, Constants.EVERYTHING, w), w);
        getBody().setFixedRotation(true);
        this.color = color;
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
        PolygonShape polygonShape = (PolygonShape) getBody().getFixtureList().getShape();
        Vec2f last = Vec2f.fromVec2(polygonShape.getVertex(polygonShape.getVertexCount()-1)).plus(getCenterPosition());

        Path2D path = new Path2D.Float();
        Vec2f screenP = viewport.gameToScreen(last);
        path.moveTo(screenP.x, screenP.y);

        for (int x = 0; x < polygonShape.getVertexCount(); x++) {
            screenP = viewport.gameToScreen(Vec2f.fromVec2(polygonShape.getVertex(x)).plus(getCenterPosition()));
            path.lineTo(screenP.x, screenP.y);
        }

        AffineTransform transform = new AffineTransform();
        transform.rotate(getBody().getAngle(), path.getBounds().getMinX() + path.getBounds().width/2, path.getBounds().getMinY() + path.getBounds().height/2);
        Shape transformed = transform.createTransformedShape(path);

        path.closePath();
        g.setColor(color.drawColor);
        g.fill(transformed);
    }

    @Override
    public void setObjectColor(Constants.ObjectColor color) {
        this.color = color;
        Filter filter = this.getBody().getFixtureList().getFilterData();
        filter.categoryBits = color.category;
        this.getBody().getFixtureList().setFilterData(filter);
    }

    @Override
    public Constants.ObjectColor getObjectColor() {
        return color;
    }
}
