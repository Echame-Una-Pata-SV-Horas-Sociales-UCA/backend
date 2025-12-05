package com.echameunapata.backend.services.contract;

import com.echameunapata.backend.domain.models.Role;

import java.util.List;
import java.util.UUID;

public interface IRoleService {
    Role findById(String id);
    List<Role> findAllRoles();
    List<Role> findAllByIds(List<String>ids);
}
