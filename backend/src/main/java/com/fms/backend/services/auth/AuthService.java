package com.fms.backend.services.auth;

import com.fms.backend.dto.auth.AuthServiceResponseDTO;
import com.fms.backend.dto.auth.LoginRequestDTO;
import com.fms.backend.dto.auth.LoginResponseDTO;
import com.fms.backend.dto.auth.TokenRefreshResponseDTO;
import com.fms.backend.exceptions.auth.CustomAuthenticationException;
import com.fms.backend.exceptions.auth.TokenRefreshException;
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
    private final RefreshTokenService refreshTokenService;

    public AuthService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, TokenGeneratorService tokenGeneratorService, RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenGeneratorService = tokenGeneratorService;
        this.refreshTokenService = refreshTokenService;
    }

    private final Logger logger = LoggerFactory.getLogger(AuthService.class.getName());

    @Transactional
    public AuthServiceResponseDTO login(LoginRequestDTO loginRequest) {

        logger.info("LoginService: login.");

        var user = userRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new CustomAuthenticationException("Invalid email or password."));

        if (!user.isLoginCorrect(loginRequest, passwordEncoder)) {
            throw new CustomAuthenticationException("Invalid email or password.");
        }

        var scopes = user.getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.joining(" "));

        var accessToken = tokenGeneratorService.generateAccessToken(user.getId(), user.getEmail(), scopes);
        var refreshToken = refreshTokenService.createRefreshToken(user.getId());

        return new AuthServiceResponseDTO(
                accessToken.accessToken(),
                accessToken.expirationDate(),
                refreshToken.refreshToken(),
                refreshToken.expirationDate());
    }

    @Transactional
    public AuthServiceResponseDTO updateRefreshToken(TokenRefreshResponseDTO refreshTokenResponse) {

        logger.info("LoginService: update refresh token.");

        var newRefreshToken = refreshTokenService.revokeAndReplace(refreshTokenResponse.refreshToken(), refreshTokenResponse.expirationDate());
        var user = newRefreshToken.user().orElseThrow(
                () -> new TokenRefreshException("User not found for this token.")
        );

        var scopes = user.getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.joining(" "));

        var accessToken = tokenGeneratorService.generateAccessToken(user.getId(), user.getEmail(), scopes);

        return new AuthServiceResponseDTO(
                accessToken.accessToken(),
                accessToken.expirationDate(),
                newRefreshToken.refreshToken(),
                newRefreshToken.expirationDate()
        );
    }
}
