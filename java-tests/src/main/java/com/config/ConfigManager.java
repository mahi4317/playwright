package com.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfigManager {
	private static final Logger logger = Logger.getLogger(ConfigManager.class.getName());
	private static Properties properties;
	private static final String DEFAULT_ENV = "dev";

	static {
		loadConfiguration();
	}

	private static void loadConfiguration() {
		String environment = System.getProperty("env", DEFAULT_ENV);
	String configFile = String.format("src/test/resources/config/%s.properties", environment);

		properties = new Properties();

		try (InputStream input = new FileInputStream(configFile)) {
			properties.load(input);
			logger.info("Loaded configuration from " + configFile);
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Failed to load configuration file: " + configFile, e);
		}
	}

	public static String getBaseUrl() {
		return getProperty("base.url");
	}

	public static String getUserName() {
		return getProperty("username");
	}

	public static String getPassword() {
		return getProperty("password");
	}

	public static String getBrowser() {
		return getProperty("browser", "chromium");
	}

	public static double getTimeout() {
		String timeout = getProperty("timeout", "30000");
		return Double.parseDouble(timeout);
	}

	private static String getProperty(String key) {
		return properties == null ? null : properties.getProperty(key);
	}

	private static String getProperty(String key, String defaultValue) {
		return properties == null ? defaultValue : properties.getProperty(key, defaultValue);
	}
}
