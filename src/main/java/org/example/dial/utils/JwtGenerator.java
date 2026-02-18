package org.example.dial.utils;

import java.util.Date;

import javax.crypto.SecretKey;

import org.example.dial.config.JwtConfig;
import org.example.dial.dto.UserJwt;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtGenerator {
    private final JwtConfig config;
    private final SecretKey key;

    private Claims parseToken(String token) {
        return Jwts
            .parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }
    
    public JwtGenerator(JwtConfig config) {
        this.config = config; 
        this.key = Keys.hmacShaKeyFor(config.getSecret().getBytes());
    }

    public boolean isValidToken(String token) {
        try {
            this.parseToken(token);
            return true;
        } catch (Exception _) {
            return false;
        }
    }
    
    public String generateToken(UserJwt meta) {
        return Jwts.builder()
            .subject(meta.getId().toString())
            .claim("name", meta.getName())
            .claim("role", meta.getRole().name())
            .issuer(config.getIssuer())
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + config.getExpiration()))
            .signWith(this.key)
            .compact();
    }

    public String parseId(String token) {
        if (!this.isValidToken(token)) return null;
        return (String) this.parseToken(token).getSubject();
    }

    public String parseName(String token) {
        if (!this.isValidToken(token)) return null;
        return (String) this.parseToken(token).get("name");
    } 

    public String parseRole(String token) {
        if (!this.isValidToken(token)) return null;
        return (String) this.parseToken(token).get("role"); 
    }
}