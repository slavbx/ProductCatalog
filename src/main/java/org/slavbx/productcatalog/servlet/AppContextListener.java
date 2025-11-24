package org.slavbx.productcatalog.servlet;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.slavbx.productcatalog.repository.DatabaseProvider;

import java.io.InputStream;
import java.util.Properties;


@WebListener
public class AppContextListener implements ServletContextListener {
    private static final String PROPERTY_REPO_TYPE = "repository.type";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.setProperty("env", "dev");
        try (InputStream in = AppContextListener.class.getClassLoader().getResourceAsStream("application.properties")) {
            Properties properties = new Properties();
            properties.load(in);
            String repoType = properties.getProperty(PROPERTY_REPO_TYPE);
            System.setProperty("repository.type", repoType);
            System.setProperty("env", "dev");
            DatabaseProvider.initDatabase();
        } catch (Exception e) {
            throw new RuntimeException("Initialization database failed");
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
