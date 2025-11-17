package com.codegym.repository;

import com.codegym.model.CcdvProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CcdvProfileRepository extends JpaRepository<CcdvProfile, Long> {
    boolean existsByUserId(Long userId);
    CcdvProfile findByUserId(Long userId);

    @Query("SELECT c FROM CcdvProfile c WHERE c.gender = :gender ORDER BY c.hireCount DESC")
    List<CcdvProfile> findTopByGenderOrderByHireCountDesc(@Param("gender") String gender);
}