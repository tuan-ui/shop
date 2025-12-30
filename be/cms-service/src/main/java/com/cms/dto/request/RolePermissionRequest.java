package com.cms.dto.request;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RolePermissionRequest {
    private UUID roleId;
    private List<UUID> checkedKeys;
    private List<UUID> checkedHalfKeys;
}
