package org.karthik.store.util;

import java.util.Properties;

public class AppPropertiesHelper {

    private static Properties properties;

    static {
    }

    public static Properties getProperties() {
        if(properties == null) {
            loadProperties();
        }
        return properties;
    }

    public static void loadProperties() {
        System.out.println("loading app.properties");
        properties = new Properties();
        try {
            properties.load(AppPropertiesHelper.class.getClassLoader().getResourceAsStream("app.properties"));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


}
