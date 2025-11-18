package com.codegym.config;

import com.codegym.service.CustomUserDetailsService;
import com.codegym.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getServletPath();

        // ✅ BỎ QUA FILTER CHO PUBLIC API
        if (path.startsWith("/api/users/login")
                || path.startsWith("/api/users/register")
                || path.startsWith("/api/users/exists")
                || path.startsWith("/api/users/check-email")
                || path.startsWith("/api/users/check-cccd")
                || path.startsWith("/api/users/service")
                || path.startsWith("/ws")
                || path.startsWith("/api/messages")
                || path.startsWith("/api/users/profiles")) {

            filterChain.doFilter(request, response);
            return;
        }

        // 🔥 Nếu không có token thì bỏ qua filter
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Lấy token
        String token = authHeader.substring(7);
        String username = jwtService.extractUsername(token);

        // Validate token
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtService.isTokenValid(token, username)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}