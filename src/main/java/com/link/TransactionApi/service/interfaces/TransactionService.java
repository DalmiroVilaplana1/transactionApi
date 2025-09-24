package com.link.TransactionApi.service.interfaces;


import com.link.TransactionApi.dto.CreateTransferRequest;
import com.link.TransactionApi.dto.TransactionResponse;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionService {
    TransactionResponse createBankTransfer(CreateTransferRequest cmd);
    TransactionResponse  getById(String transactionId);
    Page<TransactionResponse> listApprovedByUser(String userId, Pageable pageable, @Nullable String currency);
}