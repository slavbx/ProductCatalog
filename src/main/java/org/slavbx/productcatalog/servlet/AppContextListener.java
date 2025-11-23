package org.slavbx.productcatalog.servlet;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.slavbx.productcatalog.repository.DatabaseProvider;


@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.setProperty("env", "dev");
        try {
            DatabaseProvider.initDatabase();
        } catch (Exception e) {
            //throw new RuntimeException("Initialization database failed");
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
