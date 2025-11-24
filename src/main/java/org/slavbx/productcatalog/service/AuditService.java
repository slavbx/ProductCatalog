package org.slavbx.productcatalog.service;

import org.slavbx.productcatalog.exception.NotFoundException;
import org.slavbx.productcatalog.model.AuditRecord;

import java.time.LocalDateTime;
import java.util.List;

public interface AuditService {
    /**
     * Получение записи аудита по её уникальному идентификатору.
     *
     * @param id уникальный идентификатор записи аудита.
     * @return объект AuditRecord, соответствующий указанному id.
     * @throws NotFoundException если запись аудита не найдена
     */
    AuditRecord getAuditRecordById(Long id) throws NotFoundException;

    /**
     * Получение записей аудита по email пользователя.
     *
     * @param email email пользователя.
     * @return список объектов AuditRecord, соответствующих указанному email.
     * @throws NotFoundException если записи аудита с таким email не найдены
     */
    List<AuditRecord> getAuditRecordsByEmail(String email) throws NotFoundException;

    /**
     * Сохранение записи аудита.
     *
     * @param auditRecord объект AuditRecord, который нужно сохранить.
     * @return сохранённый объект AuditRecord.
     */
    AuditRecord save(AuditRecord auditRecord);

    /**
     * Получение записей аудита за указанный период времени.
     *
     * @param startDate начало периода для поиска записей аудита.
     * @param endDate конец периода для поиска записей аудита.
     * @return список записей аудита за указанный период.
     */
    List<AuditRecord> findAuditRecordsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
}
