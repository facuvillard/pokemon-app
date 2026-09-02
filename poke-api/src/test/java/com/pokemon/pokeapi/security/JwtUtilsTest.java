package com.pokemon.pokeapi.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

public class JwtUtilsTest {

    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();
        ReflectionTestUtils.setField(jwtUtils, "secret", "a-long-random-secret-key-for-jwt-token-generation-min-256-bits-long-enough");
        ReflectionTestUtils.setField(jwtUtils, "expiration", 3600000L);
    }

    @Test
    void testGenerateToken_ReturnsValidToken() {
        String token = jwtUtils.generateToken("user");
        assertNotNull(token);
    }

    @Test
    void testGetUsernameFromToken_ReturnsCorrectUsername() {
        String token = jwtUtils.generateToken("user");
        assertEquals("user", jwtUtils.getUsernameFromToken(token));
    }

    @Test
    void testValidateToken_ValidToken_ReturnsTrue() {
        String token = jwtUtils.generateToken("user");
        assertTrue(jwtUtils.validateToken(token));
    }

    @Test
    void testValidateToken_ExpiredToken_ReturnsFalse() throws InterruptedException {
        ReflectionTestUtils.setField(jwtUtils, "expiration", 1L);
        String token = jwtUtils.generateToken("user");
        Thread.sleep(10);
        assertFalse(jwtUtils.validateToken(token));
    }

    @Test
    void testValidateToken_InvalidToken_ReturnsFalse() {
        assertFalse(jwtUtils.validateToken("invalid.token.string"));
    }
}
