package com.bgrfacile.bgrsignapi.config;

import com.bgrfacile.bgrsignapi.security.JwtAuthenticationFilter;
import com.bgrfacile.bgrsignapi.utils.CustomAccessDeniedHandler;
import com.bgrfacile.bgrsignapi.utils.CustomAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Autowired
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(withDefaults()) // Active la configuration CORS
                .csrf(AbstractHttpConfigurer::disable) // Désactive CSRF (nécessaire pour les API stateless)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(customAuthenticationEntryPoint) // Gestion des erreurs d'authentification
                        .accessDeniedHandler(customAccessDeniedHandler) // Gestion des erreurs d'accès refusé
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // API stateless
                )
                .authorizeHttpRequests(authorize ->
                        authorize
                                // Autoriser l'accès à Swagger UI et aux endpoints publics
                                .requestMatchers(
                                        "/v3/api-docs/**",
                                        "/swagger-ui/**",
                                        "/swagger-ui.html",
                                        "/api-docs.yaml",
                                        "/api-docs.json",
                                        "/api/auth/**"
                                ).permitAll()
                                // Restreindre l'accès aux endpoints protégés
                                .requestMatchers("/api/teacher/**").hasAuthority("teacher")
                                .requestMatchers("/api/admin/**").hasAuthority("admin")
                                .anyRequest().authenticated() // Toutes les autres requêtes nécessitent une authentification
                );

        // Ajouter le filtre JWT avant le filtre d'authentification par défaut
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*")); // Autorise toutes les origines (à adapter pour la prod)
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Méthodes autorisées
        configuration.setAllowedHeaders(List.of("*")); // En-têtes autorisés
        configuration.setAllowCredentials(true); // Autoriser les credentials (cookies, etc.)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Appliquer la configuration à tous les chemins
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Utiliser BCrypt pour le hachage des mots de passe
    }
}