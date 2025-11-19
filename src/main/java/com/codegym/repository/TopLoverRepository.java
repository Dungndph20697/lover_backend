package com.codegym.repository;

import com.codegym.dto.TopCcdvDTO;
import com.codegym.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TopLoverRepository extends JpaRepository<User, Long> {
    @Modifying
    @Query("UPDATE User u SET u.viewCount = u.viewCount + 1 WHERE u.id = :id")
    void increaseView(@Param("id") Long id);

    @Query("SELECT new com.codegym.dto.TopCcdvDTO(u.id, p.fullName, p.avatar, p.description, u.viewCount) " +
            "FROM User u JOIN CcdvProfile p ON u.id = p.user.id " +
            "WHERE u.role.id = :roleId " +
            "ORDER BY u.viewCount DESC")
    List<TopCcdvDTO> findTopCcdvWithProfile(@Param("roleId") Long roleId, Pageable pageable);
}
