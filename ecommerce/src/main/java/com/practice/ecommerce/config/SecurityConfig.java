package com.practice.ecommerce.config;

import com.practice.ecommerce.customAuth.filter.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.addFilterAfter(jwtFilter, BasicAuthenticationFilter.class); // second parameter is to place it before which filter, null means before all

        http
                .cors(cors -> cors.configurationSource(new WebConfig().corsConfigurationSource()))
                .csrf(customizer -> customizer.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/public/***", "/user/customer", "/user/request-otp/*", "/admin/add/admin", "/admin/add/product").permitAll()
                        .anyRequest().authenticated()
                );


        return http.build();
    }
}
