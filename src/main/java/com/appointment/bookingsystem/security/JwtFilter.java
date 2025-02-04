package com.appointment.bookingsystem.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.Claims;
import java.io.IOException;
import java.util.Collections;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    @Lazy
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        System.out.println("🚀 JwtFilter triggered for request: " + request.getServletPath());

        String authorizationHeader = request.getHeader("Authorization");
        System.out.println("🔍 Authorization Header: " + authorizationHeader);

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            System.out.println("❌ No valid Bearer token found. Skipping JWT validation.");
            chain.doFilter(request, response);
            return;
        }

        String token = authorizationHeader.substring(7);
        System.out.println("🔑 Extracted Token: " + token);

        String username;
        Claims claims;
        try {
            username = jwtUtil.extractUsername(token);
            claims = jwtUtil.extractAllClaims(token); // ✅ Extract all claims, including role
            System.out.println("✅ Extracted Username: " + username);
        } catch (Exception e) {
            System.out.println("❌ Invalid JWT Token: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Token");
            return; // ✅ Stop request from proceeding
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // ✅ Extract role from token
            String role = claims.get("role", String.class);
            System.out.println("✅ Extracted Role from Token: " + role);

            // ✅ Assign role as authority
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null,
                            Collections.singletonList(new SimpleGrantedAuthority(role)));

            SecurityContextHolder.getContext().setAuthentication(authToken);
            System.out.println("🎉 User authenticated successfully!");
            System.out.println("🔹 Assigned Authorities: " + SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        }

        chain.doFilter(request, response);
    }
}
