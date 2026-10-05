package com.uv.api_expedientes.Auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uv.api_expedientes.Auth.dtos.AuthResponse;
import com.uv.api_expedientes.Auth.dtos.LoginDto;
import com.uv.api_expedientes.Auth.dtos.RefreshTokenRequest;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginDto loginDto) {
        return ResponseEntity.ok(authService.login(loginDto));
    }
    // Response cookie not implemented yet

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(HttpServletRequest requestToken,
            @RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.RefreshToken(requestToken, request));
    }

}