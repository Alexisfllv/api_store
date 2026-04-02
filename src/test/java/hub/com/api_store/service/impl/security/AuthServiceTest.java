package hub.com.api_store.service.impl.security;

import hub.com.api_store.dto.security.AuthResponse;
import hub.com.api_store.dto.security.LoginRequest;
import hub.com.api_store.dto.security.RegisterRequest;
import hub.com.api_store.entity.security.Role;
import hub.com.api_store.entity.security.TokenBlacklist;
import hub.com.api_store.entity.security.User;
import hub.com.api_store.repo.TokenBlacklistRepo;
import hub.com.api_store.repo.UserRepo;
import hub.com.api_store.security.JwtUtil;
import hub.com.api_store.service.impl.security.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepo userRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenBlacklistRepo tokenBlacklistRepo;

    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("register - success")
    void register_success() {
        // Arrange
        RegisterRequest request = new RegisterRequest("alexis", "alexis123", Role.ROLE_ADMIN);

        when(passwordEncoder.encode("alexis123")).thenReturn("$2a$10$encodedPassword");

        // Act
        AuthResponse result = authService.register(request);

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertNull(result.token()),
                () -> assertEquals("User Registered successfully", result.message())
        );

        // Verify
        InOrder inOrder = inOrder(passwordEncoder, userRepo);
        inOrder.verify(passwordEncoder, times(1)).encode("alexis123");
        inOrder.verify(userRepo, times(1)).save(any(User.class));
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @DisplayName("login - success")
    void login_success() {
        // Arrange
        LoginRequest request = new LoginRequest("alexis", "alexis123");
        User user = new User(1L, "alexis", "$2a$10$encodedPassword", Role.ROLE_ADMIN);

        when(userRepo.findByUsername("alexis")).thenReturn(Optional.of(user));
        when(jwtUtil.generateToken(user)).thenReturn("eyJhbGci...");

        // Act
        AuthResponse result = authService.login(request);

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertNotNull(result.token()),
                () -> assertEquals("eyJhbGci...", result.token()),
                () -> assertEquals("Login successful", result.message())
        );

        // Verify
        InOrder inOrder = inOrder(authenticationManager, userRepo, jwtUtil);
        inOrder.verify(authenticationManager, times(1)).authenticate(any());
        inOrder.verify(userRepo, times(1)).findByUsername("alexis");
        inOrder.verify(jwtUtil, times(1)).generateToken(user);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @DisplayName("login - user not found")
    void login_userNotFound() {
        // Arrange
        LoginRequest request = new LoginRequest("noexiste", "password123");

        when(userRepo.findByUsername("noexiste")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class,
                () -> authService.login(request));

        // Verify
        verify(userRepo, times(1)).findByUsername("noexiste");
    }

    @Test
    @DisplayName("logout - success")
    void logout_success() {
        // Arrange
        String token = "eyJhbGci...";

        when(tokenBlacklistRepo.save(any(TokenBlacklist.class)))
                .thenReturn(new TokenBlacklist());

        // Act
        AuthResponse result = authService.logout(token);

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertNull(result.token()),
                () -> assertEquals("Logout successful", result.message())
        );

        // Verify
        verify(tokenBlacklistRepo, times(1)).save(any(TokenBlacklist.class));
        verifyNoMoreInteractions(tokenBlacklistRepo);
    }
}