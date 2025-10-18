package com.echameunapata.backend.repositories;

import com.echameunapata.backend.domain.models.Token;
import com.echameunapata.backend.domain.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TokenRepository extends JpaRepository<Token, UUID> {
    List<Token>findByUserAndCanActive(User user, boolean canActive);
}
