package org.slavbx.productcatalog.exception;

/**
 * Исключение для ситуации, когда при обращении к репозиторию возникла ошибка
 */
public class RepositoryException extends RuntimeException {
    public RepositoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
