package org.slavbx.productcatalog.model;

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
public class AuditRecord {
    @EqualsAndHashCode.Exclude
    private Long id;
    @EqualsAndHashCode.Include
    private String email;
    private String action;
    private LocalDateTime dateTime;

    @Override
    public String toString() {
        return String.format("| %-20s | %-60s | %-20s |",
                "Email: " + getEmail(),
                "Описание: " + getAction(),
                "Время: " + getDateTime());
    }
}