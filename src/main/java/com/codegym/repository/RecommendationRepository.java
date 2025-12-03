package com.codegym.repository;

import com.codegym.model.CcdvProfile;
import com.codegym.model.CcdvServiceDetail;
import com.codegym.model.enums.ProfileStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecommendationRepository extends JpaRepository<CcdvProfile, Long> {
    // Lấy theo giới tính và status ACTIVE
    List<CcdvProfile> findByGenderAndStatusOrderByJoinDateDesc(
            String gender,
            ProfileStatus status,
            Pageable pageable
    );

    // Lấy tất cả ACTIVE, order theo joinDate
    List<CcdvProfile> findByStatusOrderByJoinDateDesc(
            ProfileStatus status,
            Pageable pageable
    );

    // Lấy 3 service random theo userId
    @Query(value = """
        SELECT st.name
        FROM service_types st
        JOIN ccdv_service_detail d ON d.service_type_id = st.id
        WHERE d.user_id = :profileId
        ORDER BY RAND()
        LIMIT 3
    """, nativeQuery = true)
    List<String> findRandomServicesByUserId(Long profileId);

    @Query("""
        SELECT p FROM CcdvProfile p
        WHERE (:name IS NULL OR LOWER(p.fullName) LIKE LOWER(CONCAT('%', :name, '%')))
            AND (:gender IS NULL OR p.gender = :gender)
            AND (:address IS NULL OR LOWER(p.city) LIKE LOWER(CONCAT('%', :address, '%')))
            AND (:minAge IS NULL OR (YEAR(CURRENT_DATE) - p.yearOfBirth) >= :minAge)
            AND (:maxAge IS NULL OR (YEAR(CURRENT_DATE) - p.yearOfBirth) <= :maxAge)
            AND p.status = 'ACTIVE'
    """)
    List<CcdvProfile> filterProfiles(
            String name,
            Integer minAge,
            Integer maxAge,
            String gender,
            String address
    );

    @Query("""
        SELECT p FROM CcdvProfile p
        WHERE (:gender IS NULL OR LOWER(p.gender) = LOWER(:gender))
          AND p.status = 'ACTIVE'
        ORDER BY p.joinDate DESC
    """)
    List<CcdvProfile> findByGenderAndStatus(@Param("gender") String gender, Pageable pageable);

    @Query("""
        SELECT d FROM CcdvServiceDetail d WHERE d.user.id = :userId
    """)
    List<CcdvServiceDetail> findServicesByUserId(Long userId);
}
