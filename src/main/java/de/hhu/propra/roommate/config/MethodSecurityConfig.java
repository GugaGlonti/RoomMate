package de.hhu.propra.roommate.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;


@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class MethodSecurityConfig { }
