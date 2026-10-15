package com.attendance.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Lightweight API-key gate for all {@code /api/**} endpoints.
 * <p>
 * When {@code app.api-key} is blank (the default during local dev) every
 * request is allowed through.  In production, set the {@code API_KEY}
 * environment variable to a strong random value; the frontend must then
 * send the same value in the {@code X-API-Key} request header.
 * <p>
 * CORS preflight ({@code OPTIONS}) requests are always passed through so
 * the browser handshake is never blocked.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 10) // run after CORS filter
public class ApiKeyFilter extends OncePerRequestFilter {

    private static final String HEADER = "X-API-Key";

    @Value("${app.api-key:}")
    private String apiKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        // No key configured → wide-open (local dev)
        if (apiKey == null || apiKey.isBlank()) {
            chain.doFilter(request, response);
            return;
        }

        // Allow CORS preflight through
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            chain.doFilter(request, response);
            return;
        }

        // Only guard /api and /api/** paths; actuator + static assets stay open
        String uri = request.getRequestURI();
        if (!uri.equals("/api") && !uri.startsWith("/api/")) {
            chain.doFilter(request, response);
            return;
        }

        String provided = request.getHeader(HEADER);
        if (apiKey.equals(provided)) {
            chain.doFilter(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Invalid or missing API key\"}");
        }
    }
}
