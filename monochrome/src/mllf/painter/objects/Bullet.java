package mllf.painter.objects;

import static mllf.painter.mechanics.Constants.*;
import cs195n.Vec2f;
import mllf.gameengine.ui.Viewport;
import mllf.gameengine.gameloop.World;
import mllf.gameengine.collision.BodyGenerator;
import mllf.gameengine.entity.PhysicsEntity;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

import org.jbox2d.dynamics.contacts.Contact;

/**
 * Created By: Max Lesser
 * Date: 11/23/13
 * Time: 4:55 PM
 */
public class Bullet extends PhysicsEntity{

    private ObjectColor color;
    private int transparency = 255;

    public Bullet(Vec2f center, Vec2f rayDir, float power, ObjectColor color, World w)
    {
        super(BodyGenerator.generateBullet(center, rayDir, power, color.projectileCategory, color.category | BOUNDARIES, w), w);
        zIndex = 2;
        this.color = color;
    }
   
    @Override
    public void tick(float s) { }

    public void setTransparency(int trans)
    {
        transparency = trans;
        if(transparency < 0)
            transparency = 0;
        else if(transparency > 255)
            transparency = 255;
    }

    @Override
    public void draw(Graphics2D g, Viewport viewport) {
        Vec2f screenPos = viewport.gameToScreen(getCenterPosition().minus(0.2f, 0.2f));
        float screenSize = 0.2f * 2 * viewport.getScale();
        Ellipse2D.Float ellipse = new Ellipse2D.Float(screenPos.x, screenPos.y, screenSize, screenSize);

        AffineTransform transform = new AffineTransform();
        //transform.rotate(body.getAngle(), ellipse.getX() + ellipse.width/2, ellipse.getY() + ellipse.height/2);
        Shape transformed = transform.createTransformedShape(ellipse);

        g.setColor(
        		new Color(color.drawColor.getRed(), color.drawColor.getBlue(), color.drawColor.getGreen(),
        				transparency));
        g.setStroke(new BasicStroke());
        g.fill(transformed);
    }

    @Override
    public void beginContact(PhysicsEntity e, Contact contact) {

    }

    @Override
    public void endContact(PhysicsEntity e, Contact contact) {

    }
}
