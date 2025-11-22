package com.codegym.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/api/users/register", "/api/users/login").permitAll()
                        .requestMatchers("/api/users/exists/**").permitAll()
                        .requestMatchers("/api/users/check-email/**").permitAll()
                        .requestMatchers("/api/users/check-phone/**").permitAll()
                        .requestMatchers("/api/users/profiles/**").permitAll()
                        .requestMatchers("/api/users/service/**").permitAll()
                        .requestMatchers("/api/users/check-cccd/**").permitAll()

                        // Test email endpoints
                        .requestMatchers("/api/ccdv/hire-sessions/test-email").permitAll()
                        .requestMatchers("/api/ccdv/hire-sessions/test-email-html").permitAll()
                        // Hire sessions endpoints
                        .requestMatchers("/api/ccdv/hire-sessions/**").permitAll()
                        .requestMatchers("/ws/**").permitAll()
                        .requestMatchers("/api/messages/**").permitAll()

                        .requestMatchers("/api/sepay/webhook").permitAll()


                        .requestMatchers("/api/ccdv-profiles/create").hasRole("SERVICE_PROVIDER")

                        .requestMatchers("/api/revenue/**").permitAll()
                        .requestMatchers("/api/users/top-ccdv-view").permitAll()
                        .requestMatchers("/api/home/top-ccdv").permitAll()

                        .requestMatchers("/api/ccdv-profiles/user/**").hasRole("SERVICE_PROVIDER")
                        .requestMatchers("/api/ccdv-profiles/update/**").hasRole("SERVICE_PROVIDER")
                        .requestMatchers("/api/ccdv-profiles/toggle-status/**").hasRole("SERVICE_PROVIDER")
                        .requestMatchers("/api/wallet/topup").hasAnyRole("U?????????????????????SERVICE_PROVIDER")
                        .requestMatchers("/api/hire/create").hasRole("USER")

                        // admin
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")



                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5173"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}