package com.auth.service;

import com.auth.entity.RefreshToken;
import com.auth.repository.RefreshTokenRepository;
import com.auth.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RefreshTokenService {
	RefreshTokenRepository refreshTokenRepository;
	UserRepository userRepository;

	public RefreshToken createRefreshToken(String username) {
		if (userRepository.findByUsername(username).isEmpty()) {
			return null;
		}
		LocalDateTime expiryDate = LocalDateTime.now().plusDays(7);
		RefreshToken refreshToken = new RefreshToken();
		Optional<RefreshToken> optionalRefreshToken=refreshTokenRepository.findByUsername(username);
		if (optionalRefreshToken.isPresent()) {
			refreshToken=optionalRefreshToken.get();
		}
		refreshToken.setUsername(username);
		refreshToken.setRefreshToken(UUID.randomUUID().toString());
		refreshToken.setExpireTime(expiryDate);
		return refreshTokenRepository.save(refreshToken);
	}

	public Optional<RefreshToken> findByRefreshToken(String refreshToken) {
		return refreshTokenRepository.findByRefreshToken(refreshToken);
	}

	public RefreshToken verifyExpiration(RefreshToken token) {
		if (token.getExpireTime().isBefore(LocalDateTime.now())) {
			refreshTokenRepository.delete(token);
			throw new RuntimeException("Refresh token was expired. Please make a new signin request");
		}
		return token;
	}

}
