package mllf.painter.mechanics;

import static mllf.painter.mechanics.Constants.*;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Map;

import org.jbox2d.dynamics.contacts.Contact;

import mllf.gameengine.entity.SoundSource;
import mllf.gameengine.ui.Viewport;
import mllf.gameengine.gameloop.World;
import mllf.gameengine.collision.BodyGenerator;
import mllf.gameengine.entity.PhysicsEntity;
import mllf.gameengine.io.Input;
import cs195n.Vec2f;

public class Speaker extends PhysicsEntity {
	
	private SoundSource source;

	public Speaker(Vec2f centerPosition, Vec2f size, String clipName, final World world) {
		super(BodyGenerator.generateAAB(centerPosition, size, 0, true, true, BACKGROUND, NOTHING, world), world);
		source = new SoundSource(this.getCenterPosition(), Constants.SOUND_DIR + clipName);
		this.addInput("doPlay", new Input() {

			@Override
			public void run(Map<String, String> args) {
				source.play(world.getPlayer());
			}
			
		});
	}

    public void play()
    {
        source.play(world.getPlayer());
    }

	@Override
	public void beginContact(PhysicsEntity e, Contact contact) {}

	@Override
	public void endContact(PhysicsEntity e, Contact contact) {}

	@Override
	public void tick(float s) {}

	@Override
	public void draw(Graphics2D g, Viewport v) {
		Vec2f screenPos = v.gameToScreen(getPosition());
		Vec2f screenSize = getSize().smult(v.getScale());
		g.setColor(Color.ORANGE);
		g.fillRect((int) screenPos.x, (int) screenPos.y, (int) screenSize.x, (int) screenSize.y);	
	}
}
