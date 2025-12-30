package com.cms.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cms.dto.response.RoleResponse;
import com.cms.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    @Query(
            "SELECT COUNT(r) > 0 FROM Role r WHERE LOWER(r.roleCode) = LOWER(:roleCode) AND r.isActive = true AND r.isDeleted = false")
    boolean existsByRoleCodeIgnoreCase(@Param("roleCode") String roleCode);

    @Query(
            "SELECT COUNT(r) > 0 FROM Role r WHERE LOWER(r.roleCode) = LOWER(:roleCode) AND r.isActive = true AND r.isDeleted = false AND r.id <> :roleId")
    boolean existsByRoleCodeIgnoreCaseNotId(@Param("roleCode") String roleCode, @Param("roleId") UUID roleId);

    @Query(
            """
	SELECT new com.cms.dto.response.RoleResponse(
		r.id,
		r.roleName,
		r.roleCode,
		r.roleDescription,
		r.isActive,
		r.isDeleted,
		r.createAt,
		r.updateAt,
		r.createBy,
		r.updateBy
	)
	FROM Role r
	WHERE r.isDeleted = false
	AND (COALESCE(:searchString, '') = ''
		OR LOWER(FUNCTION('convert_to_unsign', CAST(r.roleName AS string)))
			LIKE CONCAT('%', LOWER(:searchString), '%')
		OR LOWER(FUNCTION('convert_to_unsign', CAST(r.roleCode AS string)))
			LIKE CONCAT('%', LOWER(:searchString), '%')
		OR LOWER(FUNCTION('convert_to_unsign', CAST(r.roleDescription AS string)))
			LIKE CONCAT('%', LOWER(:searchString), '%')
		)
	AND (COALESCE(:roleName, '') = ''
		OR LOWER(FUNCTION('convert_to_unsign', CAST(r.roleName AS string)))
			LIKE CONCAT('%', LOWER(:roleName), '%')
		)
	AND (COALESCE(:roleCode, '') = ''
		OR LOWER(FUNCTION('convert_to_unsign', CAST(r.roleCode AS string)))
			LIKE CONCAT('%', LOWER(:roleCode), '%')
		)
	AND (COALESCE(:roleDescription, '') = ''
		OR LOWER(FUNCTION('convert_to_unsign', CAST(r.roleDescription AS string)))
			LIKE CONCAT('%', LOWER(:roleDescription), '%')
		)
	AND (:status IS NULL OR r.isActive = :status)
	ORDER BY r.roleName ASC
	""")
    Page<RoleResponse> searchRoles(
            @Param("searchString") String searchString,
            @Param("roleName") String roleName,
            @Param("roleCode") String roleCode,
            @Param("roleDescription") String roleDescription,
            @Param("status") Boolean status,
            Pageable pageable);

    @Query("SELECT r FROM Role r Where r.isActive = true AND r.isDeleted = false")
    List<Role> getAllRole();

    @Query("FROM Role r Join UserRoles ur on r.id = ur.id.roleId WHERE ur.id.userId = :id AND r.isDeleted = false")
    List<UUID> findLstIdByUserId(UUID id);

    @Query("FROM Role r WHERE r.id = :id ")
    Role findByRoleIdIncluideDeleted(UUID id);
}
