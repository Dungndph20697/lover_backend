package com.codegym.repository;

import com.codegym.model.HireSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TongDoanhThuRepository extends JpaRepository<HireSession, Long> {

    @Query("SELECT COALESCE(SUM(h.totalPrice), 0) FROM HireSession h " +
            "WHERE h.ccdv.id = :ccdvId " +
            "AND h.status = 'DONE' " +
            "AND h.endTime BETWEEN :start AND :end")
    Double sumRevenueByCcdvAndDateRange(@Param("ccdvId") Long ccdvId,
                                        @Param("start") LocalDateTime start,
                                        @Param("end") LocalDateTime end);

    @Query("SELECT s FROM HireSession s WHERE s.ccdv.id = :ccdvId " +
            "AND s.endTime >= :start AND s.startTime <= :end")
    List<HireSession> findAllByCcdvAndDateRange(@Param("ccdvId") Long ccdvId,
                                                @Param("start") LocalDateTime start,
                                                @Param("end") LocalDateTime end);
}