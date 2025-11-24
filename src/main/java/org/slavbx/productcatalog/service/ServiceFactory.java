package org.slavbx.productcatalog.service;


import org.slavbx.productcatalog.repository.RepositoryType;
import org.slavbx.productcatalog.repository.impl.*;
import org.slavbx.productcatalog.security.AuthenticationService;
import org.slavbx.productcatalog.security.impl.AuthenticationServiceImpl;
import org.slavbx.productcatalog.service.impl.*;

/**
 * Фабрика для создания сервисов.
 * Предоставляет статические методы для получения экземпляров
 * сервисов с соответствующими репозиториями
 */
public class ServiceFactory {
    /**
     * Создает экземпляр AuthenticationService
     * @return новый экземпляр AuthenticationService
     */
    public static AuthenticationService getAuthService(RepositoryType repositoryType) {
        return switch (repositoryType) {
            case CORE -> new AuthenticationServiceImpl(new UserRepositoryCore());
            case JDBC -> new AuthenticationServiceImpl(new UserRepositoryJdbc());
        };
    }

    /**
     * Создает экземпляр UserService
     * @return новый экземпляр UserService
     */
    public static UserService getUserService(RepositoryType repositoryType) {
        return switch (repositoryType) {
            case CORE -> new UserServiceImpl(new UserRepositoryCore());
            case JDBC -> new UserServiceImpl(new UserRepositoryJdbc());
            default -> throw new IllegalArgumentException("Unknown repository type");
        };
    }

    /**
     * Создает экземпляр CategoryService
     * @return новый экземпляр CategoryService
     */
    public static CategoryService getCategoryService(RepositoryType repositoryType) {
        return switch (repositoryType) {
            case CORE -> new CategoryServiceImpl(new CategoryRepositoryCore());
            case JDBC -> new CategoryServiceImpl(new CategoryRepositoryJdbc());
            default -> throw new IllegalArgumentException("Unknown repository type");
        };
    }

    /**
     * Создает экземпляр BrandService
     * @return новый экземпляр BrandService
     */
    public static BrandService getBrandService(RepositoryType repositoryType) {
        return switch (repositoryType) {
            case CORE -> new BrandServiceImpl(new BrandRepositoryCore());
            case JDBC -> new BrandServiceImpl(new BrandRepositoryJdbc());
            default -> throw new IllegalArgumentException("Unknown repository type");
        };
    }

    /**
     * Создает экземпляр ProductService
     * @return новый экземпляр ProductService
     */
    public static ProductService getProductService(RepositoryType repositoryType) {
        return switch (repositoryType) {
            case CORE -> new ProductServiceImpl(new ProductRepositoryCore());
            case JDBC -> new ProductServiceImpl(new ProductRepositoryJdbc());
            default -> throw new IllegalArgumentException("Unknown repository type");
        };
    }

    /**
     * Создает экземпляр AuditService
     * @return новый экземпляр AuditService
     */
    public static AuditService getAuditService(RepositoryType repositoryType) {
        return switch (repositoryType) {
            case JDBC -> new AuditServiceImpl(new AuditRepositoryJdbc());
            default -> throw new IllegalArgumentException("Unknown repository type");
        };
    }
}
