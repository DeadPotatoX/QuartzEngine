package quartz.engine.main;

import quartz.engine.main.Quartz.Init;
import quartz.engine.main.Quartz.Render;
import quartz.engine.main.Quartz.Update;

@Quartz(name="Quartz Test Game", version="1.0")
public class QuartzGame {
	@Init
	public void initGame() {
		
	}
	
	@Render
	public void renderGame() {
		
	}
	
	@Update
	public void updateGame() {
	}
}
