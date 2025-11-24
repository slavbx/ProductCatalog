package org.slavbx.productcatalog.service.impl;

import lombok.RequiredArgsConstructor;
import org.slavbx.productcatalog.exception.NotFoundException;
import org.slavbx.productcatalog.model.AuditRecord;
import org.slavbx.productcatalog.repository.AuditRepository;
import org.slavbx.productcatalog.service.AuditService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Реализация сервиса для управления записями аудита.
 * Обеспечивает операции получения, создания и поиска записей аудита.
 */
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {
    private final AuditRepository auditRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public AuditRecord getAuditRecordById(Long id) throws NotFoundException {
        return auditRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Audit record not found with id: " + id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AuditRecord> getAuditRecordsByEmail(String email) throws NotFoundException {
        List<AuditRecord> records = auditRepository.findByEmail(email);
        if (records.isEmpty()) {
            throw new NotFoundException("Audit records not found for email: " + email);
        }
        return records;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AuditRecord save(AuditRecord auditRecord) {
        return auditRepository.save(auditRecord);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AuditRecord> findAuditRecordsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return auditRepository.findByDateRange(startDate, endDate);
    }
}
