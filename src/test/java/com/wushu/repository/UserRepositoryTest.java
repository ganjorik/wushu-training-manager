package com.wushu.repository;

import com.wushu.entity.Role;
import com.wushu.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByUsername_shouldReturnUser_whenUserExists() {
        // given
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("testpassword");
        user.setRole(Role.values()[0]);

        userRepository.save(user);

        // when
        Optional<User> result =
                userRepository.findByUsername("testuser");

        // then
        assertTrue(result.isPresent());

        User foundUser = result.get();

        assertEquals(user.getId(), foundUser.getId());
        assertEquals("testuser", foundUser.getUsername());
        assertEquals("testpassword", foundUser.getPassword());
        assertEquals(user.getRole(), foundUser.getRole());
    }

    @Test
    void findByUsername_shouldReturnEmpty_whenUserDoesNotExist() {
        // given
        String username = "unknown";

        // when
        Optional<User> result =
                userRepository.findByUsername(username);

        // then
        assertTrue(result.isEmpty());
    }
}