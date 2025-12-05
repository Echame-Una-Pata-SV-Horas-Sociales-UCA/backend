package com.echameunapata.backend.utils.security;

import com.echameunapata.backend.domain.models.User;
import com.echameunapata.backend.services.contract.IAuthService;
import com.echameunapata.backend.services.contract.IUserService;
import com.echameunapata.backend.utils.token.JwtTools;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class AuthFiltersTools extends OncePerRequestFilter {

    // Logger para reemplazar System.out.println
    private static final Logger logger = LoggerFactory.getLogger(AuthFiltersTools.class);

    private final IAuthService authService;
    private final JwtTools jwtTools;
    private final IUserService userService;

    public AuthFiltersTools(IAuthService authService, JwtTools jwtTools, IUserService userService) {
        this.authService = authService;
        this.jwtTools = jwtTools;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String tokenHeader = request.getHeader("Authorization");
        String token = null;
        String email = null;

        // CORRECCIÓN: Estándar "Bearer " (con espacio, longitud 7)
        if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
            token = tokenHeader.substring(7);

            try {
                email = jwtTools.getEmailFromToken(token);
            } catch (IllegalArgumentException e) {
                logger.warn("Unable to get JWT token");
            } catch (ExpiredJwtException e) {
                logger.warn("JWT token has expired");
            } catch (MalformedJwtException e) {
                logger.warn("JWT token is malformed");
            } catch (Exception e) {
                logger.error("JWT validation error: {}", e.getMessage());
            }
        } else {
            // No es un error crítico, puede ser una ruta pública
            logger.debug("Bearer string not found or header is missing");
        }

        if (token != null && email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            User user = userService.findUserByEmail(email);

            if (user != null) {
                Boolean tokenValidity = authService.isTokenValid(user, token);

                if (tokenValidity) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            user, null, user.getAuthorities());

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            } else {
                logger.warn("User not found for token email: {}", email);
            }
        }

        filterChain.doFilter(request, response);
    }
}