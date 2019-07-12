package com.rojunaid.roacademy.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class AuditingConfig {

  @Bean
  public AuditorAware<String> auditorProvider() {

    // TODO: return logged in user from Spring Security

    // SecurityContextHolder.getContext().getAuthentication().getName()
    return () -> Optional.ofNullable("Junaid");
  }
}
