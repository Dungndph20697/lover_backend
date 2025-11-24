package com.codegym.repository;

import com.codegym.model.Revenue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RevenueRepository extends JpaRepository<Revenue, Long> {
    Page<Revenue> findByCcdvId(Long ccdvId, Pageable pageable);
    
    @Query("SELECT r FROM Revenue r ORDER BY r.createdAt DESC")
    Page<Revenue> findAllRevenues(Pageable pageable);
    
    @Query("SELECT COALESCE(SUM(r.amount), 0) FROM Revenue r WHERE r.ccdv.id = :ccdvId")
    Double getTotalRevenueByCcdvId(Long ccdvId);
}