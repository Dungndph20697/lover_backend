package com.codegym.repository;

import com.codegym.dto.CcdvProfileDTO;
import com.codegym.model.CcdvProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CcdvProfileRepository extends JpaRepository<CcdvProfile, Long> {
    boolean existsByUserId(Long userId);
    CcdvProfile findByUserId(Long userId);
}