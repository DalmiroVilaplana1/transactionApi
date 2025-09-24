package com.link.TransactionApi.service.interfaces;

import com.link.TransactionApi.model.PaymentMethod;
import com.link.TransactionApi.model.Transaction;

public interface PaymentHandler {
    PaymentMethod method();               // BANK_TRANSFER | CARD | WALLET ...
    Transaction create(CreateCommand cmd);// persiste Transaction + details + evento CREATED
}
