package com.link.transactionapi.repository;

import com.link.transactionapi.model.Transaction;
import com.link.transactionapi.model.TransactionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, String> {
    Page<Transaction> findByUserIdAndStatus(String userId, TransactionStatus status, Pageable pageable);
    List<Transaction> findByUserId(String userId);
}