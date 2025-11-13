package org.slavbx.productcatalog;

import org.slavbx.productcatalog.exception.NotFoundException;
import org.slavbx.productcatalog.model.*;
import org.slavbx.productcatalog.security.AuthenticationService;
import org.slavbx.productcatalog.service.*;

import java.math.BigDecimal;
import java.time.LocalDate;
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
    private final AuthenticationService authService;
    private final UserService userService;
    private final CategoryService categoryService;
    private final BrandService brandService;
    private final ProductService productService;

    public ConsoleUI(AuthenticationService authService, UserService userService, CategoryService categoryService, BrandService brandService, ProductService productService) {
        this.authService = authService;
        this.userService = userService;
        this.categoryService = categoryService;
        this.brandService = brandService;
        this.productService = productService;
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
        try {
            System.out.println("--Регистрация");
            User user = User.builder()
                    .level(Level.USER)
                    .build();
            System.out.print("Введите email: ");
            user.setEmail(scanner.next());
            System.out.print("Введите пароль: ");
            user.setPassword(scanner.next());
            System.out.print("Введите имя: ");
            user.setName(scanner.next());
            userService.create(user);
            System.out.println("Пользователь зарегистрирован\n");
            start();
        } catch (RuntimeException e) {
            System.err.println(e.getMessage() + "\n");
            start();
        }
    }

    private void signIn() {
        try {
            System.out.println("--Вход");
            User user = User.builder()
                    .level(Level.USER)
                    .build();
            System.out.print("Введите email: ");
            user.setEmail(scanner.next());
            System.out.print("Введите пароль: ");
            user.setPassword(scanner.next());
            authService.signIn(user);
            System.out.println("Вход выполнен");
            if (authService.getCurrentUser().getLevel() == Level.ADMIN) {
                actionsAdmin();
            } else {
                actionsUser();
            }
        } catch (RuntimeException e) {
            System.err.println(e.getMessage() + "\n");
            start();
        }
    }

    private void actionsAdmin() {
        System.out.println("\n--Личный кабинет " + authService.getCurrentUser().getName());
        System.out.println("""
                Введите команду:
                1 - Сброс пароля аккаунта
                2 - Просмотр списка пользователей
                0 - Выход""");
        switch (scanner.next()) {
            case "1" -> resetPassword();
            case "2" -> viewAllUsers();
            case "0" -> {
                authService.signOut();
                start();
            }
            default -> {
                System.out.println("Неверная команда\n");
                actionsAdmin();
            }
        }
    }

    private void resetPassword() {
        try {
            System.out.print("--Сброс пароля\n" + "Введите email: ");
            userService.resetPassword(scanner.next());
            System.out.println("Пароль сброшен");
            actionsAdmin();
        } catch (RuntimeException e) {
            System.err.println(e.getMessage() + "\n");
            actionsAdmin();
        }
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
        System.out.println("\n--Личный кабинет " + authService.getCurrentUser().getName());
        System.out.println("""
                Введите команду:
                1 - Управление профилем
                2 - Управление товарами
                0 - Выход""");
        switch (scanner.next()) {
            case "1" -> editProfile(authService.getCurrentUser());
            case "2" -> manageProduct();
            case "0" -> {
                authService.signOut();
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
                authService.signIn(user);
                editProfile(user);
            }
            case "3" -> {
                editUserField(user, h -> h.setPassword(scanner.next()), "пароль");
                authService.signIn(user);
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
        System.out.println("\n--Управление товарами " + authService.getCurrentUser().getName());
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
        System.out.println("--Список товаров пользователя " + authService.getCurrentUser().getName() + ": ");
        productService.findAllProductsByUser(authService.getCurrentUser()).forEach(System.out::println);
    }

    private void createProduct() {
        Product product = Product.builder()
                .seller(authService.getCurrentUser())
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
            System.err.println(e.getMessage());
            manageProduct();
        }

    }

    private void editProduct(Product product, String label) {
        System.out.println("\n--" + label + " " + authService.getCurrentUser().getName());
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
            case "3" -> editProductViewAllCategories(product, label);
            case "4" -> editProductViewAllBrands(product, label);
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

    private void editProductViewAllCategories(Product product, String label) {
        System.out.println("--Список категорий товаров: ");
        categoryService.findAllCategories().forEach(System.out::println);
        System.out.print("Введите название выбранной категории: ");
        try {
            Category category = categoryService.getCategoryByName(scanner.next());
            product.setCategory(category);
        } catch (NotFoundException e) {
            System.err.println(e.getMessage());
        }
        editProduct(product, label);
    }

    private void editProductViewAllBrands(Product product, String label) {
        try {
            System.out.println("--Список брендов: ");
            brandService.findAllBrands().forEach(System.out::println);
            System.out.print("Введите название выбранного бренда: ");
            Brand brand = brandService.getBrandByName(scanner.next());
            product.setBrand(brand);
            editProduct(product, label);
        } catch (NotFoundException e) {
            System.err.println(e.getMessage());
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
        try {
            viewAllProductsByUser();
            System.out.print("--Удаление товара\n" + "Введите название: ");
            scanner.nextLine();
            productService.deleteByName(scanner.nextLine());
            System.out.println("Товар удалён");
            manageProduct();
        } catch (NotFoundException e) {
            System.err.println(e.getMessage());
        }
    }
}