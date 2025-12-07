package org.slavbx.auditstarter.repository;

import org.slavbx.auditstarter.model.AuditRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditRepository extends JpaRepository<AuditRecord, Long> {

    /**
     * Находит записи аудита по указанному email
     * @param email email для поиска записей аудита
     * @return список записей аудита
     */
    List<AuditRecord> findByEmail(String email);

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
    List<AuditRecord> findByDateTimeBetween(LocalDateTime startDate, LocalDateTime endDate);
}
