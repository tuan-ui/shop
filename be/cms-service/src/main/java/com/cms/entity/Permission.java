package com.cms.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Permission {
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "permission_name")
    private String permissionName;

    @Column(name = "permission_code")
    private String permissionCode;

    @Column(name = "permission_url")
    private String permissionUrl;

    @Column(name = "permission_parent")
    private UUID permissionParent;

    @Column(name = "position")
    private Integer position;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "is_menus")
    private Boolean isMenus;
}
