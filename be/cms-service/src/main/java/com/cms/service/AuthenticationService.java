package com.cms.service;

import java.security.SecureRandom;
import java.util.*;

import com.cms.dto.request.ChangePasswordDTO;
import com.cms.dto.request.UserLoginDTO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.BeanUtils;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.cms.entity.User;
import com.cms.dto.response.AuthenticationResponse;
import com.cms.repository.RolePermissionsRepository;
import com.cms.repository.RoleRepository;
import com.cms.repository.UserRepository;
import jakarta.transaction.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository usersRepository;
    RoleRepository rolesRepository;
    RolePermissionsRepository permissionsRolesRepository;
    JwtService jwtService;
    BCryptPasswordEncoder passwordEncoder;
    RefreshTokenService refreshTokenService;

    public AuthenticationResponse authenticate(UserLoginDTO user) {
        if (!checkUsernameAndPassword(user)) {
            throw new IllegalArgumentException("Tên đăng nhập hoặc mật khẩu không đúng!");
        }

        User u = usersRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Tên đăng nhập hoặc mật khẩu không đúng!"));

        User userOpt = new User();
        BeanUtils.copyProperties(u, userOpt);

        List<UUID> userRoles =  rolesRepository.findLstIdByUserId(userOpt.getId());

        String token = jwtService.generateToken(userOpt);
        String refreshToken = refreshTokenService.createRefreshToken(user.getUsername()).getRefreshToken();

        return new AuthenticationResponse(
                token,
                refreshToken,
                userOpt.getUsername(),
                userRoles,
                userOpt.getFullName(),
                userOpt.getEmail(),
                userOpt.getPhone(),
                userOpt.getIsActive(),
                userOpt.getId()
        );
    }

    public boolean checkPassword(UserLoginDTO user) {
        User userOpt = usersRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Người dùng không tồn tại!"));
        return passwordEncoder.matches(user.getPassword(), userOpt.getPassword());
    }

    public boolean checkUsernameAndPassword(UserLoginDTO user) {
        User userOpt = usersRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Tên đăng nhập hoặc mật khẩu không đúng!"));
        return passwordEncoder.matches(user.getPassword(), userOpt.getPassword());
    }

    @Transactional
    public void changePassword(ChangePasswordDTO changePasswordDTO, User currentUser) {
        User currUser = usersRepository.findByUsername(currentUser.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng hiện tại"));

        if (!passwordEncoder.matches(changePasswordDTO.getStaffPassword(), currUser.getPassword())) {
            throw new IllegalArgumentException("Mật khẩu người dùng hiện tại không đúng");
        }

        User targetUser = usersRepository.getUserByUserId(changePasswordDTO.getUserId());
        if (targetUser == null) {
            throw new IllegalArgumentException("Không tìm thấy người dùng với ID: " + changePasswordDTO.getUserId());
        }

        if (changePasswordDTO.getUserNewPassword() == null ||
                changePasswordDTO.getUserNewPassword().length() < 8) {
            throw new IllegalArgumentException("Mật khẩu mới phải có ít nhất 8 ký tự");
        }

        targetUser.setPassword(passwordEncoder.encode(changePasswordDTO.getUserNewPassword()));
        usersRepository.save(targetUser);
    }

}
