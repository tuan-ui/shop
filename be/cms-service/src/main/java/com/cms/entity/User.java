package com.cms.entity;

import java.util.Date;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "users")
public class User extends BaseEntity {
    @Column(name = "user_name", nullable = false, length = 100)
    private String username;

    @Column(name = "user_code")
    private String userCode;

    @Column(name = "identify_code", length = 20)
    private String identifyCode;

    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "password", length = 100)
    private String password;

    @Column(name = "issue_place")
    private String issuePlace;

    @Column(name = "issue_date")
    private Date issueDate;

    @Column(name = "is_admin")
    private Integer isAdmin;

    @Column(name = "gender")
    private Boolean gender;

    @Column(name = "full_name", length = 255)
    private String fullName;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "birthday")
    private Date birthday;
}
