package com.auth.service;

import com.auth.entity.User;
import com.auth.repository.RolePermissionsRepository;
import com.auth.repository.RoleRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {
	@Value("${jwt.secretKey}")
	private String secretKey;
    private final RolePermissionsRepository rolePermissionsRepository;
	private final RoleRepository roleRepository;
	private final long timeOutMS = TimeUnit.MINUTES.toMillis(30);

	public String generateToken(User user) {
	    List<String> role = roleRepository.findLstRoleNameByUserId(user.getId());
		long now = System.currentTimeMillis();
		long idleExp = now + timeOutMS;
		long absoluteExp = now + 12 * 60 * 60 * 1000; // 12 giờ

	    JwtBuilder builder = Jwts.builder()
	        .subject(user.getUsername())
	        .claim("role", role)
	        .claim("user_id", user.getId())
			.claim("isAdmin", user.getIsAdmin() == 1 ? "true" : "false")
			.claim("permissions", rolePermissionsRepository.findPermissionsByRoleIds(user.getId()))
			.claim("loginAt", now)
			.claim("lastActive", now)
			.claim("absoluteExp", absoluteExp)
			.issuedAt(new Date(now))
			.expiration(new Date(idleExp))
			.signWith(getSignKey());

	    return builder.compact();
	}

	private SecretKey getSignKey() {
		byte[] secretBytes = Decoders.BASE64URL.decode(secretKey);
		return Keys.hmacShaKeyFor(secretBytes);
	}

	public Claims extractAllClaims(String token) {
		return Jwts.parser().verifyWith(getSignKey()).build().parseSignedClaims(token).getPayload();

	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public boolean isValidateToken(String token, UserDetails user) {
		String username = extractUsername(token);
		return (username.equals(user.getUsername())) && !isTokenExpired(token);
	}

	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}


}
