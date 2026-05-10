package com.quickbill.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter
        extends OncePerRequestFilter {

    private static final Logger log =
            LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)

            throws ServletException, IOException {

        log.info("JWT filter executed for request: {}",
                request.getRequestURI());

        // read authorization header
        String authHeader = request.getHeader("Authorization");

        log.info("Authorization Header: {}", authHeader);

        // check bearer token existence
        if (authHeader == null
                || !authHeader.startsWith("Bearer ")) {

            log.warn("JWT token missing or invalid format");

            filterChain.doFilter(request, response);
            return;
        }

        // extract token
        String token = authHeader.substring(7);

        log.info("JWT token extracted successfully");

        try {

            // extract email from token
            String email = jwtService.extractUsername(token);

            log.info("JWT belongs to user: {}", email);

            // validate token
            boolean isValid = jwtService.validateToken(token, email);

            if (isValid) {

                log.info("JWT token validated successfully");

                // extract role from token
                String role = jwtService
                        .extractAllClaims(token)
                        .get("role", String.class);

                log.info("User role extracted from token: {}", role);

                // create authentication object
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                email,
                                null,
                                List.of(
                                        new SimpleGrantedAuthority(
                                                "ROLE_" + role
                                        )
                                )
                        );

                // set authentication in security context
                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);

                log.info("Security context updated for user: {}",
                        email);

            } else {

                log.warn("JWT token validation failed");
            }

        } catch (Exception ex) {

            log.error("JWT processing failed: {}",
                    ex.getMessage());
        }

        // continue request flow
        filterChain.doFilter(request, response);
    }
}