package com.echameunapata.backend.services.impl;

import com.echameunapata.backend.domain.dtos.user.UpdateUserInfoDto;
import com.echameunapata.backend.domain.enums.user.RoleActions;
import com.echameunapata.backend.domain.models.Role;
import com.echameunapata.backend.domain.models.User;
import com.echameunapata.backend.exceptions.HttpError;
import com.echameunapata.backend.repositories.RoleRepository;
import com.echameunapata.backend.repositories.UserRepository;
import com.echameunapata.backend.services.contract.IUserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    /**
     * Este método busca un usuario por su correo electrónico.
     *
     * @param email El correo electrónico del usuario.
     * @return El usuario encontrado.
     * @throws HttpError Si el usuario no existe.
     */
    @Override
    public User findUserByEmail(String email) {
        try{
            var user = userRepository.findByEmail(email);
            if(user == null){
                throw new HttpError(HttpStatus.NOT_FOUND, "User not found");
            }
            return user;
        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public Page<User> findAllUsersByRoleAndIsActive(List<String>idsRoles, Boolean isActive, Pageable pageable) {
        try{
            Page<User> users = userRepository.findByFilters(idsRoles, isActive, pageable);
            if (users.isEmpty()){
                throw new HttpError(HttpStatus.NOT_FOUND, "List users is empty");
            }
            return users;
        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public User findUserById(UUID id) {
        try{
            var user = userRepository.findById(id).orElse(null);
            if (user == null){
                throw new HttpError(HttpStatus.NOT_FOUND, "User with id not exist");
            }
            return user;
        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public User updateUserInfo(UUID userId, UpdateUserInfoDto userInfoDto) {
        try{
            var user = userRepository.findById(userId).orElse(null);
            if (user == null){
                throw new HttpError(HttpStatus.NOT_FOUND, "User with id not exist");
            }
            user.setName(userInfoDto.getName());
            user.setEmail(userInfoDto.getEmail());

            return userRepository.save(user);
        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public void updateIsActive(UUID id) {
        try{
            var user = userRepository.findById(id).orElse(null);
            if (user == null){
                throw new HttpError(HttpStatus.NOT_FOUND, "User with id not exist");
            }
            user.setIsActive(!user.getIsActive());

            userRepository.save(user);
        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public void updateRolesInUser(UUID id, List<String> rolesIds, String action) {
        var user = userRepository.findById(id).orElse(null);
        if (user==null){
            throw new HttpError(HttpStatus.FOUND, "User with id not exist");
        }
        var roles = roleRepository.findAllById(rolesIds);
        user = actionRoles(user, roles, action);

        userRepository.save(user);
    }

    private User actionRoles (User user, List<Role> roles, String action){
        switch (RoleActions.fromString(action)){
            case ADD -> roles.forEach(user.getRoles()::add);
            case REMOVE -> roles.forEach(user.getRoles()::remove);
            default -> throw new IllegalArgumentException("Invalid actions"+action);
        }
        return user;
    }


    @Override
    public List<User> findAllUsersByIds(List<UUID> ids) {
        try{
            var users = userRepository.findAllById(ids);
            if(users.isEmpty()){
                throw new HttpError(HttpStatus.NOT_FOUND, "Empty users");
            }
            return users;
        }catch (Exception e){
            throw e;
        }
    }
}
