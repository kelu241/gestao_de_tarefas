package com.luciano.gestao.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    public String extractRole(String token) {
        return (String) io.jsonwebtoken.Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .get("role");
    }
    public String generateTokenWithRole(@NotNull String username, @NotNull String role) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenValidity))
                .claim("role", role)
                .signWith(key)
                .compact();
    }
    // Chave secreta fixa (use uma string forte em produção)
    private static final String SECRET = "minha-chave-secreta-supersegura-123456";
        public static final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());
    private final long accessTokenValidity = 1000 * 60 * 15; // 15 minutos
    private final long refreshTokenValidity = 1000 * 60 * 60 * 24 * 7; // 7 dias

    public String generateToken(@NotNull String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenValidity))
                .signWith(key)
                .compact();
    }

    public String generateRefreshToken(@NotNull String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenValidity))
                .claim("type", "refresh")
                .signWith(key)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public boolean isRefreshToken(String token) {
        Object type = Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().get("type");
        return "refresh".equals(type);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
