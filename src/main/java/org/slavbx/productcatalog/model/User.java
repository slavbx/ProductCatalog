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
@EqualsAndHashCode
public class User {
    private Long id;
    private String email;
    @EqualsAndHashCode.Exclude
    private String password;
    private String name;
    private Level level;

    @Override
    public String toString() {
        return String.format("| %-30s | %-20s |",
                "Email: " + getEmail(),
                "Имя: " + getName());
    }
}
