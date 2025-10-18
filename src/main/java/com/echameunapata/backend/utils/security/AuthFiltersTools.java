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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthFiltersTools extends OncePerRequestFilter {

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
        String tokenHeader  = request.getHeader("Authorization");
        String token=null;
        String email = null;

        if(tokenHeader != null && tokenHeader.length() > 7 && tokenHeader.startsWith("Bearer: ") ){
            token = tokenHeader.substring(7);

            try {
                email = jwtTools.getEmailFromToken(token);
            }catch (IllegalArgumentException e){

                System.out.println("Unable to get JWT token");

            }catch (ExpiredJwtException e) {

                System.out.println("JWT token has expired");

            }catch (MalformedJwtException e){

                System.out.println("JWT token is malformed");
            }
        }else {
            System.out.println("Bearer string not foud");
        }
        if (token != null && email != null && SecurityContextHolder.getContext().getAuthentication() == null){
            User user = userService.findUserByEmail(email);
            if (user != null){
                Boolean tokenValidity = authService.isTokenValid(user, token);
                if(tokenValidity){
                    UsernamePasswordAuthenticationToken authToken =  new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }else{
                System.out.println("User not found");
            }
        }
        filterChain.doFilter(request, response);
    }
}
