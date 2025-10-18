package com.echameunapata.backend.services.contract;

import com.echameunapata.backend.domain.dtos.auth.RegisterUserDto;
import com.echameunapata.backend.domain.models.Token;
import com.echameunapata.backend.domain.models.User;

public interface IAuthService {
    User registerUser(RegisterUserDto userDto);
    Token registerToken(User user);
    Boolean isTokenValid(User user, String token) ;
    void cleanToken(User user);
    User findUserAuthenticated();
}
