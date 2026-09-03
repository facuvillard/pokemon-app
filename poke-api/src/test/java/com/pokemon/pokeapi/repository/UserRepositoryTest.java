package com.pokemon.pokeapi.repository;

import com.pokemon.pokeapi.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByUsername_UserExists_ReturnsUser() {
        User user = User.builder()
                .username("ash")
                .email("ash@pallet.town")
                .password("pikachu")
                .build();
        userRepository.save(user);

        Optional<User> result = userRepository.findByUsername("ash");

        assertTrue(result.isPresent());
        assertEquals("ash", result.get().getUsername());
    }

    @Test
    void existsByUsername_UserExists_ReturnsTrue() {
        User user = User.builder()
                .username("misty")
                .email("misty@cerulean.city")
                .password("staryu")
                .build();
        userRepository.save(user);

        assertTrue(userRepository.existsByUsername("misty"));
    }

    @Test
    void existsByEmail_UserExists_ReturnsTrue() {
        User user = User.builder()
                .username("brock")
                .email("brock@pewter.city")
                .password("onix")
                .build();
        userRepository.save(user);

        assertTrue(userRepository.existsByEmail("brock@pewter.city"));
    }

    @Test
    void findByUsername_UserDoesNotExist_ReturnsEmpty() {
        Optional<User> result = userRepository.findByUsername("gary");
        assertFalse(result.isPresent());
    }
}
