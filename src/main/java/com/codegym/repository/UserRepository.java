package com.codegym.repository;

import com.codegym.dto.TopCcdvDTO;
import com.codegym.dto.UserListDTO;
import com.codegym.model.User;

import jakarta.transaction.Transactional;

import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findById(Long id);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    boolean existsByCccd(String cccd);

    @Modifying
    @Query("UPDATE User u SET u.viewCount = u.viewCount + 1 WHERE u.id = :id")
    void increaseView(@Param("id") Long id);

    @Query("SELECT new com.codegym.dto.TopCcdvDTO(u.id, p.fullName, p.avatar, p.description, u.viewCount) " +
            "FROM User u JOIN CcdvProfile p ON u.id = p.user.id " +
            "WHERE u.role.id = :roleId " +
            "ORDER BY u.viewCount DESC")
    List<TopCcdvDTO> findTopCcdvWithProfile(@Param("roleId") Long roleId, Pageable pageable);



    // Lấy danh sách user TRỪ ADMIN
    @Query("SELECT new com.codegym.dto.UserListDTO(u.id, CONCAT(u.firstName, ' ', u.lastName), u.nickname, u.role.name,u.status) " +
            "FROM User u WHERE u.role.name <> 'ADMIN'")
    List<UserListDTO> getAllUsersExceptAdmin();

    // Lọc theo role
    @Query("SELECT new com.codegym.dto.UserListDTO(u.id, CONCAT(u.firstName, ' ', u.lastName), u.nickname, u.role.name,u.status) " +
            "FROM User u WHERE u.role.name = :roleName")
    List<UserListDTO> findByRole(@Param("roleName") String roleName);

    boolean existsById(Long id);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.status = :status WHERE u.id = :userId")
    void updateStatus(@Param("userId") Long userId, @Param("status") String status);

    // lấy danh sách tài khoản có phân trang
    Page<User> findAll(Pageable pageable);

    // lấy danh sách user VIP
    Page<User> findByIsVipTrue(Pageable pageable);

    // lấy danh sách ccdv vip (id = 2)
    Page<User> findByRole_IdAndIsVipTrue(Long roleId, Pageable pageable);


}