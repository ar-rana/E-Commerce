package com.practice.ecommerce.service;

import com.practice.ecommerce.defaultModels.DefaultModels;
import com.practice.ecommerce.model.Enums.UserType;
import com.practice.ecommerce.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class JwtServiceTest {

    @Autowired
    private JwtService jwtService;

    private String username;
    private String token;

    @BeforeEach
    public void setUp() {
        username = DefaultModels.username;
        token = jwtService.generateToken(username, UserType.admin);
    }

    @Test
    public void testExtractUsername() {
        String userIdentifier = jwtService.extractUserName(token);

        assertEquals(username, userIdentifier);
    }

    @Test
    public void testRoleValidation() {
        assertTrue(jwtService.validateAdminRole(token, new User(username, UserType.admin)));
    }

    @Test
    public void testValidation() {
        assertTrue(jwtService.validateToken(token, new User(username, UserType.admin)));
    }
}
