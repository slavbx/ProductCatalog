package org.slavbx.productcatalog.model;

import lombok.*;

/**
 * Класс, представляющий пользователя.
 * Предоставляет информацию о email, пароле, имени, уровне доступа пользователя
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    /**
     * Идентификатор
     */
    private Long id;
    /**
     * Электронная почта
     */
    private String email;
    /**
     * Пароль
     */
    private String password;
    /**
     * Имя пользователя
     */
    private String name;
    /**
     * Уровень доступа пользователя
     */
    private Level level;
    /**
     * Перечисление, представляющее уровень доступа пользователя
     */
    public enum Level {
        USER, ADMIN
    }

    @Override
    public int hashCode() {
        return email.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        User other = (User) obj;
        return other.getEmail().equals(this.getEmail());
    }

    @Override
    public String toString() {
        return String.format("| %-30s | %-20s |",
                "Email: " + getEmail(),
                "Имя: " + getName());
    }
}
