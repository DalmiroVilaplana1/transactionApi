// service/command/CreateBankTransferCommand.java
package com.link.transactionapi.service.implementacion;



import com.link.transactionapi.model.PaymentMethod;
import com.link.transactionapi.service.interfaces.CreateCommand;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/**
 * Comando de creación para transferencias bancarias.
 * Agrega los campos específicos de transferencia bancaria: bankCode, recipientAccount.
 */

public class CreateBankTransferCommand implements CreateCommand {

    private final String userId;
    private final BigDecimal amount;
    private final String currency;
    private final String bankCode;
    private final String recipientAccount;
    private final Map<String, Object> metadata;

    public CreateBankTransferCommand(
            String userId,
            BigDecimal amount,
            String currency,
            String bankCode,
            String recipientAccount,
            Map<String, Object> metadata
    ) {
        this.userId = Objects.requireNonNull(userId, "userId is required");
        this.amount = Objects.requireNonNull(amount, "amount is required");
        this.currency = Objects.requireNonNull(currency, "currency is required");
        this.bankCode = Objects.requireNonNull(bankCode, "bankCode is required");
        this.recipientAccount = Objects.requireNonNull(recipientAccount, "recipientAccount is required");
        this.metadata = (metadata == null) ? Collections.emptyMap() : Map.copyOf(metadata);
    }

    @Override
    public PaymentMethod method() {
        return PaymentMethod.BANK_TRANSFER;
    }
    @Override
    public String userId() {
        return userId;
    }
    @Override
    public BigDecimal amount() {
        return amount;
    }
    @Override
    public String currency() {
        return currency;
    }

    @Override
    public Map<String, Object> metadata() {
        return metadata;
    }

    public String bankCode() { return bankCode; }
    public String recipientAccount() { return recipientAccount; }
}
