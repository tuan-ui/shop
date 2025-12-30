package com.cms.dto.response;

import java.util.List;
import java.util.UUID;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationResponse {
    private String token;
    private String refreshToken;
    private String username;
    private List<UUID> roleId;
    private String fullName;
    private String email;
    private String phone;
    private Boolean isActive;
    private UUID userId;
}
