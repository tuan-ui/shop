package com.cms.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cms.entity.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {
    @Query("SELECT r FROM Role r " + "Join UserRoles ur on r.id = ur.id.roleId "
            + "Join User u on u.id = ur.id.userId "
            + "Join PermissionRole pr on r.id = pr.id.roleId "
            + "Join Permission p on p.id = pr.id.permissionId "
            + "WHERE u.id = :userId AND r.isActive = true AND r.isDeleted = false")
    List<Permission> getListRoleByUserId(@Param("userId") UUID userId);

    @Query(value = "FROM Permission p " + "WHERE p.isDeleted = false " + "ORDER BY p.position ASC")
    List<Permission> findAllByIsDeletedFalseOrderByPositionAsc();
}
