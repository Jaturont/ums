package org.example.usermanagement.service;

import org.example.usermanagement.dto.UpdateRequest;
import org.example.usermanagement.dto.UserDTO;
import org.example.usermanagement.entity.UserEntity;
import org.example.usermanagement.exception.UserAlreadyExistsException;
import org.example.usermanagement.repository.UserRepository;
import org.example.usermanagement.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    // Constructor-based injection
    public AuthenticationService(AuthenticationManager authenticationManager, UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    public String login(String username, String password) {
        //String encryptedPassword = passwordEncoder.encode(password);
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        UserEntity user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));


        return jwtUtil.generateToken(username);
    }

    // Register method to create a new user
    public UserEntity register(String username, String password, String email) {

        List<UserEntity> users = userRepository.findAllByUsername(username);

        if (users.size() > 0) {
            throw new UserAlreadyExistsException("Username is already taken.");
        }


        UserEntity user = new UserEntity();
        user.setUsername(username);

        String encryptedPassword = passwordEncoder.encode(password);
        user.setPassword(encryptedPassword);

        user.setEmail(email);
        return userRepository.save(user);
    }

    public UserEntity updateUser(String username, UpdateRequest userDTO) {
        // Find the user by username
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Update user details (e.g., email)
        user.setEmail(userDTO.getEmail());

        // Optionally, if the password is being updated, you might want to hash it
        if (userDTO.getPassword() != null) {
            // Assuming passwordEncoder is autowired and available
            String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
            user.setPassword(encodedPassword);
        }

        return userRepository.save(user); // Save updated user details
    }
}


