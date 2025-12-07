package org.slavbx.auditstarter.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slavbx.auditstarter.annotation.Auditable;
import org.slavbx.auditstarter.model.AuditRecord;
import org.slavbx.auditstarter.model.UserProvider;
import org.slavbx.auditstarter.service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Aspect
@Component
public class AuditableAspect {

    private final UserProvider userProvider;
    private final AuditService auditService;

    @Autowired
    public AuditableAspect(UserProvider userProvider, AuditService auditService) {
        this.userProvider = userProvider;
        this.auditService = auditService;
    }

    @Before("@annotation(org.slavbx.auditstarter.annotation.Auditable)")
    public void logAuditableMethods(JoinPoint joinPoint) {
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            Auditable auditable = method.getAnnotation(Auditable.class);

            AuditRecord auditRecord = AuditRecord.builder()
                    .email(userProvider.getCurrentUserEmail())
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