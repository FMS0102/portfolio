package com.fms.backend.services.auth;

import com.fms.backend.dto.auth.TokenRefreshRequestDTO;
import com.fms.backend.exceptions.auth.TokenRefreshException;
import com.fms.backend.exceptions.validation.ResourceNotFoundException;
import com.fms.backend.models.RefreshToken;
import com.fms.backend.models.User;
import com.fms.backend.repositories.RefreshTokenRepository;
import com.fms.backend.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Value("${jwt.refresh.token-expiration-minutes}")
    private Long refreshTokenExpirationMinutes;

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final TokenGeneratorService tokenGeneratorService;
    private final BCryptPasswordEncoder encoder;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository, TokenGeneratorService tokenGeneratorService, BCryptPasswordEncoder encoder) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
        this.tokenGeneratorService = tokenGeneratorService;
        this.encoder = encoder;
    }

    private final Logger logger = LoggerFactory.getLogger(RefreshTokenService.class.getName());

    public TokenRefreshRequestDTO createRefreshToken(UUID userId) {

        logger.info("RefreshTokenService: create refresh token");

        String tokenId = UUID.randomUUID().toString();
        String rawSecret = tokenGeneratorService.generateRefreshToken();
        String compositeToken = tokenId + "." + rawSecret;

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        RefreshToken savedRefreshToken = saveRefreshToken(user, tokenId, rawSecret);

        return new TokenRefreshRequestDTO(compositeToken,  savedRefreshToken.getExpiryDate(), Optional.empty());
    }

    private RefreshToken saveRefreshToken(User user, String tokenId, String rawSecret) {
        String hashed = encoder.encode(rawSecret);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(tokenId);
        refreshToken.setTokenHash(hashed);
        refreshToken.setExpiryDate(Instant.now().plus(refreshTokenExpirationMinutes, ChronoUnit.MINUTES));

        return refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    public TokenRefreshRequestDTO revokeAndReplace(String compositeToken, Instant expirationDate) {

        if(expirationDate.isBefore(Instant.now())){
            throw new TokenRefreshException("Expired refresh token.");
        }

        String[] parts = compositeToken.split("\\.");
        if (parts.length != 2) {
            throw new TokenRefreshException("Invalid refresh token format.");
        }

        String tokenId = parts[0];
        String rawSecret = parts[1];

        var refreshTokenDb = refreshTokenRepository.findByToken(tokenId).orElseThrow(
                () -> new TokenRefreshException("Refresh token not found or invalid.")
        );

        var user = refreshTokenDb.getUser();

        if (!encoder.matches(rawSecret, refreshTokenDb.getTokenHash())) {
            throw new TokenRefreshException("Invalid refresh token.");
        }

        refreshTokenRepository.deleteAllByUserId(user.getId());

        String newTokenId = UUID.randomUUID().toString();
        String newRawSecret = tokenGeneratorService.generateRefreshToken();
        String newCompositeToken = tokenId + "." + newRawSecret;

        RefreshToken savedRefreshToken = saveRefreshToken(user, newTokenId, newRawSecret);

        return new TokenRefreshRequestDTO(newCompositeToken,  savedRefreshToken.getExpiryDate(), Optional.of(user));
    }

}
