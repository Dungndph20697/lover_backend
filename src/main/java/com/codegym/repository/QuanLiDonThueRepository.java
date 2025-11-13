package com.codegym.repository;

import com.codegym.model.HireSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuanLiDonThueRepository extends JpaRepository<HireSession, Long> {
    
    // Lấy danh sách đơn thuê theo CCDV
    List<HireSession> findByCcdvIdOrderByStartTimeDesc(Long ccdvId);
    
    // Lấy theo CCDV và status
    List<HireSession> findByCcdvIdAndStatus(Long ccdvId, String status);
    
    // Lấy danh sách đơn theo người thuê
    List<HireSession> findByUserIdOrderByStartTimeDesc(Long userId);
    
    // Đếm số đơn đã hoàn thành của CCDV
    long countByCcdvIdAndStatusIn(Integer ccdvId, List<String> statuses);
}