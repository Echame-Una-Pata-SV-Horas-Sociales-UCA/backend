package com.echameunapata.backend.domain.dtos.user;

import com.echameunapata.backend.domain.models.Role;
import com.echameunapata.backend.domain.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInformationDto {
    private String name;
    private String email;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private List<String> roles;
    private boolean isActive;

    public UserInformationDto(User user){
        this.name = user.getName();
        this.email = user.getEmail();
        this.roles = user.getRoles().stream().map(Role::getId).toList();
        this.isActive = user.getIsActive();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
    }
}
