package com.codegym.repository;

import com.codegym.model.CcdvServiceDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CcdvServiceDetailRepository extends JpaRepository<CcdvServiceDetail, Long> {

    List<CcdvServiceDetail> findByUser_Id(Long userId);

    @Query("SELECT COUNT(c) > 0 FROM CcdvServiceDetail c WHERE c.user.id = :userId AND c.serviceType.id = :serviceId")
    boolean existsByUserAndService(Long userId, Long serviceId);
    // thêm sửa giá dịch vụ mở rộng theo id user
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE CcdvServiceDetail c SET c.totalPrice = :price " +
            "WHERE c.user.id = :userId AND c.serviceType.id = :serviceId " +
            "AND UPPER(c.serviceType.type) <> 'FREE'")
    void updatePriceByUserAndService(Long userId, Long serviceId, BigDecimal price);
    @Query(value = "SELECT * FROM ccdv_service_detail WHERE provider_id = :providerId ORDER BY RAND() LIMIT :limit",
            nativeQuery = true)
    List<CcdvServiceDetail> findRandomServices(Long providerId, int limit);

}