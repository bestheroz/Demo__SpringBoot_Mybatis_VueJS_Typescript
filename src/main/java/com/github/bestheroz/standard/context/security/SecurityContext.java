package com.github.bestheroz.standard.context.security;

import com.github.bestheroz.standard.common.authenticate.AuthenticationFilter;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;


@EnableWebSecurity
public class SecurityContext extends WebSecurityConfigurerAdapter {
    private static final String[] PUBLIC = new String[]{
            "/error", "/login", "/logout", "/register", "/api/registrations"};

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
                .exceptionHandling().accessDeniedHandler(this.accessDeniedHandler())
                .and()
                .authorizeRequests()
                .antMatchers(PUBLIC).permitAll()
                .requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterAt(this.authenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(this.apiRequestExceptionTranslationFilter(), ExceptionTranslationFilter.class)
                .formLogin()
                .loginPage("/login")
                .and()
                .logout()
                .logoutUrl("/api/me/logout")
                .logoutSuccessHandler(this.logoutSuccessHandler())
                .and()
                .csrf().disable();
    }

    @Override
    public void configure(final WebSecurity web) {
        web.ignoring().antMatchers("/static/**", "/js/**", "/css/**", "/images/**", "/favicon.ico");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationFilter authenticationFilter() throws Exception {
        final AuthenticationFilter authenticationFilter = new AuthenticationFilter();
        authenticationFilter.setAuthenticationSuccessHandler(this.authenticationSuccessHandler());
        authenticationFilter.setAuthenticationFailureHandler(this.authenticationFailureHandler());
        authenticationFilter.setAuthenticationManager(this.authenticationManagerBean());
        return authenticationFilter;
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new SimpleAuthenticationSuccessHandler();
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new SimpleAuthenticationFailureHandler();
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new SimpleLogoutSuccessHandler();
    }

    public AccessDeniedHandler accessDeniedHandler() {
        return new AccessDeniedHandlerImpl();
    }

    public ApiRequestAccessDeniedExceptionTranslationFilter apiRequestExceptionTranslationFilter() {
        return new ApiRequestAccessDeniedExceptionTranslationFilter();
    }
}
