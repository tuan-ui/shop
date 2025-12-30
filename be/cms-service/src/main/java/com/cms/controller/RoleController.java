package com.cms.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.cms.constant.Constants;
import com.cms.dto.request.DeleteMultiDTO;
import com.cms.dto.request.RolePermissionRequest;
import com.cms.dto.request.RoleRequest;
import com.cms.dto.response.ErrorListResponse;
import com.cms.dto.response.RoleResponse;
import com.cms.entity.Permission;
import com.cms.entity.Role;
import com.cms.entity.User;
import com.cms.response.ResponseAPI;
import com.cms.service.RolePermissionsService;
import com.cms.service.RoleService;

import io.micrometer.common.util.StringUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RoleController {
    private final RoleService roleService;
    private final RolePermissionsService rolePermissionsService;

    @PostMapping("/searchRoles")
    public ResponseEntity<ResponseAPI> searchRoles(@RequestBody RoleRequest request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User userDetails = (User) authentication.getPrincipal();

            Page<RoleResponse> result = roleService.searchRoles(
                    request.getSearchString(),
                    request.getRoleName(),
                    request.getRoleCode(),
                    request.getRoleDescription(),
                    request.getStatus(),
                    request.getPage(),
                    request.getSize());

            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseAPI(result, Constants.messageResponse.SUCCESS, 200));
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseAPI(null, Constants.messageResponse.ERROR + e.getMessage(), 500));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ResponseAPI> save(@RequestBody @Valid RoleRequest roleDTO) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User userDetails = (User) authentication.getPrincipal();

            String message = roleService.save(roleDTO, userDetails);
            if (StringUtils.isNotBlank(message))
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseAPI(null, message, 400));
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseAPI(null, Constants.messageResponse.SUCCESS, 200));
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseAPI(null, Constants.messageResponse.ERROR + e.getMessage(), 500));
        }
    }

    @PostMapping("/update")
    public ResponseEntity<ResponseAPI> update(@RequestBody RoleRequest roleDTO) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User userDetails = (User) authentication.getPrincipal();

            String message = roleService.update(roleDTO, userDetails);
            if (StringUtils.isNotBlank(message))
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseAPI(null, message, 400));
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseAPI(null, Constants.messageResponse.SUCCESS, 200));
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseAPI(null, Constants.messageResponse.ERROR + e.getMessage(), 500));
        }
    }

    @GetMapping("/delete")
    public ResponseEntity<ResponseAPI> delete(@RequestParam(value = "id") UUID id) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User userDetails = (User) authentication.getPrincipal();

            String message = roleService.delete(id, userDetails);

            if (StringUtils.isNotBlank(message)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseAPI(null, message, 400));
            }

            return ResponseEntity.ok(new ResponseAPI(null, "Xóa thành công", 200));
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseAPI(null, Constants.messageResponse.ERROR + e.getMessage(), 500));
        }
    }

    @GetMapping("/lock")
    public ResponseEntity<ResponseAPI> lock(@RequestParam(value = "id") UUID id) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User userDetails = (User) authentication.getPrincipal();
            String message = roleService.lockRole(id, userDetails);

            if (StringUtils.isNotBlank(message)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseAPI(null, message, 400));
            }

            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseAPI(null, Constants.messageResponse.SUCCESS, 200));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseAPI(null, Constants.messageResponse.ERROR + e.getMessage(), 500));
        }
    }

    @PostMapping("/deleteMuti")
    public ResponseEntity<ResponseAPI> deleteMuti(@RequestBody List<DeleteMultiDTO> ids) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User userDetails = (User) authentication.getPrincipal();

            String message = roleService.deleteMuti(ids, userDetails);

            if (StringUtils.isNotBlank(message)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseAPI(null, message, 400));
            }

            return ResponseEntity.ok(new ResponseAPI(null, Constants.messageResponse.SUCCESS, 200));
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseAPI(null, Constants.messageResponse.ERROR + e.getMessage(), 500));
        }
    }

    @GetMapping("/getAllRole")
    public ResponseAPI getAllRole() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User userDetails = (User) authentication.getPrincipal();
            List<Role> roles = roleService.getAllRole();
            return new ResponseAPI(roles, Constants.messageResponse.SUCCESS, 200);
        } catch (Exception e) {
            return new ResponseAPI(null, Constants.messageResponse.ERROR + e.getMessage(), 500);
        }
    }

    @GetMapping("/getALlPermisstion")
    public ResponseAPI getALlPermisstion() {
        try {
            List<Permission> response = roleService.getALlPermisstion();
            return new ResponseAPI(response, Constants.messageResponse.SUCCESS, 200);
        } catch (Exception e) {
            return new ResponseAPI(null, Constants.messageResponse.ERROR + e.getMessage(), 500);
        }
    }

    @GetMapping("/getRolePermisstion")
    public ResponseAPI getRolePermissions(@RequestParam UUID roleId) {
        List<Permission> response = rolePermissionsService.getRolePermissions(roleId);
        return new ResponseAPI(response, Constants.messageResponse.SUCCESS, 200);
    }

    @GetMapping("/getRolePermissionsHalf")
    public ResponseAPI getRolePermissionsHalf(@RequestParam UUID roleId) {
        List<Permission> response = rolePermissionsService.getRolePermissionsHalf(roleId);
        return new ResponseAPI(response, Constants.messageResponse.SUCCESS, 200);
    }

    // Gán lại danh sách permission cho role
    @PostMapping("/updateRolePermisstion")
    public ResponseAPI updateRolePermissions(@RequestBody RolePermissionRequest res) {

        try {
            String message = rolePermissionsService.updatePermissionsForRole(
                    res.getRoleId(), res.getCheckedKeys(), res.getCheckedHalfKeys());
            if (StringUtils.isNotBlank(message)) {
                return new ResponseAPI(null, message, 400);
            }
            return new ResponseAPI(null, Constants.messageResponse.SUCCESS, 200);
        } catch (Exception e) {
            return new ResponseAPI(null, Constants.messageResponse.ERROR + e.getMessage(), 500);
        }
    }

    @PostMapping("/checkDeleteMulti")
    public ResponseAPI checkDeleteMulti(@RequestBody List<DeleteMultiDTO> ids) {
        try {
            ErrorListResponse message = roleService.checkDeleteMulti(ids);
            return new ResponseAPI(message, Constants.messageResponse.SUCCESS, 200);
        } catch (Exception e) {
            return new ResponseAPI(null, "fail", 500);
        }
    }

    @GetMapping("/getUserPermissions")
    public ResponseAPI getUserPermissions() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userDetails = (User) authentication.getPrincipal();
        List<Permission> response = rolePermissionsService.getUserPermissions(userDetails);
        return new ResponseAPI(response, Constants.messageResponse.SUCCESS, 200);
    }

    @GetMapping("/getUserOriginDataPermissions")
    public ResponseAPI getUserOriginDataPermissions() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userDetails = (User) authentication.getPrincipal();
        List<Permission> response = rolePermissionsService.getUserOriginDataPermissions("ORIGINDATA", userDetails);
        return new ResponseAPI(response, Constants.messageResponse.SUCCESS, 200);
    }

    @GetMapping("/getPermissionsCurrent")
    public ResponseEntity<?> getPermissionsCurrent(@RequestParam String menuCode) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userDetails = (User) authentication.getPrincipal();
        List<String> userPermissions = rolePermissionsService.getPermissionsCurrent(menuCode, userDetails);

        Map<String, Boolean> pagePerm = new HashMap<>();
        pagePerm.put("add", userPermissions.contains(menuCode + "_ADD"));
        pagePerm.put("edit", userPermissions.contains(menuCode + "_EDIT"));
        pagePerm.put("delete", userPermissions.contains(menuCode + "_DELETE"));
        pagePerm.put("permission", userPermissions.contains(menuCode + "_PERMISSION"));

        return ResponseEntity.ok(pagePerm);
    }
}
