package com.echameunapata.backend.controllers;

import com.echameunapata.backend.domain.dtos.auth.LoginDto;
import com.echameunapata.backend.domain.dtos.auth.RegisterUserDto;
import com.echameunapata.backend.domain.dtos.auth.TokenDto;
import com.echameunapata.backend.domain.dtos.commons.GeneralResponse;
import com.echameunapata.backend.services.contract.IAuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.base-path}/auth")
public class AuthController {

    private final IAuthService authService;

    public AuthController(IAuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<GeneralResponse>registerController(@RequestBody @Valid RegisterUserDto userDto){
        var user = authService.registerUser(userDto);
        return GeneralResponse.getResponse(HttpStatus.CREATED, "user registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<GeneralResponse>loginController(@RequestBody @Valid LoginDto loginDto){
        var token = authService.loginUser(loginDto);
        return GeneralResponse.getResponse(HttpStatus.OK, "Success", new TokenDto(token));
    }

}
