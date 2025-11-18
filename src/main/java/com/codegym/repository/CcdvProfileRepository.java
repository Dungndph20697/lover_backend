package com.codegym.repository;

import com.codegym.model.CcdvProfile;
import com.codegym.model.enums.ProfileStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import java.util.Optional;


public interface CcdvProfileRepository extends JpaRepository<CcdvProfile, Long> {
    // kiểm tra xem user có profile chưa
    boolean existsByUserId(Long userId);

    // lấy profile theo user id
    CcdvProfile findByUserId(Long userId);


    @Query("SELECT c FROM CcdvProfile c WHERE c.gender = :gender ORDER BY c.hireCount DESC")
    List<CcdvProfile> findTopByGenderOrderByHireCountDesc(@Param("gender") String gender);

    // lấy danh sách profile dang hoat dong
    List<CcdvProfile> findAllByStatus(ProfileStatus status);

}