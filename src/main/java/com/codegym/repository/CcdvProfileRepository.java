package com.codegym.repository;

import com.codegym.dto.CcdvDetailAdminDTO;
import com.codegym.model.CcdvProfile;
import com.codegym.model.enums.ProfileStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import java.util.Optional;


public interface CcdvProfileRepository extends JpaRepository<CcdvProfile, Long>, JpaSpecificationExecutor<CcdvProfile> {
    // kiểm tra xem user có profile chưa
    boolean existsByUserId(Long userId);

    // lấy profile theo user id
    CcdvProfile findByUserId(Long userId);


    @Query("SELECT c FROM CcdvProfile c WHERE c.gender = :gender and c.status='ACTIVE' ORDER BY c.hireCount DESC")
    List<CcdvProfile> findTopByGenderOrderByHireCountDesc(@Param("gender") String gender);

    // lấy danh sách profile dang hoat dong
    List<CcdvProfile> findAllByStatus(ProfileStatus status);


    @Query("SELECT new com.codegym.dto.CcdvDetailAdminDTO(" +
            "u.id, p.fullName, u.nickname, u.email, u.phone, u.cccd, " +
            "p.city, p.yearOfBirth, " +
            "p.avatar, p.portrait1, p.portrait2, p.portrait3, " +
            "p.height, p.weight, p.description, p.requirement) " +
            "FROM CcdvProfile p JOIN p.user u " +
            "WHERE u.id = :userId AND u.role.name = 'SERVICE_PROVIDER'")
    CcdvDetailAdminDTO getCcdvDetail(@Param("userId") Long userId);

    @Query("SELECT DISTINCT c.city FROM CcdvProfile c WHERE c.city IS NOT NULL")
    List<String> findDistinctCities();

    // Lấy VIP active, sort theo vipStartTime desc, dùng Pageable để limit 6
    @Query("""
        SELECT p
        FROM CcdvProfile p
        JOIN p.user u
        WHERE p.status = :status
          AND u.isVip = true
        ORDER BY p.vipStartTime DESC
    """)
    List<CcdvProfile> findVipProfilesByStatusOrderByVipStartTimeDesc(ProfileStatus status, Pageable pageable);

}