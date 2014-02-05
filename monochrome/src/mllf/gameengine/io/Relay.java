package mllf.gameengine.io;

import java.awt.Graphics2D;
import java.util.Map;

import mllf.gameengine.ui.Viewport;
import mllf.gameengine.gameloop.World;
import mllf.gameengine.entity.Entity;

public class Relay extends Entity {
	
	private boolean isActive = false;

	public Relay(World world) {
		super(world);
		this.addInput("doActivate", new Input() {

			@Override
			public void run(Map<String, String> args) {
				isActive = true;
			}
			
		});
		this.addInput("doDeactivate", new Input() {

			@Override
			public void run(Map<String, String> args) {
				isActive = false;
			}
			
		});
		this.addInput("doFire", new Input() {

			@Override
			public void run(Map<String, String> args) {
				if (isActive) {
					Relay.this.getOutputByName("onFire").run();
				}
			}
			
		});
		this.addOutput("onFire", new Output());
	}

	@Override
	public void tick(float n) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Graphics2D g, Viewport v) {
		// TODO Auto-generated method stub
	}
}
