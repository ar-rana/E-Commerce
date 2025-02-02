package com.practice.ecommerce.customAuth.provider;

import com.practice.ecommerce.customAuth.authentication.CustomAuthentication;
import com.practice.ecommerce.model.User;
import com.practice.ecommerce.service.JwtService;
import com.practice.ecommerce.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        CustomAuthentication customAuthentication = (CustomAuthentication) authentication;
        String authToken = customAuthentication.getToken(); // the key will get filled by the filter

        if (jwtService.validateToken(authToken, userService.getUserByIdentifier(customAuthentication.getName()))) {
            log.info("User: {} Authorized", customAuthentication.getName());
            return new CustomAuthentication(true, authToken, customAuthentication.getName());
        } else {
            throw new BadCredentialsException("Unauthorized user, Invalid Token");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return CustomAuthentication.class.equals(authentication);
    }
}
