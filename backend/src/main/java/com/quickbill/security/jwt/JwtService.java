package com.quickbill.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.util.function.Function;

import java.util.Date;

@Service
public class JwtService {

   @Value("${jwt.secret}")
private String secretKey;

@Value("${jwt.expiration}")
private long expirationTime;

    public String generateToken(String email, String role) {

        return Jwts.builder()

                // subject
                .setSubject(email)

                // custom claim
                .claim("role", role)

                // issued time
                .setIssuedAt(new Date())

                // expiration
                .setExpiration(
                        new Date(System.currentTimeMillis()
                                + expirationTime)
                )

                // signature
                .signWith(
                        SignatureAlgorithm.HS256,
                        secretKey
                )

                .compact();
    }
    public  Claims extractAllClaims(String token) {

    return Jwts.parser()

            .setSigningKey(secretKey)

            .parseClaimsJws(token)

            .getBody();
}
public String extractUsername(String token) {

    return extractAllClaims(token)
            .getSubject();
}
public Date extractExpiration(String token) {

    return extractAllClaims(token)
            .getExpiration();
}
private boolean isTokenExpired(String token) {

    return extractExpiration(token)
            .before(new Date());
}
public boolean validateToken(String token, String email) {

    final String extractedEmail = extractUsername(token);

    return extractedEmail.equals(email)
            && !isTokenExpired(token);
}
}