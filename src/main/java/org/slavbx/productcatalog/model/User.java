package org.slavbx.productcatalog.model;

import jakarta.persistence.*;
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
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq")
    @SequenceGenerator(name = "users_seq", sequenceName = "users_id_seq", allocationSize = 1)
    @EqualsAndHashCode.Exclude
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column
    @EqualsAndHashCode.Exclude
    private String password;

    @Column(unique = true, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Level level;

    @Override
    public String toString() {
        return String.format("| %-30s | %-20s |",
                "Email: " + getEmail(),
                "Имя: " + getName());
    }
}
