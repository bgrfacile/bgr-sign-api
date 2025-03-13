package com.bgrfacile.bgrsignapi.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import io.jsonwebtoken.security.WeakKeyException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationInMs}")
    private int jwtExpirationInMs;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(userDetails.getUsername())  // On stocke l'email au lieu de l'ID
                .claim("roles", userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    // Valide le token JWT
    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(authToken);
            return true;
        } catch (WeakKeyException ex) {
            // La clé utilisée est trop faible
            System.err.println("Clé JWT trop faible : " + ex.getMessage());
        } catch (SignatureException ex) {
            // La signature JWT est invalide
            System.err.println("Signature JWT invalide : " + ex.getMessage());
        } catch (MalformedJwtException ex) {
            // Le token JWT est mal formé
            System.err.println("Token JWT mal formé : " + ex.getMessage());
        } catch (ExpiredJwtException ex) {
            // Le token JWT a expiré
            System.err.println("Token JWT expiré : " + ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            // Le token JWT n'est pas supporté
            System.err.println("Token JWT non supporté : " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            // Le token JWT est vide ou invalide
            System.err.println("Token JWT invalide : " + ex.getMessage());
        }
        return false;
    }

    public int getExpiryDuration() {
        return jwtExpirationInMs;
    }

}