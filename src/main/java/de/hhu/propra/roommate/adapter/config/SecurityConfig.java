package de.hhu.propra.roommate.adapter.config;

import de.hhu.propra.roommate.application.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;


@Configuration
public class SecurityConfig {

  @Bean
  public SecurityFilterChain configure(HttpSecurity chainBuilder) throws Exception {
    return chainBuilder
        .authorizeHttpRequests(configurer -> configurer
            .requestMatchers("/", "/css/*", "/api/*").permitAll().anyRequest().authenticated())

        .csrf(csrf -> csrf
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))

        .exceptionHandling(e -> e
            .accessDeniedHandler((request, response, accessDeniedException) -> {
              response.setStatus(HttpStatus.FORBIDDEN.value());
              response.getWriter().print("Access Denied!");
            }))

        .logout(logout -> logout
            .logoutSuccessUrl("/").permitAll())

        .oauth2Login(config -> config
            .userInfoEndpoint(info -> info.userService(new UserService())))

        .build();
  }

}
