package org.slavbx.productcatalog.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.slavbx.productcatalog.dto.AuditRecordDTO;
import org.slavbx.productcatalog.model.AuditRecord;

import java.util.List;

@Mapper
public interface AuditRecordMapper {
    AuditRecordMapper INSTANCE = Mappers.getMapper(AuditRecordMapper.class);

    AuditRecordDTO auditRecordToAuditRecordDTO(AuditRecord auditRecord);

    List<AuditRecordDTO> auditRecordsToAuditRecordDTOs(List<AuditRecord> auditRecords);

    AuditRecord auditRecordDTOToAuditRecord(AuditRecordDTO auditRecordDTO);
}
