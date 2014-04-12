package quartz.engine.util;

public class Resource {
	private String domain;
	private String location;
	
	/**
	 * A resource (stored as string) in the resources folder <br>
	 * Example: <br>
	 * {@code new Resource("default", "images/img.jpg");} <br>
	 * will load the resource at the path "resources/default/images/img.jpg".  Make sense?
	 * @param type = The sub-folder of the resources folder
	 * @param loc = The path from that folder
	 */
	public Resource(String type, String loc) {
		domain = type;
		location = loc;
	}
	
	/**
	 * Where is the string
	 * @return a sub-folder of resources (where you defined)
	 */
	public String getType() {
		return domain;
	}
	
	/**
	 * The location defined at the beginning
	 * @return the second parameter for this resource!
	 */
	public String getLocation() {
		return location;
	}
	/**
	 * Get the complete path to the resource
	 * @return a string usable for files like  <br>{@code new File(resource.getAbbPath);}
	 */
	public String getAbbPath() {
		return "resources/" + domain + "/" + location;
	}
}
