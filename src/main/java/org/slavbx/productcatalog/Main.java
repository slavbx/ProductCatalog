package org.slavbx.productcatalog;

import org.slavbx.productcatalog.exception.NotFoundException;
import org.slavbx.productcatalog.repository.DatabaseProvider;
import org.slavbx.productcatalog.repository.RepositoryType;
import org.slavbx.productcatalog.repository.impl.*;
import org.slavbx.productcatalog.security.AuthenticationService;
import org.slavbx.productcatalog.service.*;


import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Главный класс приложения
 */
public class Main {
    private static final String PROPERTY_PATH = "src/main/resources/application.properties";
    private static final String PROPERTY_REPO_TYPE = "repository.type";

    /**
     * Метод-точка входа в приложение.
     * @param args не используется
     */
    public static void main(String[] args) throws NotFoundException {
        System.setProperty("env", "dev");
        Properties properties = new Properties();
        try {
            InputStream in = Files.newInputStream(Paths.get(PROPERTY_PATH));
            properties.load(in);
            RepositoryType repositoryType = RepositoryType.valueOf(properties.getProperty(PROPERTY_REPO_TYPE));
            final AuthenticationService authService = ServiceFactory.getAuthService(repositoryType);
            final UserService userService = ServiceFactory.getUserService(repositoryType);
            final CategoryService categoryService = ServiceFactory.getCategoryService(repositoryType);
            final BrandService brandService = ServiceFactory.getBrandService(repositoryType);
            final ProductService productService = ServiceFactory.getProductService(repositoryType);
            DatabaseProvider.initDatabase();
            ConsoleUI consoleUI = new ConsoleUI(authService, userService, categoryService, brandService, productService);
            consoleUI.start();
        } catch (Exception e) {
            throw new NotFoundException("Property not found");
        }
    }
}
