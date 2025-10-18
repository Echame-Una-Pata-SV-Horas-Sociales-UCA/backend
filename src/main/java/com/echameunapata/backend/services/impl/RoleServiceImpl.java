package com.echameunapata.backend.services.impl;

import com.echameunapata.backend.domain.models.Role;
import com.echameunapata.backend.exceptions.HttpError;
import com.echameunapata.backend.repositories.RoleRepository;
import com.echameunapata.backend.services.contract.IRoleService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements IRoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role findById(String id) {
        try{
            var role = roleRepository.findById(id).orElse(null);
            if (role == null)
            {
                throw  new HttpError(HttpStatus.NOT_FOUND, "Role not exists");
            }
            return role;
        }catch (Exception e){
            throw  e;
        }
    }
}
