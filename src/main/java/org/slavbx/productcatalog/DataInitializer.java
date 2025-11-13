package org.slavbx.productcatalog;

import org.slavbx.productcatalog.model.*;
import org.slavbx.productcatalog.service.BrandService;
import org.slavbx.productcatalog.service.CategoryService;
import org.slavbx.productcatalog.service.ProductService;
import org.slavbx.productcatalog.service.UserService;

import java.math.BigDecimal;
import java.util.List;

public class DataInitializer {
    private final UserService userService;
    private final CategoryService categoryService;
    private final BrandService brandService;
    private final ProductService productService;


    public DataInitializer(UserService userService, CategoryService categoryService, BrandService brandService, ProductService productService) {
        this.userService = userService;
        this.categoryService = categoryService;
        this.brandService = brandService;
        this.productService = productService;
    }

    public void init() {
        createUsers();
        createCategories();
        createBrands();
        createSlavCatalog();
    }

    public void createUsers() {
        userService.save(User.builder().email("admin@admin.com").level(Level.ADMIN).name("administrator").password("admin").build());
        userService.save(User.builder().email("slav@slav.com").level(Level.USER).name("slav").password("slav").build());
    }

    public void createCategories() {
        categoryService.saveAll(List.of(
                Category.builder().name("Электроника").desc("Электронные устройства и аксессуары").build(),
                Category.builder().name("Одежда").desc("Одежда, обувь и аксессуары").build(),
                Category.builder().name("Спорт").desc("Спортивная одежда, инвентарь, снаряжение").build(),
                Category.builder().name("Детские").desc("Игрушки, одежда, средства гигиены и товары для ухода за детьми").build(),
                Category.builder().name("Автотовары").desc("Автоаксессуары, запчасти, масла, оборудование").build()
        ));
    }

    public void createBrands() {
        brandService.saveAll(List.of(
                Brand.builder().name("Zalman").build(),
                Brand.builder().name("MSI").build(),
                Brand.builder().name("AMD").build(),
                Brand.builder().name("Kingston").build(),
                Brand.builder().name("Samsung").build(),
                Brand.builder().name("Bridgestone").build(),
                Brand.builder().name("Tenax").build()
        ));
    }

    public void createSlavCatalog() {
        User user = userService.getUserByEmail("slav@slav.com");
        productService.saveAll(List.of(
                Product.builder().name("Корпус ПК").brand(new Brand("Zalman"))
                        .category(new Category("Электроника")).price(new BigDecimal("3200.00")).quantity(12).seller(user).build(),
                Product.builder().name("Материнская плата").brand(new Brand("MSI"))
                        .category(new Category("Электроника")).price(new BigDecimal("12700.99")).quantity(15).seller(user).build(),
                Product.builder().name("Процессор").brand(new Brand("AMD"))
                        .category(new Category("Электроника")).price(new BigDecimal("20900.00")).quantity(11).seller(user).build(),
                Product.builder().name("ОЗУ").brand(new Brand("Kingston"))
                        .category(new Category("Электроника")).price(new BigDecimal("9100.50")).quantity(20).seller(user).build(),
                Product.builder().name("Накопитель").brand(new Brand("Samsung"))
                        .category(new Category("Электроника")).price(new BigDecimal("8150.99")).quantity(19).seller(user).build(),
                Product.builder().name("Аккумулятор").brand(new Brand("Tenax"))
                        .category(new Category("Автотовары")).price(new BigDecimal("5250.90")).quantity(9).seller(user).build()
        ));
    }
}
