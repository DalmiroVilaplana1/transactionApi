package com.link.transactionapi.service.implementacion;

import com.link.transactionapi.model.Transaction;
import com.link.transactionapi.model.TransactionEvent;
import com.link.transactionapi.repository.TransactionRepository;
import com.link.transactionapi.service.interfaces.DomainEventPublisher;
import com.link.transactionapi.sse.SseHub;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Primary
@Component
public class SseDomainEventPublisher implements DomainEventPublisher {

    private final SseHub hub;
    private final TransactionRepository txRepo;

    public SseDomainEventPublisher(SseHub hub, TransactionRepository txRepo) {
        this.hub = hub;
        this.txRepo = txRepo;
    }

    @Override
    public void publish(TransactionEvent event) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("transactionId", event.getTransactionId());
        payload.put("type", event.getType());
        payload.put("statusFrom", event.getStatusFrom());
        payload.put("statusTo", event.getStatusTo());
        payload.put("at", event.getCreatedAt() == null ? null : event.getCreatedAt().toString());

        hub.publish(event.getTransactionId(), payload);

        Optional<Transaction> txOpt = txRepo.findById(event.getTransactionId());
        txOpt.ifPresent(tx -> hub.publishToUser(tx.getUserId(), payload));
    }
}