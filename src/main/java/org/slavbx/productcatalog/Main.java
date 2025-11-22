package org.slavbx.productcatalog;

import org.slavbx.productcatalog.repository.DatabaseProvider;
import org.slavbx.productcatalog.repository.UserRepository;
import org.slavbx.productcatalog.repository.impl.*;
import org.slavbx.productcatalog.security.AuthenticationService;
import org.slavbx.productcatalog.security.impl.AuthenticationServiceImpl;
import org.slavbx.productcatalog.service.*;
import org.slavbx.productcatalog.service.impl.BrandServiceImpl;
import org.slavbx.productcatalog.service.impl.CategoryServiceImpl;
import org.slavbx.productcatalog.service.impl.ProductServiceImpl;
import org.slavbx.productcatalog.service.impl.UserServiceImpl;

/**
 * Главный класс приложения
 */
public class Main {
    /**
     * Метод-точка входа в приложение.
     * @param args не используется
     */
    public static void main(String[] args) {
        System.setProperty("env", "dev");
        final UserRepository userRepository = new UserRepositoryJdbc();
        final UserService userService = new UserServiceImpl(userRepository);
        final AuthenticationService authService = new AuthenticationServiceImpl(userRepository);
        final CategoryService categoryService = new CategoryServiceImpl(new CategoryRepositoryJdbc());
        final ProductService productService = new ProductServiceImpl(new ProductRepositoryJdbc());
        final BrandService brandService = new BrandServiceImpl(new BrandRepositoryJdbc());
        DatabaseProvider.initDatabase();
        ConsoleUI consoleUI = new ConsoleUI(authService, userService, categoryService, brandService, productService);
        consoleUI.start();
    }
}
