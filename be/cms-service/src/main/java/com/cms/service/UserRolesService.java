package com.cms.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cms.entity.*;
import com.cms.repository.*;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserRolesService {
    private final UserRolesRepository userRolesRepository;

    @Transactional
    public void saveUserRoles(UUID userId, List<UUID> roleIds) {
        userRolesRepository.deleteByUserId(userId);

        List<UserRoles> newPermissions = roleIds.stream()
                .map(roleId -> new UserRoles(new UserRolesId(userId, roleId)))
                .toList();

        userRolesRepository.saveAll(newPermissions);
    }
}
