package com.example.rentalrequest.config;

import com.example.rentalrequest.service.CustomUserDetailsService;
import com.example.rentalrequest.util.JwtUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        String jwt = null;
        String username = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            try {
                username = jwtUtil.extractUsername(jwt);
                System.out.println("🔍 Extracted username from JWT: " + username);
            } catch (Exception e) {
                System.out.println("❌ JWT parsing failed: " + e.getMessage());
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            System.out.println("🔍 UserDetails authorities: " + userDetails.getAuthorities());

            if (jwtUtil.validateToken(jwt, userDetails)) {
                // CRITICAL FIX: Use database authorities directly - they already have ROLE_ prefix
                Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

                System.out.println("🔍 Final authorities being set: " + authorities);

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);

                System.out.println("✅ Authentication set in SecurityContext");
                System.out.println("✅ Authenticated: " + username + ", roles: " + authorities);

                // Verify the authentication is in context
                System.out.println("🔍 SecurityContext auth: " + SecurityContextHolder.getContext().getAuthentication());
            } else {
                System.out.println("❌ JWT validation failed for user: " + username);
            }
        } else if (username == null) {
            System.out.println("🔍 No username extracted from JWT");
        } else {
            System.out.println("🔍 Authentication already exists in SecurityContext");
        }

        filterChain.doFilter(request, response);
    }
}





