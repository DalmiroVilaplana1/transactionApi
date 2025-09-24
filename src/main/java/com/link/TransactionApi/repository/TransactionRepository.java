package com.link.TransactionApi.repository;

import com.link.TransactionApi.model.Transaction;
import com.link.TransactionApi.model.TransactionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, String> {
    Page<Transaction> findByUserIdAndStatus(String userId, TransactionStatus status, Pageable pageable);
    List<Transaction> findByUserId(String userId);
}