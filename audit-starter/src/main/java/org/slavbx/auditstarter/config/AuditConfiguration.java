package org.slavbx.auditstarter.config;

import org.slavbx.auditstarter.annotation.EnableAudition;
import org.slavbx.auditstarter.aspect.AuditableAspect;
import org.slavbx.auditstarter.model.UserProvider;
import org.slavbx.auditstarter.repository.AuditRepository;
import org.slavbx.auditstarter.service.AuditService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
@ConditionalOnBean(annotation = EnableAudition.class)
public class AuditConfiguration {
    final AuditRepository auditRepository;
    final AuditService auditService;

    @Lazy
    public AuditConfiguration(AuditRepository auditRepository, AuditService auditService) {
        this.auditRepository = auditRepository;
        this.auditService = auditService;
    }

    @Bean
    public AuditableAspect auditAspect(UserProvider userProvider, AuditService auditService) {
        return new AuditableAspect(userProvider, auditService);
    }

    @Bean
    @ConditionalOnMissingBean(UserProvider.class)
    public UserProvider defaultUserProvider() {
        return new DefaultUserProvider();
    }

    private static class DefaultUserProvider implements UserProvider {

        @Override
        public String getCurrentUserEmail() {
            return "unknown@example.com";
        }

        @Override
        public String getCurrentUsername() {
            return "unknown";
        }
    }

}
