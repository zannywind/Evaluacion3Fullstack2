package cl.duoc.biblioteca.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {
    
    @Value ("${jwt.secret}")
    private String llaveSecreta;

    @Value ("${jwt.expiration}")
    private long tiempoExpiracion;

    private SecretKey obtenerLlaveSecreta() {
        return Keys.hmacShaKeyFor(llaveSecreta.getBytes(StandardCharsets.UTF_8));
    }

    public String generarToken(String email, String rol) {
        return Jwts.builder()
                .subject(email)
                .claim("rol", rol)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + tiempoExpiracion))
                .signWith(obtenerLlaveSecreta())
                .compact();
    }

    public Claims obtenerClaims(String token) {
        return Jwts.parser()
                .verifyWith(obtenerLlaveSecreta())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String obtenerEmail(String token) {
        return obtenerClaims(token).getSubject();
    }

    public String obtenerRol(String token) {
        return obtenerClaims(token).get("rol", String.class);
    }

    public boolean esValidoToken(String token) {
        try {
            Claims claims = obtenerClaims(token);
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
