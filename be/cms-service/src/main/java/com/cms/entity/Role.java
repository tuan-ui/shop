package com.cms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Role extends BaseEntity {

    @Column(name = "role_name", nullable = false, length = 255)
    private String roleName;

    @Column(name = "role_code", nullable = false, length = 50)
    private String roleCode;

    @Column(name = "role_description", length = 2000)
    private String roleDescription;
}
