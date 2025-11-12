package com.codegym.repository;

import com.codegym.model.CcdvServiceOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CcdvServiceOptionRepository extends JpaRepository<CcdvServiceOption, Long> {
    Optional<CcdvServiceOption> findByUser_Id(Long userId);
}