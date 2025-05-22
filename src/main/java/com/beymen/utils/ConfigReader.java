package com.beymen.utils;

import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    private static final Logger logger = LoggerHelper.getLogger(ConfigReader.class);
    private static final String CONFIG_FILE_PATH = "src/main/resources/config.properties";
    private static final Properties properties;


    static {
        try {
            FileInputStream input = new FileInputStream(CONFIG_FILE_PATH);
            properties = new Properties();
            properties.load(input);
            input.close();
        } catch (IOException e) {
            logger.error("Error loading config.properties: {}", e.getMessage());
            throw new RuntimeException("Failed to load config.properties file.", e);
        }
    }

    public static String getProperty(String keyName) {
        return properties.getProperty(keyName);
    }
}
