package mllf.painter.mechanics;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import mllf.gameengine.ui.Viewport;
import mllf.gameengine.gameloop.World;
import mllf.painter.mechanics.Constants.ObjectColor;
import cs195n.Vec2f;
import mllf.painter.objects.Wall;

/**
 * Created By: Max Lesser
 * Date: 12/3/13
 * Time: 10:20 PM
 */
public class ColorWall extends Wall {

	private ObjectColor color;
	
    public ColorWall(Vec2f centerLoc, Vec2f size, ObjectColor color, World w)
    {
        super(centerLoc, size, Constants.BOUNDARIES, Constants.getOpposite(color).category | Constants.GRAY_OBJECTS |
        		Constants.getOpposite(color).projectileCategory | Constants.PLAYER, w);
        this.color = color;
    }

    @Override
    public void draw(Graphics2D g, Viewport viewport)
    {
        Vec2f screenPos = viewport.gameToScreen(getCenterPosition().minus(getSize().pdiv(2, 2)));
        Vec2f screenSize = getSize().smult(viewport.getScale());
        Rectangle2D.Float rect = new Rectangle2D.Float(screenPos.x, screenPos.y, screenSize.x, screenSize.y);
        g.setColor(color.darkDrawColor);

        AffineTransform transform = new AffineTransform();
        transform.rotate(getBody().getAngle(), rect.getX() + rect.width/2, rect.getY() + rect.height/2);
        Shape transformed = transform.createTransformedShape(rect);

        g.fill(transformed);
    }
}
