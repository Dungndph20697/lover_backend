package com.codegym.repository;

import com.codegym.model.CcdvProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CcdvFilterRepository extends JpaRepository<CcdvProfile, Long> {

    @Query("""
        SELECT p FROM CcdvProfile p
        WHERE (:name IS NULL OR LOWER(p.fullName) LIKE LOWER(CONCAT('%', :name, '%')))
        AND (:gender IS NULL OR p.gender = :gender)
        AND (:minAge IS NULL OR (YEAR(CURRENT_DATE) - p.yearOfBirth) >= :minAge)
        AND (:maxAge IS NULL OR (YEAR(CURRENT_DATE) - p.yearOfBirth) <= :maxAge)
        AND (:address IS NULL OR LOWER(p.city) LIKE LOWER(CONCAT('%', :address, '%')))
    """)
    List<CcdvProfile> searchProfiles(
            String name,
            String gender,
            Integer minAge,
            Integer maxAge,
            String address
    );
}