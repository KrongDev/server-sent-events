package com.krong.serversentevents.sse.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class SseService {
    //
    private final long TIMEOUT_MILLISEC = 60L * 60 * 1000;
    private final Map<String, SseEmitter> emitterMap = new ConcurrentHashMap<>();

    public SseEmitter register(String requestId) {
        SseEmitter emitter = new SseEmitter(TIMEOUT_MILLISEC);
        config(emitter, requestId);
        emitterMap.put(requestId, emitter);
        return emitter;
    }

    public <E> void send(SseEmitter emitter, E data) {
        try {
            emitter.send(
                    SseEmitter
                            .event()
                            .name("message")
                            .data(data)
            );
        } catch (IOException e) {
            emitter.completeWithError(e);
            log.error(e.getMessage(), e);
        }
    }

    public <E> void notification(E data) {
        //
        emitterMap.forEach((k,v) -> send(v, data));
    }

    private void config(SseEmitter emitter, String requestId) {
        emitter.onCompletion(() -> emitterMap.remove(requestId));
        emitter.onTimeout(() -> emitterMap.remove(requestId));
    }
}
