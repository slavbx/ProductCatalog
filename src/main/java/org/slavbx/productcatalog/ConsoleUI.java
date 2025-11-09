package org.slavbx.productcatalog;

import org.slavbx.productcatalog.exception.NotFoundException;
import org.slavbx.productcatalog.model.Brand;
import org.slavbx.productcatalog.model.Category;
import org.slavbx.productcatalog.model.Product;
import org.slavbx.productcatalog.model.User;
import org.slavbx.productcatalog.repository.BrandRepositoryCore;
import org.slavbx.productcatalog.repository.CategoryRepositoryCore;
import org.slavbx.productcatalog.repository.ProductRepositoryCore;
import org.slavbx.productcatalog.repository.UserRepositoryCore;
import org.slavbx.productcatalog.service.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;

/**
 * Класс пользовательского интерфейса консольного приложения.
 * Предоставляет интерфейс для взаимодействия с пользователем,
 * включая вход в систему и регистрацию
 */
public class ConsoleUI {
    private final Scanner scanner = new Scanner(System.in);
    private final UserService userService = new UserServiceImpl(new UserRepositoryCore());
    private final CategoryService categoryService = new CategoryServiceImpl(new CategoryRepositoryCore());
    private final ProductService productService = new ProductServiceImpl(new ProductRepositoryCore());
    private final BrandService brandService = new BrandServiceImpl(new BrandRepositoryCore());

    /**
     * Инициализирует репозитории
     */
    public void init() {
        User user = User.builder().email("admin@admin.com").level(User.Level.ADMIN).name("administrator").password("admin").build();
        userService.save(user);
        user = User.builder().email("slav@slav.com").level(User.Level.USER).name("slav").password("slav").build();
        userService.save(user);
        List<Category> categories = new ArrayList<>(List.of(
                Category.builder().name("Электроника").desc("Электронные устройства и аксессуары").build(),
                Category.builder().name("Одежда").desc("Одежда, обувь и аксессуары").build(),
                Category.builder().name("Спорт").desc("Спортивная одежда, инвентарь, снаряжение").build(),
                Category.builder().name("Детские").desc("Игрушки, одежда, средства гигиены и товары для ухода за детьми").build(),
                Category.builder().name("Автотовары").desc("Автоаксессуары, запчасти, масла, оборудование").build()
        ));
        categoryService.saveAll(categories);
        List<Brand> brands = new ArrayList<>(List.of(
                Brand.builder().name("Zalman").build(),
                Brand.builder().name("MSI").build(),
                Brand.builder().name("AMD").build(),
                Brand.builder().name("Kingston").build(),
                Brand.builder().name("Samsung").build(),
                Brand.builder().name("Bridgestone").build(),
                Brand.builder().name("Tenax").build()
        ));
        brandService.saveAll(brands);
        List<Product> products = new ArrayList<>(List.of(
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
        productService.saveAll(products);
    }

    /**
     * Запускает главное меню для взаимодействия с пользователем
     */
    public void start() {
        System.out.println("""
                --Начальное меню
                Введите для продолжения:\s
                1 - Вход
                2 - Регистрация
                0 - Завершение работы""");
        switch (scanner.next()) {
            case "1" -> signIn();
            case "2" -> signUp();
            case "0" -> {}
            default -> {
                System.out.println("Неверная команда\n");
                start();
            }
        }
    }

    private void signUp() {
        System.out.println("--Регистрация");
        User user = User.builder()
                .level(User.Level.USER)
                .build();
        System.out.print("Введите email: ");
        user.setEmail(scanner.next());
        System.out.print("Введите пароль: ");
        user.setPassword(scanner.next());
        System.out.print("Введите имя: ");
        user.setName(scanner.next());

        try {
            userService.create(user);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage() + "\n");
            start();
        }
        System.out.println("Пользователь зарегистрирован\n");
        start();
    }

    private void signIn() {
        System.out.println("--Вход");
        User user = User.builder()
                .level(User.Level.USER)
                .build();
        System.out.print("Введите email: ");
        user.setEmail(scanner.next());
        System.out.print("Введите пароль: ");
        user.setPassword(scanner.next());

        try {
            userService.signIn(user);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage() + "\n");
            start();
        }

        System.out.println("Вход выполнен");
        if (userService.getCurrentUser().getLevel() == User.Level.ADMIN) {
            actionsAdmin();
        } else {
            actionsUser();
        }
    }

    private void actionsAdmin() {
        System.out.println("\n--Личный кабинет " + userService.getCurrentUser().getName());
        System.out.println("""
                Введите команду:
                1 - Сброс пароля аккаунта
                2 - Просмотр списка пользователей
                0 - Выход""");
        switch (scanner.next()) {
            case "1" -> resetPassword();
            case "2" -> viewAllUsers();
            case "0" -> {
                userService.signOut();
                start();
            }
            default -> {
                System.out.println("Неверная команда\n");
                actionsAdmin();
            }
        }
    }

    private void resetPassword() {
        System.out.print("--Сброс пароля\n" + "Введите email: ");
        try {
            userService.resetPassword(scanner.next());
        } catch (RuntimeException e) {
            System.out.println(e.getMessage() + "\n");
            actionsAdmin();
        }
        System.out.println("Пароль сброшен");
        actionsAdmin();
    }

    private void viewAllUsers() {
        System.out.println("--Список пользователей: ");
        List<User> list = userService.findAllUsers();
        if (!list.isEmpty()) {
            list.forEach(System.out::println);
        } else {
            System.out.println("Пользователей не найдено");
        }
        System.out.println("\nВведите любую команду для возврата:");
        scanner.next();
        actionsAdmin();
    }

    private void actionsUser() {
        System.out.println("\n--Личный кабинет " + userService.getCurrentUser().getName());
        System.out.println("""
                Введите команду:
                1 - Управление профилем
                2 - Управление товарами
                0 - Выход""");
        switch (scanner.next()) {
            case "1" -> editProfile(userService.getCurrentUser());
            case "2" -> manageProduct();
            case "0" -> {
                userService.signOut();
                start();
            }
            default -> {
                System.out.println("Неверная команда\n");
                actionsUser();
            }
        }
    }

    private void editProfile(User user) {
        System.out.println("\n--Редактирование профиля " + user.getName());
        System.out.println("Введите команду:\n" +
                "1 - Редактировать имя: " + user.getName() + "\n" +
                "2 - Редактировать email: " + user.getEmail() + "\n" +
                "3 - Редактировать пароль: " + user.getPassword() + "\n" +
                "0 - Назад");
        switch (scanner.next()) {
            case "1" -> editUserField(user, h -> h.setName(scanner.next()), "имя");
            case "2" -> {
                editUserField(user, h -> h.setEmail(scanner.next()), "email");
                userService.signIn(user);
                editProfile(user);
            }
            case "3" -> {
                editUserField(user, h -> h.setPassword(scanner.next()), "пароль");
                userService.signIn(user);
                editProfile(user);
            }
            case "0" -> actionsUser();
            default -> {
                System.out.println("Неверная команда\n");
                editProfile(user);
            }
        }
    }

    private void editUserField(User user, Consumer<User> consumer, String field) {
        System.out.print("--Новое " + field + ": ");
        consumer.accept(user);
        userService.save(user);
        System.out.println(field + " отредактировано");
        editProfile(user);
    }

    private void manageProduct() {
        System.out.println("\n--Управление товарами " + userService.getCurrentUser().getName());
        System.out.println("""
                Введите команду:
                1 - Создание товара
                2 - Редактирование товара
                3 - Удаление товара
                0 - Назад""");
        switch (scanner.next()) {
            case "1" -> createProduct();
            case "2" -> {
                viewAllProductsByUser();
                System.out.print("Введите название редактируемого товара: ");
                scanner.nextLine();
                updateProduct(scanner.nextLine());
            }
            case "3" -> deleteProduct();
            case "0" -> actionsUser();
            default -> {
                System.out.println("Неверная команда\n");
                manageProduct();
            }
        }
    }

    private void viewAllProductsByUser() {
        System.out.println("--Список товаров пользователя " + userService.getCurrentUser().getName() + ": ");
        productService.findAllProductsByUser(userService.getCurrentUser()).forEach(System.out::println);
    }

    private void viewAllCategories() {
        System.out.println("--Список категорий товаров: ");
        categoryService.findAllCategories().forEach(System.out::println);
    }

    private void viewAllBrands() {
        System.out.println("--Список брендов: ");
        brandService.findAllBrands().forEach(System.out::println);
    }

    private void createProduct() {
        Product product = Product.builder()
                .seller(userService.getCurrentUser())
                .createDate(LocalDate.now())
                .category(new Category())
                .brand(new Brand())
                .build();
        editProduct(product, "Создание товара");
        System.out.println("Товар создан");
    }

    private void updateProduct(String name) {
        try {
            Product product = productService.getProductByName(name);
            editProduct(product, "Редактирование товара");
        } catch (NotFoundException e) {
            System.out.println(e.getMessage());
            manageProduct();
        }

    }

    private void editProduct(Product product, String label) {
        System.out.println("\n--" + label + " " + userService.getCurrentUser().getName());
        System.out.println("Введите команду для редактирования:\n" +
                "1 - Название: " + product.getName() + "\n" +
                "2 - Описание: " + product.getDesc() + "\n" +
                "3 - Категория: " + product.getCategory().getName() + "\n" +
                "4 - Бренд: " + product.getBrand().getName() + "\n" +
                "5 - Цена: " + product.getPrice() + "\n" +
                "6 - Количество на складе: " + product.getQuantity() + "\n" +
                "7 - Сохранить изменения\n" +
                "0 - Назад");
        switch (scanner.nextLine()) {
            case "1" -> editProductField(product, p -> p.setName(scanner.nextLine()), "название", label);
            case "2" -> editProductField(product, p -> p.setDesc(scanner.nextLine()), "описание", label);
            case "3" -> {
                viewAllCategories();
                System.out.print("Введите название выбранной категории: ");
                try {
                    Category category = categoryService.getCategoryByName(scanner.next());
                    product.setCategory(category);
                } catch (NotFoundException e) {
                  System.out.println(e.getMessage());
                }
                editProduct(product, label);
            }
            case "4" -> {
                viewAllBrands();
                System.out.print("Введите название выбранного бренда: ");
                try {
                    Brand brand = brandService.getBrandByName(scanner.next());
                    product.setBrand(brand);
                } catch (NotFoundException e) {
                    System.out.println(e.getMessage());
                }
                editProduct(product, label);
            }
            case "5" -> editProductField(product, p -> p.setPrice(new BigDecimal(scanner.nextLine())), "стоимость", label);
            case "6" -> editProductField(product, p -> p.setQuantity(scanner.nextInt()), "количество", label);
            case "7" -> {
                productService.save(product);
                System.out.println("Товар отредактирован");
                editProduct(product, label);
            }
            case "0" -> actionsUser();
            default -> {
                System.out.println("Неверная команда\n");
                editProduct(product, label);
            }
        }
    }

    private void editProductField(Product product, Consumer<Product> consumer, String field, String label) {
        System.out.print("--Редактируем " + field + ": ");
        consumer.accept(product);
        productService.save(product);
        System.out.println(field + " отредактировано");
        editProduct(product, label);
    }

    private void deleteProduct() {
        viewAllProductsByUser();
        System.out.print("--Удаление товара\n" + "Введите название: ");
        scanner.nextLine();
        try {
            productService.deleteByName(scanner.nextLine());
            System.out.println("Товар удалён");
        } catch (NotFoundException e) {
            System.out.println(e.getMessage());
        }
        manageProduct();
    }
}