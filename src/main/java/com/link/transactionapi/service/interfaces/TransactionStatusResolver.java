package com.link.transactionapi.service.interfaces;

import com.link.transactionapi.model.Transaction;
import com.link.transactionapi.model.TransactionStatus;

public interface TransactionStatusResolver {
    TransactionStatus resolve(Transaction tx); // APPROVED o REJECTED

}
