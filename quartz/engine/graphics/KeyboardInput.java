package quartz.engine.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardInput implements KeyListener {
	public boolean[] keys = new boolean[800000];

	public boolean up, down, left, right;
	
	public void keyTyped(KeyEvent e) {
		keys[e.getKeyCode()] = true;
		try {
			Thread.sleep(50);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		keys[e.getKeyCode()] = false;
	}

	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;
	}

	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;		
	}
	
	public void updateKeys() {
		// Called 60 times per second
		up  =  keys[KeyEvent.VK_W] ||   keys[KeyEvent.VK_UP];
		down = keys[KeyEvent.VK_S] || keys[KeyEvent.VK_DOWN];
		left = keys[KeyEvent.VK_A] || keys[KeyEvent.VK_LEFT];
		right =keys[KeyEvent.VK_D] ||keys[KeyEvent.VK_RIGHT];
	}
}
