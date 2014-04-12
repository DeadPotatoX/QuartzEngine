package quartz.engine.main;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Quartz {
	
	/**
	 * What is the name of this game?
	 * @return the name (for title)
	 */
	public String name();
	
	/**
	 * What version is this version of the game
	 * @return the game version
	 */
	public String version();
	
	/**
	 * Show the debug in the title?  If true it looks like. <br>
	 * NAME VERSION | FPS: 148 UPS: 60
	 * @return does the game include debug (in title)
	 */
	public boolean showDebug() default false;
	
	/**
	 * @return the location of an "addition settings file" (.qasf) NOTE: don't return "nowhere"
	 */
	public String additionalSettingLocation() default "nowhere";
	
	/**
	 * The {@link @Init} annotation!  Used to call a method when {@link QuartzEngine} is 
	 * initialized <br>
	 * It is now ALSO safe to use QuartzEngine.getQuartz() and use the renderer!
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public @interface Init { }
	
	/**
	 * The {@link @Update} annotation!  Used to call a method every update cycle
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public @interface Update { }
	
	/**
	 * The {@link @Render} annotation!  Used to call a method every render cycle
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public @interface Render { }
	
	/**
	 * The {@link @Exit} annotation!  Used to call a method on close
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public @interface Exit { }
}
