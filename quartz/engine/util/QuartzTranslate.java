package quartz.engine.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class QuartzTranslate {
	private static QuartzLanguage[] allLanguages = new QuartzLanguage[1000]; // Quartz supports 1000 languages
	private static QuartzLanguage currentLanguage = null;
	private static QuartzLanguage defaultLanguage = null;
	
	public class QuartzLanguage {
		private String name;
		private Resource file;
		
		public QuartzLanguage(String languageName, Resource language) {
			name = languageName;
			file = language;
		}
		
		public String translateString(String toTrans) {
			File tFile = new File(file.getAbbPath());
			try {
				@SuppressWarnings("resource")
				BufferedReader reader = new BufferedReader(new FileReader(tFile));
				String line = reader.readLine();
				while (line != null) {
					// '#' is a comment
					String[] parts = line.split("=");
					if (parts[0].equals(toTrans)) {
						return parts[1];
					}
					
					line= reader.readLine();
				}
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		public String getName() { return name; }
		
		public Resource getFile() { return file; }
	}
	
	/**
	 * @param defLang default language
	 */
	public QuartzTranslate(String defLang, String gn) {
		int langs = 0;
		File l = new File(new Resource(gn, "assets/").getAbbPath());
		String[] files = l.list();
		if (files == null) {
			System.out.println("[Language API] No files found");
			return;
		}
		for (int i = 0; i < files.length; i++) {
			if (files[i].endsWith(".lang")) {
				String name = "english_US"; // english_US is my games default
				try {
					name = new BufferedReader(new FileReader(new File(new Resource(gn, "assets/" + files[i]).getAbbPath()))).readLine();
				} catch (Exception e) {
					System.out.println("Failed to read language files.");
					e.printStackTrace();
				}
				allLanguages[langs] = new QuartzLanguage(name, new Resource(gn, "assets/" + files[i]));
				if (name.equals(defLang)) {
					currentLanguage = allLanguages[langs];
				}
				langs++;
			}
		}
		defaultLanguage = currentLanguage;
	}
	
	/**
	 * Gets the language with the name assigned
	 * @param name the name of the language
	 * @return the language
	 */
	public static QuartzLanguage getLanguage(String name) {
		for (QuartzLanguage cLang : allLanguages) {
			if (cLang == null) break;
			if (cLang.getName().equals(name)) {
				return cLang;
			}
		}
		return null;
	}
	
	public static QuartzLanguage[] getLanguages() { return allLanguages; }
	public static QuartzLanguage getCurrentLanguage() { return currentLanguage; }
	public static QuartzLanguage getDefaultLanguage() { return defaultLanguage; }
	
	public static void setLanguage(String lName) {
		QuartzLanguage setL = getLanguage(lName);
		if (setL == null)
			throw new NullPointerException("Language " + lName + " does not exist!");
		else
			currentLanguage = setL;
	}
	
	public static String translate(String toTrans) {
		String returns = currentLanguage.translateString(toTrans);
		if (returns == null) returns = defaultLanguage.translateString(toTrans);
		if (returns == null) throw new NullPointerException("The key " + toTrans + " was not found.  Add it to your .lang file");
		return returns;
	}
}
