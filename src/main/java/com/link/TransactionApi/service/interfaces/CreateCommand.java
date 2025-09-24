package com.link.TransactionApi.service.interfaces;


import com.link.TransactionApi.model.PaymentMethod;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Comando de creación de transacción.
 * Define atributos COMUNES a cualquier método de pago.
 * Los específicos se agregan en implementaciones concretas (ej. CreateBankTransferCommand).
 */
public interface CreateCommand {
    PaymentMethod method();
    String userId();
    BigDecimal amount();
    String currency();
    String idempotencyKey();
    Map<String, Object> metadata();
}