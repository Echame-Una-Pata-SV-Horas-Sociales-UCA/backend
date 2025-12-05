package com.echameunapata.backend.services.contract;

import com.echameunapata.backend.domain.dtos.user.UpdateUserInfoDto;
import com.echameunapata.backend.domain.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;
import java.util.UUID;

public interface IUserService {
    User findUserByEmail(String email);
    List<User> findAllUsersByRoleAndIsActive(List<String>idsRoles, Boolean isActive, Pageable pageable);
    User findUserById(UUID id);
    User updateUserInfo(UUID userId, UpdateUserInfoDto userInfoDto);
    void updateIsActive(UUID id);
    void updateRolesInUser(UUID id, List<String>roles, String action);
    List<User>findAllUsersByIds(List<UUID>ids);
}
