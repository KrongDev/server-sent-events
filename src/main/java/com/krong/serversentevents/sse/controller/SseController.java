package com.krong.serversentevents.sse.controller;

import com.krong.serversentevents.sse.service.SseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sse")
public class SseController {
    //
    private final SseService sseService;

    @GetMapping("/request-id")
    public String genRequestId() {
        //
        return UUID.randomUUID().toString();
    }

    @GetMapping("/connect/{requestId}")
    public SseEmitter connect(@PathVariable String requestId) {
        //
        return sseService.register(requestId);
    }

    @PostMapping("/action")
    public void createAction() {
        //
        sseService.notification("create data");
    }

    @PutMapping("/action")
    public void updateAction() {
        //
        sseService.notification("update data");
    }

    @DeleteMapping("/action")
    public void removeAction() {
        //
        sseService.notification("remove data");
    }
}
