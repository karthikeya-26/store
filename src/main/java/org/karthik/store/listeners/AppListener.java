package org.karthik.store.listeners;

import org.karthik.store.dbutil.Database;
import org.karthik.store.util.AppPropertiesHelper;

import javax.servlet.ServletContextListener;

public class AppListener implements ServletContextListener {

    @Override
    public void contextInitialized(javax.servlet.ServletContextEvent sce) {
//        AppPropertiesHelper.loadProperties();
//        try {
//            Database.openPool();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
    }

    @Override
    public void contextDestroyed(javax.servlet.ServletContextEvent sce) {
//        Database.closePool();
    }
}
