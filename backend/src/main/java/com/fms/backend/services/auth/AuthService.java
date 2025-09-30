package com.fms.backend.services.auth;

import com.fms.backend.dto.auth.LoginRequestDTO;
import com.fms.backend.dto.auth.LoginResponseDTO;
import com.fms.backend.exceptions.auth.CustomAuthenticationException;
import com.fms.backend.models.Role;
import com.fms.backend.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TokenGeneratorService tokenGeneratorService;

    public AuthService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, TokenGeneratorService tokenGeneratorService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenGeneratorService = tokenGeneratorService;
    }

    private final Logger logger = LoggerFactory.getLogger(AuthService.class.getName());

    @Transactional
    public ResponseEntity<LoginResponseDTO> login(LoginRequestDTO loginRequest) {

        logger.info("LoginService: login.");

        var userOptional = userRepository.findByEmail(loginRequest.email());

        if (userOptional.isEmpty() || !userOptional.get().isLoginCorrect(loginRequest, passwordEncoder)) {
            throw new CustomAuthenticationException("Invalid email or password.");
        }

        var scopes = userOptional.get().getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.joining(" "));

        var accessToken = tokenGeneratorService.generateAccessToken(userOptional.get().getId(), userOptional.get().getEmail(), scopes);

        return ResponseEntity.ok(new LoginResponseDTO(
                accessToken.accessToken(),
                accessToken.expirationDate()));
    }
}
