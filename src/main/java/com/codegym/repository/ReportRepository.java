package com.codegym.repository;

import com.codegym.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long> {
    Optional<Report> findByHireSessionId(Long hireSessionId);

    List<Report> findAllByHireSessionId(Long hireSessionId);

    void deleteByHireSessionId(Long hireSessionId);

    Long countByCcdvIdAndStatus(Long ccdvId, String status);
}
