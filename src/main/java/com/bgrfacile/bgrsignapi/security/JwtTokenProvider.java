package com.bgrfacile.bgrsignapi.security;

import com.bgrfacile.bgrsignapi.model.CustomUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import io.jsonwebtoken.security.WeakKeyException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationInMs}")
    private int jwtExpirationInMs;

    // Génère une instance de clé à partir de la chaîne jwtSecret.
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    // Génère un token à partir des informations de l'utilisateur authentifié
    public String generateToken(Authentication authentication) {
        CustomUserDetails userPrincipal = (CustomUserDetails) authentication.getPrincipal();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(Long.toString(userPrincipal.getId()))
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    // Extrait l'ID de l'utilisateur contenu dans le token
    public Long getUserIdFromJWT(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return Long.parseLong(claims.getSubject());
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
}