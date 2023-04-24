package com.sourcefuse.jarc.services.auditservice.audit.mixin;

import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

@Service
@EnableAsync
public class MyService {

    @Async
    public static CompletableFuture<String> doSomethingAsync(String input) {
        // Do some async work here...
        String result = input.toUpperCase();
        // Simulate some delay...
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return CompletableFuture.completedFuture(result);
    }
    
}
