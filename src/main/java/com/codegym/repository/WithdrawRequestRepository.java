package com.codegym.repository;

import com.codegym.model.User;
import com.codegym.model.WithdrawRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WithdrawRequestRepository extends JpaRepository<WithdrawRequest, Long> {

    // CCDV xem TẤT CẢ lịch sử rút
    List<WithdrawRequest> findByUserOrderByCreatedAtDesc(User user);

    // CCDV xem lịch sử KHÔNG bao gồm PENDING
    List<WithdrawRequest> findByUserAndStatusNotOrderByCreatedAtDesc(User user, String status);

    // Admin xem toàn bộ yêu cầu
    List<WithdrawRequest> findAllByOrderByCreatedAtDesc();

    // Admin lọc theo trạng thái
    List<WithdrawRequest> findByStatusOrderByCreatedAtDesc(String status);

    // Admin tìm kiếm linh hoạt
    @Query("""
        SELECT w FROM WithdrawRequest w
        WHERE 
            (:keyword IS NULL OR 
                LOWER(w.user.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                LOWER(w.user.username) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                LOWER(w.bankName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                LOWER(w.bankAccountNumber) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                LOWER(w.bankAccountName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                LOWER(w.status) LIKE LOWER(CONCAT('%', :keyword, '%'))
            )
        ORDER BY w.createdAt DESC
    """)
    List<WithdrawRequest> searchAdmin(@Param("keywork") String keyword);
}
