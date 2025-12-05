package com.echameunapata.backend.repositories;

import com.echameunapata.backend.domain.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    @EntityGraph(attributePaths = {"roles"})
    User findByEmail(String email);

    @Query("""
            SELECT DISTINCT u FROM User u
            LEFT JOIN u.roles r
            WHERE (:roles IS NULL OR r.id IN :roles)
            AND (:isActive IS NULL OR u.IsActive = :isActive)
    """)
    List<User> findByFilters(@Param("roles")List<String>roles, @Param("isActive")Boolean isActive, Pageable pageable);
}
