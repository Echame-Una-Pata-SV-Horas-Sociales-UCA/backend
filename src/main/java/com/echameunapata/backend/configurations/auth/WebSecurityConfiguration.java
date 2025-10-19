package com.echameunapata.backend.configurations.auth;

import com.echameunapata.backend.domain.models.User;
import com.echameunapata.backend.services.contract.IUserService;
import com.echameunapata.backend.utils.security.AuthFiltersTools;
import com.echameunapata.backend.utils.token.JwtTools;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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

    /** Configuración del AuthenticationManager
     *
     * @param httpSecurity
     * @return
     * @throws Exception
     */
    @Bean
    AuthenticationManager authenticationManager(HttpSecurity httpSecurity)throws Exception{
        AuthenticationManagerBuilder authenticationManagerBuilder = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder.userDetailsService(email ->{
                    User user = userService.findUserByEmail(email);
                    if(user ==null){
                        throw new UsernameNotFoundException("User not found whit identifier");
                    }
                    return user;
                })
                .passwordEncoder(passwordEncoder);
        return authenticationManagerBuilder.build();
    }

    /** Configuración de seguridad HTTP
     *
     * @param httpSecurity
     * @return
     * @throws Exception
     */
    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.httpBasic(withDefaults()).csrf(csrf-> csrf.disable());

        httpSecurity.authorizeHttpRequests(auth->
                auth.requestMatchers(apiBasePath+"/auth/**").permitAll()
                        .anyRequest().authenticated()
        );
        httpSecurity.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        httpSecurity.exceptionHandling(handling -> handling.authenticationEntryPoint((req, res, ex)->{
            res.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Authentication failed: "+ ex.getMessage()
            );
        }));

        httpSecurity.addFilterBefore(authFiltersTools, UsernamePasswordAuthenticationFilter.class);
        return  httpSecurity.build();
    }
}
