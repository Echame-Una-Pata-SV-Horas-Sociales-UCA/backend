package com.echameunapata.backend.services.contract;

import com.echameunapata.backend.domain.models.Role;

public interface IRoleService {
    Role findById(String id);
}
