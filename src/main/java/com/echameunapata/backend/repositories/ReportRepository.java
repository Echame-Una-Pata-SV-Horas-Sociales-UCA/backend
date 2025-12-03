package com.echameunapata.backend.repositories;

import com.echameunapata.backend.domain.enums.reports.ReportStatus;
import com.echameunapata.backend.domain.enums.reports.ReportType;
import com.echameunapata.backend.domain.models.Report;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
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
    ORDER BY 
        CASE WHEN r.status = 'OPEN' THEN 0 ELSE 1 END,
        r.receptionDate DESC
""")
    List<Report> findByFilters(
            @Param("type") ReportType type,
            @Param("status") ReportStatus status,
            @Param("startDate") Instant startDate,
            @Param("endDate") Instant endDate
    );


    @EntityGraph(attributePaths = {"person"})
    @Query("SELECT r FROM Report r WHERE r.id = :id")
    Optional<Report> findByIdWithEvidence(@Param("id") UUID id);


}
