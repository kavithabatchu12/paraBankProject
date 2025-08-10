package com.parabank.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private Properties properties;

    public ConfigReader() {
        properties = new Properties();
        
        String configFilePath = "src/test/resources/config.properties";
        try (FileInputStream fis = new FileInputStream(configFilePath)) {
            properties.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
            // Throw a runtime exception to fail fast if the config file is missing
            throw new RuntimeException("Configuration properties file not found at " + configFilePath);
        }
    }

    /**
     * Gets a property value by its key.
     * @param key The key of the property to retrieve.
     * @return The value of the property.
     */
    public String getProperty(String key) {
        String property = properties.getProperty(key);
        if (property != null) {
            return property;
        } else {
            throw new RuntimeException("Property not found in the config.properties file for key: " + key);
        }
    }
}
