package mllf.painter.objects;

import static mllf.painter.mechanics.Constants.*;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.Map;

import mllf.gameengine.ui.Viewport;
import mllf.gameengine.gameloop.World;
import mllf.gameengine.collision.BodyGenerator;
import mllf.gameengine.entity.PhysicsEntity;
import mllf.gameengine.io.Input;
import mllf.gameengine.io.Output;
import mllf.painter.mechanics.Constants;
import mllf.painter.mechanics.Constants.ObjectColor;

import mllf.painter.mechanics.Paintable;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.contacts.Contact;

import cs195n.Vec2f;

/**
 * Created By: Max Lesser
 * Date: 10/22/13
 * Time: 11:35 PM
 */
public class Player extends PhysicsEntity implements Paintable {

    private boolean hasJumped;

    private boolean isJumping;
    private boolean isMovingLeft;
    private boolean isMovingRight;

    private LinkedList<StickyBomb> stuckBombs;
    private int bombLimit;
    private ObjectColor color = ObjectColor.RED;

    private Vec2f gravity;

    private Fixture foot;

    public Player(Vec2f centerPosition, Vec2f dimensions, float density, World w)
    {
        super(BodyGenerator.generateAAB(centerPosition, new Vec2f(4, 6), density, false, false, PLAYER, EVERYTHING, w), w);
        zIndex = 10;
        PolygonShape polygonShape = new PolygonShape();
        FixtureDef myFixtureDef = new FixtureDef();
        myFixtureDef.shape = polygonShape;
        myFixtureDef.density = 1;
        myFixtureDef.filter.categoryBits = PLAYER;
        myFixtureDef.filter.maskBits = BOUNDARIES | OBJECTS;
        polygonShape.setAsBox(1.9f, 0.3f, new Vec2(0, 3f), 0);
        myFixtureDef.isSensor = true;
        foot = getBody().createFixture(myFixtureDef);

        getBody().setFixedRotation(true);
        getBody().setLinearDamping(0.5f);

        stuckBombs = new LinkedList<>();
        bombLimit = 5;

        hasJumped = false;

        gravity = new Vec2f(1, 1);

        this.addOutput("onInteract", new Output());
        
        addInput("ReverseGravity", new Input() {
        	public void run(Map<String, String> args) {
                gravity = gravity.pmult(1, -1);
            }
        });
    }

    @Override
    public void tick(float t) {
        if(isMovingLeft)
        {
            applyForce(new Vec2f(-Constants.PLAYER_MOVEMENT, 0));
        }
        if(isMovingRight)
        {
            applyForce(new Vec2f(Constants.PLAYER_MOVEMENT, 0));
        }
        if(isJumping && !hasJumped)
        {
            applyImpulse(new Vec2f(0, -7000f));
            hasJumped = true;
            isJumping = false;
        }
    }

    @Override
    public void draw(Graphics2D g, Viewport viewport) {
        Vec2f screenPos = viewport.gameToScreen(getCenterPosition().minus(getSize().sdiv(2)));
        Vec2f screenSize = getSize().smult(viewport.getScale());
        Rectangle2D.Float rect = new Rectangle2D.Float(screenPos.x, screenPos.y, screenSize.x, screenSize.y);
        g.setColor(Constants.PLAYER_COLOR);

        AffineTransform transform = new AffineTransform();
        transform.rotate(getBody().getAngle(), rect.getX() + rect.width / 2, rect.getY() + rect.height / 2);
        Shape transformed = transform.createTransformedShape(rect);

        g.fill(transformed);
    }

    public void isMovingLeft(boolean isTrue)
    {
        isMovingLeft = isTrue;
    }

    public void isMovingRight(boolean isTrue)
    {
        isMovingRight = isTrue;
    }

    public void isJumping(boolean isTrue)
    {
        isJumping = isTrue;
    }

    public void switchColors()
    {
        if(color == ObjectColor.RED)
            color = ObjectColor.BLUE;
        else
            color = ObjectColor.RED;
    }

    public void explodeFirstBomb()
    {
        StickyBomb stickyBomb;
        do{
            if(stuckBombs.size() <= 0)
                return;
            stickyBomb = stuckBombs.pop();
        } while(stickyBomb.hasExploded());
        stickyBomb.explode();
    }
    
    public void firePaint(float time, Vec2f attackDirection)
    {
    	Vec2f vel = attackDirection.normalized();
    	if(time > Constants.LAUNCH_TIME)
            vel = vel.smult(Constants.MAX_LAUNCH);
        else
            vel = vel.smult(time / Constants.LAUNCH_TIME * (Constants.MAX_LAUNCH-Constants.MIN_LAUNCH) + Constants.MIN_LAUNCH);
        PaintBall pball = new PaintBall(getCenterPosition(), vel, color, world);
        world.addEntity(pball);
    }

    public void fireGrenade(float time, Vec2f attackDirection)
    {
        int stuckBombsLeft = 0;
        for(int x = 0; x < stuckBombs.size(); x++)
        {
            if(!stuckBombs.get(x).hasExploded())
                stuckBombsLeft++;
        }
        if(stuckBombsLeft < bombLimit)
        {
            Vec2f vel = attackDirection.normalized();
            if(time > Constants.LAUNCH_TIME)
                vel = vel.smult(Constants.MAX_LAUNCH);
            else
                vel = vel.smult(time / Constants.LAUNCH_TIME * (Constants.MAX_LAUNCH-Constants.MIN_LAUNCH) + Constants.MIN_LAUNCH);
            StickyBomb bomb = new StickyBomb(getCenterPosition(), vel, color, world, this);
            stuckBombs.add(bomb);
            world.addEntity(bomb);
        }
    }

    public void interact() {
    	Output interact = this.getOutputByName("onInteract");
    	if (interact != null) {
    		interact.run();
    	}
    }

    public int getBombLimit() {
        return bombLimit;
    }

    public int getBombsLeft() {
        return getBombLimit() - stuckBombs.size();
    }

    public void setBombLimit(int bombLimit) {
        this.bombLimit = bombLimit;
    }

    public void removeBomb(StickyBomb b)
    {
        stuckBombs.remove(b);
    }

    public ObjectColor getColor() {
        return color;
    }

    public void setColor(ObjectColor color) {
        this.color = color;
    }

    @Override
    public void beginContact(PhysicsEntity e, Contact contact) {
        if(contact.getFixtureA() == foot || contact.getFixtureB() == foot)
        {
            hasJumped = false;
        }
    }

    @Override
    public void endContact(PhysicsEntity e, Contact contact) {

    }

	@Override
	public void setObjectColor(ObjectColor color) {
		
	}

	@Override
	public ObjectColor getObjectColor() {
		return ObjectColor.GRAY;
	}
}
