package com.cms.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cms.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    @Query("FROM User u WHERE u.id = :id AND u.isDeleted = false")
    User getUserByUserId(UUID id);

    @Query("SELECT u FROM User u WHERE LOWER(u.username) = LOWER(:username) AND u.isDeleted = false")
    Optional<User> findByUsername(String username);


    @Query(value = """
    SELECT DISTINCT u.*
    FROM users u
    WHERE u.is_deleted = false
      AND (
        :searchString IS NULL OR :searchString = ''
        OR LOWER(u.user_name) LIKE LOWER(CONCAT('%', :searchString, '%'))
        OR u.full_name LIKE CONCAT('%', :searchString, '%')
        OR u.email LIKE CONCAT('%', :searchString, '%')
        OR LOWER(u.phone) LIKE LOWER(CONCAT('%', :searchString, '%'))
        OR LOWER(CAST(u.user_code AS CHAR)) LIKE LOWER(CONCAT('%', :searchString, '%'))
      )
      AND (
        :userName IS NULL OR :userName = ''
        OR LOWER(u.user_name) LIKE LOWER(CONCAT('%', :userName, '%'))
      )
      AND (
        :fullName IS NULL OR :fullName = ''
        OR u.full_name LIKE CONCAT('%', :fullName, '%')
      )
      AND (
        :phone IS NULL OR :phone = ''
        OR LOWER(u.phone) LIKE LOWER(CONCAT('%', :phone, '%'))
      )
      AND (
        :birthStr IS NULL OR :birthStr = ''
        OR u.birthday = STR_TO_DATE(:birthStr, '%d/%m/%Y')
      )
      AND (
        :userCode IS NULL OR :userCode = ''
        OR LOWER(CAST(u.user_code AS CHAR)) LIKE LOWER(CONCAT('%', :userCode, '%'))
      )
    ORDER BY u.create_at DESC
    """, nativeQuery = true)
    Page<User> listUsersNative(
            @Param("searchString") String searchString,
            @Param("userName") String userName,
            @Param("fullName") String fullName,
            @Param("phone") String phone,
            @Param("birthStr") String birthStr,
            @Param("userCode") String userCode,
            Pageable pageable);


    @Query("UPDATE User u SET u.password = :password WHERE u.id = :userId")
    @Modifying
    void resetPassword(@Param("userId") UUID userId, @Param("password") String password);

    @Query(
            "SELECT COUNT(u) FROM User u " +
                    "WHERE u.username = :username " +
                    "AND u.isDeleted = false " )
    Integer existsByUsername(@Param("username") String username);

    @Query(
            "SELECT COUNT(u) FROM User u WHERE u.isDeleted = false " )
    Integer existsUserByPartnerId(@Param("partnerId") UUID partnerId);

    @Query(
            "SELECT COUNT(u) FROM User u " +
                    "WHERE u.userCode = :userCode AND u.isDeleted = false " )
    Integer existsByUserCode(@Param("userCode") String userCode);

    @Query("SELECT u FROM User u WHERE LOWER(u.username) = LOWER(:username) ")
    User findByUsernameIncluideDeleted(String username);

    @Query("FROM User u WHERE u.id = :id ")
    User getUserByUserIdIncluideDeleted(UUID id);

    @Query("SELECT COUNT(r) > 0 FROM User r WHERE LOWER(r.userCode) = LOWER(:userCode) AND r.isActive = true AND r.isDeleted = false AND r.id <> :userId ")
    boolean existsByUserCodeIgnoreCaseNotId(@Param("userCode") String userCode, @Param("userId") UUID userId);

    @Query("FROM User u WHERE u.isDeleted = false")
    List<User> findUser();

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END " +
            "FROM User u WHERE u.userCode = :userCode AND u.isDeleted = false")
    boolean existsByUserCodeAndPartnerId(@Param("userCode") String userCode);

    @Query("FROM User u WHERE u.id IN :userIds AND u.isDeleted = false")
    List<User> findAllById(List<UUID> userIds);

    @Query("SELECT u FROM User u WHERE LOWER(u.username) = LOWER(:username) And LOWER(u.phone) = LOWER(:phone)  AND u.isDeleted = false")
    User findByUsernameandPhone(String username, String phone);

    @Modifying
    @Query("DELETE FROM User d WHERE d.id = :id")
    void deleteUserByUserId(@Param("id") UUID id);
}
