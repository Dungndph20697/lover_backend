package com.codegym.repository;

import com.codegym.model.TopupTransaction;
import com.codegym.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopupTransactionRepository extends JpaRepository<TopupTransaction, Long> {
    boolean existsByBankTxnId(String bankTxnId);
    List<TopupTransaction> findByUserOrderByCreatedAtDesc(User user);
}
