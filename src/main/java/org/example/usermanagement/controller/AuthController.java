package org.example.usermanagement.controller;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.example.usermanagement.dto.*;
import org.example.usermanagement.entity.UserEntity;
import org.example.usermanagement.mapper.ResponseUserMapper;
import org.example.usermanagement.mapper.UserMapper;
import org.example.usermanagement.security.JwtUtil;
import org.example.usermanagement.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.util.Base64;

@RestController
@RequestMapping("/auth")
public class AuthController {


    private final AuthenticationService authenticationService;
    private final JwtUtil jwtUtil;


    public AuthController(AuthenticationService authenticationService, JwtUtil jwtUtil) {
        this.authenticationService = authenticationService;
        this.jwtUtil = jwtUtil;

    }

    @PostMapping("/register")
    public UserResponse registerUser(@RequestBody RegisterRequest registerRequest) {


        UserEntity user = authenticationService.register(
                registerRequest.getUsername(),
                registerRequest.getPassword(),
                registerRequest.getEmail()
        );

        //return UserMapper.INSTANCE.toDto(user);
        return ResponseUserMapper.INSTANCE.toDto(user);
    }

    @PostMapping("/login")
    public String loginUser(@RequestBody LoginRequest loginRequest) {
        return authenticationService.login(loginRequest.getUsername(), loginRequest.getPassword());
    }

    @PostMapping("/genkey")
    public String genkey() {
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256); // Generates a secure key for HS256
        String base64Key = Base64.getEncoder().encodeToString(key.getEncoded());
        return "Base64-encoded key: " + base64Key;


    }

    @PostMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody UpdateRequest updateRequest, HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        System.out.println("Token extracted: " + token); // Log the token for debugging

        String username = jwtUtil.extractUsername(token);
        System.out.println("Username extracted from token: " + username); // Log the username

        if (jwtUtil.validateToken(token, username)) {
            UserEntity updatedUser = authenticationService.updateUser(username, updateRequest);
            return ResponseEntity.ok(ResponseUserMapper.INSTANCE.toDto(updatedUser));
        } else {
            System.out.println("Token validation failed");
            return ResponseEntity.status(403).body("Invalid or expired token");
        }
    }

    // Helper method to extract token from Authorization header
    private String extractTokenFromRequest(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7); // Remove "Bearer " prefix
        }
        return null;
    }

}