package mllf.gameengine.gameloop;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import mllf.gameengine.entity.Entity;
import mllf.gameengine.entity.PhysicsEntity;
import mllf.gameengine.ui.Overlayable;
import mllf.gameengine.ui.Viewport;
import mllf.painter.objects.Player;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.contacts.Contact;

import cs195n.Vec2f;


public class World {

	public static final Vec2f GRAVITY = new Vec2f(0, 10f);
	public final float TIMESTEP = 0.05f;
	public final int VEL_ITER = 1; // Velocity iterations for bWorld
	public final int POS_ITER = 3; // Position iterations for bWorld
	
	private org.jbox2d.dynamics.World bWorld;
	protected List<Entity> entities;
	private List<Entity> toRemove;
	private List<Overlayable> overlays;

    private HashMap<Body, PhysicsEntity> bodyMapping;

	private Player player;
	
	public World() {
		bWorld = new org.jbox2d.dynamics.World(GRAVITY.toVec2());

        bodyMapping = new HashMap<>();

        bWorld.setContactListener(new ContactListener() {
        	
        	/**
        	 * Called only if entities collide (according to collision filters)
        	 */
            @Override
            public void beginContact(Contact contact) {
                PhysicsEntity e1 = bodyMapping.get(contact.getFixtureA().getBody());
                PhysicsEntity e2 = bodyMapping.get(contact.getFixtureB().getBody());
                
                e1.beginContact(e2, contact);
                e2.beginContact(e1, contact);
            }

            /**
        	 * Called only if entities collide (according to collision filters)
        	 */
            @Override
            public void endContact(Contact contact) {
            	PhysicsEntity e1 = bodyMapping.get(contact.getFixtureA().getBody());
                PhysicsEntity e2 = bodyMapping.get(contact.getFixtureB().getBody());

                e1.endContact(e2, contact);
                e2.endContact(e1, contact);
            }

            /**
        	 * Called only if entities collide (according to collision filters) and neither entity is a sensor
        	 */
            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {
            }

            /**
        	 * Called only if entities collide (according to collision filters) and neither entity is a sensor
        	 */
            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {
            }
        });

		entities = new ArrayList<Entity>();
		toRemove = new ArrayList<Entity>();
		overlays = new ArrayList<Overlayable>();
	}
	
	public void movePlayer(Vec2f direction) {
		player.applyImpulse(direction);
	}
	
	public void addEntity(Entity e) {
		entities.add(e);
		Collections.sort(entities, new Comparator<Entity>() {

			@Override
			public int compare(Entity o1, Entity o2) {
				return (o1.getZIndex() < o2.getZIndex()) ? -1 : 1;
			}
			
		});
		if (e instanceof Overlayable) {
			overlays.add((Overlayable) e);
		}
	}
	
	public List<Entity> getEntities() {
		return entities;
	}
	
	public void draw(Graphics2D g, Viewport viewport) {
		for (Entity e : entities) {
			e.draw(g, viewport);
		}
		for (Overlayable d : overlays) {
			d.drawOnTop(g, viewport);
		}
	}
	
	public void tick(float seconds) {
		bWorld.step(TIMESTEP, VEL_ITER, POS_ITER);
		for (int x = 0; x < entities.size(); x++) {
			entities.get(x).tick(seconds);
		}
		this.completeRemoval();
	}
	
	public void remove(Entity e) {
		toRemove.add(e);
	}
	
	public void completeRemoval() {
		for (Entity e : toRemove) {
			entities.remove(e);
			if(PhysicsEntity.class.isAssignableFrom(e.getClass()))
				bWorld.destroyBody(((PhysicsEntity)e).getBody());
		}
		toRemove.clear();
	}
	
	public org.jbox2d.dynamics.World getBWorld() {
		return bWorld;
	}

    public Player getPlayer()
    {
        return player;
    }

    public void setPlayer(Player p)
    {
        player = p;
    }

    public HashMap<Body, PhysicsEntity> getBodyMapping() {
    	return bodyMapping;
    }
}
