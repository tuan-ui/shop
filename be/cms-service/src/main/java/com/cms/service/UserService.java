package com.cms.service;

import com.cms.constant.Constants;
import com.cms.dto.request.DeleteMultiDTO;
import com.cms.dto.request.UserRequestDTO;
import com.cms.dto.response.ErrorListResponse;
import com.cms.dto.response.UserResponse;
import com.cms.entity.User;
import com.cms.repository.RoleRepository;
import com.cms.repository.UserRepository;
import com.cms.repository.UserRolesRepository;
import com.cms.utils.DateUtil;
import com.cms.utils.StringUtils;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRolesRepository userRolesRepository;
    private final ModelMapper mapper;
    private final UserRolesService userRolesService;
    private final RoleService roleService;
    private final RoleRepository roleRepository;


    public Page<UserResponse> listUsers(String searchString, String userName, String fullName, String phone,
                                String birth, String userCode, User user, Pageable pageable) {
        String searchStringUnaccented = StringUtils.removeAccents(searchString);
        String fullNameUnaccented = StringUtils.removeAccents(fullName);

        if (pageable == null || pageable.isUnpaged()) {
            pageable = Pageable.unpaged();
        }

        Page<User> users = userRepository.listUsersNative(searchStringUnaccented, userName, fullNameUnaccented, phone, birth,
                userCode, pageable);
        List<UserResponse> responses = new ArrayList<>();
        for (User u : users.getContent()) {
            UserResponse userResponse = mapper.map(u, UserResponse.class);
            List<UUID> lstRoles = userRolesRepository.getRolesByUserId(u.getId());
            if (!lstRoles.isEmpty()) {
                userResponse.setListRole(lstRoles.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(",")));
            }
            if (u.getIssueDate() != null) {
                userResponse.setIssueDate(DateUtil.convertStringDateFormat(u.getIssueDate().toString()));
            }
            if (u.getBirthday() != null) {
                userResponse.setBirthDay(DateUtil.convertStringDateFormat(u.getBirthday().toString()));
            }
            responses.add(userResponse);
        }

        return new PageImpl<>(responses, pageable, users.getTotalElements());
    }

    public UserResponse getByUserId(UUID userId) {
        User result = userRepository.getUserByUserId(userId);
        UserResponse userDetailDTO = mapper.map(result, UserResponse.class);
        List<UUID> lstRoles = userRolesRepository.getRolesByUserId(result.getId());
        if (!lstRoles.isEmpty()) {
            userDetailDTO.setListRole(lstRoles.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(",")));
        }
        if (result.getIssueDate() != null) {
            userDetailDTO.setIssueDate(DateUtil.convertStringDateFormat(result.getIssueDate().toString()));
        }
        if (result.getBirthday() != null) {
            userDetailDTO.setBirthDay(DateUtil.convertStringDateFormat(result.getBirthday().toString()));
        }
        return userDetailDTO;
    }

    @Transactional
    public String deleteUser(UUID id, User user, Long version) {
        User deletedUser = userRepository.getUserByUserIdIncluideDeleted(id);
            if (userRolesRepository.existsUserByUserId(deletedUser.getId())) {
                return "error.UserRolesUsed";
            }
            userRepository.deleteUserByUserId(id);
        return "";
    }

    @Transactional
    public String deleteMultiUser(List<DeleteMultiDTO> ids, User userDetails) {
        for (DeleteMultiDTO id : ids) {
            User user = userRepository.getUserByUserIdIncluideDeleted(id.getId());
                if (userRolesRepository.existsUserByUserId(user.getId())) {
                    return "error.UserRolesUsed";
                }
                userRepository.deleteUserByUserId(id.getId());
        }
        return "";
    }

    @Transactional
    public String updateUser(UserRequestDTO userCreateDTO, String profileImagePath, User user, List<UUID> roleIds) {
        User existingUser = userRepository.findByUsernameIncluideDeleted(userCreateDTO.getUsername());if (userCreateDTO.getUserCode() != null && !userCreateDTO.getUserCode().equals(existingUser.getUserCode())) {
                return "error.UserCodeError";
            }
            User updatedUser = mapToUserTest(userCreateDTO, existingUser, profileImagePath);
            updatedUser.setUpdateAt(LocalDateTime.now());
            updatedUser.setUpdateBy(user.getId());
            User savedUser = userRepository.save(updatedUser);
            userRolesService.saveUserRoles(savedUser.getId(), roleIds);
        return "";

    }

    @Transactional
    public String createUser(UserRequestDTO userCreateDTO, String profileImagePath, User userDetails, List<UUID> roleIds) {
        if (userRepository.existsByUserCode(userCreateDTO.getUserCode()) > 0) {
            return "error.UserCodeDoesExist";
        }
        User user = mapToUserTest(userCreateDTO, profileImagePath);
        user.setCreateAt(LocalDateTime.now());
        user.setCreateBy(userDetails.getId());
        user.setIsActive(true);
        user.setIsDeleted(Constants.isDeleted.ACTIVE);
        String newPasswordEncode = passwordEncoder.encode(userCreateDTO.getPassword());
        user.setPassword(newPasswordEncode);
        User savedUser = userRepository.save(user);
        userRolesService.saveUserRoles(savedUser.getId(), roleIds);
        return "";
    }

    private User mapToUserTest(UserRequestDTO userCreateDTO, String profileImagePath) {
        User user = new User();
        user.setFullName(userCreateDTO.getFullname());
        user.setPhone(userCreateDTO.getPhone() != null && !userCreateDTO.getPhone().isEmpty() ? userCreateDTO.getPhone() : null);
        user.setEmail(userCreateDTO.getEmail() != null && !userCreateDTO.getEmail().isEmpty() ? userCreateDTO.getEmail() : null);
        user.setIdentifyCode(userCreateDTO.getIdentifyCode());
        user.setUsername(userCreateDTO.getUsername());
        user.setIsAdmin(Boolean.TRUE.equals(userCreateDTO.getIsAdmin()) ? 1 : 0);
        user.setUserCode(userCreateDTO.getUserCode());
        user.setBirthday(userCreateDTO.getBirthDay());
        user.setGender(userCreateDTO.getGender() == null ? null : userCreateDTO.getGender() == 1);
        user.setIssueDate(userCreateDTO.getIssueDate());
        user.setIssuePlace(userCreateDTO.getIssuePlace());
        user.setProfileImage(profileImagePath != null && !profileImagePath.isEmpty() ? profileImagePath : null);    // Set path if provided
        user.setPassword(userCreateDTO.getPassword());
        return user;
    }

    private User mapToUserTest(UserRequestDTO userCreateDTO, User existingUser, String profileImagePath) {
        existingUser.setFullName(userCreateDTO.getFullname());
        existingUser.setPhone(userCreateDTO.getPhone() != null && !userCreateDTO.getPhone().isEmpty()
                ? userCreateDTO.getPhone() : null);
        existingUser.setEmail(userCreateDTO.getEmail() != null && !userCreateDTO.getEmail().isEmpty()
                ? userCreateDTO.getEmail() : null);
        existingUser.setIdentifyCode(userCreateDTO.getIdentifyCode());
        existingUser.setUsername(userCreateDTO.getUsername());
        existingUser.setIsAdmin(Boolean.TRUE.equals(userCreateDTO.getIsAdmin()) ? 1 : 0);
        existingUser.setUserCode(userCreateDTO.getUserCode());
        existingUser.setBirthday(userCreateDTO.getBirthDay());
        existingUser.setGender(userCreateDTO.getGender() == null ? null : userCreateDTO.getGender() == 1);
        existingUser.setIssueDate(userCreateDTO.getIssueDate());
        existingUser.setIssuePlace(userCreateDTO.getIssuePlace());

        // Only update images if new files are provided and not empty
        if (profileImagePath != null && !profileImagePath.isEmpty()) {
            existingUser.setProfileImage(profileImagePath);
        }

        // Only update password if provided
        if (userCreateDTO.getPassword() != null && !userCreateDTO.getPassword().isEmpty()) {
            String newPasswordEncode = passwordEncoder.encode(userCreateDTO.getPassword());
            existingUser.setPassword(newPasswordEncode);
        }
        return existingUser;
    }

    public Resource downFile(String fileName) throws IOException {
        String uploadDir = Constants.upload.IMAGE_DIRECTORY;

        String cleanedFileName = fileName.replaceFirst("^/+", "");

        Path filePath;

        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            if (cleanedFileName.matches("^[A-Za-z]:.*")) {
                filePath = Paths.get(cleanedFileName);
            } else {
                filePath = Paths.get(uploadDir, cleanedFileName);
            }
        } else {
            filePath = Paths.get(uploadDir, cleanedFileName);
        }

        filePath = filePath.normalize();

        if (!Files.exists(filePath)) {
            throw new FileNotFoundException("Không tìm thấy file: " + filePath.toAbsolutePath());
        }

        return new FileSystemResource(filePath.toFile());
    }


    @Transactional
    public String lockUser(UUID id, User userDetails,Long version) {
        User user = userRepository.getUserByUserIdIncluideDeleted(id);
            user.setIsActive(!user.getIsActive());
            user.setUpdateAt(LocalDateTime.now());
            user.setUpdateBy(userDetails.getId());
            User savedUser = userRepository.save(user);
        return "";
    }

    public void updateUserImages(UUID userId, String profileImagePath, String signatureImagePath) {
        User user = userRepository.getUserByUserId(userId);
        if (user != null) {
            if (profileImagePath != null) {
                user.setProfileImage(profileImagePath);
            }
            userRepository.save(user);
        }
    }

    public List<User> getAllUser() {
        return userRepository.findUser();
    }

    @Transactional
    public ErrorListResponse checkDeleteMulti(List<DeleteMultiDTO> ids) {
        ErrorListResponse response = new ErrorListResponse();
        List<ErrorListResponse.ErrorResponse> lstObject = new ArrayList<>();
        for(DeleteMultiDTO id : ids) {
            ErrorListResponse.ErrorResponse object = new ErrorListResponse.ErrorResponse();
            object.setId(id.getId());
            User user = userRepository.getUserByUserIdIncluideDeleted(id.getId());
            object.setCode(user.getUserCode());
            object.setName(user.getFullName());
            lstObject.add(object);
        }
        response.setErrors(lstObject);
        response.setTotal(ids.size());
        long countNum = response.getErrors().stream()
                .filter(item -> item.getErrorMessage()!=null)
                .count();
        response.setHasError(countNum != 0);
        if(Boolean.FALSE.equals(response.getHasError()))
        {
            return null;
        }
        return response;
    }
}
