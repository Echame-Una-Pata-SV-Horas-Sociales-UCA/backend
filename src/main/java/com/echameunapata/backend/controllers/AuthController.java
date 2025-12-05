package com.echameunapata.backend.controllers;

import com.echameunapata.backend.domain.dtos.auth.LoginDto;
import com.echameunapata.backend.domain.dtos.auth.RegisterUserDto;
import com.echameunapata.backend.domain.dtos.auth.ResetPasswordRequest;
import com.echameunapata.backend.domain.dtos.auth.TokenDto;
import com.echameunapata.backend.domain.dtos.commons.GeneralResponse;
import com.echameunapata.backend.exceptions.HttpError;
import com.echameunapata.backend.services.contract.IAuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    /**
     * Endpoint para solicitar el reseteo de contraseña.
     * El usuario envía su email.
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<GeneralResponse> forgotPassword(@RequestParam("email") String email) {

        authService.forgotPasswordRequest(email);

        // Siempre devolvemos 200 OK con un mensaje genérico.
        // NUNCA digas "Email no encontrado".
        String responseMessage = "Si tu dirección de correo electrónico está en nuestra base de datos, "
                + "recibirás un enlace para restablecer tu contraseña en breve.";

        return GeneralResponse.getResponse(HttpStatus.OK, responseMessage);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<GeneralResponse> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new HttpError(HttpStatus.BAD_REQUEST, "Passwords do not match");
        }
        authService.resetPassword(request.getToken(), request.getNewPassword());
        return GeneralResponse.getResponse(HttpStatus.OK, "Password has been reset successfully");
    }
}
