package com.snapdeal.objectstore.common;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Configurations.
 * 
 * @author prakashdey
 *
 */

public final class ConfigurationConstants {

    private static final Logger LOGGER = Logger.getLogger(ConfigurationConstants.class.getName());

    private final static Properties properties;

    static {
        String configFile = "configuration.properties";
        properties = loadProperties(configFile);
    }

    private ConfigurationConstants() {
    }

    /**
     * Utility method to load properties from a file.
     * 
     * @param configFile
     * @return
     * @throws FileNotFoundException
     */
    private static Properties loadProperties(String configFile) {
        Properties properties = new Properties();
        try {
            properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(configFile));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Properties not loaded for file : " + configFile);
            System.exit(-1);
        }
        return properties;
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static interface ConfigurationConstantKeys {
        String FOLDER_LOCATION = "object.file.location";
        String METADATA_FOLDER_LOCATION = "metadata.file.location";
        String INSTANT_SYNC = "filestore.instantstore";
        String CACHE_IMPLEMENTATION = "cache.implementation";
        String STORAGE_SERVICE_IMPLEMENTATION = "storage.service.impl";
    }
}
