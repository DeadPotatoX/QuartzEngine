package quartz.engine.graphics;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import quartz.engine.main.QuartzEngine;

public class QuartzRender {
	protected final int WIDTH;
	protected final int HEIGHT;
	
	protected BufferedImage gImage;
	protected int[] pixels;
	
	public QuartzRender() {
		WIDTH = QuartzEngine.WIDTH;
		HEIGHT = QuartzEngine.HEIGHT;
		
		gImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) gImage.getRaster().getDataBuffer()).getData();
	}
	
	public BufferedImage getImage() {
		return gImage;
	}
	
	public void clear(int colour) {
		for (int i = 0; i < pixels.length; i++) {
			this.pixels[i] = colour;
		}
	}
	
	public int[] getPixels() {
		return pixels;
	}
	
	public void setPixels(int[] pixels) {
		for (int i = 0; i < pixels.length; i++) {
			this.pixels[i] = pixels[i];
		}
	}
}
