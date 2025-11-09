package org.slavbx.productcatalog.exception;

/**
 * Исключение для ситуации, когда пользователь запрашивает несуществующий объект
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
