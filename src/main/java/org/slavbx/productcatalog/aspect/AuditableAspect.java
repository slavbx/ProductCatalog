package org.slavbx.productcatalog.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slavbx.productcatalog.repository.DatabaseProvider;
import org.slavbx.productcatalog.annotation.Auditable;
import org.slavbx.productcatalog.repository.RepositoryType;
import org.slavbx.productcatalog.security.AuthenticationService;
import org.slavbx.productcatalog.service.ServiceFactory;
import org.slavbx.productcatalog.service.UserService;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;


@Aspect
public class AuditableAspect {
    private final Connection connection = DatabaseProvider.getConnection();
    private final AuthenticationService authService = ServiceFactory.getAuthService(RepositoryType.JDBC);

    public AuditableAspect() throws SQLException {
    }

    @Around("@annotation(org.slavbx.productcatalog.annotation.Auditable) && execution(* * (..))")
    public Object auditing(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = signature.getMethod();
        Auditable auditable = method.getAnnotation(Auditable.class);

        Object result = proceedingJoinPoint.proceed();

        String email = authService.getCurrentUser() == null ? "unknown" : authService.getCurrentUser().getEmail();
        String action = auditable.action();
        String datetime = LocalDateTime.now().toString();

        String sql = "INSERT INTO audit (email, action, datetime) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            statement.setString(2, action);
            statement.setString(3, datetime);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Audit write record failed");
        }

        return result;
    }


}
