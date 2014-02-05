package mllf.gameengine.ui;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import cs195n.Vec2f;
import mllf.gameengine.ui.Layer;

public class ParallaxBackground {
	
	private ArrayList<Layer> layers;

	public ParallaxBackground() {
		layers = new ArrayList<Layer>();
	}
	
	public void addLayer(Layer l) {
		layers.add(l);
		Collections.sort(layers, new Comparator<Layer>(){
			@Override
			public int compare(Layer o1, Layer o2) {
				return o1.getDepth() > o2.getDepth() ? -1 : 1;
			}
		});
	}
	
	public void move(Vec2f delta) {
		for (Layer l : layers) {
			l.move(delta.smult(1-(l.getDepth()/100f)));
		}
	}

	public void draw(Graphics2D g) {
		for (Layer l : layers) {
			l.draw(g);
		}
	}

}
