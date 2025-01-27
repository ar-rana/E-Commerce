package com.practice.ecommerce.repository;

import java.util.Optional;

import com.practice.ecommerce.defaultModels.DefaultModels;
import com.practice.ecommerce.model.Enums.UserType;
import com.practice.ecommerce.model.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@Rollback // transaction is rolled back after the test completes
public class UserRepoTest {

    @Autowired
    private UserRepository userRepository;

    private static final String username = DefaultModels.username;
    private static final UserType type = UserType.customer;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User(username, type);
    }

    @Test
    @Transactional // Changes made during the test are not persisted beyond the scope of the test, and it is optional as @DataJpaTest does it automatically
    public void testSaveUser() {
        User savedUser = userRepository.save(user);

        assertNotNull(savedUser);
        assertNotNull(savedUser.getId());
        assertEquals(savedUser.getIdentifier(), username);
        assertEquals(savedUser.getType(),type);
    }

    @Test
    public void testFindByIdentifier() {
        userRepository.save(user);

        Optional<User> identifier = userRepository.findByIdentifier(username);

        assertNotNull(identifier);
        assertEquals(identifier.get().getIdentifier(), username);
        assertEquals(identifier.get().getType(), type);
    }
}
