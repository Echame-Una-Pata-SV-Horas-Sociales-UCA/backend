package com.echameunapata.backend.utils.token;

import com.echameunapata.backend.domain.models.User;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class JwtTools {
    @Value("${jwt.secrety-key}")
    private String secretKey;
    @Value("${jwt.expiration-time}")
    private Long expiration;

    /** Genera un token JWT para un usuario dado
     *
     * @param user Usuario para el cual se genera el token
     * @return Token JWT
     */
    public String generateToken(User user){
        Map<String, Object>claims = new HashMap<>();
        claims.put("id", user.getId().toString());

        return Jwts.builder()
                .claims(claims)
                .subject(user.getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date( System.currentTimeMillis()+expiration*1000L))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    /** Verifica la validez de un token JWT
     *
     * @param token Token JWT a verificar
     * @return true si el token es válido, false en caso contrario
     */
    public boolean verifyTokens(String token){
        try{
            JwtParser parser = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                    .build();

            parser.parse(token);

            return true;
        }catch (Exception e){
            return false;
        }
    }

    /** Extrae el correo electrónico del usuario desde un token JWT
     *
     * @param token Token JWT
     * @return Correo electrónico del usuario o null si el token no es válido
     */
    public String getEmailFromToken(String token){
        try{
            JwtParser parser = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                    .build();

            return parser.parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
        }catch (Exception e){
            return null;
        }
    }
}
