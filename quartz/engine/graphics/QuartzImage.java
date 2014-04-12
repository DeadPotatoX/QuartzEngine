package quartz.engine.graphics;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import quartz.engine.main.QuartzEngine;
import quartz.engine.util.QuartzMath;
import quartz.engine.util.Resource;

public class QuartzImage {
	protected File path;
	protected int[] pixels;
	protected int width, height;
	protected QuartzRender renderer;
	
	public QuartzImage(String path, int[] pixels, QuartzRender renderer, BufferedImage image) {
		this.path = new File(path);
		this.pixels = pixels;
		this.renderer = renderer;
		width = image.getWidth();
		height = image.getHeight();
	}
	
	public void colourReplacer(int colour, int repColour) {
		for (int i = 0; i < pixels.length; i++) {
			if (pixels[i] == colour) 
				pixels[i] = repColour;
		}
	}
	
	public void bgRemove() {
		colourReplacer(0, 0xfffffff);
	}
	
	public void bgReAdd() {
		colourReplacer(0xfffffff, 0);
	}
	
	/**
	 * Replaces the old rotation code I created :) STILL DOESNT WORK WITH RECTANGLES! (ONLY SQUARES!)
	 * Saves about 100 FPS
	 * @param rotation = zero through three (1 = 90 degrees, 2 = 180 degrees, 3 = 270 degrees)
	 * @return a newly rotated image!  NOTE: put in init!  Not render!
	 */
	public QuartzImage smrtRotate(int rotation) {
		BufferedImage image = null;
		
		try {
			image = ImageIO.read(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int[] pixelst = QuartzMath.rotateArray(rotation, this.pixels, width, height);
		
		return new QuartzImage(path.getPath(), pixelst, renderer, image);
	}
	
	public void renderImage(int xPos, int yPos) {
		int[] totalP = renderer.getPixels();
		
		for (int y = 0; y < height; y++) {
			int ya = y + yPos;
			for (int x = 0; x < width; x++) {
				int xa = x + xPos;
				if (pixels[x + y * width] != 0xfffffff)
					totalP[xa + ya * QuartzEngine.WIDTH] = pixels[x + y * width];
			}
		}		
		renderer.setPixels(totalP);
	}
	
	public File getFile() { return path; }
	
	public static QuartzImage loadImage(String path) {
		BufferedImage bi = null;
		int[] pixels = new int[800 * 600]; // MAX IMAGE SIZE -- 800x600 (KEEP THIS IN MIND)
		QuartzEngine quartz = QuartzEngine.getQuartz();
		try {
			bi = ImageIO.read(new File(path));
			int w = bi.getWidth(), h = bi.getHeight();
			bi.getRGB(0, 0, w, h, pixels, 0, w);
		} catch(IOException e) {
			System.err.println("Unable to load image " + path + ", maybe it doesn't exist?");
			e.printStackTrace();
		}
		return new QuartzImage(path, pixels, quartz.getRenderer(), bi);
	}
	
	public static QuartzImage loadImage(Resource res) {
		return loadImage(res.getAbbPath());
	}
	
	public int[] getPixels() { return pixels; }
	public void setPixels(int[] p) { pixels=p;}
}
