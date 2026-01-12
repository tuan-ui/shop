package com.auth.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDTO {
    private String username;
    private String fullname;
    private String phone;
    private String email;
    private String identifyCode;
    private String password;
    private String userCode;
    private Date birthDay;
    private Integer gender;
    private Date issueDate;
    private String issuePlace;
    private MultipartFile profileImage;
    private Boolean isAdmin = false;

}
