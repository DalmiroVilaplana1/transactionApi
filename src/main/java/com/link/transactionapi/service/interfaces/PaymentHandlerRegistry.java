package com.link.transactionapi.service.interfaces;

import com.link.transactionapi.model.PaymentMethod;

public interface PaymentHandlerRegistry {
    PaymentHandler get(PaymentMethod method);
}

