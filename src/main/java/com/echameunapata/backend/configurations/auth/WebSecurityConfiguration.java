package com.echameunapata.backend.configurations.auth;

import com.echameunapata.backend.domain.models.User;
import com.echameunapata.backend.services.contract.IUserService;
import com.echameunapata.backend.utils.security.AuthFiltersTools;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class WebSecurityConfiguration {

    @Value("${api.base-path}")
    private String apiBasePath;

    private final PasswordEncoder passwordEncoder;
    private final IUserService userService;
    private final AuthFiltersTools authFiltersTools;

    public WebSecurityConfiguration(PasswordEncoder passwordEncoder, IUserService userService, AuthFiltersTools authFiltersTools) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.authFiltersTools = authFiltersTools;
    }

    @Bean
    AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder.userDetailsService(email -> {
            User user = userService.findUserByEmail(email);
            if (user == null) {
                throw new UsernameNotFoundException("User not found with identifier: " + email);
            }
            return user;
        }).passwordEncoder(passwordEncoder);

        return authenticationManagerBuilder.build();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        // Habilitar CORS y deshabilitar CSRF (API Stateless)
        httpSecurity.cors(withDefaults()).csrf(AbstractHttpConfigurer::disable);

        httpSecurity.authorizeHttpRequests(auth -> {
            // 1. Endpoints de Autenticación
            auth.requestMatchers(
                    apiBasePath + "/auth/**",
                    apiBasePath +"/reports/create",
                    apiBasePath + "/evidence/create/**",
                    apiBasePath + "/animals/find-all"
                    ).permitAll();

//            // 2. Endpoints Públicos (Según SRS y diseño)
//            auth.requestMatchers(HttpMethod.GET, apiBasePath + "/public/**").permitAll();
//            // Catálogo público de animales
//            auth.requestMatchers(HttpMethod.GET, apiBasePath + "/animals/**").permitAll();
//            // Formulario público de adopción
//            auth.requestMatchers(HttpMethod.POST, apiBasePath + "/adoptions").permitAll();
//            // Formulario público de denuncias
//            auth.requestMatchers(HttpMethod.POST, apiBasePath + "/reports").permitAll();
//
//            // 3. Resto de endpoints requieren autenticación
            auth.requestMatchers(HttpMethod.GET, apiBasePath + "/animal/find-all").permitAll();
            auth.anyRequest().authenticated();
        });

        httpSecurity.sessionManagement(management ->
                management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        httpSecurity.exceptionHandling(handling ->
                handling.authenticationEntryPoint((req, res, ex) -> {
                    res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication failed: " + ex.getMessage());
                })
        );

        httpSecurity.addFilterBefore(authFiltersTools, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    /**
     * Configuración de CORS para permitir peticiones desde el Frontend
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // CORRECCIÓN PRINCIPAL:
        // En lugar de setAllowedOrigins(""), usamos setAllowedOriginPatterns("")
        // Esto permite credenciales Y acepta cualquier origen dinámicamente.
        configuration.setAllowedOriginPatterns(List.of("*"));

        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}