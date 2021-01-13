package com.github.bestheroz.standard.context.security;

import com.github.bestheroz.standard.common.authenticate.JwtAuthenticationFilter;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
  public static final String[] PUBLIC =
      new String[] {
        "/login",
        "/error",
        "/api/auth/login",
        "/api/variables/**",
        "/actuator/**",
        "/api/auth/login",
        "/api/auth/refreshToken",
        "/api/auth/initPassword"
      };

  @Override
  protected void configure(final HttpSecurity http) throws Exception {
    http.httpBasic()
        .disable()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeRequests()
        .antMatchers(PUBLIC)
        .permitAll()
        .requestMatchers(EndpointRequest.toAnyEndpoint())
        .permitAll()
        .anyRequest()
        .authenticated()
        .and()
        .addFilterBefore(
            new JwtAuthenticationFilter(this.authenticationManagerBean()),
            UsernamePasswordAuthenticationFilter.class)
        .csrf()
        .disable()
        .cors();
  }

  @Override
  public void configure(final WebSecurity web) {
    web.ignoring()
        .antMatchers(
            "/static/**",
            "/**/*.js",
            "/**/*.js.map",
            "/**/*.css",
            "/**/*.html",
            "/images/**",
            "/fonts/**",
            "/favicon.*",
            "/manifest.json");
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    final CorsConfiguration configuration = new CorsConfiguration();

    configuration.addAllowedOrigin("http://localhost:8080");
    configuration.addAllowedOrigin("http://localhost:8081");
    configuration.addAllowedOrigin("http://127.0.0.1:8080");
    configuration.addAllowedOrigin("http://127.0.0.1:8081");
    configuration.addAllowedHeader("*");
    configuration.addAllowedMethod("*");
    configuration.setAllowCredentials(true);
    configuration.addExposedHeader("accessToken");
    configuration.addExposedHeader("refreshToken");
    configuration.addExposedHeader("Content-Disposition");

    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new Pbkdf2PasswordEncoder();
  }
}
