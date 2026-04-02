package hub.com.api_store.service.impl.security;


import hub.com.api_store.dto.security.AuthResponse;
import hub.com.api_store.dto.security.LoginRequest;
import hub.com.api_store.dto.security.RegisterRequest;
import hub.com.api_store.dto.security.UserDTOResponse;
import hub.com.api_store.entity.security.TokenBlacklist;
import hub.com.api_store.entity.security.User;
import hub.com.api_store.repo.TokenBlacklistRepo;
import hub.com.api_store.repo.UserRepo;
import hub.com.api_store.security.JwtUtil;
import hub.com.api_store.util.response.GenericResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


// #8
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepo userRepo;
    private final TokenBlacklistRepo tokenBlacklistRepo;
    private final PasswordEncoder  passwordEncoder;
    private final JwtUtil  jwtUtil;
    private final AuthenticationManager  authenticationManager;

    // Register
    public AuthResponse register(RegisterRequest  registerRequest){
        User user = new User();
        user.setUsername(registerRequest.username());
        user.setPassword(passwordEncoder.encode(registerRequest.password()));
        user.setRole(registerRequest.role());
        userRepo.save(user);
        return new AuthResponse(null,"User Registered successfully");
    }

    // Login
    public AuthResponse login(LoginRequest loginRequest){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.username(),
                        loginRequest.password()
                )
        );
        User user = userRepo.findByUsername(loginRequest.username())
                .orElseThrow(() -> new UsernameNotFoundException("username not found"));
        return new AuthResponse(jwtUtil.generateToken(user),"Login successful");
    }


    // find all
    public List<UserDTOResponse> listUser(){
        List<User> users = userRepo.findAll();
        return users.stream().map(user -> new UserDTOResponse(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getRole()
        )).toList();
    }

    // Log out
    public AuthResponse logout(String token){
        TokenBlacklist tokenBlacklist = new TokenBlacklist();
        tokenBlacklist.setToken(token);
        tokenBlacklist.setExpiredAt(LocalDateTime.now());
        tokenBlacklistRepo.save(tokenBlacklist);
        return new AuthResponse(null,"Logout successful");
    }
}
