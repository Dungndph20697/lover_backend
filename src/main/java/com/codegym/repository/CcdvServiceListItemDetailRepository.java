package com.codegym.repository;

import com.codegym.model.CcdvServiceDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CcdvServiceListItemDetailRepository extends JpaRepository<CcdvServiceDetail, Long> {
    // Get all services for a list of user ids (we'll sample random 3 per user in service layer)
    @Query("SELECT sd FROM CcdvServiceDetail sd " +
            "WHERE sd.user.id IN :userIds " +
            "AND sd.serviceType.name = 'Cử chỉ thân mật'")
    List<CcdvServiceDetail> findAllByUserIds(@Param("userIds") List<Long> userIds);
}
