package com.auth.service;

import java.util.List;
import java.util.UUID;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.auth.entity.*;
import com.auth.repository.RolePermissionsRepository;
import com.auth.repository.RoleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RolePermissionsService {
    private final RolePermissionsRepository rolePermissionsRepository;
    private final RoleRepository roleRepository;

    public List<Permission> getRolePermissions(UUID roleId) {

        return rolePermissionsRepository.findPermissionsByRoleId(roleId);
    }

    public List<Permission> getRolePermissionsHalf(UUID roleId) {

        return rolePermissionsRepository.findPermissionsHalfByRoleId(roleId);
    }

    @Transactional
    public String updatePermissionsForRole(UUID roleId, List<UUID> checkedKeys, List<UUID> checkedHalfKeys) {

        Role role = roleRepository.findByRoleIdIncluideDeleted(roleId);
        if (role == null) {
            return "error.DataChange";
        } else {
            // 1. Xóa tất cả quyền cũ của role
            rolePermissionsRepository.deleteByRoleId(roleId);

            // 2. Thêm các quyền mới bằng cách tạo entity với @EmbeddedId
            List<PermissionRole> newPermissions = checkedKeys.stream()
                    .map(permissionId -> new PermissionRole(new PermissionRoleId(permissionId, roleId, false)))
                    .toList();

            List<PermissionRole> newHalfPermissions = checkedHalfKeys.stream()
                    .map(permissionId -> new PermissionRole(new PermissionRoleId(permissionId, roleId, true)))
                    .toList();

            rolePermissionsRepository.saveAll(newPermissions);
            rolePermissionsRepository.saveAll(newHalfPermissions);
        }
        return "";
    }

    public List<Permission> getUserPermissions(User user) {

        return rolePermissionsRepository.findPermissionsByUserID(user.getId());
    }

    public List<Permission> getUserOriginDataPermissions(String code, User user) {

        return rolePermissionsRepository.findChildrenByParentCodeAndUserId(code, user.getId());
    }

    public List<String> getPermissionsCurrent(String code, User user) {

        return rolePermissionsRepository.getPermissionsCurrent(code, user.getId());
    }
}
