package com.link.transactionapi.controller;

import com.link.transactionapi.dto.CreateTransferRequest;
import com.link.transactionapi.dto.TransactionResponse;
import com.link.transactionapi.model.Transaction;
import com.link.transactionapi.repository.TransactionRepository;
import com.link.transactionapi.service.interfaces.TransactionService;
import com.link.transactionapi.sse.SseHub;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
public class TransactionController {

    private final TransactionService service;
    private final SseHub hub;
    private final TransactionRepository txRepo;

    public TransactionController(TransactionService service, SseHub hub, TransactionRepository txRepo) {
        this.service = service;
        this.txRepo = txRepo;
        this.hub = hub;
    }

    @PostMapping("/transactions/transfers")
    public ResponseEntity<TransactionResponse> createTransfer(
            @Valid @RequestBody CreateTransferRequest req,
            UriComponentsBuilder ucb
    ) {
        TransactionResponse resp = service.createBankTransfer(req);
        URI location = ucb.path("/transactions/{id}").build(resp.transactionId());
        return ResponseEntity.created(location).body(resp);
    }

    @GetMapping("/transactions/{id}")
    public TransactionResponse getById(@PathVariable String id) {
        return service.getById(id);
    }

    @GetMapping("/users/{userId}/transactions/approved")
    public Page<TransactionResponse> listApprovedByUser(
            @PathVariable String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String currency,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "desc") String direction
    ) {
        Sort sortSpec = "desc".equalsIgnoreCase(direction)
                ? Sort.by(sort).descending()
                : Sort.by(sort).ascending();
        PageRequest pageable = PageRequest.of(page, size, sortSpec);
        return service.listApprovedByUser(userId, pageable, currency);
    }


    @GetMapping(value = "/users/{userId}/transactions/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamUserTransactions(@PathVariable("userId") String userId, HttpServletResponse httpResp) {
        httpResp.setHeader("Cache-Control", "no-cache");
        httpResp.setHeader("X-Accel-Buffering", "no");

        SseEmitter emitter = hub.subscribeUser(userId, -1L);

        // Se envia un evento por cada transacción actual del usuario
        List<Transaction> list = txRepo.findByUserId(userId);
        for (Transaction tx : list) {
            Map<String, Object> snapshot = new LinkedHashMap<>();
            snapshot.put("transactionId", tx.getId());
            snapshot.put("amount", tx.getAmount().toString());
            snapshot.put("type", "SNAPSHOT");
            snapshot.put("statusFrom", null);
            snapshot.put("statusTo", tx.getStatus() == null ? null : tx.getStatus().name());
            snapshot.put("at", tx.getCreatedAt() == null ? null : tx.getCreatedAt().toString());
            try {
                emitter.send(SseEmitter.event().name("status").data(snapshot));
            } catch (IOException ignored) { /* si falla uno, seguimos con el resto */ }
        }
        return emitter;
    }
}