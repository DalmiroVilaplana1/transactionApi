// src/main/java/com/acme/payments/service/impl/TransactionServiceImpl.java
package com.link.TransactionApi.service.implementacion;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.Executor;

import com.link.TransactionApi.dto.CreateTransferRequest;
import com.link.TransactionApi.dto.TransactionResponse;
import com.link.TransactionApi.exceptions.ResourceNotFoundException;
import com.link.TransactionApi.model.*;
import com.link.TransactionApi.repository.BankTransferDetailsRepository;
import com.link.TransactionApi.repository.TransactionEventRepository;
import com.link.TransactionApi.repository.TransactionRepository;
import com.link.TransactionApi.service.interfaces.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final PaymentHandlerRegistry registry;
    private final TransactionRepository txRepo;
    private final BankTransferDetailsRepository detailsRepo;
    private final TransactionEventRepository eventRepo;
    private final TransactionStatusResolver resolver;
    private final DomainEventPublisher publisher;
    private final Executor txExecutor;

    public TransactionServiceImpl(
            PaymentHandlerRegistry registry,
            TransactionRepository txRepo,
            BankTransferDetailsRepository detailsRepo,
            TransactionEventRepository eventRepo,
            TransactionStatusResolver resolver,
            DomainEventPublisher publisher,
            Executor txExecutor
    ) {
        this.registry = registry;
        this.txRepo = txRepo;
        this.detailsRepo = detailsRepo;
        this.eventRepo = eventRepo;
        this.resolver = resolver;
        this.publisher = publisher;
        this.txExecutor = txExecutor;
    }

    @Override
    public TransactionResponse createBankTransfer(CreateTransferRequest req) {
        CreateCommand cmd = new CreateBankTransferCommand(
                req.userId(),
                new BigDecimal(req.amount()),
                req.currency(),
                req.bankCode(),
                req.recipientAccount(),
                java.util.Map.of()
        );

        PaymentHandler handler = registry.get(PaymentMethod.BANK_TRANSFER);
        Transaction tx = handler.create(cmd);

        TransactionEvent created = TransactionEvent.created(tx.getId(), Instant.now());
        eventRepo.save(created);
        publisher.publish(created);

        txExecutor.execute(() -> finalizeStatus(tx.getId()));

        return toResponse(tx);
    }

    private void finalizeStatus(String txId) {
        Transaction tx = txRepo.findById(txId).orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
        TransactionStatus from = tx.getStatus();
        TransactionStatus to = resolver.resolve(tx);

        if (to == TransactionStatus.APPROVED) {
            tx.markApproved(Instant.now());
        } else {
            tx.markRejected(Instant.now());
        }
        txRepo.save(tx);

        TransactionEvent evt = TransactionEvent.statusChanged(txId, from, to, Instant.now());
        eventRepo.save(evt);
        publisher.publish(evt);
    }

    @Override
    public TransactionResponse getById(String transactionId) {
        Transaction tx = txRepo.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
        return toResponse(tx);
    }

    @Override
    public Page<TransactionResponse> listApprovedByUser(String userId, Pageable pageable, String currencyFilter) {
        Page<Transaction> page = txRepo.findByUserIdAndStatus(userId, TransactionStatus.APPROVED, pageable);
        return page.map(this::toResponse);
    }

    private TransactionResponse toResponse(Transaction tx) {
        Optional<BankTransferDetails> opt = detailsRepo.findByTransactionId(tx.getId());
        BankTransferDetails d = opt.orElse(null);

        return new TransactionResponse(
                tx.getId(),
                tx.getUserId(),
                tx.getAmount().toPlainString(),
                tx.getCurrency(),
                tx.getStatus().name(),
                tx.getCreatedAt() == null ? null : tx.getCreatedAt().toString(),
                tx.getApprovedAt() == null ? null : tx.getApprovedAt().toString(),
                tx.getRejectedAt() == null ? null : tx.getRejectedAt().toString(),
                tx.getMethod().name(),
                d == null ? null : d.getBankCode(),
                d == null ? null : d.getRecipientAccount()
        );
    }
}
