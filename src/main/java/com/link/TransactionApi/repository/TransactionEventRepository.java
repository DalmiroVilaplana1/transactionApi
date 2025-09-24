package com.link.TransactionApi.repository;

import com.link.TransactionApi.model.TransactionEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionEventRepository extends JpaRepository<TransactionEvent, String> {
    List<TransactionEvent> findByTransactionIdOrderByCreatedAtAsc(String transactionId);
}