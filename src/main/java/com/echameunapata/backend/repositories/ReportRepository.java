package com.echameunapata.backend.repositories;

import com.echameunapata.backend.domain.enums.reports.ReportStatus;
import com.echameunapata.backend.domain.enums.reports.ReportType;
import com.echameunapata.backend.domain.models.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReportRepository extends JpaRepository<Report, UUID> {
    @Query("""
        SELECT r FROM Report r
        WHERE (:type IS NULL OR r.type = :type)
          AND (:status IS NULL OR r.status = :status)
          AND (COALESCE(:startDate, r.receptionDate) <= r.receptionDate)
          AND (COALESCE(:endDate, r.receptionDate) >= r.receptionDate)
    """)
    Page<Report> findByFilters(
            @Param("type")ReportType type,
            @Param("status")ReportStatus status,
            @Param("startDate") Instant startDate,
            @Param("endDate") Instant endDate,
            Pageable pageable
    );

    @EntityGraph(attributePaths = {"reportEvidences"})
    @Query("SELECT r FROM Report r WHERE r.id = :id")
    Optional<Report> findByIdWithEvidence(@Param("id") UUID id);


}
