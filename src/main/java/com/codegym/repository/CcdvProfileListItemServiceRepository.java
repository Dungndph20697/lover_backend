package com.codegym.repository;

import com.codegym.model.CcdvProfile;
import com.codegym.model.enums.ProfileStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CcdvProfileListItemServiceRepository extends JpaRepository<CcdvProfile, Long> {
    // Page CCDV active sorted by joinDate (pageable provides sort)
    @Query("SELECT p FROM CcdvProfile p WHERE p.status = :status")
    Page<CcdvProfile> findByStatus(@Param("status") ProfileStatus status, Pageable pageable);

    // Projection: get min price per user from CcdvServiceDetail
    @Query("SELECT sd.user.id AS userId, MIN(sd.totalPrice) AS minPrice " +
            "FROM CcdvServiceDetail sd WHERE sd.user.id IN :userIds GROUP BY sd.user.id")
    List<Object[]> findMinPriceByUserIds(@Param("userIds") List<Long> userIds);
}
