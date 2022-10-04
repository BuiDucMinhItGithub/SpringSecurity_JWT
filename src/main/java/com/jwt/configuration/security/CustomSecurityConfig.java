package com.jwt.configuration.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class CustomSecurityConfig {

  @Bean
  public BCryptPasswordEncoder metodoCrittografia() {
    return new BCryptPasswordEncoder();
  }
}
