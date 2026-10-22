/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.springframework.context.annotation.Bean
 *  org.springframework.context.annotation.Configuration
 *  org.springframework.security.authentication.AuthenticationManager
 *  org.springframework.security.authentication.AuthenticationProvider
 *  org.springframework.security.authentication.dao.DaoAuthenticationProvider
 *  org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
 *  org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
 *  org.springframework.security.config.annotation.web.builders.HttpSecurity
 *  org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
 *  org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer$AuthorizedUrl
 *  org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer
 *  org.springframework.security.core.userdetails.UserDetailsService
 *  org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
 *  org.springframework.security.crypto.password.PasswordEncoder
 *  org.springframework.security.web.SecurityFilterChain
 *  org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter$ReferrerPolicy
 */
package com.healthsure.config;

import com.healthsure.security.CustomUserDetailsService;
import lombok.Generated;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final CustomUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService((UserDetailsService)this.userDetailsService);
        provider.setPasswordEncoder(this.passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authenticationProvider((AuthenticationProvider)this.authenticationProvider()).csrf(csrf -> csrf.ignoringRequestMatchers(new String[]{"/api/**"})).authorizeHttpRequests(auth -> ((AuthorizeHttpRequestsConfigurer.AuthorizedUrl)((AuthorizeHttpRequestsConfigurer.AuthorizedUrl)((AuthorizeHttpRequestsConfigurer.AuthorizedUrl)((AuthorizeHttpRequestsConfigurer.AuthorizedUrl)((AuthorizeHttpRequestsConfigurer.AuthorizedUrl)((AuthorizeHttpRequestsConfigurer.AuthorizedUrl)auth.requestMatchers(new String[]{"/", "/login", "/register", "/logout", "/css/**", "/js/**", "/actuator/health", "/actuator/health/**", "/actuator/info", "/error", "/access-denied", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html"})).permitAll().requestMatchers(new String[]{"/admin/**"})).hasRole("ADMIN").requestMatchers(new String[]{"/reviewer/**"})).hasAnyRole(new String[]{"REVIEWER", "ADMIN"}).requestMatchers(new String[]{"/policyholder/**"})).hasAnyRole(new String[]{"POLICYHOLDER", "ADMIN"}).requestMatchers(new String[]{"/claims/**", "/documents/**", "/profile/**", "/api/**"})).authenticated().anyRequest()).authenticated()).formLogin(form -> ((FormLoginConfigurer)((FormLoginConfigurer)((FormLoginConfigurer)form.loginPage("/login").loginProcessingUrl("/login")).defaultSuccessUrl("/dashboard", true)).failureUrl("/login?error=true")).usernameParameter("username").passwordParameter("password").permitAll()).logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/login?logout=true").invalidateHttpSession(true).deleteCookies(new String[]{"JSESSIONID"}).permitAll()).sessionManagement(session -> session.maximumSessions(1).maxSessionsPreventsLogin(false)).exceptionHandling(ex -> ex.accessDeniedPage("/access-denied")).headers(headers -> headers.frameOptions(fo -> fo.deny()).contentTypeOptions(cto -> {}).referrerPolicy(rp -> rp.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN)));
        return (SecurityFilterChain)http.build();
    }

    @Generated
    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
}
