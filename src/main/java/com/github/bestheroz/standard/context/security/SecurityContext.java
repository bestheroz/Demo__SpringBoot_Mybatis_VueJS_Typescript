package com.github.bestheroz.standard.context.security;

import com.github.bestheroz.standard.common.authenticate.JwtAuthenticationFilter;
import com.github.bestheroz.standard.common.authenticate.JwtTokenProvider;
import com.github.bestheroz.standard.common.security.AccessDeniedHandlerImpl;
import com.github.bestheroz.standard.common.security.ApiRequestAccessDeniedExceptionTranslationFilter;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.annotation.Resource;


@EnableWebSecurity
public class SecurityContext extends WebSecurityConfigurerAdapter {
    private static final String[] PUBLIC = new String[]{
            "/error", "/api/codes/**", "/api/variables/**", "/api/auth/login", "/"};

    @Resource private JwtTokenProvider jwtTokenProvider;

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.httpBasic().disable()
                .exceptionHandling().accessDeniedHandler(this.accessDeniedHandler())
                .and()
                .authorizeRequests()
                .antMatchers(PUBLIC).permitAll()
                .requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(this.jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class)
//                .addFilterAt(this.authenticationFilter(), UsernamePasswordAuthenticationFilter.class)
//                .addFilterAfter(this.apiRequestExceptionTranslationFilter(), ExceptionTranslationFilter.class)
//                .formLogin()
//                .loginPage("/#/login")
//                .and()
//                .logout()
//                .logoutUrl("/api/auth/logout")
//                .logoutSuccessHandler(this.logoutSuccessHandler())
//                .and()
                .csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().cors();
    }

    @Override
    public void configure(final WebSecurity web) {
        web.ignoring().antMatchers("/static/**", "/js/**", "/css/**", "/images/**", "/favicon.ico");
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOrigin("http://localhost:8081");
        configuration.addAllowedOrigin("http://localhost:8080");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new Pbkdf2PasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

//    @Bean
//    public AuthenticationFilter authenticationFilter() throws Exception {
//        final AuthenticationFilter authenticationFilter = new AuthenticationFilter();
//        authenticationFilter.setAuthenticationSuccessHandler(this.authenticationSuccessHandler());
//        authenticationFilter.setAuthenticationFailureHandler(this.authenticationFailureHandler());
//        authenticationFilter.setAuthenticationManager(this.authenticationManagerBean());
//        return authenticationFilter;
//    }
//
//    @Bean
//    public AuthenticationSuccessHandler authenticationSuccessHandler() {
//        return new SimpleAuthenticationSuccessHandler();
//    }
//
//    @Bean
//    public AuthenticationFailureHandler authenticationFailureHandler() {
//        return new SimpleAuthenticationFailureHandler();
//    }
//
//    @Bean
//    public LogoutSuccessHandler logoutSuccessHandler() {
//        return new SimpleLogoutSuccessHandler();
//    }

    public AccessDeniedHandler accessDeniedHandler() {
        return new AccessDeniedHandlerImpl();
    }

    public ApiRequestAccessDeniedExceptionTranslationFilter apiRequestExceptionTranslationFilter() {
        return new ApiRequestAccessDeniedExceptionTranslationFilter();
    }
}
