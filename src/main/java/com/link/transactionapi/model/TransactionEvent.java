package com.link.transactionapi.model;


import jakarta.persistence.*;
import java.time.Instant;

@Entity @Table(name = "transaction_events",
        indexes = {@Index(name="idx_event_tx_created", columnList = "transactionId,createdAt")})
public class TransactionEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String transactionId;

    @Column(nullable = false, length = 40)
    private String type; // CREATED | STATUS_CHANGED | ...

    @Column(length = 20)
    private String statusFrom;

    @Column(length = 20)
    private String statusTo;

    @Column(nullable = false)
    private Instant createdAt;

    @Lob
    private String payload;

    public static TransactionEvent created(String txId, Instant at) {
        var e = new TransactionEvent();
        e.transactionId = txId;
        e.type = "CREATED";
        e.createdAt = at;
        return e;
    }

    public static TransactionEvent statusChanged(String txId, TransactionStatus from, TransactionStatus to, Instant at) {
        var e = new TransactionEvent();
        e.transactionId = txId;
        e.type = "STATUS_CHANGED";
        e.statusFrom = from.name();
        e.statusTo = to.name();
        e.createdAt = at;
        return e;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatusFrom() {
        return statusFrom;
    }

    public void setStatusFrom(String statusFrom) {
        this.statusFrom = statusFrom;
    }

    public String getStatusTo() {
        return statusTo;
    }

    public void setStatusTo(String statusTo) {
        this.statusTo = statusTo;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}
