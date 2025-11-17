package com.codegym.repository;

import com.codegym.model.HireSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserHireRepository extends JpaRepository<HireSession, Long> {
    
    // Lấy danh sách đơn thuê của user (có phân trang)
    Page<HireSession> findByUserIdOrderByStartTimeDesc(Long userId, Pageable pageable);
    
    // Lấy danh sách đơn thuê của user theo status (có phân trang)
    Page<HireSession> findByUserIdAndStatusOrderByStartTimeDesc(Long userId, String status, Pageable pageable);
    
    // Lấy tất cả đơn của user (không phân trang)
    List<HireSession> findByUserIdOrderByStartTimeDesc(Long userId);
    
    // Đếm số đơn theo status
    Long countByUserIdAndStatus(Long userId, String status);
    
    // Tính tổng tiền đã chi
    @Query("SELECT SUM(h.totalPrice) FROM HireSession h WHERE h.user.id = :userId")
    Double sumTotalPriceByUserId(@Param("userId") Long userId);
    
    // Tính tổng tiền theo status
    @Query("SELECT SUM(h.totalPrice) FROM HireSession h WHERE h.user.id = :userId AND h.status = :status")
    Double sumTotalPriceByUserIdAndStatus(@Param("userId") Long userId, @Param("status") String status);
}