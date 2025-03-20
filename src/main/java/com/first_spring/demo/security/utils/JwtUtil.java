package com.first_spring.demo.security.utils;

import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

/**
 * This class is responsible for generating and validating JWT tokens.
 */
@Component
public class JwtUtil {

    private final SecretKey secretKey;

    /**
     * Load the secret key from application.properties and decode it.
     */
    public JwtUtil(@Value("${jwt.secret}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
    }

    /**
     * Generate a JWT token for a given username.
     * 
     * @param username The username to generate the token for.
     * @return The generated JWT token.
     */
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours validity
                .signWith(secretKey, SignatureAlgorithm.HS256) // ✅ Use secure key
                .compact();
    }

    /**
     * Extract the username from a JWT token.
     * 
     * @param token The JWT token to extract the username from.
     * @return The username of the JWT token.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extract the expiration date from a JWT token.
     * 
     * @param token The JWT token to extract the expiration date from.
     * @return The expiration date of the JWT token.
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extract a claim from a JWT token.
     * 
     * @param token          The JWT token to extract the claim from.
     * @param claimsResolver The function to extract the claim from the JWT token.
     * @return The claim of the JWT token.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extract all claims from a JWT token.
     * 
     * @param token The JWT token to extract the claims from.
     * @return The claims of the JWT token.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey) // ✅ Load key dynamically
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Check if a JWT token is valid.
     * 
     * @param token    The JWT token to check if it is valid.
     * @param username The username of the JWT token.
     * @return True if the JWT token is valid, false otherwise.
     */
    public boolean isTokenValid(String token, String username) {
        return (extractUsername(token).equals(username) && !isTokenExpired(token));
    }

    /**
     * Check if a JWT token is expired.
     * 
     * @param token The JWT token to check if it is expired.
     * @return True if the JWT token is expired, false otherwise.
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}
