package com.cms.service;

import java.time.LocalDateTime;
import java.util.*;

import jakarta.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cms.dto.request.DeleteMultiDTO;
import com.cms.dto.request.RoleRequest;
import com.cms.dto.response.ErrorListResponse;
import com.cms.dto.response.RoleResponse;
import com.cms.entity.Permission;
import com.cms.entity.Role;
import com.cms.entity.User;
import com.cms.repository.PermissionRepository;
import com.cms.repository.RoleRepository;
import com.cms.repository.UserRolesRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
    RoleRepository roleRepository;
    UserRolesRepository userRolesRepository;
    PermissionRepository permissionsRepository;

    public Page<RoleResponse> searchRoles(
            String searchString,
            String roleName,
            String roleCode,
            String roleDescription,
            Boolean status,
            int page,
            int size) {

        Pageable pageable = PageRequest.of(page, size);

        if (searchString != null && !searchString.trim().isEmpty()) {
            roleName = null;
            roleCode = null;
            roleDescription = null;
        }
        return roleRepository.searchRoles(searchString, roleName, roleCode, roleDescription, status, pageable);
    }

    @Transactional
    public String save(RoleRequest roleDTO, User token) {
        if (roleDTO.getRoleCode() != null && roleRepository.existsByRoleCodeIgnoreCase(roleDTO.getRoleCode())) {
            return "error.SameRoleCode";
        }
        Role role = new Role();
        role.setRoleCode(roleDTO.getRoleCode());
        role.setRoleName(roleDTO.getRoleName());
        role.setRoleDescription(roleDTO.getRoleDescription());
        role.setIsActive(true);
        role.setIsDeleted(false);
        role.setCreateAt(LocalDateTime.now());
        role.setCreateBy(token.getId());
        role.setUpdateAt(LocalDateTime.now());
        role.setUpdateBy(token.getId());
        roleRepository.save(role);
        return "";
    }

    @Transactional
    public String update(RoleRequest roleDTO, User token) {
        Role role = roleRepository.findByRoleIdIncluideDeleted(roleDTO.getId());
        if (roleDTO.getRoleCode() != null
                && roleRepository.existsByRoleCodeIgnoreCaseNotId(roleDTO.getRoleCode(), role.getId())) {
            return "error.SameRoleCode";
        }
        role.setRoleCode(roleDTO.getRoleCode());
        role.setRoleName(roleDTO.getRoleName());
        role.setRoleDescription(roleDTO.getRoleDescription());
        role.setUpdateAt(LocalDateTime.now());
        role.setUpdateBy(token.getId());
        roleRepository.save(role);
        return "";
    }

    @Transactional
    public String delete(UUID id, User userDetails) {
        Role role = roleRepository.findByRoleIdIncluideDeleted(id);
        if (userRolesRepository.existsUserByRoleId(role.getId())) {
            return "error.RoleAlreadyUseOnUser";
        }
        role.setIsActive(!role.getIsDeleted());
        role.setDeletedBy(userDetails.getId());
        roleRepository.save(role);
        return "";
    }

    @Transactional
    public String lockRole(UUID id, User userDetails) {
        Role role = roleRepository.findByRoleIdIncluideDeleted(id);
        role.setIsActive(!role.getIsActive());
        role.setUpdateBy(userDetails.getId());
        roleRepository.save(role);
        return "";
    }

    @Transactional
    public String deleteMuti(List<DeleteMultiDTO> ids, User userDetails) {

        for (DeleteMultiDTO id : ids) {
            Role role = roleRepository.findByRoleIdIncluideDeleted(id.getId());
            if (userRolesRepository.existsUserByRoleId(role.getId())) {
                return "error.RoleAlreadyUseOnUser";
            }
            role.setIsActive(!role.getIsDeleted());
            role.setDeletedBy(userDetails.getId());
            roleRepository.save(role);
        }
        return "";
    }

    public List<Role> getAllRole() {
        return roleRepository.getAllRole();
    }

    public List<Permission> getALlPermisstion() {
        return permissionsRepository.findAllByIsDeletedFalseOrderByPositionAsc();
    }

    @Transactional
    public ErrorListResponse checkDeleteMulti(List<DeleteMultiDTO> ids) {
        ErrorListResponse response = new ErrorListResponse();
        List<ErrorListResponse.ErrorResponse> lstObject = new ArrayList<>();
        for (DeleteMultiDTO id : ids) {
            ErrorListResponse.ErrorResponse object = new ErrorListResponse.ErrorResponse();
            object.setId(id.getId());
            Role role = roleRepository.findByRoleIdIncluideDeleted(id.getId());
            if (userRolesRepository.existsUserByRoleId(role.getId())) {
                object.setErrorMessage("error.RoleAlreadyUseOnUser");
                object.setCode(role.getRoleCode());
                object.setName(role.getRoleName());
            } else {
                object.setCode(role.getRoleCode());
                object.setName(role.getRoleName());
            }
            lstObject.add(object);
        }
        response.setErrors(lstObject);
        response.setTotal(ids.size());
        long countNum = response.getErrors().stream()
                .filter(item -> item.getErrorMessage() != null)
                .count();
        response.setHasError(countNum != 0);
        if (Boolean.FALSE.equals(response.getHasError())) {
            return null;
        }
        return response;
    }
}
