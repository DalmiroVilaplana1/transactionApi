package com.link.TransactionApi.service.implementacion;


import com.link.TransactionApi.model.Transaction;
import com.link.TransactionApi.model.TransactionStatus;
import com.link.TransactionApi.service.interfaces.TransactionStatusResolver;
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

