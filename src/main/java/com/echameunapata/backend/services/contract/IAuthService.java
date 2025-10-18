package com.echameunapata.backend.services.contract;

import com.echameunapata.backend.domain.dtos.auth.RegisterUserDto;
import com.echameunapata.backend.domain.models.User;

public interface IAuthService {
    User registerUser(RegisterUserDto userDto);
}
