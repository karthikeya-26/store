package org.karthik.store.listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.spi.LoggerContext;
import org.karthik.store.dbutil.Database;
import org.karthik.store.util.AppPropertiesHelper;

import javax.servlet.ServletContextListener;
import java.net.URI;
import java.sql.SQLException;

public class AppListener implements ServletContextListener {

    @Override
    public void contextInitialized(javax.servlet.ServletContextEvent sce) {
        AppPropertiesHelper.loadProperties();
        Database.openPool();
    }

    @Override
    public void contextDestroyed(javax.servlet.ServletContextEvent sce) {
        Database.closePool();
    }
}
