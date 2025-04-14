package org.karthik.store.util;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AppPropertiesHelper {

    private static final Logger LOGGER = Logger.getLogger(AppPropertiesHelper.class.getName());

    private static Properties properties;

    public static Properties getProperties() {
        if(properties == null || properties.isEmpty()) {
            loadProperties();
        }
        return properties;
    }

    public static void loadProperties() {
        System.out.println("loading app.properties");
        properties = new Properties();
        try {
            properties.load(AppPropertiesHelper.class.getClassLoader().getResourceAsStream("app.properties"));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }


}
