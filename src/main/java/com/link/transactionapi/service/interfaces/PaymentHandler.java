package com.link.transactionapi.service.interfaces;

import com.link.transactionapi.model.PaymentMethod;
import com.link.transactionapi.model.Transaction;

public interface PaymentHandler {
    PaymentMethod method();               // BANK_TRANSFER | CARD | WALLET ...
    Transaction create(CreateCommand cmd);// persiste Transaction + details + evento CREATED
}
