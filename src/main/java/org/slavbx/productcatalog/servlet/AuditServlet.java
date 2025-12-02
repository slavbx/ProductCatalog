package org.slavbx.productcatalog.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slavbx.productcatalog.annotation.Auditable;
import org.slavbx.productcatalog.dto.AuditRecordDTO;
import org.slavbx.productcatalog.dto.ResponseDTO;
import org.slavbx.productcatalog.exception.NotFoundException;
import org.slavbx.productcatalog.mapper.AuditRecordMapper;
import org.slavbx.productcatalog.model.AuditRecord;
import org.slavbx.productcatalog.repository.RepositoryType;
import org.slavbx.productcatalog.service.AuditService;
import org.slavbx.productcatalog.service.ServiceFactory;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Сервлет для обработки HTTP-запросов к аудит-логам.
 * Поддерживает получение записей аудита по ID, email и диапазону дат.
 * Доступные эндпоинты:
 * GET /audit - записи за период (параметры: startDate, endDate)
 * GET /audit/{id} - запись по идентификатору
 * GET /audit/{email} - записи по email пользователя *
 */
@WebServlet("/audit/*")
public class AuditServlet extends BaseHttpServlet {
    RepositoryType repoType = RepositoryType.valueOf(System.getProperty("repository.type"));
    private final AuditService auditService = ServiceFactory.getAuditService(repoType);
    private final AuditRecordMapper auditRecordMapper = AuditRecordMapper.INSTANCE;

    @Override
    protected void doGet(HttpServletRequest httpReq, HttpServletResponse httpResp) throws IOException {
        String pathInfo = httpReq.getRequestURI().substring(httpReq.getContextPath().length());

        if ("/audit".equals(pathInfo)) {
            doGetAuditRecordsByDateRange(httpReq, httpResp);
        } else if (pathInfo.startsWith("/audit/")) {
            String endOfPath = pathInfo.substring("/audit/".length());
            if (endOfPath.matches("\\d+")) {
                doGetAuditRecordById(httpReq, httpResp, Long.parseLong(endOfPath));
            } else {
                doGetAuditRecordsByEmail(httpReq, httpResp, URLDecoder.decode(endOfPath, StandardCharsets.UTF_8));
            }
        } else {
            throw new NotFoundException("Resource not found");
        }
    }

    protected void doGetAuditRecordById(HttpServletRequest httpReq, HttpServletResponse httpResp, Long id) throws IOException {
        AuditRecord auditRecord = auditService.getAuditRecordById(id);
        AuditRecordDTO auditRecordDTO = auditRecordMapper.auditRecordToAuditRecordDTO(auditRecord);
        sendJsonResponse(httpResp, auditRecordDTO, HttpServletResponse.SC_OK);
    }

    protected void doGetAuditRecordsByEmail(HttpServletRequest httpReq, HttpServletResponse httpResp, String email) throws IOException {
        List<AuditRecord> auditRecords = auditService.getAuditRecordsByEmail(email);
        List<AuditRecordDTO> auditRecordDTOs = auditRecordMapper.auditRecordsToAuditRecordDTOs(auditRecords);
        sendJsonResponse(httpResp, auditRecordDTOs, HttpServletResponse.SC_OK);
    }

    protected void doGetAuditRecordsByDateRange(HttpServletRequest httpReq, HttpServletResponse httpResp) throws IOException {
        String startDateParam = httpReq.getParameter("startDate");
        String endDateParam = httpReq.getParameter("endDate");

        if (startDateParam == null || endDateParam == null) {
            sendJsonResponse(httpResp,
                    ResponseDTO.builder().message("Parameters 'startDate' and 'endDate' are required").build(),
                    HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        LocalDateTime startDate = LocalDateTime.parse(startDateParam);
        LocalDateTime endDate = LocalDateTime.parse(endDateParam);

        List<AuditRecord> auditRecords = auditService.findAuditRecordsByDateRange(startDate, endDate);
        List<AuditRecordDTO> auditRecordDTOs = auditRecordMapper.auditRecordsToAuditRecordDTOs(auditRecords);
        sendJsonResponse(httpResp, auditRecordDTOs, HttpServletResponse.SC_OK);
    }

    @Auditable(action = "Создание аудит записи")
    protected void doPost(HttpServletRequest httpReq, HttpServletResponse httpResp) throws IOException {
        String jsonReq = new String(httpReq.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

        AuditRecordDTO auditRecordDTO = objectMapper.readValue(jsonReq, AuditRecordDTO.class);
        validate(auditRecordDTO);

        AuditRecord auditRecord = auditRecordMapper.auditRecordDTOToAuditRecord(auditRecordDTO);

        if (auditRecord.getDateTime() == null) {
            auditRecord.setDateTime(LocalDateTime.now());
        }

        AuditRecord createdAuditRecord = auditService.save(auditRecord);
        AuditRecordDTO createdAuditRecordDTO = auditRecordMapper.auditRecordToAuditRecordDTO(createdAuditRecord);

        sendJsonResponse(httpResp, createdAuditRecordDTO, HttpServletResponse.SC_CREATED);
    }
}
