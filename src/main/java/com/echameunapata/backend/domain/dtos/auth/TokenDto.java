package com.echameunapata.backend.domain.dtos.auth;

import com.echameunapata.backend.domain.models.Token;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TokenDto {

    private String token;

    public TokenDto(Token token){
        this.token = token.getToken();
    }
}
