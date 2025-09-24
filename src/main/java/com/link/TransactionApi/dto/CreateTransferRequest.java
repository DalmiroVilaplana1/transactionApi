package com.link.TransactionApi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CreateTransferRequest(
        @NotBlank String userId,
        @Pattern(regexp="^\\d+(\\.\\d{1,2})?$", message="amount debe ser un decimal positivo con hasta 2 decimales")
        String amount,
        @Pattern(regexp="^[A-Z]{3}$", message="currency debe ser ISO-4217 (3 letras)")
        String currency,
        @NotBlank String bankCode,
        @NotBlank String recipientAccount
) {}