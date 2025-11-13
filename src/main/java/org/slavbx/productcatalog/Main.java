package org.slavbx.productcatalog;

import org.slavbx.productcatalog.repository.UserRepository;
import org.slavbx.productcatalog.repository.impl.BrandRepositoryCore;
import org.slavbx.productcatalog.repository.impl.CategoryRepositoryCore;
import org.slavbx.productcatalog.repository.impl.ProductRepositoryCore;
import org.slavbx.productcatalog.repository.impl.UserRepositoryCore;
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
        final UserRepository userRepository = new UserRepositoryCore();
        final UserService userService = new UserServiceImpl(userRepository);
        final AuthenticationService authService = new AuthenticationServiceImpl(userRepository);
        final CategoryService categoryService = new CategoryServiceImpl(new CategoryRepositoryCore());
        final ProductService productService = new ProductServiceImpl(new ProductRepositoryCore());
        final BrandService brandService = new BrandServiceImpl(new BrandRepositoryCore());
        DataInitializer dataInitializer = new DataInitializer(userService, categoryService, brandService, productService);
        dataInitializer.init();
        ConsoleUI consoleUI = new ConsoleUI(authService, userService, categoryService, brandService, productService);
        consoleUI.start();
    }
}
