package org.slavbx.productcatalog.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slavbx.productcatalog.model.AuditRecord;
import org.slavbx.productcatalog.annotation.Auditable;
import org.slavbx.productcatalog.repository.RepositoryType;
import org.slavbx.productcatalog.security.AuthenticationService;
import org.slavbx.productcatalog.service.AuditService;
import org.slavbx.productcatalog.service.ServiceFactory;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * Аспект для автоматического аудирования действий
 */
@Aspect
public class AuditableAspect {
    RepositoryType repoType = RepositoryType.valueOf(System.getProperty("repository.type"));
    private final AuthenticationService authService = ServiceFactory.getAuthService(repoType);
    private final AuditService auditService = ServiceFactory.getAuditService(repoType);

    public AuditableAspect() {
    }

    @Around("@annotation(org.slavbx.productcatalog.annotation.Auditable) && execution(* * (..))")
    public Object auditing(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = signature.getMethod();
        Auditable auditable = method.getAnnotation(Auditable.class);

        Object result = proceedingJoinPoint.proceed();

        AuditRecord auditRecord = AuditRecord.builder()
                .email(authService.getCurrentUser() == null ? "unknown" : authService.getCurrentUser().getEmail())
                .action(auditable.action())
                .dateTime(LocalDateTime.now())
                .build();
        auditService.save(auditRecord);
        return result;
    }
}
