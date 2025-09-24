package com.link.transactionapi.service.implementacion;


import com.link.transactionapi.model.*;
import com.link.transactionapi.repository.BankTransferDetailsRepository;
import com.link.transactionapi.repository.TransactionRepository;
import com.link.transactionapi.service.interfaces.CreateCommand;
import com.link.transactionapi.service.interfaces.PaymentHandler;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class BankTransferHandler implements PaymentHandler {

    private final TransactionRepository txRepo;
    private final BankTransferDetailsRepository detailsRepo;

    public BankTransferHandler(TransactionRepository txRepo,
                               BankTransferDetailsRepository detailsRepo) {
        this.txRepo = txRepo;
        this.detailsRepo = detailsRepo;
    }

    @Override
    public PaymentMethod method() {
        return PaymentMethod.BANK_TRANSFER;
    }

    @Override
    public Transaction create(CreateCommand baseCmd) {
        if (!(baseCmd instanceof CreateBankTransferCommand cmd)) {
            throw new IllegalArgumentException("Expected CreateBankTransferCommand for BANK_TRANSFER");
        }

        Transaction tx = new Transaction();
        tx.setUserId(cmd.userId());
        tx.setAmount(cmd.amount());
        tx.setCurrency(cmd.currency());
        tx.setStatus(TransactionStatus.PENDING);
        tx.setMethod(PaymentMethod.BANK_TRANSFER);
        tx.setCreatedAt(Instant.now());
        tx = txRepo.save(tx);

        BankTransferDetails det = new BankTransferDetails();
        det.setTransaction(tx);
        det.setBankCode(cmd.bankCode());
        det.setRecipientAccount(cmd.recipientAccount());
        detailsRepo.save(det);

        return tx;
    }
}
