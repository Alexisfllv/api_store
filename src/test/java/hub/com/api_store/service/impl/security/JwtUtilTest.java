package hub.com.api_store.service.impl.security;

import hub.com.api_store.entity.security.Role;
import hub.com.api_store.entity.security.User;
import hub.com.api_store.security.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtUtilTest {

    private JwtUtil jwtUtil;

    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        userDetails = new User(
                1L,
                "alexis",
                "password123",
                Role.ROLE_ADMIN
        );
    }

    @Test
    @DisplayName("generateToken - success")
    void generateToken_success() {
        // Act
        String token = jwtUtil.generateToken(userDetails);

        // Assert
        assertAll(
                () -> assertNotNull(token),
                () -> assertFalse(token.isEmpty()),
                () -> assertEquals(3, token.split("\\.").length) // header.payload.signature
        );
    }

    @Test
    @DisplayName("extractUsername - success")
    void extractUsername_success() {
        // Arrange
        String token = jwtUtil.generateToken(userDetails);

        // Act
        String username = jwtUtil.extractUsername(token);

        // Assert
        assertEquals("alexis", username);
    }

    @Test
    @DisplayName("isTokenValid - success")
    void isTokenValid_success() {
        // Arrange
        String token = jwtUtil.generateToken(userDetails);

        // Act
        boolean valid = jwtUtil.isTokenValid(token, userDetails);

        // Assert
        assertTrue(valid);
    }

    @Test
    @DisplayName("isTokenValid - expired token")
    void isTokenValid_expiredToken() throws Exception {
        // Arrange - generar token ya expirado
        String expiredToken = Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 25)) // hace 25h
                .expiration(new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 1)) // expiró hace 1h
                .signWith(Keys.hmacShaKeyFor(
                        "miClaveSuperSecretaEs1234567890Ab".getBytes(StandardCharsets.UTF_8)))
                .compact();

        // Act & Assert
        assertThrows(ExpiredJwtException.class,
                () -> jwtUtil.isTokenValid(expiredToken, userDetails));
    }
}