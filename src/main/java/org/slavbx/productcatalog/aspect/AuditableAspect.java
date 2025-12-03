package org.slavbx.productcatalog.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slavbx.productcatalog.model.AuditRecord;
import org.slavbx.productcatalog.annotation.Auditable;
import org.slavbx.productcatalog.security.AuthenticationService;
import org.slavbx.productcatalog.service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * Аспект для автоматического аудирования действий
 */
@Aspect
@Component
public class AuditableAspect {

    @Autowired
    private AuthenticationService authService;

    @Autowired
    private AuditService auditService;

    @Before("@annotation(org.slavbx.productcatalog.annotation.Auditable)")
    public void logAuditableMethods(JoinPoint joinPoint) {
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            Auditable auditable = method.getAnnotation(Auditable.class);

            AuditRecord auditRecord = AuditRecord.builder()
                    .email(authService.getCurrentUser() == null ? "unknown" : authService.getCurrentUser().getEmail())
                    .action(auditable.action())
                    .dateTime(LocalDateTime.now())
                    .build();
            auditService.save(auditRecord);
        } catch (Exception e) {
            System.err.println("Error in AuditableAspect: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
