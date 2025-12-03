package com.codegym.repository;

import com.codegym.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long> {
    // ✅ FIX: Trả về List thay vì Optional
    List<Report> findByHireSessionId(Long hireSessionId);

    // Lấy tất cả báo cáo của 1 CCDV với status cụ thể
    Long countByCcdvIdAndStatus(Long ccdvId, String status);

    // Xóa tất cả báo cáo của 1 đơn thuê
    void deleteByHireSessionId(Long hireSessionId);
}
