package com.basic.example.facadeserviceexample.controller;

import com.basic.example.facadeserviceexample.dto.Invitation;
import com.basic.example.facadeserviceexample.dto.Notification;
import com.basic.example.facadeserviceexample.dto.Receiver;
import com.basic.example.facadeserviceexample.dto.Subscriber;
import com.basic.example.facadeserviceexample.dto.User;
import com.basic.example.facadeserviceexample.dto.UserDto;
import com.basic.example.facadeserviceexample.enums.MessageType;
import com.sourcefuse.jarc.core.constants.CommonConstants;
import com.sourcefuse.jarc.core.models.session.CurrentUser;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;

@RestController
@Slf4j
@RequestMapping("/invite-user")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class FacadeServiceExampleController {

  private final WebClient webClient;

  @Value("${invitation-mail-template}")
  String InvitationMailTemplate;

  @Value("${frontend.url}")
  private String feUrl;

  @PostMapping
  @PreAuthorize("isAuthenticated()")
  public Mono<Notification> callEndpoints(
    @Valid @RequestBody UserDto userDto,
    @RequestHeader("Authorization") String bearerToken
  ) {
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      bearerToken = bearerToken.substring(CommonConstants.NUMERIC_SEVEN);
      log.info("Token  received  ......... " + bearerToken);
    } else {
      log.info("Token  not received  !!!!! " + bearerToken);
    }
    SecurityContextHolder.getContext().getAuthentication();
    // Call the 3rd endpoint
    CurrentUser currentUser =
      (
        (CurrentUser) (
          SecurityContextHolder.getContext().getAuthentication().getPrincipal()
        )
      );
    // Call the 1st endpoint
    String finalBearerToken = bearerToken;
    return webClient
      .post()
      .uri("http://localhost:8084/tenants/{id}/users", userDto.getTenantId())
      .accept(MediaType.APPLICATION_JSON)
      .bodyValue(userDto)
      .headers(headers -> headers.setBearerAuth(finalBearerToken))
      .retrieve()
      .bodyToMono(UserDto.class)
      .flatMap(createdUserDto ->
        checkUserSignUp(createdUserDto, finalBearerToken)
      )
      .flatMap(string -> sendInvitation(userDto, finalBearerToken))
      .flatMap(invitation ->
        sendNotification(
          userDto.getUserDetails(),
          invitation.getId().toString(),
          finalBearerToken,
          currentUser
        )
      );
  }

  private Mono<String> checkUserSignUp(
    UserDto userDto,
    String finalBearerToken
  ) {
    log.info("Facade service called UserSignUp Service ");
    return webClient
      .post()
      .uri("http://localhost:8084/user-credentials")
      .accept(MediaType.APPLICATION_JSON)
      .bodyValue(userDto)
      .headers(headers -> headers.setBearerAuth(finalBearerToken))
      .retrieve()
      .bodyToMono(String.class);
  }

  private Mono<Invitation> sendInvitation(
    UserDto userDto,
    String finalBearerToken
  ) {
    // Call the 2nd endpoint
    Invitation invitation = Invitation
      .builder()
      .email(userDto.getUserDetails().getEmail())
      .expires(LocalDateTime.now())
      .build();
    log.info("Facade service called Invitation Service ");
    return webClient
      .post()
      .uri("http://localhost:8081/invitation")
      .accept(MediaType.APPLICATION_JSON)
      .bodyValue(invitation)
      .headers(headers -> headers.setBearerAuth(finalBearerToken))
      .retrieve()
      .bodyToMono(Invitation.class);
  }

  private Mono<Notification> sendNotification(
    User userDetails,
    String invitationId,
    String finalBearerToken,
    CurrentUser currentUser
  ) {
    String link = feUrl + "invitation/" + invitationId;
    String emailBody = InvitationMailTemplate
      .replace(
        "{USER_NAME}",
        currentUser.getFirstName() + " " + currentUser.getLastName()
      )
      .replace("{INVITATION_LINK}", link);

    HashMap<String, Object> options = new HashMap<>();
    options.put("html", emailBody);
    Subscriber subscriber = new Subscriber();
    subscriber.setId(userDetails.getEmail());

    Receiver receiver = new Receiver();
    receiver.setTo(Arrays.asList(subscriber));

    Notification notification = new Notification(); // Set notification details as needed
    notification.setBody("Invitation link - " + link);
    notification.setReceiver(receiver);
    notification.setOptions(options);
    notification.setSentDate(LocalDateTime.now());
    notification.setSubject("User Invitation");
    notification.setType(MessageType.EMAIL);
    log.info("Facade service called Notification Service ");
    return webClient
      .post()
      .uri("http://localhost:8083/notifications")
      .accept(MediaType.APPLICATION_JSON)
      .bodyValue(notification)
      .headers(headers -> headers.setBearerAuth(finalBearerToken))
      .retrieve()
      .bodyToMono(Notification.class);
  }
}
