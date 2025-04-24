package com.openclassrooms.chatop.configuration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtils {
    @Value("${app.secret-key}")
    private String secretKey;

    @Value("${app.expiration-time}")
    private long expirationTime;

    /**
     * Generates a JWT token for the given user.
     */
    public String generateToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, email);
    }

    /**
     * Creates a JWT token based on the provided claims and subject.
     *
     * @param claims a map containing the claims to be included in the token
     * @param subject the subject for which the token is being created
     * @return a String representing the generated JWT token
     */
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Retrieves the signing key used for HMAC algorithm in JWT creation and validation.
     *
     * @return a {@code Key} object initialized with the secret key for signing JWTs.
     */
    private Key getSignKey() {
        byte[] keyBytes = secretKey.getBytes();
        return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    /**
     * Validates a given JWT token by checking its email claim against the provided user details
     * and ensuring the token has not expired.
     *
     * @param token the JWT token string to validate
     * @param userDetails the user details to match with the email extracted from the token
     * @return a Boolean indicating whether the token is valid
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        String email = extractEmail(token);
        return (email.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Checks if the provided JWT token is expired.
     *
     * @param token the JWT token to check for expiration
     * @return true if the token is expired, false otherwise
     */
    private boolean isTokenExpired(String token) {
        return extractExpirationDate(token).before(new Date());
    }

    /**
     * Extracts the email (subject) from the provided JWT token.
     *
     * @param token the JWT token from which the email (subject) will be extracted
     * @return the email (subject) contained within the provided token
     */
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts the expiration date from the provided JWT token.
     *
     * @param token the JWT token from which the expiration date will be extracted
     * @return a {@code Date} representing the expiration date of the provided token
     */
    private Date extractExpirationDate(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts a specific claim from a JWT token by applying a provided claims resolver function.
     *
     * @param token the JWT token from which the claim is to be extracted
     * @param claimsResolver a function that resolves the desired claim from the parsed {@code Claims} object
     * @param <T> the type of the claim to be extracted
     * @return the extracted claim of type {@code T}
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extracts all claims from the provided JWT token.
     *
     * @param token the JWT token from which the claims will be extracted
     * @return a {@code Claims} object containing all the claims present in the token
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSignKey())
                .parseClaimsJws(token)
                .getBody();
    }
}
