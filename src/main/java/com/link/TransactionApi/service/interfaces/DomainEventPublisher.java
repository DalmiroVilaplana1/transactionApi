package com.link.TransactionApi.service.interfaces;

import com.link.TransactionApi.model.TransactionEvent;

public interface DomainEventPublisher {
    void publish(TransactionEvent event);
}
