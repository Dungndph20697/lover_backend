package com.codegym.repository;

import com.codegym.model.CcdvProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CcdvProfileRepository extends JpaRepository<CcdvProfile, Integer> {
    boolean existsByUserId(Integer userId);
}