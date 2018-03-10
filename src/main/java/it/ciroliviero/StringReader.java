package it.ciroliviero;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class StringReader {

	private static StringReader instance = new StringReader();
	private Properties strings;
	private Properties configurations;

	public StringReader() {
		try {
			URL stringsPropertiesFile = getClass().getResource("/strings.properties");
			URL configurationPropertiesFile = getClass().getResource("/application.properties");
			this.strings = new Properties();
			this.strings.load(stringsPropertiesFile.openStream());
			this.configurations = new Properties();
			this.configurations.load(configurationPropertiesFile.openStream());
		} catch (IOException e) {
			System.out.println("Properties files not found...");
			e.printStackTrace();
		}
	}

	public static StringReader getInstance() {
		return instance;
	}

	public String getString(String propertyName){
		return strings.getProperty(propertyName);
	}
	
	public String getConfiguration(String configurationName){
		return configurations.getProperty(configurationName);
	}
}

