package org.slavbx.productcatalog.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Класс, представляющий запись аудита.
 * Предоставляет информацию о email пользователя, действии и времени его фиксации
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "audit")
public class AuditRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "audit_seq")
    @SequenceGenerator(name = "audit_seq", sequenceName = "audit_id_seq", allocationSize = 1)
    @EqualsAndHashCode.Exclude
    private Long id;

    @Column(nullable = false)
    @EqualsAndHashCode.Include
    private String email;

    @Column(nullable = false)
    private String action;

    @Column(name = "datetime")
    private LocalDateTime dateTime;

    @Override
    public String toString() {
        return String.format("| %-20s | %-60s | %-20s |",
                "Email: " + getEmail(),
                "Описание: " + getAction(),
                "Время: " + getDateTime());
    }
}