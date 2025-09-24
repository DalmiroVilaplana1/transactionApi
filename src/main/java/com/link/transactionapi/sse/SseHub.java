package com.link.transactionapi.sse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class SseHub {

    private final Map<String, CopyOnWriteArrayList<SseEmitter>> subscribers = new ConcurrentHashMap<>();

    public SseEmitter subscribe(String transactionId, long timeoutMs) {
        return subscribeToTopic(topicTx(transactionId), timeoutMs);
    }
    public void publish(String transactionId, Object data) {
        publishToTopic(topicTx(transactionId), "status", data);
    }

    public SseEmitter subscribeUser(String userId, long timeoutMs) {
        return subscribeToTopic(topicUser(userId), timeoutMs);
    }
    public void publishToUser(String userId, Object data) {
        publishToTopic(topicUser(userId), "status", data);
    }

    private SseEmitter subscribeToTopic(String topic, long timeoutMs) {
        SseEmitter emitter = new SseEmitter(timeoutMs);
        subscribers.computeIfAbsent(topic, k -> new CopyOnWriteArrayList<>()).add(emitter);
        emitter.onCompletion(() -> remove(topic, emitter));
        emitter.onTimeout(() -> remove(topic, emitter));
        emitter.onError(e -> remove(topic, emitter));
        return emitter;
    }

    private void publishToTopic(String topic, String eventName, Object data) {
        List<SseEmitter> list = subscribers.getOrDefault(topic, new CopyOnWriteArrayList<>());
        List<SseEmitter> dead = new ArrayList<>();
        for (SseEmitter emitter : list) {
            try {
                emitter.send(SseEmitter.event().name(eventName).data(data));
            } catch (IOException e) {
                dead.add(emitter);
            }
        }
        for (SseEmitter emitter : dead) {
            remove(topic, emitter);
        }
    }

    private void remove(String topic, SseEmitter emitter) {
        CopyOnWriteArrayList<SseEmitter> list = subscribers.get(topic);
        if (list != null) {
            list.remove(emitter);
            if (list.isEmpty()) {
                subscribers.remove(topic);
            }
        }
    }

    private static String topicTx(String txId)   { return "tx:" + txId; }
    private static String topicUser(String uid)  { return "user:" + uid; }
}