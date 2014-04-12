package quartz.engine.main;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferStrategy;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import javax.swing.JFrame;

import quartz.engine.graphics.QuartzRender;
import quartz.engine.input.KeyboardInput;

/**
 * A base class for the stable QuartzEngine!  Holds almost everything!
 */
public class QuartzEngine extends Canvas implements Runnable {	

	private Class<?> gameClass;

	private static final long serialVersionUID = 1L;
	
	public static final int WIDTH = 800, HEIGHT = 600;
		
	private Quartz quartzGame;
	private Method gameInit;
	private Method gameRender;
	private Method gameUpdate;
	private Method gameExit;
	
	private static QuartzEngine instance;
	public static String title = "QuartzEngine 1.0.0 Alpha";
	
	public JFrame quartzFrame;
	private Thread quartzThread;
	private QuartzRender quartzRenderer;
	
	private KeyboardInput kIn;
	
	// The default game update fps is 60 (editing will change speed -- use at risk)
	private int gameFPS = 60;
	
	public boolean running = false;
				
	public QuartzEngine(String farg) {
		gameClass = getGameClass(farg);
		
		if (gameClass.isAnnotationPresent(Quartz.class)) {
			Annotation annotation = gameClass.getAnnotation(Quartz.class);
			quartzGame = (Quartz) annotation;
		} else {
			System.err.println("Class does not use the @Quartz annotation!  TELL THE AUTHOR OF GAME!");
			System.exit(0);
		}
		title = String.format("%s %s - QuartzEngine", quartzGame.name(), quartzGame.version());
		
		instance = this;
						
		// Setup canvas
		Dimension size = new Dimension(WIDTH, HEIGHT);
		setPreferredSize(size);
		
		quartzFrame = new JFrame(title);
		quartzThread = new Thread(this, "QuartzEngine (Thread)");
		quartzRenderer = new QuartzRender();
		kIn = new KeyboardInput();
		
		init();						
	}
	
	private void init() {
		// Some processing
		setupGameMethods();
		running = true;
		
		// Init jframe
		quartzFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		quartzFrame.add(this);
		quartzFrame.setResizable(false);
		quartzFrame.pack();
		quartzFrame.setVisible(true);
		quartzFrame.setLocationRelativeTo(null);
				
		quartzFrame.addKeyListener(kIn);
		
		quartzFrame.addWindowListener(new WindowListener() {
			public void windowClosed(WindowEvent e) {
				instance.quit(0);
			}

			public void windowOpened(WindowEvent e) {}
			public void windowClosing(WindowEvent e) {}
			public void windowIconified(WindowEvent e) {}
			public void windowDeiconified(WindowEvent e) {}
			public void windowActivated(WindowEvent e) {}
			public void windowDeactivated(WindowEvent e) {}
		});
		
		try {
			if (gameInit != null)
				gameInit.invoke(gameClass.newInstance());
		} catch (Exception e) {
			e.printStackTrace();
		}		
		quartzThread.start();
	}
			
	public void render() {
		BufferStrategy bufferStrat = getBufferStrategy();
		if (bufferStrat == null) {
			createBufferStrategy(3);
			return;
		}
		
		quartzRenderer.clear(0xffffff);
				
		try {
			if (gameRender != null)
				gameRender.invoke(gameClass.newInstance());
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
		Graphics g = bufferStrat.getDrawGraphics();		
		
		g.drawImage(quartzRenderer.getImage(), 0, 0, getWidth(), getHeight(), null);
		
		g.dispose();
		bufferStrat.show();
	}
		
	public void update() {
		try {
			if (gameUpdate != null)
				gameUpdate.invoke(gameClass.newInstance());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void quit(int code) {
		try {
			if (gameExit != null)
				gameExit.invoke(gameClass.newInstance());
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
		try {
			quartzThread.join();
			running = false;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void exit() {
		instance.quit(0);
	}

	public void run() {
		requestFocus();
		
		long prevTime = System.nanoTime();
		final double fpsl = 1000000000 / gameFPS;
		long tmr = System.currentTimeMillis();
		
		double delta = 0;
		
		double[] debugs = new double[2];
		
		while (running) {
			long currTime = System.nanoTime();
			delta += (currTime - prevTime) / fpsl;
			prevTime = currTime;
			
			while (delta >= 1) {
				debugs[1]++;
				delta--;
				update();
			}
			
			render();
			debugs[0]++;
			
			if (System.currentTimeMillis() - tmr > 1000) {
				tmr += 1000;
				if (quartzGame.showDebug()) {
					quartzFrame.setTitle(title + " | FPS: " + debugs[0] + " UPS: " + debugs[1]);
				}
				debugs = new double[2];
			}
		}
	}
	
	public static QuartzEngine getQuartz() {
		return instance;
	}
	
	public QuartzRender getRenderer() {
		return instance.quartzRenderer;
	}
	
	public Class<?> getGameClass(String loc) {
		try {
			return Class.forName(loc);
		} catch (ClassNotFoundException e) {
			System.err.println("ERR loading the class file -- playing Quartz(test)Game");
			e.printStackTrace();
			return QuartzGame.class;
		}
	}
	
	public void setupGameMethods() {
		for (Method c : gameClass.getDeclaredMethods()) {
			if (c.isAnnotationPresent(Quartz.Init.class)) {
				gameInit = c;
			} else if (c.isAnnotationPresent(Quartz.Render.class)) {
				gameRender = c;
			} else if (c.isAnnotationPresent(Quartz.Update.class)) {
				gameUpdate = c;
			} else if (c.isAnnotationPresent(Quartz.Exit.class)) {
				gameExit = c;
			}
		}
	}
	

	public static void main(String[] args) {
		try {
			new QuartzEngine(args[0]);
		} catch (ArrayIndexOutOfBoundsException e) {
			System.err.println("Quartz Run paramaters are npot setup correctly!  Tell the author!");
			e.printStackTrace();
		}
	}
}
