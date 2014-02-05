package mllf.gameengine.collision;

import mllf.gameengine.gameloop.World;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

import cs195n.Vec2f;

/**
 * Created By: Max Lesser
 * Date: 11/18/13
 * Time: 3:28 AM
 */
public class BodyGenerator {
	
	private static Body initCommonProperties(
			FixtureDef fd, Vec2f centerPosition, float density, boolean isStatic, boolean isSensor,
			int category, int collidesWith, World w){
		fd.friction = 0.6f;
        fd.density = density;
        fd.setSensor(isSensor);
        fd.filter.categoryBits = category;
        fd.filter.maskBits = collidesWith;
        BodyDef bd = new BodyDef();
        bd.type = (isStatic) ? BodyType.STATIC : BodyType.DYNAMIC;
        bd.position = centerPosition.toVec2();
        Body body = w.getBWorld().createBody(bd);
        body.createFixture(fd);
        return body;
	}

    public static Body generateAAB(
    		Vec2f centerPosition, Vec2f size, float density, boolean isStatic, boolean isSensor,
    		int category, int collidesWith, World w) {
        FixtureDef fd = new FixtureDef();
        PolygonShape sd = new PolygonShape();
        sd.setAsBox(size.x / 2, size.y / 2);
        fd.shape = sd;
        return initCommonProperties(fd, centerPosition, density, isStatic, isSensor, category, collidesWith, w);
    }

    public static Body generateCircle(
    		Vec2f centerPosition, float radius, float density, boolean isStatic, boolean isSensor,
    		int category, int collidesWith, World w) {
    	FixtureDef fd = new FixtureDef();
    	CircleShape c = new CircleShape();
        c.setRadius(radius);
        fd.shape = c;
        return initCommonProperties(fd, centerPosition, density, isStatic, isSensor, category, collidesWith, w);
    }

    public static Body generatePolygon(
    		Vec2f centerPosition, java.util.List<Vec2f> points, float density, boolean isStatic, boolean isSensor,
    		int category, int collidesWith, World w) {
        assert points.size() >= 3;

        FixtureDef fd = new FixtureDef();
        PolygonShape sd = new PolygonShape();

        Vec2[] bPoints = new Vec2[8];
        for(int x = 0; x < points.size(); x++)
        {
            bPoints[x] = points.get(x).minus(centerPosition).toVec2();
        }
        sd.set(bPoints, points.size());
        fd.shape = sd;
        Body body = initCommonProperties(fd, centerPosition, density, isStatic, isSensor, category, collidesWith, w);
        PolygonShape polygonShape = (PolygonShape)body.getFixtureList().getShape();
        polygonShape.set(bPoints, points.size());

        return body;
    }

    public static Body generateBullet(Vec2f loc, Vec2f rayDir, float power, int category, int collidesWith, World w)
    {
        BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;
        bd.fixedRotation = true; // rotation not necessary
        bd.bullet = true; // prevent tunneling at high speed
        bd.linearDamping = 1; // drag due to moving through air
        bd.gravityScale = 0; // ignore gravity
        bd.position = loc.toVec2(); // start at blast center
        bd.linearVelocity = rayDir.smult(power).toVec2();
        Body body = w.getBWorld().createBody(bd);

        CircleShape circleShape = new CircleShape();
        circleShape.m_radius = 0.05f; // very small

        FixtureDef fd = new FixtureDef();
        fd.shape = circleShape;
        fd.filter.categoryBits = category;
        fd.filter.maskBits = collidesWith;
        fd.density = 100f; // very high - shared across all particles
        fd.friction = 0; // friction not necessary
        fd.restitution = 0.99f; // high restitution to reflect off obstacles
        body.createFixture(fd);

        return body;
    }
}
