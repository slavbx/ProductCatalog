package org.slavbx.productcatalog.controller;

import org.slavbx.productcatalog.dto.AuditRecordDTO;
import org.slavbx.productcatalog.mapper.AuditRecordMapper;
import org.slavbx.productcatalog.model.AuditRecord;
import org.slavbx.productcatalog.service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Контроллер для обработки HTTP-запросов к аудит-логам.
 * Поддерживает получение записей аудита по ID, email и диапазону дат.
 */
@RestController
@RequestMapping("/audit")
public class AuditController {

    private final AuditService auditService;
    private final AuditRecordMapper auditRecordMapper;

    @Autowired
    public AuditController(AuditService auditService, AuditRecordMapper auditRecordMapper) {
        this.auditService = auditService;
        this.auditRecordMapper = auditRecordMapper;
    }

    @GetMapping("/{id}")
    public AuditRecordDTO getAuditRecordById(@PathVariable Long id) {
        AuditRecord auditRecord = auditService.getAuditRecordById(id);
        return auditRecordMapper.auditRecordToAuditRecordDTO(auditRecord);
    }

    @GetMapping("/email/{email}")
    public List<AuditRecordDTO> getAuditRecordsByEmail(@PathVariable String email) {
        List<AuditRecord> auditRecords = auditService.getAuditRecordsByEmail(email);
        return auditRecordMapper.auditRecordsToAuditRecordDTOs(auditRecords);
    }

    @GetMapping
    public ResponseEntity<?> getAuditRecordsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        List<AuditRecord> auditRecords = auditService.findAuditRecordsByDateRange(startDate, endDate);
        List<AuditRecordDTO> auditRecordDTOs = auditRecordMapper.auditRecordsToAuditRecordDTOs(auditRecords);

        return ResponseEntity.ok(auditRecordDTOs);
    }
}
