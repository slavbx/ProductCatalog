package org.slavbx.productcatalog.repository.impl;

import org.slavbx.productcatalog.exception.RepositoryException;
import org.slavbx.productcatalog.model.AuditRecord;
import org.slavbx.productcatalog.repository.AuditRepository;
import org.slavbx.productcatalog.repository.DatabaseProvider;
import org.slavbx.productcatalog.repository.SqlQueries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Реализация репозитория для хранения записей аудита
 */
public class AuditRepositoryJdbc implements AuditRepository {

    @Override
    public AuditRecord save(AuditRecord auditRecord) throws RepositoryException {
        try (Connection connection = DatabaseProvider.getConnection()) {
            PreparedStatement prep = connection.prepareStatement(SqlQueries.INSERT_AUDIT_RECORD);
            prep.setString(1, auditRecord.getEmail());
            prep.setString(2, auditRecord.getAction());
            prep.setObject(3, auditRecord.getDateTime());
            prep.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Error. Failed to save audit record: " + e.getMessage(), e);
        }
        return auditRecord;
    }

    @Override
    public List<AuditRecord> findByEmail(String email) throws RepositoryException {
        List<AuditRecord> records = new ArrayList<>();
        try (Connection connection = DatabaseProvider.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SqlQueries.SELECT_AUDIT_RECORDS_BY_EMAIL);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                records.add(mapResultSetToAuditRecord(resultSet));
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error. Failed to return audit records: " + e.getMessage(), e);
        }
        return records;
    }

    @Override
    public Optional<AuditRecord> findById(Long id) throws RepositoryException {
        try (Connection connection = DatabaseProvider.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SqlQueries.SELECT_AUDIT_RECORD_BY_ID);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(mapResultSetToAuditRecord(resultSet));
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error. Failed to return audit record: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public boolean existsByEmail(String email) {
        return !findByEmail(email).isEmpty();
    }

    @Override
    public List<AuditRecord> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) throws RepositoryException {
        List<AuditRecord> records = new ArrayList<>();
        try (Connection connection = DatabaseProvider.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(SqlQueries.SELECT_AUDIT_RECORDS_BY_DATE_RANGE);
            preparedStatement.setObject(1, startDate);
            preparedStatement.setObject(2, endDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                records.add(mapResultSetToAuditRecord(resultSet));
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error. Failed to return audit records for the period: " + e.getMessage(), e);
        }
        return records;
    }

    private AuditRecord mapResultSetToAuditRecord(ResultSet resultSet) throws SQLException {
        String dateTimeString = resultSet.getString("datetime");
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString);

        return AuditRecord.builder()
                .id(resultSet.getLong("id"))
                .email(resultSet.getString("email"))
                .action(resultSet.getString("action"))
                .dateTime(dateTime)
                .build();
    }
}
