package mllf.gameengine.sprites;

import java.awt.image.BufferedImage;
import java.util.List;

public class Animation {
	
	private List<BufferedImage> images;
	private int currFrame;
	private float secondsSincePreviousFrame;
	private int fps;

	public Animation(List<BufferedImage> images) {
		this.images = images;
		currFrame = 0;
		secondsSincePreviousFrame = 0;
		fps = 12;
	}
	
	public void tick(float secondsSincePreviousTick) {
		secondsSincePreviousFrame += secondsSincePreviousTick;
		if (secondsSincePreviousFrame > (1f / fps)) {
			secondsSincePreviousFrame = 0;
			currFrame = (currFrame + 1) % images.size();
		}
	}
	
	public BufferedImage getCurrentImage() {
		return images.get(currFrame);
	}

}
