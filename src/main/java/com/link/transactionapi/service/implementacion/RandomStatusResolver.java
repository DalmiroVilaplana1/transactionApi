package com.link.transactionapi.service.implementacion;


import com.link.transactionapi.model.Transaction;
import com.link.transactionapi.model.TransactionStatus;
import com.link.transactionapi.service.interfaces.TransactionStatusResolver;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component
public class RandomStatusResolver implements TransactionStatusResolver {
    @Override
    public TransactionStatus resolve(Transaction tx) {
        return ThreadLocalRandom.current().nextBoolean()
                ? TransactionStatus.APPROVED
                : TransactionStatus.REJECTED;
    }
}

