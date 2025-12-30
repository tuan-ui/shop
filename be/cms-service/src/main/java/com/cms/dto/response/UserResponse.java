package com.cms.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private String id;
    private String fullName;
    private String email;
    private String username;
    private String userCode;
    private String birthDay;
    private String phone;
    private String identifyCode;
    private String issueDate;
    private String issuePlace;
    private String gender;
    private String profileImage;
    private String listRole;
    private Integer status;
}