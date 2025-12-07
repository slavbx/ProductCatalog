package org.slavbx.logstarter.config;

import org.slavbx.logstarter.aspect.LoggableAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
public class LogConfiguration {

    @Bean
    public LoggableAspect logAspect() {
        return new LoggableAspect();
    }

}
