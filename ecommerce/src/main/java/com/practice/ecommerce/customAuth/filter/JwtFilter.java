package com.practice.ecommerce.customAuth.filter;

import java.io.IOException;
import java.util.List;

import com.practice.ecommerce.customAuth.authentication.CustomAuthentication;
import com.practice.ecommerce.customAuth.manager.CustomAuthenticationManager;
import com.practice.ecommerce.service.JwtService;
import com.practice.ecommerce.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

@Component
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    private final CustomAuthenticationManager customAuthenticationManager;

    @Override // checked
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String userName = null;
        String requestURL = request.getRequestURI();
        System.out.println("Request Received");

        List<String> publicEndpoints = List.of("/public/", "/user/customer", "/user/request-otp", "/admin/add/admin", "/admin/add/product");
        if (publicEndpoints.stream().anyMatch(requestURL::startsWith)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (authHeader != null && authHeader.startsWith("Bearer ")) { // all auth tokens start with "Bearer" like "Bearer ytfut6iitvtivi..."
            token = authHeader.substring(7); // removing "Bearer " to get token only
            userName = jwtService.extractUserName(token);
        }

        if (userName != null) {
            if (requestURL.contains("admin") && !jwtService.validateAdminRole(token, userService.getUserByIdentifier(userName))) {
                response.sendError(SC_UNAUTHORIZED, "UNAUTHORIZED Access to ADMIN API");
                return;
            }
            CustomAuthentication customAuthentication = new CustomAuthentication(false, token, userName);
            Authentication authenticatedObject = customAuthenticationManager.authenticate(customAuthentication);
            SecurityContextHolder.getContext().setAuthentication(authenticatedObject);
        }

        filterChain.doFilter(request, response);
    }
}
