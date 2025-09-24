package com.link.TransactionApi.service.interfaces;

import com.link.TransactionApi.model.Transaction;
import com.link.TransactionApi.model.TransactionStatus;

public interface TransactionStatusResolver {
    TransactionStatus resolve(Transaction tx); // APPROVED o REJECTED

}
