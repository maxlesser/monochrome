package mllf.gameengine.ui;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import cs195n.Vec2f;

public class ImageLayer extends Layer {
	
	private BufferedImage image;

	public ImageLayer(Vec2f position, Vec2f size, int depth, BufferedImage image) {
		super(position, size, depth);
		this.image = image;
	}

	@Override
	public void draw(Graphics2D g) {
		g.drawImage(
				image,
				(int) position.x,
				(int) position.y,
				(int) size.x,
				(int) size.y,
				null);

	}

}
