package com.echameunapata.backend.repositories;

import com.echameunapata.backend.domain.models.Token;
import com.echameunapata.backend.domain.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TokenRepository extends JpaRepository<Token, UUID> {
    List<Token> findByUserAndCanActive(User user, boolean canActive);

    // Password reset helpers
    Optional<Token> findByTokenAndTypeAndCanActive(String token, String type, boolean canActive);
    List<Token> findByUserAndTypeAndCanActive(User user, String type, boolean canActive);
    List<Token> findByTypeAndCanActiveAndTimestampBefore(String type, boolean canActive, Date before);

    Optional<Token> findByTokenAndType(String resetToken, String tokenTypePasswordReset);
}
