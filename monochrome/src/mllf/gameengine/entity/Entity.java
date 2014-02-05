package mllf.gameengine.entity;

import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

import mllf.gameengine.ui.Viewport;
import mllf.gameengine.gameloop.World;
import mllf.gameengine.io.Input;
import mllf.gameengine.io.Output;

public abstract class Entity {

	protected World world;
	private Map<String, Output> outputs;
	private Map<String, Input> inputs;
	protected int zIndex;
	
	public Entity(World world) {
		this.world = world;
		outputs = new HashMap<>();
		inputs = new HashMap<>();
		zIndex = 0;
	}
	
	public void addOutput(String name, Output output) {
		outputs.put(name, output);
	}
	
	public void addInput(String name, Input input) {
		inputs.put(name, input);
	}
	
	public Output getOutputByName(String name) {
		return outputs.get(name);
	}
	
	public Input getInputByName(String name) {
		return inputs.get(name);
	}
	
	public int getZIndex() {
		return zIndex;
	}

    public abstract void tick(float s);
    public abstract void draw(Graphics2D g, Viewport v);
}
