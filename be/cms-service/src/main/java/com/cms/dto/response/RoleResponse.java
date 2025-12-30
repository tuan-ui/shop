package com.cms.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleResponse {
    private UUID id;
    private String roleName;
    private String roleCode;
    private String roleDescription;
    private Boolean isActive;
    private Boolean isDeleted;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private UUID createBy;
    private UUID updateBy;
}
