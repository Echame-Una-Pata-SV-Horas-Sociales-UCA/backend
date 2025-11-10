package com.echameunapata.backend.services.contract;

import com.echameunapata.backend.domain.dtos.auth.LoginDto;
import com.echameunapata.backend.domain.dtos.auth.RegisterUserDto;
import com.echameunapata.backend.domain.models.Token;
import com.echameunapata.backend.domain.models.User;

public interface IAuthService {
    User registerUser(RegisterUserDto userDto);
    Token loginUser(LoginDto userDto);

    Token registerToken(User user);
    Boolean isTokenValid(User user, String token) ;
    void cleanToken(User user);
    User findUserAuthenticated();

    // Password reset flow
    void resetPassword(String resetToken, String newPassword);
    public void forgotPasswordRequest(String email);
}
