package org.slavbx.productcatalog.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.slavbx.productcatalog.dto.AuditRecordDto;
import org.slavbx.productcatalog.model.AuditRecord;

import java.util.List;

/**
 * Маппер для преобразования между сущностью AuditRecord и DTO
 */
@Mapper(componentModel = "spring")
public interface AuditRecordMapper {
    AuditRecordMapper INSTANCE = Mappers.getMapper(AuditRecordMapper.class);

    AuditRecordDto auditRecordToAuditRecordDTO(AuditRecord auditRecord);

    List<AuditRecordDto> auditRecordsToAuditRecordDTOs(List<AuditRecord> auditRecords);

    AuditRecord auditRecordDTOToAuditRecord(AuditRecordDto auditRecordDTO);
}
