package com.codegym.repository;

import com.codegym.model.User;
import com.codegym.model.WithdrawRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    // Admin xem toàn bộ yêu cầu (có phân trang)
    Page<WithdrawRequest> findAllByOrderByCreatedAtDesc(Pageable pageable);

    // Admin lọc theo trạng thái (có phân trang)
    Page<WithdrawRequest> findByStatusOrderByCreatedAtDesc(String status, Pageable pageable);

    // Admin tìm kiếm linh hoạt + phân trang
    @Query("""
        SELECT w FROM WithdrawRequest w
        WHERE 
            (:keyword IS NULL OR 
                LOWER(w.user.firstName)      LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                LOWER(w.user.username)       LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                LOWER(w.bankName)            LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                LOWER(w.bankAccountNumber)   LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                LOWER(w.bankAccountName)     LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                LOWER(w.status)              LIKE LOWER(CONCAT('%', :keyword, '%'))
            )
    """)
    Page<WithdrawRequest> searchAdmin(
            @Param("keyword") String keyword,
            Pageable pageable
    );
}
