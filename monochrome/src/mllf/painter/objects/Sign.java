package mllf.painter.objects;

import static mllf.painter.mechanics.Constants.*;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mllf.painter.mechanics.Constants;
import org.jbox2d.dynamics.contacts.Contact;

import mllf.gameengine.ui.Overlayable;
import mllf.gameengine.sprites.ImageManager;
import mllf.gameengine.ui.TextSequence;
import mllf.gameengine.ui.Viewport;
import mllf.gameengine.gameloop.World;
import mllf.gameengine.collision.BodyGenerator;
import mllf.gameengine.entity.PhysicsEntity;
import mllf.gameengine.io.Connection;
import mllf.gameengine.io.Input;
import mllf.gameengine.io.Sensor;
import cs195n.Vec2f;

public class Sign extends PhysicsEntity implements Overlayable {

	private TextSequence textSequence;
	private World world;
	private final float INTERACT_DISTANCE = 8f;
	private boolean activated;
	private Connection playerConnection;
	private Sensor sensor;
	private BufferedImage eImage;
	private final int Y_OFFSET = 7;
	
	public Sign(Vec2f centerPosition, Vec2f size, World world, String ... text) {
		super(BodyGenerator.generateAAB(centerPosition, size, 0, true, true, BACKGROUND, NOTHING, world), world);
		zIndex = 1;
		this.textSequence = new TextSequence(this, Constants.TEXTBOX_FONT.deriveFont(Constants.FONT_SIZE), text);
		this.world = world;
		try {
			eImage = ImageManager.loadSingleImage("images/einteract2.png");
		} catch (IOException e) {
			
		}
		List<PhysicsEntity> triggers = new ArrayList<>();
		triggers.add(world.getPlayer());
		sensor = new Sensor(
				BodyGenerator.generateCircle(centerPosition, INTERACT_DISTANCE, 0,
						true, true, SENSORS, PLAYER, world), false,
						world);
		
		this.addInput("doInteract", new Input() {

			@Override
			public void run(Map<String, String> args) {
				if (!textSequence.isPlaying()) {
					textSequence.start();
					activated = false;
				} else {
					textSequence.next();
					if (!textSequence.isPlaying()) { // If text sequence just ended
						activated = true;
					}
				}
			}
			
		});
		
		this.addInput("doActivate", new Input() {

			@Override
			public void run(Map<String, String> args) {
				activated = true;
				playerConnection = new Connection(Sign.this.getInputByName("doInteract"), new HashMap<String, String>());
				Sign.this.world.getPlayer().getOutputByName("onInteract").connect(playerConnection);
			}
			
		});
		
		this.addInput("doDeactivate", new Input() {

			@Override
			public void run(Map<String, String> args) {
				activated = false;
				Sign.this.world.getPlayer().getOutputByName("onInteract").disconnect(playerConnection);
				playerConnection = null;
				textSequence.stop();
			}
			
		});
		
		sensor.getOutputByName("onBeginContact").connect(new Connection(this.getInputByName("doActivate"), new HashMap<String, String>()));
		sensor.getOutputByName("onEndContact").connect(new Connection(this.getInputByName("doDeactivate"), new HashMap<String, String>()));
	}

	@Override
	public void tick(float s) {
		
	}

	@Override
	public void draw(Graphics2D g, Viewport v) {
		Vec2f screenPos = v.gameToScreen(getPosition());
		Vec2f screenSize = getSize().smult(v.getScale());
		g.setColor(Constants.SIGN_COLOR);
		g.fillRect((int) screenPos.x, (int) screenPos.y, (int) screenSize.x, (int) screenSize.y);
		
	}
	
	public void drawOnTop(Graphics2D g, Viewport viewport) {
		Vec2f screenPos = viewport.gameToScreen(getPosition());
		Vec2f screenSize = getSize().smult(viewport.getScale());
		textSequence.draw(g, viewport);
		if (activated) {
			g.drawImage(eImage,
					(int) (screenPos.x + (screenSize.x / 2) - (eImage.getWidth() / 2)),
					(int) (screenPos.y - (eImage.getHeight()) - Y_OFFSET),
					(int) (eImage.getWidth()),
					(int) (eImage.getHeight()),
					null);
		}
	}


	@Override
	public void beginContact(PhysicsEntity e, Contact contact) {}

	@Override
	public void endContact(PhysicsEntity e, Contact contact) {}
}
