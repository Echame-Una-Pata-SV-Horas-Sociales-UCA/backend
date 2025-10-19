package com.echameunapata.backend.services.contract;

import com.echameunapata.backend.domain.models.User;

public interface IUserService {
    User findUserByEmail(String email);
}
