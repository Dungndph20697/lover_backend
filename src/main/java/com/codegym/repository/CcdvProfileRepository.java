package com.codegym.repository;

import com.codegym.dto.CcdvProfileDTO;
import com.codegym.model.CcdvProfile;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CcdvProfileRepository extends JpaRepository<CcdvProfile, Long>, JpaSpecificationExecutor<CcdvProfile> {
    boolean existsByUserId(Long userId);
    CcdvProfile findByUserId(Long userId);
    List<CcdvProfile> findAllByOrderByJoinDateDesc(Pageable pageable);




}