package com.codegym.repository;

import com.codegym.dto.TopFrequentCustomerDTO;
import com.codegym.dto.TopRecentCustomerDTO;
import com.codegym.model.HireSession;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


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


    //người thuê nhiều nhất
    @Query("""
    SELECT new com.codegym.dto.TopFrequentCustomerDTO(h.user, COUNT(h))
    FROM HireSession h
    WHERE h.ccdv.id = :ccdvId
      AND h.status = 'DONE'
    GROUP BY h.user
    ORDER BY COUNT(h) DESC
    """)
    List<TopFrequentCustomerDTO> findTopFrequentCustomers(
            @Param("ccdvId") Long ccdvId,
            Pageable pageable);


    //người thuê gần đây nhất
    @Query("""
    SELECT new com.codegym.dto.TopRecentCustomerDTO(h.user, MAX(h.startTime))
    FROM HireSession h
    WHERE h.ccdv.id = :ccdvId
      AND h.status = 'DONE'
    GROUP BY h.user
    ORDER BY MAX(h.startTime) DESC
    """)
    List<TopRecentCustomerDTO> findTopRecentCustomers(
            @Param("ccdvId") Long ccdvId,
            Pageable pageable);


}