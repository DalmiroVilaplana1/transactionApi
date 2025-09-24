package com.link.transactionapi.service.implementacion;


import com.link.transactionapi.model.PaymentMethod;
import com.link.transactionapi.service.interfaces.PaymentHandler;
import com.link.transactionapi.service.interfaces.PaymentHandlerRegistry;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryPaymentHandlerRegistry implements PaymentHandlerRegistry {

    private final Map<PaymentMethod, PaymentHandler> map;

    public InMemoryPaymentHandlerRegistry(List<PaymentHandler> handlers) {
        Map<PaymentMethod, PaymentHandler> tmp = new EnumMap<>(PaymentMethod.class);
        for (PaymentHandler h : handlers) {
            tmp.put(h.method(), h);
        }
        this.map = tmp;
    }

    @Override
    public PaymentHandler get(PaymentMethod method) {
        PaymentHandler handler = map.get(method);
        if (handler == null) {
            throw new IllegalArgumentException("No handler for method " + method);
        }
        return handler;
    }
}