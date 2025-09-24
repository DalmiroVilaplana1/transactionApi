package com.link.TransactionApi.dto;

public record TransactionResponse(
        String transactionId,
        String userId,
        String amount,
        String currency,
        String status,
        String createdAt,
        String approvedAt,
        String rejectedAt,
        String method,
        String bankCode,
        String recipientAccount
) {}