package hub.com.api_store.service.impl.security;


// #9

import hub.com.api_store.dto.security.AuthResponse;
import hub.com.api_store.dto.security.LoginRequest;
import hub.com.api_store.dto.security.RegisterRequest;
import hub.com.api_store.dto.security.UserDTOResponse;
import hub.com.api_store.util.response.GenericResponse;
import hub.com.api_store.util.response.StatusApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<GenericResponse<AuthResponse>> register(@RequestBody RegisterRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(
                        new GenericResponse<>(StatusApi.CREATED, authService.register(request))
                );
    }

    @PostMapping("/login")
    public  ResponseEntity<GenericResponse<AuthResponse>> login(@RequestBody LoginRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(
                new GenericResponse<>(StatusApi.SUCCESS, authService.login(request)));
    }

    @GetMapping
    public ResponseEntity<GenericResponse<List<UserDTOResponse>>> findallGet(){
        List<UserDTOResponse> list = authService.listUser();
        return ResponseEntity.status(HttpStatus.OK).body(
                new GenericResponse<>(StatusApi.SUCCESS, list)
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<GenericResponse<AuthResponse>> logout(
            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7); // quita "Bearer "
        return ResponseEntity.status(HttpStatus.OK).body(
                new GenericResponse<>(StatusApi.SUCCESS, authService.logout(token))
        );
    }

}
