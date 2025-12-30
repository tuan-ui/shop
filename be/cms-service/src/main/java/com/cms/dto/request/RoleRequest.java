package com.cms.dto.request;

import java.util.UUID;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleRequest {
    private UUID id;
    private String searchString;
    private String roleName;
    private String roleCode;
    private String roleDescription;
    private Boolean status;
    private Integer page = 0;
    private Integer size = 10;
}
