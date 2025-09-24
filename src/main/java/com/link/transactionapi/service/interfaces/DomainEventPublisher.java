package com.link.transactionapi.service.interfaces;

import com.link.transactionapi.model.TransactionEvent;

public interface DomainEventPublisher {
    void publish(TransactionEvent event);
}
