package com.cms.controller;

import java.util.Optional;

import com.cms.dto.response.TokenResponse;
import com.cms.entity.RefreshToken;
import com.cms.entity.User;
import com.cms.repository.UserRepository;
import com.cms.response.ResponseAPI;
import com.cms.service.JwtService;
import com.cms.service.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cms.dto.request.*;
import com.cms.dto.response.AuthenticationResponse;
import com.cms.service.AuthenticationService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;
    UserRepository userRepository;
    RefreshTokenService refreshTokenService;
    JwtService jwtService;

    @PostMapping("/logout")
    public ResponseEntity<ResponseAPI> logout(HttpServletRequest request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User userDetails = (User) authentication.getPrincipal();
            if (authentication instanceof AnonymousAuthenticationToken || !authentication.isAuthenticated() || authentication.getPrincipal() == null) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseAPI(null, "Không có token hoặc phiên đăng nhập hợp lệ", 401));
            }


            if (!(authentication.getPrincipal() instanceof User)) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseAPI(null, "Thông tin người dùng không hợp lệ", 401));
            }


            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseAPI(null, "Thành công", 200));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseAPI(null, "Lỗi hệ thống: " + e.getMessage(), 500));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseAPI> loginNoReturnToken(@RequestBody UserLoginDTO user) {
        try {
            AuthenticationResponse response = authenticationService.authenticate(user);

            User dbUser = userRepository.findByUsername(user.getUsername())
                    .orElseThrow(() -> new IllegalArgumentException("Tên đăng nhập hoặc mật khẩu không đúng!"));

            return ResponseEntity.ok(new ResponseAPI(response, "Đăng nhập thành công", 200));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseAPI(null, e.getMessage(), 400));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseAPI(null, "Lỗi hệ thống: " + e.getMessage(), 500));
        }
    }
    @PostMapping("/refreshToken")
    public ResponseEntity<ResponseAPI> refreshToken(@RequestBody RefreshTokenReqDTO refreshTokenReqDTO) {
        String refreshToken = refreshTokenReqDTO.getRefreshToken();
        Optional<RefreshToken> optionalRefreshToken = refreshTokenService.findByRefreshToken(refreshToken);
        if (optionalRefreshToken.isPresent()) {
            try {
                RefreshToken verifiedToken = refreshTokenService.verifyExpiration(optionalRefreshToken.get());
                String username = verifiedToken.getUsername();
                User userOpt = userRepository.findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
                String token = jwtService.generateToken(userOpt);
                return ResponseEntity.ok(new ResponseAPI(new TokenResponse(token), "success", 200));
            } catch (Exception e) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(new ResponseAPI(null, "Refresh token is expired", 401));
            }
        } else {
            return ResponseEntity.ok(new ResponseAPI(null, "token not found", 401));
        }
    }

    @PostMapping("/changePassword")
    public ResponseEntity<ResponseAPI> changePassword(@Valid @RequestBody ChangePasswordDTO changePasswordDTO,
                                                      HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        try {
            // Thực hiện đổi mật khẩu
            authenticationService.changePassword(changePasswordDTO, currentUser);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseAPI(null, "Thành công", 200));
        } catch (IllegalArgumentException e) {
            // Bắt lỗi từ service và trả về message cụ thể
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseAPI(null, e.getMessage(), 400));
        } catch (Exception e) {
            // Bắt các lỗi khác (nếu có)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseAPI(null, e.getMessage(), 500));
        }
    }
}
