package com.link.TransactionApi.service.interfaces;

import com.link.TransactionApi.model.PaymentMethod;

public interface PaymentHandlerRegistry {
    PaymentHandler get(PaymentMethod method);
}

