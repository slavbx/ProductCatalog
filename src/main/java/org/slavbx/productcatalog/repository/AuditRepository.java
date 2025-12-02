package org.slavbx.productcatalog.repository;

import org.slavbx.productcatalog.model.AuditRecord;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AuditRepository {
    /**
     * Сохраняет запись аудита
     * @param auditRecord объект записи аудита для сохранения
     */
    AuditRecord save(AuditRecord auditRecord);

    /**
     * Находит записи аудита по указанному email
     * @param email email для поиска записей аудита
     * @return список записей аудита
     */
    List<AuditRecord> findByEmail(String email);

    /**
     * Находит запись аудита по id
     * @param id идентификатор для поиска записи аудита
     * @return объект Optional, содержащий найденную запись аудита, или пустой объект, если запись не найдена
     */
    Optional<AuditRecord> findById(Long id);

    /**
     * Существуют ли записи аудита для email
     * @param email email для проверки существования записей
     * @return boolean, означающий существование записей аудита
     */
    boolean existsByEmail(String email);

    /**
     * Находит записи аудита за указанный период
     * @param startDate начало периода
     * @param endDate конец периода
     * @return список записей аудита за период
     */
    List<AuditRecord> findByDateRange(LocalDateTime startDate, LocalDateTime endDate);
}
