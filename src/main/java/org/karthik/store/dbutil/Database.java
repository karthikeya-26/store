package org.karthik.store.dbutil;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.karthik.store.util.AppPropertiesHelper;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;


public class Database {

    public static Properties properties;

    private static  HikariDataSource dataSource;

    static {
           try {
               properties =AppPropertiesHelper.getProperties();
               openPool();
           }catch (Exception e){
              e.printStackTrace();
           }
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static void openPool()  throws Exception {
        System.out.println("Opening database connection pool");
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(properties.getProperty("database_url"));
        config.setUsername(properties.getProperty("database_username"));
        config.setPassword(properties.getProperty("database_password"));
        config.setDriverClassName(properties.getProperty("database_driver"));
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setIdleTimeout(30000);
        config.setMaxLifetime(1800000);
        dataSource = new HikariDataSource(config);
        System.out.println("Opened database connection pool");
    }

    public static void closePool() {
        System.out.println("Closing database connection pool");
        if(dataSource != null) {
            dataSource.close();
        }
    }
}
