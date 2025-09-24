package com.link.transactionapi.repository;

import com.link.transactionapi.model.BankTransferDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BankTransferDetailsRepository  extends JpaRepository<BankTransferDetails, String> {
    Optional<BankTransferDetails> findByTransactionId(String transactionId);

}
