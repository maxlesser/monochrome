package mllf.gameengine.sprites;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import cs195n.Vec2i;

public class ImageManager {
	
	String imgDir;
	HashMap<String, BufferedImage> images;

	public ImageManager(String imgDir) {
		this.imgDir = imgDir;
		images = new HashMap<String, BufferedImage>();		
	}
	
	public void addImage(String imageName) throws IOException {
		BufferedImage image = ImageIO.read(new File(imgDir + imageName));
		images.put(imageName, image);
	}
	
	public BufferedImage getImage(String imageName) {
		return images.get(imageName);
	}
	
	public boolean hasImage(String imageName) {
		return images.get(imageName) != null;
	}
	
	public BufferedImage createAndAddSubImage(BufferedImage fromImage, String newImageName, int x, int y, int w, int h) {
		BufferedImage subImage = fromImage.getSubimage(x, y, w, h);
		images.put(newImageName, subImage);
		return subImage;
	}
	
	public BufferedImage createAndAddSubImage(BufferedImage fromImage, String newImageName, Vec2i pos, Vec2i size) {
		return this.createAndAddSubImage(fromImage, newImageName, pos.x, pos.y, size.x, size.y);
	}
	
	public static BufferedImage loadSingleImage(String fullImagePath) throws IOException {
		return ImageIO.read(new File(fullImagePath));
	}
}
