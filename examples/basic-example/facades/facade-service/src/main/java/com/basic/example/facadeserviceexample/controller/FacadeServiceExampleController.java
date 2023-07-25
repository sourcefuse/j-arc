package com.basic.example.facadeserviceexample.controller;

import com.basic.example.facadeserviceexample.dto.Invitation;
import com.basic.example.facadeserviceexample.dto.Notification;
import com.basic.example.facadeserviceexample.dto.UserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/tenantsExample/{id}/users")
@RequiredArgsConstructor
public class FacadeServiceExampleController {

    private final WebClient webClient;

    @PostMapping
    public Mono<Object> callEndpoints(@Valid @RequestBody UserDto userDto,
                                      @PathVariable("id") UUID id) {
        log.info("hello .............................................");
        // Call the 1st endpoint
        return webClient.post()
                .uri("http://localhost:8084/tenants/{id}/users", id)
                .bodyValue(userDto)
                .retrieve()
                .bodyToMono(UserDto.class) // Change to appropriate response class if needed
                .flatMap(response -> callSecondEndpoint());
                //.flatMap(response -> callThirdEndpoint());
    }

    private Mono<Invitation> callSecondEndpoint() {
        // Call the 2nd endpoint
        Invitation invitation =Invitation.builder().email("test@sourcefuse.com").
                expires(LocalDateTime.now()).build(); // Set invitation details as needed

        return webClient.post()
                .uri("http://localhost:8081/invitation")
                .bodyValue(invitation)
                .retrieve()
                .bodyToMono(Invitation.class); // Change to appropriate response class if needed
    }

    private Mono<Notification> callThirdEndpoint() {
        // Call the 3rd endpoint
        Notification notification = new Notification(); // Set notification details as needed
        return webClient.post()
                .uri("http://localhost:8083/notifications")
                .bodyValue(notification)
                .retrieve()
                .bodyToMono(Notification.class); // Change to appropriate response class if needed
    }
}