package org.slavbx.productcatalog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
@Tag(name = "AuditController", description = "API for audit records management")
@RestController
@RequestMapping("/audit")
@RequiredArgsConstructor
public class AuditController {

    private final AuditService auditService;
    private final AuditRecordMapper auditRecordMapper;

    @Operation(summary = "Get audit record by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Audit record found"),
            @ApiResponse(responseCode = "404", description = "Audit record not found")
    })
    @GetMapping("/{id}")
    public AuditRecordDTO getAuditRecordById(
            @Parameter(description = "ID of the audit record", example = "1")
            @PathVariable Long id) {
        AuditRecord auditRecord = auditService.getAuditRecordById(id);
        return auditRecordMapper.auditRecordToAuditRecordDTO(auditRecord);
    }

    @Operation(summary = "Get audit records by email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Audit records retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No audit records found for the email")
    })
    @GetMapping("/email/{email}")
    public List<AuditRecordDTO> getAuditRecordsByEmail(
            @Parameter(description = "Email address of the user", example = "admin@example.com")
            @PathVariable String email) {
        List<AuditRecord> auditRecords = auditService.getAuditRecordsByEmail(email);
        return auditRecordMapper.auditRecordsToAuditRecordDTOs(auditRecords);
    }

    @Operation(summary = "Get audit records by date range")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Audit records retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid date parameters")
    })
    @GetMapping
    public ResponseEntity<List<AuditRecordDTO>> getAuditRecordsByDateRange(
            @Parameter(
                    description = "Start date for filtering (ISO format)",
                    example = "2024-01-01T00:00:00",
                    required = true
            )
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,

            @Parameter(
                    description = "End date for filtering (ISO format)",
                    example = "2024-12-31T23:59:59",
                    required = true
            )
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        List<AuditRecord> auditRecords = auditService.findAuditRecordsByDateRange(startDate, endDate);
        List<AuditRecordDTO> auditRecordDTOs = auditRecordMapper.auditRecordsToAuditRecordDTOs(auditRecords);

        return ResponseEntity.ok(auditRecordDTOs);
    }
}