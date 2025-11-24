package com.codegym.repository;

import com.codegym.model.HireSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface HireSessionRepository extends JpaRepository<HireSession, Long> {
    
    // Tìm đơn thuê theo người thuê
    Page<HireSession> findByUserId(Long userId, Pageable pageable);
    
    // Tìm đơn thuê theo CCDV
    Page<HireSession> findByCcdvId(Long ccdvId, Pageable pageable);
    
    // Tìm đơn thuê theo trạng thái
    Page<HireSession> findByStatus(String status, Pageable pageable);
    
    // Tìm đơn thuê theo trạng thái và CCDV
    Page<HireSession> findByCcdvIdAndStatus(Long ccdvId, String status, Pageable pageable);
    
    // Tìm đơn thuê theo trạng thái và người thuê
    Page<HireSession> findByUserIdAndStatus(Long userId, String status, Pageable pageable);
    
    // Tìm tất cả đơn chờ duyệt báo cáo
    List<HireSession> findByStatus(String status);
    
    // Tìm đơn thuê trong khoảng thời gian
    @Query("SELECT h FROM HireSession h WHERE h.startTime BETWEEN :startDate AND :endDate")
    List<HireSession> findByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    // Đếm số đơn theo trạng thái
    Long countByStatus(String status);
    
    // Đếm số đơn theo CCDV và trạng thái
    Long countByCcdvIdAndStatus(Long ccdvId, String status);
    
    // Đếm số đơn theo người thuê và trạng thái
    Long countByUserIdAndStatus(Long userId, String status);
    
    // Lấy danh sách đơn chờ duyệt báo cáo
    @Query("SELECT h FROM HireSession h WHERE h.status = 'REVIEW_REPORT' ORDER BY h.startTime DESC")
    Page<HireSession> findPendingReports(Pageable pageable);
    
    // Lấy danh sách đơn đã hoàn thành
    @Query("SELECT h FROM HireSession h WHERE h.status = 'COMPLETED' ORDER BY h.startTime DESC")
    Page<HireSession> findCompletedSessions(Pageable pageable);
}